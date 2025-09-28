### Resposta curta

Sim — consigo trabalhar com mais de um repositório ao mesmo tempo. Posso analisar o projeto atual em
JavaScript (Node/Express, Nest, etc.), extrair domínio, regras de negócio, API e modelo de dados, e
então orientar (ou gerar) a migração para esta infraestrutura Java (Spring Boot + Gradle em módulos
domain/application/infrastructure). Também posso orientar a migração dos dois frontends para
TypeScript + React, garantindo compatibilidade com a nova API.

Abaixo segue um plano detalhado para conduzir essa migração com segurança e previsibilidade.

---

### Abordagem geral de migração

- Estratégia recomendada: "strangler fig" (migração incremental).
    - Publicar a nova API em paralelo, proxyando rotas (se necessário) e migrando funcionalidades
      por fatias.
- Garantir paridade de comportamento via contratos (OpenAPI/Swagger) + testes E2E sobre o sistema
  legado e o novo.
- Automatizar os checks: CI com build, testes unitários + integração + E2E (Testcontainers no
  backend).

### Como lido com múltiplos repositórios

- Trabalhamos em esquema polyrepo ou monorepo, conforme sua preferência.
    - Polyrepo (provável):
        - repo-1: backend Java (este projeto)
        - repo-2: frontend web (React TS)
        - repo-3: painel/admin (React TS)
    - Fluxo de análise:
        1) Você compartilha os repositórios JS (links/leitura).
        2) Eu faço inventário: endpoints, entidades, casos de uso, middlewares, formatos de evento,
           integrações, migrações, testes.
        3) Proponho o mapeamento para os módulos `domain`/`application`/`infrastructure` aqui.
        4) Especifico gaps e plano de migração por fatia (sprint/épicos).
- Entregáveis típicos por repositório analisado:
    - Documento de API (OpenAPI gerado a partir do código/rotas atuais).
    - Mapa Entidade ↔ Tabela ↔ DTO.
    - Plano de rotas a migrar (com prioridades e dependências).
    - Casos de uso e gateways necessários no backend Java.
    - Plano de atualização dos frontends, incluindo geração de cliente a partir do OpenAPI.

### Mapeamento do legado JS para a arquitetura atual (Java)

- Camadas alvo neste projeto:
    - `domain` (modelo puro e regras de negócio, IDs como value objects — ex.: `CategoryID`,
      `GenreID`).
    - `application` (use cases: orquestram entidades e portas; validações; notificações).
    - `infrastructure` (adapters: persistence JPA/MySQL, controllers REST, config Spring/Undertow,
      Jackson, Flyway).
- Mapeamento típico:
    - Controllers/Rotas do Node → `infrastructure/api/controllers/*` (REST controllers Spring)
    - Serviços de domínio do Node → `application/*` (use cases) e `domain/*` (entidades/validações)
    - DAOs/Repos do Node → `infrastructure/*/persistence` + `*MySQLGateway` (JPA repositories e
      gateways)
    - Middlewares (auth/log/cors) → Spring Security/filters/config Undertow
    - Schemas/Migrations (Sequelize/Mongoose/Fastify schemas) → Flyway `V[n]__*.sql`

### Passo a passo sugerido (backend)

1) Descoberta/contratos
    - Extrair rotas atuais, modelos JSON e status codes.
    - Gerar um `openapi.yaml` inicial (a partir do legado) para servir de contrato de paridade.
2) Banco e migrações
    - Traduzir schemas do legado para Flyway (MySQL). Arquivos em
      `infrastructure/src/main/resources/db/migration`.
    - Se houver NoSQL, decidir estratégia (ex.: migrar para relacional com tabelas normalizadas ou
      manter um componente NoSQL separado).
3) Modelagem de domínio
    - Definir entidades e IDs como value objects (seguir padrão ULID como no projeto).
    - Portas de saída (interfaces) no domínio, implementadas depois na infraestrutura.
4) Casos de uso
    - Criar use cases em `application`, com validações e notificações (padrão do projeto).
5) Adapters/persistência
    - Criar `*JpaEntity`, `Spring Data Repositories`, `*MySQLGateway` (vide exemplos existentes no
      módulo `infrastructure`).
6) Web/API
    - Controllers REST espelhando o contrato OpenAPI (apoiar-se em `GlobalExceptionHandler`).
7) Testes
    - Unit (domínio e application) e integração/gateway (com `@IntegrationTest` e
      `@MySQLGatewayTest`).
    - E2E com `@E2ETest` e MockMvc.
8) Observabilidade e hardening
    - Logs estruturados, métricas, health checks.
9) Deploy
    - `bootJar` e container Docker. Opcional: Flyway em pipeline antes do start do app.

#### Exemplo de estrutura de um caso de uso (Java)

```java
// application/src/main/java/.../genre/retrieve/list/ListGenreUseCase.java
public abstract class ListGenreUseCase {
    public abstract Pagination<GenreListOutput> execute(SearchQuery query);
}

// application/src/main/java/.../genre/retrieve/list/DefaultListGenreUseCase.java
@Service
public class DefaultListGenreUseCase extends ListGenreUseCase {
    private final GenreGateway gateway;

    public DefaultListGenreUseCase(final GenreGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Pagination<GenreListOutput> execute(final SearchQuery query) {
        return gateway.findAll(query).map(GenreListOutput::from);
    }
}
```

### Passo a passo sugerido (frontends React + TS)

1) Setup
    - Inicializar projeto com Vite ou Next.js (TS), ESLint/Prettier, Jest/Vitest.
2) Tipos e cliente de API
    - Gerar cliente TS a partir do `openapi.yaml` (ex.: `openapi-typescript`, `orval`,
      `openapi-generator-cli`).
    - Centralizar `apiClient` com interceptors (auth/refresh/logging).
3) Arquitetura
    - Pages/Routes, componentes, hooks (ex.: React Query para dados), estado (Zustand/Redux se
      necessário).
    - Design System (MUI/Tailwind/Chakra) e tema consistente.
4) Migração por feature
    - Selecionar feature (ex.: gerenciamento de categorias), replicar telas, conectar à nova API.
    - Testes de regressão visuais e E2E (Playwright/Cypress).
5) Integração contínua
    - Lint + typecheck + testes unitários + E2E em PRs.

### Monorepo vs. Polyrepo

- Polyrepo (separado) costuma ser mais simples para equipes e deploys independentes.
- Monorepo facilita versionar contratos e rodar E2E ponta-a-ponta.
- Se optar por monorepo, sugiro `pnpm` workspaces pros frontends e manter o backend Java como
  subpasta separada com Gradle wrapper próprio, ou vice-versa.

### Checklist de insumos que vou precisar

- Links de leitura (read-only) dos repositórios JS e dos dois frontends.
- Desenho atual de arquitetura (ou uma visão breve): serviços externos, filas/eventos, auth.
- Banco de dados atual: schemas/migrations (Sequelize, Prisma, Mongoose etc.).
- Lista de rotas e contratos (se não houver OpenAPI, eu extraio do código ou do API Gateway).
- Principais fluxos de negócio e regras que não estão explícitas no código (conhecimentos tribais).
- Requisitos não-funcionais: SLAs, volumetria, observabilidade, segurança.

### Plano de migração incremental (exemplo em 4 etapas)

1) Fundações (1-2 semanas)
    - Extrair contratos do legado, definir OpenAPI, subir esqueleto no backend Java (JDK 22 ok),
      Docker MySQL, Flyway V1.
    - Bootstrap dos frontends TS/React e geração de cliente API.
2) Primeira feature (1-2 semanas)
    - Migrar "Categorias" ponta a ponta: entidades, use cases, gateway, controller,
      telas/listagem/formulários.
    - Testes unitários/integrados/E2E, deploy staging.
3) Features adicionais (contínuo)
    - "Gêneros", "Vídeos", permissões, etc. Medir e ajustar performance.
4) Cutover
    - Proxy/roteamento definitivo, desligar rotas antigas, freeze do legado.

### Riscos e mitigação

- Divergência de contratos: controlar via OpenAPI e testes de contrato (Pact opcional).
- Mismatch de JDK: garantir `toolchain 22` em todos os módulos (ver `guidelines`).
- Diferenças de semântica de persistência (NoSQL → SQL): prototipar e validar com dados reais
  mascarados.
- Performance: usar Testcontainers para cenários realistas e aferir queries com JPA + índices via
  Flyway.

### Como começamos agora

- Me passe os links dos repositórios JS e dos frontends.
- Diga quais domains/rotas são prioridades.
- Confirmar se seguiremos polyrepo ou monorepo.
- Eu retorno com: inventário inicial, OpenAPI preliminar, proposta de mapeamento para
  `domain/application/infrastructure`, e um plano de V1 de migração.

Se preferir, já posso te dar um template mínimo de OpenAPI e um esqueleto de controller/use case
para a primeira entidade que escolhermos (ex.: `Category`).

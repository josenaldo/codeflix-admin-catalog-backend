Codeflix Admin Catalog Backend – Development Guidelines

Scope
These notes capture project-specific information to speed up development, testing, and debugging of
this codebase. They assume familiarity with Java, Gradle, Spring Boot, JPA/Hibernate,
Testcontainers/Flyway, and layered architectures.

Project overview

- Multi-module Gradle build with modules: domain, application, infrastructure.
- Spring Boot app lives in infrastructure (main class:
  br.com.josenaldo.codeflix.catalog.infrastructure.Application). Web container uses Undertow instead
  of Tomcat.
- Persistence: JPA/Hibernate. Migrations: Flyway (MySQL). H2 is used in some test scenarios.
- Testing: JUnit 5 with custom composed annotations to standardize integration, gateway, and E2E
  contexts.

Build and configuration
Java/Gradle

- Java level: 22 (see root build.gradle.kts toolchain and infrastructure module’s
  sourceCompatibility=22).
- Gradle: Wrapper provided (Gradle 8.11.x). Always prefer the wrapper.

Important nuance about toolchains

- The root project configures Java toolchain 22, but toolchain is project-scoped in Gradle. If a
  subproject does not explicitly set a toolchain, Gradle may use your system JDK for that
  subproject, which can cause compilation errors for APIs used in this repo (e.g., List#getFirst
  introduced in recent Java versions).
- Recommended: Ensure Gradle uses JDK 22 everywhere.
  Options (choose one):
    - Use a system JDK 22 and set org.gradle.java.home to it (env or gradle.properties).
    - Set a toolchain in each subproject (if you change the build), mirroring the root’s toolchain.
      Example snippet: java { toolchain { languageVersion = JavaLanguageVersion.of(22) } }

Common commands

- Build all: ./gradlew build
- Run app (from root): ./gradlew :infrastructure:bootRun
- Package boot jar (reuses infrastructure’s bootJar): ./gradlew :infrastructure:bootJar

Runtime configuration (Spring profiles)

- application.yml is default. Additional profiles:
    - application-dev.yml
    - application-stag.yml
    - application-prod.yml
    - application-test-integration.yml (activated by tests annotated with @IntegrationTest /
      @MySQLGatewayTest)
    - application-test-e2e.yml (activated by tests annotated with @E2ETest)
      Activate a profile via SPRING_PROFILES_ACTIVE or per-test annotations.

Database and migrations

- Local MySQL via Docker Compose: docker-compose up -d (service name: db, image: mysql:8.4). Ports
  map 33064->3306. Default DB: codeflix_adm_videos; user root/root.
- Flyway is wired into the infrastructure module and the Gradle plugin is applied. Defaults are
  pulled from env vars or fall back to the docker-compose host:
    - FLYWAY_DB_URL (default jdbc:mysql://codeflix-admin-catalog-backend-db:
      3306/codeflix_adm_videos)
    - FLYWAY_DB_USER (default root)
    - FLYWAY_DB_PASSWORD (default root)
- Migrations: infrastructure/src/main/resources/db/migration (files include V1__initial.sql and
  U1__initial.sql). Clean is enabled (cleanDisabled=false) to ease local resets. Exercise caution.

Testing
Test taxonomy

- Unit tests: Primarily in domain and application modules. Fast, no Spring context.
- Integration tests: Load Spring context with test-integration profile (@IntegrationTest). Used
  across infrastructure and application-facing boundaries.
- Gateway tests (JPA/MySQL slices): Use @MySQLGatewayTest, which composes @DataJpaTest with
  ActiveProfiles("test-integration") and includes only beans matching ".*[MySQLGateway]".
- E2E tests: Full stack with MockMvc via @E2ETest and test-e2e profile.

Custom test annotations (infrastructure/src/test/java/br/com/josenaldo/codeflix/catalog/annotations)

- @IntegrationTest
    - @ActiveProfiles("test-integration")
    - @SpringBootTest(classes = WebServerConfig.class)
    - @ExtendWith(RepositoryCleanUpExtension.class)
- @MySQLGatewayTest
    - @ActiveProfiles("test-integration")
    - @DataJpaTest + component scan include filter pattern ".*[MySQLGateway]"
    - @ExtendWith(RepositoryCleanUpExtension.class)
- @E2ETest
    - @ActiveProfiles("test-e2e")
    - @SpringBootTest(classes = WebServerConfig.class)
    - @AutoConfigureMockMvc + @ExtendWith(RepositoryCleanUpExtension.class)

Repository cleanup

-

infrastructure/src/test/java/br/com/josenaldo/codeflix/catalog/testutils/RepositoryCleanUpExtension.java
cleans DB state after each test to prevent cross-test leakage.

Running tests

- All modules: ./gradlew allTests (custom task depends on subproject Test tasks)
- Or: ./gradlew test (will run tests per module)
- Single module: ./gradlew :domain:test or ./gradlew :application:test or ./gradlew :infrastructure:
  test
- Single class: ./gradlew :application:test --tests 'fully.qualified.ClassName'
- Single method: ./gradlew :infrastructure:test --tests 'fully.qualified.ClassName.testMethodName'

Notes on JDK mismatch when running tests

- If you see compilation errors such as "cannot find symbol: List#getFirst", your Gradle is
  compiling that subproject with an older JDK. Fix by pointing Gradle to JDK 22 (
  org.gradle.java.home or toolchains per subproject). The existing tests in this repo pass when
  compiled with Java 22.

Adding new tests

- Unit tests: place under <module>/src/test/java mirroring package structure from src/main/java.
- Integration tests: create tests under infrastructure/src/test/java and annotate with
  @IntegrationTest (for full context) or @MySQLGatewayTest (for repository/gateway slices). These
  annotations automatically select the appropriate Spring profile and test configuration.
- E2E: use @E2ETest to load WebServerConfig and MockMvc; place tests under
  infrastructure/src/test/java.
- DB-dependent tests: Prefer Testcontainers; ensure Docker is running. The project already depends
  on testcontainers-junit and testcontainers-mysql. Use @DynamicPropertySource to wire container
  URLs if needed, or rely on provided profiles/config.

Verified example – running an existing test

- Example executed: domain CategoryTest (
  br.com.josenaldo.codeflix.catalog.domain.category.CategoryTest) passes using the project’s
  configured test runner.
    - One-off run command (if using Gradle with proper JDK 22):
      ./gradlew :domain:test --tests '
      br.com.josenaldo.codeflix.catalog.domain.category.CategoryTest'

Verified example – adding and running a new test (suggested flow)

- Steps we validated end-to-end in this environment:
    1) Create a simple JUnit 5 test under domain/src/test/java, e.g.,
       br.com.josenaldo.codeflix.catalog.domain.SanityTest with a trivial assertion.
    2) Run only that test. Two reliable approaches:
        - Via IDE’s JUnit runner.
        - Via Gradle (ensure JDK 22):
          ./gradlew :domain:test --tests 'br.com.josenaldo.codeflix.catalog.domain.SanityTest'
    3) Remove the temporary test after validating your setup.
- Caveat: If Gradle is bound to an older system JDK, the build can fail despite the test being
  correct. Ensure Gradle uses JDK 22 as noted above.

Coding and design conventions

- Architecture: Clean/Hexagonal layering: domain (pure model and interfaces) -> application (use
  cases) -> infrastructure (adapters, Spring configuration, API, persistence).
- Entities and IDs:
    - Domain IDs are value objects (e.g., CategoryID, GenreID) built around ULIDs. Converters found
      in infrastructure map domain IDs to JPA entities.
    - Example entities: CategoryJpaEntity, GenreJpaEntity, GenreCategoryJpaEntity (join table entity
      using embedded id GenreCategoryID).
- Validation: Domain layer uses Validator/ValidationHandler, with Notification and DomainException
  for aggregated errors.
- Pagination: Domain has Pagination and SearchQuery abstractions.
- JSON: infrastructure/configuration/json contains Jackson ObjectMapper config with Afterburner.
- Web/API: Undertow server; REST endpoints in infrastructure/api/controllers; exceptions handled
  centrally by GlobalExceptionHandler.
- Persistence: SpecificationUtils aids building JPA Specifications; repositories live under
  infrastructure/category/persistence and similar packages for other aggregates.
- Migrations: Flyway maintains schema; prefer adding V[version]__description.sql files.

Debugging tips

- When investigating repository/gateway behavior, prefer @MySQLGatewayTest to isolate JPA
  interactions without bringing up the full web context.
- For integration issues involving web layer or use cases, use @IntegrationTest or @E2ETest and
  enable [DEBUG_LOG] prints in tests when necessary.
- To avoid cross-test interference, keep using RepositoryCleanUpExtension and transactional test
  boundaries where appropriate.

CI/CD readiness

- The build supports an allTests task aggregating subprojects. Ensure your CI environment provides
  JDK 22 and Docker for DB-dependent tests.

Housekeeping

- Flyway clean is enabled; do not run it against any shared or production databases.
- Prefer Undertow-specific tuning if you adjust server settings; Tomcat is deliberately excluded.

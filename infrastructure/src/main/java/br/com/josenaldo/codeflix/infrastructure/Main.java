package br.com.josenaldo.codeflix.infrastructure;


import br.com.josenaldo.codeflix.application.UseCase;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        System.out.println(new UseCase().execute());
    }
}

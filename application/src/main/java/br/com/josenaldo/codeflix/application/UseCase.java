package br.com.josenaldo.codeflix.application;

public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN input);
}

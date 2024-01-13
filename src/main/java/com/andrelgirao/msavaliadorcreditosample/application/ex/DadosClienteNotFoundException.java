package com.andrelgirao.msavaliadorcreditosample.application.ex;

public class DadosClienteNotFoundException extends Exception {

    public DadosClienteNotFoundException() {
        super("Dados do cliente com cpf informado não encontrado.");
    }
}

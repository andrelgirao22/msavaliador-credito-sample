package com.andrelgirao.msavaliadorcreditosample.application.ex;

public class ErroComunicacaoMicroServicesException extends Exception {
    private int status;

    public ErroComunicacaoMicroServicesException(String message, int status) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

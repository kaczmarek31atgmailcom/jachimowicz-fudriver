package com.fungisearch.fudriver.exception;

public class AssignedOpenMonthsException extends RuntimeException {

    private static final long serialVersionUID = -4951930752848841683L;
    private String message;

    public AssignedOpenMonthsException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }
}


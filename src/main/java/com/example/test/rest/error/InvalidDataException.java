package com.example.test.rest.error;

public class InvalidDataException extends RuntimeException {

    private final String context;

    public InvalidDataException(String message) {
        this("entity", message);
    }

    public InvalidDataException(String context, String message) {
        this(context, message, null);
    }

    public InvalidDataException(String context, String message, Throwable t) {
        super(message, t);
        this.context = context;
    }

    public String getContext() {
        return context;
    }
}

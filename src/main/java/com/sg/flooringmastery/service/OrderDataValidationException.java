package com.sg.flooringmastery.service;

public class OrderDataValidationException extends RuntimeException {
    public OrderDataValidationException(String message) {
        super(message);
    }

    public OrderDataValidationException(String message, Throwable cause) {}
}

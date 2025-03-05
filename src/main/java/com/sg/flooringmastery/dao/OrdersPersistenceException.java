package com.sg.flooringmastery.dao;

public class OrdersPersistenceException extends RuntimeException {
    public OrdersPersistenceException(String message) {
        super(message);
    }

    public OrdersPersistenceException(String message, Throwable cause) {}
}

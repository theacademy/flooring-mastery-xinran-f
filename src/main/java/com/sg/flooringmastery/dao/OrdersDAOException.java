package com.sg.flooringmastery.dao;

public class OrdersDAOException extends RuntimeException {
    public OrdersDAOException(String message) {
        super(message);
    }

    public OrdersDAOException(String message, Throwable cause) {}
}

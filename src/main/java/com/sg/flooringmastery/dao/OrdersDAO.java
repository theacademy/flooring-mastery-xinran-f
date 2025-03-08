package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Orders;

import java.util.List;

public interface OrdersDAO {
    Orders addOrder(String orderDate, int orderNumber, Orders order, boolean createFileIfNotExists) throws OrdersDAOException;

    List<Orders> getAllOrders(String orderDate, boolean createFileIfNotExists) throws OrdersDAOException;

    Orders getOrder(String orderDate, int orderNumber) throws OrdersDAOException;

    Orders getOrderToBeEditedOrRemoved(String orderDate, int orderNumber) throws OrdersDAOException;

    void editOrder(Orders orderToEdit, String orderDate, boolean createFileIfNotExists) throws OrdersDAOException;

    Orders removeOrder(String orderDate, int orderNumber, boolean createFileIfNotExists) throws OrdersDAOException;

    void exportData() throws OrdersDAOException;
}

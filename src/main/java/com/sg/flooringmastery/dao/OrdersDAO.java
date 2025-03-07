package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Orders;

import java.util.List;

public interface OrdersDAO {
    Orders addOrder(String orderDate, int orderNumber, Orders order);

    List<Orders> getAllOrders(String orderDate);

    Orders getOrder(String orderDate, int orderNumber);

    Orders getOrderToBeEditedOrRemoved(String orderDate, int orderNumber);

    void editOrder(Orders orderToEdit, String orderDate);

    void removeOrder(String orderDate, int orderNumber);
}

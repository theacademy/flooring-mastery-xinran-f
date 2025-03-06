package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Orders;

import java.util.List;
import java.util.Map;

public interface OrdersDAO {
    Orders addOrder(String orderDate, int orderNumber, Orders order);

    List<Orders> getAllOrders(String orderDate);

    Orders getOrder(int orderNumber);

    Orders removeOrder(int orderNumber);
}

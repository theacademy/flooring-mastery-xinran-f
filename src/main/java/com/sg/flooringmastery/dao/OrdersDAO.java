package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Orders;
import com.sg.flooringmastery.dto.Products;

import java.util.List;

public interface OrdersDAO {
    Orders addOrder(int orderNumber, Orders order);

    List<Orders> getAllOrders();

    Orders getOrder(int orderNumber);

    Orders removeOrder(int orderNumber);
}

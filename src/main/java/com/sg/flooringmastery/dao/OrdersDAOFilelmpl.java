package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Orders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersDAOFilelmpl implements OrdersDAO {
    private Map<Integer, Orders> orders = new HashMap<>();

    @Override
    public Orders addOrder(int orderNumber, Orders order) {
        Orders prevOrder = orders.put(orderNumber, order);

        return prevOrder;
    }

    @Override
    public List<Orders> getAllOrders() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Orders getOrder(int orderNumber) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Orders removeOrder(int orderNumber) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

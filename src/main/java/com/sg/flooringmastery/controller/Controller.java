package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.OrdersDAO;
import com.sg.flooringmastery.dao.OrdersDAOFilelmpl;
import com.sg.flooringmastery.dto.Orders;
import com.sg.flooringmastery.service.ServiceLayer;
import com.sg.flooringmastery.service.ServiceLayerImpl;
import com.sg.flooringmastery.ui.UserIO;
import com.sg.flooringmastery.ui.UserIOConsoleImpl;
import com.sg.flooringmastery.ui.View;

import java.util.List;

public class Controller {
    private UserIO io = new UserIOConsoleImpl();
    private View view = new View();
    private ServiceLayer service = new ServiceLayerImpl();
    private OrdersDAO ordersDAO = new OrdersDAOFilelmpl();

    // TODO
    public Controller() {}

    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;

        while (keepGoing) {
            menuSelection = getMenuSelection();

            switch (menuSelection) {
                case 1:
                    listOrders();
                    break;
                case 2:
                    createOrder();
                    break;
                case 3:
                    io.print("EDIT AN ORDER");
                    break;
                case 4:
                    io.print("REMOVE AN ORDER");
                    break;
                case 5:
                    io.print("EXPORT ALL DATA");
                    break;
                case 6:
                    keepGoing = false;
                    break;
                default:
                    io.print("UNKNOWN COMMAND");
            }
        }

        io.print("PROGRAM TERMINATED SUCCESSFULLY");
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    private void listOrders() {
        String orderDate = io.readString("Please enter the order date (MMDDYYYY): ");
        List<Orders> orderList = ordersDAO.getAllOrders(orderDate);

        view.displayDisplayOrderListBanner();
        view.displayOrdersList(orderList);
    }

    public void createOrder() {
        view.displayCreateOrderBanner();

        Orders newOrder = view.getNewOrderInfo();

        newOrder.setMaterialCost(service.calculateMaterialCost(newOrder.getArea(), newOrder.getCostPerSquareFoot()));
        newOrder.setLaborCost(service.calculateLaborCost(newOrder.getArea(), newOrder.getLaborCostPerSquareFoot()));
        newOrder.setTax(service.calculateTax(newOrder.getMaterialCost(), newOrder.getLaborCost(), newOrder.getTaxRate()));
        newOrder.setTotal(service.calculateTotal(newOrder.getMaterialCost(), newOrder.getLaborCost(), newOrder.getTax()));

        ordersDAO.addOrder(newOrder.getOrderNumber(), newOrder);

        view.displayCreateOrderSuccessBanner();
    }


}

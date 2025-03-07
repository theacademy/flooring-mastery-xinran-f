package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.OrdersPersistenceException;
import com.sg.flooringmastery.model.Orders;
import com.sg.flooringmastery.model.Products;

import java.math.BigDecimal;
import java.util.List;

public interface ServiceLayer {
    // MaterialCost = (Area * CostPerSquareFoot)
    BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal costPerSquareFoot);

    // LaborCost = (Area * LaborCostPerSquareFoot)
    BigDecimal calculateLaborCost(BigDecimal area, BigDecimal laborCostPerSquareFoot);

    // Tax = (MaterialCost + LaborCost) * (TaxRate/100)
    BigDecimal calculateTax(BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate);

    // Total = (MaterialCost + LaborCost + Tax)
    BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax);

    void addOrder(String orderDate, int orderNumber, Orders order) throws
            OrderDataValidationException, OrdersPersistenceException;

    List<Orders> getAllOrders(String orderDate) throws OrdersPersistenceException;

    Orders getOrder(String orderDate, int orderNumber) throws OrdersPersistenceException;

    void editOrder(Orders orderToEdit, String orderDate) throws OrdersPersistenceException;

    void removeOrder(String orderDate, int orderNumber) throws OrdersPersistenceException;

    Orders getOrderToBeEditedOrRemoved(String orderDate, int orderNumber) throws OrdersPersistenceException;

    boolean validateOrderDate(String newOrderDate);

    boolean validateOrderCustomerName(String newOrderCustomerName);

    boolean validateOrderState(String newOrderState);

    boolean validateOrderProductNumber(List<Products> productsList, int newOrderProductType);

    boolean validateOrderArea(BigDecimal newOrderArea);

    boolean validatePlaceOrderSelection(String placeOrderSelection);

    boolean checkIfNewOrderIsReadyToBePlaced(String placeOrderSelection);

    boolean checkIfNewOrderDateExists(String newOrderDate);

    boolean validateOrderDateFormat(String orderDateToEdit);

    boolean validateEditOrRemoveOrderConfirmation(String editOrderSelection);

    void exportData();
}

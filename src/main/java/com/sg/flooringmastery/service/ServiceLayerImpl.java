package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.OrdersDAO;
import com.sg.flooringmastery.dao.OrdersPersistenceException;
import com.sg.flooringmastery.dao.ProductsDAO;
import com.sg.flooringmastery.dao.TaxDAO;
import com.sg.flooringmastery.model.Orders;
import com.sg.flooringmastery.model.Products;
import com.sg.flooringmastery.model.Tax;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class ServiceLayerImpl implements ServiceLayer {
    OrdersDAO ordersDAO;
    TaxDAO taxDAO;
    ProductsDAO productsDAO;

    public ServiceLayerImpl(OrdersDAO ordersDAO, ProductsDAO productsDAO, TaxDAO taxDAO) {
        this.ordersDAO = ordersDAO;
        this.productsDAO = productsDAO;
        this.taxDAO = taxDAO;
    }

    @Override
    public BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal costPerSquareFoot) {
        return area.multiply(costPerSquareFoot);
    }

    @Override
    public BigDecimal calculateLaborCost(BigDecimal area, BigDecimal laborCostPerSquareFoot) {
        return area.multiply(laborCostPerSquareFoot);
    }

    @Override
    public BigDecimal calculateTax(BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate) {
        return (materialCost.add(laborCost)).multiply(taxRate.divide(BigDecimal.valueOf(100)));
    }

    @Override
    public BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax) {
        return materialCost.add(laborCost).add(tax);
    }

    @Override
    public void addOrder(String orderDate, int orderNumber, Orders order) throws
            OrderDataValidationException, OrdersPersistenceException {
        ordersDAO.addOrder(orderDate, orderNumber, order, true);
    }

    @Override
    public List<Orders> getAllOrders(String orderDate) throws OrdersPersistenceException {
        return ordersDAO.getAllOrders(orderDate, false);
    }

    @Override
    public Orders getOrder(String orderDate, int orderNumber) throws OrdersPersistenceException {
        return ordersDAO.getOrder(orderDate, orderNumber);
    }

    @Override
    public void editOrder(Orders orderToEdit, String orderDate) throws OrdersPersistenceException {
        ordersDAO.editOrder(orderToEdit, orderDate, false);
    }

    @Override
    public void removeOrder(String orderDate, int orderNumber) throws OrdersPersistenceException {
        ordersDAO.removeOrder(orderDate, orderNumber, false);
    }

    @Override
    public Orders getOrderToBeEditedOrRemoved(String orderDate, int orderNumber) throws OrdersPersistenceException {
        return ordersDAO.getOrderToBeEditedOrRemoved(orderDate, orderNumber);
    }

    // must be in the future
    @Override
    public boolean validateOrderDate(String orderDate) {
        if (!validateOrderDateFormat(orderDate)) {
            return false;
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMddyyyy");
        LocalDate orderDateFormatted = LocalDate.parse(orderDate, dateFormatter);

        if (!orderDateFormatted.isAfter(LocalDate.now())) {
            return false;
        }

        return true;
    }

    @Override
    public boolean validateOrderDateFormat(String newOrderDate) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMddyyyy");

        try {
            LocalDate.parse(newOrderDate, dateFormatter);
        } catch (DateTimeParseException e) {
            return false;
        }

        return true;
    }

    // may not be blank and is limited to characters [a-z][0-9] as well as periods and comma characters.
    //  "Acme, Inc." is a valid name.
    @Override
    public boolean validateOrderCustomerName(String orderCustomerName) {
        if (orderCustomerName == null || orderCustomerName.trim().isEmpty()) {
            return false;
        }

        String customerNamePattern = "^[a-z0-9., ]+$";

        if (!orderCustomerName.matches(customerNamePattern)) {
            return false;
        }

        return true;
    }

    // Entered states must be checked against the tax file. If the state does not exist in the tax file,
    // we cannot sell there. If the tax file is modified to include the state,
    // it should be allowed without changing the application code.
    @Override
    public boolean validateOrderState(String orderState) {
        List<Tax> taxesList = taxDAO.getAllTaxes();
        String newOrderStateFormatted = orderState.substring(0, 1).toUpperCase() + orderState.substring(1);

        boolean stateFound = taxesList.stream()
                .anyMatch(tax -> newOrderStateFormatted.equals(tax.getStateName()));

        if (stateFound) {
            return true;
        }
        return false;
    }

    // Show a list of available products and pricing information to choose from.
    // Again, if a product is added to the file it should show up in the application without a code change.
    @Override
    public boolean validateOrderProductNumber(List<Products> productsList, int orderProductNumber) {
        int productsListSize = productsList.size();

        if (orderProductNumber <= 0 || orderProductNumber > productsListSize) {
            return false;
        }

        return true;
    }

    // The area must be a positive decimal. Minimum order size is 100 sq ft.
    @Override
    public boolean validateOrderArea(BigDecimal orderArea) {
        if (orderArea.compareTo(BigDecimal.ZERO) <= 0 || orderArea.compareTo(new BigDecimal("100")) < 0) {
            return false;
        }

        return true;
    }

    @Override
    public boolean validatePlaceOrderSelection(String placeOrderSelection) {
        if (!(placeOrderSelection.equals("Y") ||
                placeOrderSelection.equals("N"))) {
            return false;
        }

        return true;
    }

    @Override
    public boolean checkIfNewOrderIsReadyToBePlaced(String placeOrderSelection) {
        if (placeOrderSelection.equalsIgnoreCase("N")) {
            return false;
        }

        return true;
    }

    @Override
    public boolean checkIfNewOrderDateExists(String newOrderDate) {
        String orderFileName = "Orders_" + newOrderDate + ".txt";
        String orderFilePath = "./src/main/java/com/sg/flooringmastery/SampleFileData/Orders/" + orderFileName;

        Scanner scanner;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(orderFilePath)));
        } catch (FileNotFoundException e) {
            return false;
        }

        scanner.close();

        return true;
    }

    @Override
    public boolean validateEditOrRemoveOrderConfirmation(String editOrderSelection) {
        if (editOrderSelection.equalsIgnoreCase("N")) {
            return false;
        }

        return true;
    }

    @Override
    public void exportData() {
        ordersDAO.exportData();
    }
}

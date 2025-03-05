package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Orders;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;

public class OrdersDAOFilelmpl implements OrdersDAO {
    private Map<Integer, Orders> orders = new HashMap<>();

    public static final String DELIMETER = ",";

    @Override
    public Orders addOrder(int orderNumber, Orders order) {
        Orders prevOrder = orders.put(orderNumber, order);

        return prevOrder;
    }

    @Override
    public List<Orders> getAllOrders(String orderDate) {
        loadOrdersFileByDate(orderDate);

        return new ArrayList(orders.values());
    }

    @Override
    public Orders getOrder(int orderNumber) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Orders removeOrder(int orderNumber) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //TODO: to delete
    //06012013
    private Orders unmarshallOrder(String orderAsText) {
        String[] orderTokens = orderAsText.split(DELIMETER);

        // Index 0 - Order Number
        String orderNumber = orderTokens[0];
        int orderNumberInt = Integer.parseInt(orderNumber);

        Orders orderFromFile = new Orders(orderNumberInt);

        // Index 1 - Customer Name
        orderFromFile.setCustomerName(orderTokens[1]);

        // Index 2 - State
        orderFromFile.setState(orderTokens[2]);

        // Index 3 - Tax Rate
        orderFromFile.setTaxRate(new BigDecimal(orderTokens[3]));

        // Index 4 - Product Type
        orderFromFile.setProductType(orderTokens[4]);

        // Index 5 - Area
        orderFromFile.setArea(new BigDecimal(orderTokens[5]));

        // Index 6 - Cost Per Square Foot
        orderFromFile.setCostPerSquareFoot(new BigDecimal(orderTokens[6]));

        // Index 7 - Labor Cost Per Square Foot
        orderFromFile.setLaborCostPerSquareFoot(new BigDecimal(orderTokens[7]));

        // Index 8 - Material Cost
        orderFromFile.setMaterialCost(new BigDecimal(orderTokens[8]));

        // Index 9 - Labor Cost
        orderFromFile.setLaborCost(new BigDecimal(orderTokens[9]));

        // Index 10 - Tax
        orderFromFile.setTax(new BigDecimal(orderTokens[10]));

        // Index 11 - Total
        orderFromFile.setTotal(new BigDecimal(orderTokens[11]));

        return orderFromFile;
    }

    private void loadOrdersFileByDate(String ordersDate) {
        // TODO: Method to create?
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");
//        String formattedOrderDate = orderDate.format(formatter);

        String ordersFileName = generateOrdersFileName(ordersDate);
        String ordersFilePath = generateOrdersFilePath(ordersFileName);

        Scanner scanner;

        try {
            scanner = new Scanner(new BufferedReader(new FileReader(ordersFilePath)));
        } catch (FileNotFoundException e) {
            throw new OrdersPersistenceException(
                    "No order found on this date ", e);
        }

        String currentLine;
        Orders currentOrder;

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();

            // marshall if the line stars with an order number
            if (Character.isDigit(currentLine.charAt(0))) {
                currentOrder = unmarshallOrder(currentLine);
                orders.put(currentOrder.getOrderNumber(), currentOrder);
            }
        }

        scanner.close();
    }

    private String masharshallOrder(Orders order) {
        String orderAsText = order.getOrderNumber() + DELIMETER;
        orderAsText += order.getCustomerName() + DELIMETER;
        orderAsText += order.getState() + DELIMETER;
        orderAsText += order.getTaxRate() + DELIMETER;
        orderAsText += order.getProductType() + DELIMETER;
        orderAsText += order.getArea() + DELIMETER;
        orderAsText += order.getCostPerSquareFoot() + DELIMETER;
        orderAsText += order.getLaborCostPerSquareFoot() + DELIMETER;
        orderAsText += order.getMaterialCost() + DELIMETER;
        orderAsText += order.getLaborCost() + DELIMETER;
        orderAsText += order.getTax() + DELIMETER;
        orderAsText += order.getTotal() + DELIMETER;

        return orderAsText;
    }

    //Orders_MMDDYYYY.txt.
    private void writeOrdersToFile(String ordersDate) {
        String ordersFileName = generateOrdersFileName(ordersDate);
        String ordersFilePath = generateOrdersFilePath(ordersFileName);

        PrintWriter printWriter;

        try {
            printWriter = new PrintWriter(new FileWriter(ordersFilePath));
        } catch (IOException e) {
            throw new OrdersPersistenceException(
                    "Could not save order data.", e);
        }

        String orderAsText;
        List<Orders> ordersList = this.getAllOrders(ordersDate);

        for (Orders currentOrder :ordersList) {
            orderAsText = masharshallOrder(currentOrder);
            printWriter.println(orderAsText);
            printWriter.flush();
        }
        printWriter.close();
    }

    private String generateOrdersFileName(String ordersDate) {
        return "Orders_" + ordersDate + ".txt";
    }

    private String generateOrdersFilePath(String ordersFileName) {
        // TODO: to delete
        ///Users/sandy/IdeaProjects/flooring-mastery-xinran-f/src/main/java/com/sg/flooringmastery
        return "./src/main/java/com/sg/flooringmastery/SampleFileData/Orders/" + ordersFileName;
    }

}

package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Orders;
import org.springframework.core.annotation.Order;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class OrdersDAOFilelmpl implements OrdersDAO {
    private Map<Integer, Orders> orders = new HashMap<>();
    private final String ORDER_FILES_FOLDER;
    public static final String DELIMETER = ",";
    public static final String ORDER_HEADER = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area," +
            "CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total";

    public OrdersDAOFilelmpl(){
        ORDER_FILES_FOLDER = "./src/main/java/com/sg/flooringmastery/SampleFileData/Orders/";
    }

    @Override
    public Orders addOrder(String orderDate, int orderNumber, Orders order, boolean createFileIfNotExists) throws OrdersDAOException {
        loadOrdersFileByDate(orderDate, createFileIfNotExists);

        Orders newOrder = orders.put(orderNumber, order);

        writeOrdersToFileByDate(orderDate);

        // clear current orders to avoid displaying two times the same info after adding a new order
        orders.clear();

        return newOrder;
    }

    @Override
    public List<Orders> getAllOrders(String orderDate, boolean createFileIfNotExists) throws OrdersDAOException {
        loadOrdersFileByDate(orderDate, createFileIfNotExists);

        return new ArrayList(orders.values());
    }

    @Override
    public Orders getOrder(String orderDate, int orderNumber) throws OrdersDAOException {
        Orders order = new Orders();

        if (!checkIfOrderDateFileExists(orderDate)) {
            return order;
        }

        List<Orders> allOrdersFromThatDate = getAllOrders(orderDate, false);

        order = allOrdersFromThatDate.stream()
                .filter(o -> o.getOrderNumber() == orderNumber)
                .findFirst()
                .orElse(null);

        return order;
    }


    @Override
    public Orders getOrderToBeEditedOrRemoved(String orderDate, int orderNumber) {
        Orders orderToEdit = getOrder(orderDate, orderNumber);

        return orderToEdit;
    }

    @Override
    public void editOrder(Orders orderToEdit, String orderDate, boolean createFileIfNotExists) throws OrdersDAOException {
        loadOrdersFileByDate(orderDate, createFileIfNotExists);

        if (orders.containsKey(orderToEdit.getOrderNumber())) {
            orders.put(orderToEdit.getOrderNumber(), orderToEdit);
            writeOrdersToFileByDate(orderDate);
        }  else {
            throw new OrdersDAOException("Order does not exist.");
        }
    }

    @Override
    public Orders removeOrder(String orderDate, int orderNumber, boolean createFileIfNotExists) throws OrdersDAOException {
        loadOrdersFileByDate(orderDate, createFileIfNotExists);

        Orders removedOrder;

        if (orders.containsKey(orderNumber)) {
            removedOrder = orders.remove(orderNumber);
            writeOrdersToFileByDate(orderDate);
            return removedOrder;
        } else {
            throw new OrdersDAOException("Order does not exist.");
        }
    }

    @Override
    public void exportData() throws OrdersDAOException {
        final String BACKUP_DIRECTORY = "./src/main/java/com/sg/flooringmastery/SampleFileData/Backup/";

        // verify if backup folder exists
        File backupFolder = new File(BACKUP_DIRECTORY);

        if (!backupFolder.exists()) {
            backupFolder.mkdir();   // create the folder if it doesn't exist
        }

        File exportFile = new File(backupFolder, "DataExport.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(exportFile))) {
            writer.write(ORDER_HEADER);
            writer.newLine();

            // get all order files in the Orders directory
            File ordersFolder = new File(ORDER_FILES_FOLDER);
            File[] orderFiles = ordersFolder.listFiles((dir, name) -> name.endsWith(".txt"));

            if (orderFiles != null) {
                for (File orderFile : orderFiles) {
                    String orderDateAsString = orderFile.getName().substring(7, 15);  // get file date

                    // Clear orders map before loading a new file
                    orders.clear();

                    loadOrdersFile(orderFile.getPath(), true);

                    // parse the original string into a Date object
                    SimpleDateFormat originalDateFormat = new SimpleDateFormat("MMddyyyyy");
                    Date orderDate;

                    try {
                        orderDate = originalDateFormat.parse(orderDateAsString);
                    } catch (ParseException e) {
                        throw new OrdersDAOException("Error parsing order date: " + orderDateAsString, e);
                    }

                    // format the date into MM-dd-yyyy
                    SimpleDateFormat targetFormat = new SimpleDateFormat("MM-dd-yyyy");
                    String orderDateFormatted = targetFormat.format(orderDate);

                    orders.values().forEach(order -> {
                        try {
                            writer.write(String.join(DELIMETER,
                                    String.valueOf(order.getOrderNumber()),
                                    order.getCustomerName(),
                                    order.getState(),
                                    String.valueOf(order.getTaxRate()),
                                    order.getProductType(),
                                    String.valueOf(order.getArea()),
                                    String.valueOf(order.getCostPerSquareFoot()),
                                    String.valueOf(order.getLaborCostPerSquareFoot()),
                                    String.valueOf(order.getMaterialCost()),
                                    String.valueOf(order.getLaborCost()),
                                    String.valueOf(order.getTax()),
                                    String.valueOf(order.getTotal()),
                                    orderDateFormatted
                            ) + "\n");

                            writer.flush();
                        } catch (IOException e) {
                            throw new OrdersDAOException("Error exporting order: " + orderDateAsString, e);
                        }
                    });
                }
            } else {
                System.out.println("No order files found in the Orders directory.");
            }
        } catch (IOException e) {
            throw new OrdersDAOException("Error exporting orders: " + e.getMessage(), e);
        }
    }

    private void loadOrdersFile(String fileName, boolean createFileIfNotExists) throws OrdersDAOException {
        File file = new File(fileName);

        if (file.exists()) {
            loadExistingFile(file);
            return;
        } else if (createFileIfNotExists){
            createNewFile(file);
        }

        throw new OrdersDAOException("Error reading order file.");
    }

    private void loadExistingFile(File orderFile) throws OrdersDAOException {
        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader(orderFile)))) {
            String currentLine;
            Orders currentOrder;

            while (scanner.hasNextLine()) {
                currentLine = scanner.nextLine();

                // unmarshall if the line starts with a digit
                if (!currentLine.isEmpty() && Character.isDigit(currentLine.charAt(0))) {
                    currentOrder = unmarshallOrder(currentLine);
                    orders.put(currentOrder.getOrderNumber(), currentOrder);
                }
            }
        } catch (IOException e) {
            throw new OrdersDAOException("Error reading order file.", e);
        }
    }

    private void createNewFile(File orderFile) throws OrdersDAOException {
        try {
            orderFile.createNewFile();
        } catch (IOException e) {
            throw new OrdersDAOException("Error creating order file: " + orderFile.getName(), e);
        }
    }

    private void loadOrdersFileByDate(String ordersDate, boolean createFileIfNotExists) {
        String ordersFileName = generateOrdersFileName(ordersDate);
        String ordersFilePath = generateOrdersFilePath(ordersFileName);

        loadOrdersFile(ordersFilePath, createFileIfNotExists);
    }

    public boolean checkIfOrderDateFileExists(String orderDate) {
        String orderFileName = generateOrdersFileName(orderDate);
        String orderFilePath = generateOrdersFilePath(orderFileName);

        File file = new File(orderFilePath);

        if (!file.exists()) {
            return false;
        }

        return true;
    }

    private Orders unmarshallOrder(String orderAsText) {
        String[] orderTokens = orderAsText.split(DELIMETER);
        Orders orderFromFile = new Orders();

        String orderNumber = orderTokens[0];
        int orderNumberInt = Integer.parseInt(orderNumber);

        // Index 0 - Order Number
        orderFromFile.setOrderNumber(orderNumberInt);

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
        orderAsText += order.getTotal();

        return orderAsText;
    }

    //Orders_MMDDYYYY.txt.
    private void writeOrdersToFileByDate(String ordersDate) throws OrdersDAOException {
        String ordersFileName = generateOrdersFileName(ordersDate);
        String ordersFilePath = generateOrdersFilePath(ordersFileName);

        PrintWriter printWriter = null;

        try {
            File file = new File(ordersFilePath);

            // check if the file exists
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    throw new IOException("Failed to create new file: " + ordersFilePath);
                }
            }

            printWriter = new PrintWriter(new FileWriter(file));

            String orderAsText;
            List<Orders> ordersList = this.getAllOrders(ordersDate, true);

            printWriter.println(ORDER_HEADER);

            for (Orders currentOrder : ordersList) {
                orderAsText = masharshallOrder(currentOrder);
                printWriter.println(orderAsText);
                printWriter.flush();
            }
        } catch (IOException e) {
            throw new OrdersDAOException("Could not save order data.", e);
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    private String generateOrdersFileName(String ordersDate) {
        return "Orders_" + ordersDate + ".txt";
    }

    private String generateOrdersFilePath(String ordersFileName) {
        return ORDER_FILES_FOLDER + ordersFileName;
    }
}

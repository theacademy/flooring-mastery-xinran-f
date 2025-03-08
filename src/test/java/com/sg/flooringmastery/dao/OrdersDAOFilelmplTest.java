package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Orders;
import org.junit.jupiter.api.*;
import org.springframework.core.annotation.Order;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrdersDAOFilelmplTest {

    OrdersDAO testOrdersDAO = new OrdersDAOFilelmpl();
    private final String ORDER_FILE_PATH = "./src/main/java/com/sg/flooringmastery/SampleFileData/Orders/Orders_05052025.txt";

    public OrdersDAOFilelmplTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterEach
    public void tearDown() {
        File file = new File(ORDER_FILE_PATH);

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("Test file deleted successfully.");
            } else {
                System.out.println("Failed to delete the test file.");
            }
        }
    }

//    @BeforeEach
//    public void setUp() throws IOException {
//        String testFile = "./src/test/java/com/sg/flooringmastery/dao/Orders_01012025.txt";
//
//        // use the FileWriter to quickly blank the file
//        new FileWriter(testFile);
//        testOrdersDAO = new OrdersDAOFilelmpl();
//    }
//
//    @AfterEach
//    public void tearDown() {
//        testOrdersDAO = null;
//    }

    @Test
    void addOrder() throws Exception {
        String orderDate = "05052025";

        Orders firstOrder = new Orders();
        firstOrder.setCustomerName("ziggs");
        firstOrder.setState("texas");
        firstOrder.setTaxRate(new BigDecimal(25.00));
        firstOrder.setProductType("Carpet");
        firstOrder.setArea(new BigDecimal(249.00));
        firstOrder.setCostPerSquareFoot(new BigDecimal(3.50));
        firstOrder.setLaborCostPerSquareFoot(new BigDecimal(4.15));
        firstOrder.setMaterialCost(new BigDecimal(871.50));
        firstOrder.setLaborCost(new BigDecimal(1033.35));
        firstOrder.setTax(new BigDecimal(476.21));
        firstOrder.setTotal(new BigDecimal(2381.06));

//        Orders secondOrder = new Orders();
//        secondOrder.setCustomerName("jhin");
//        secondOrder.setState("calfornia");
//        secondOrder.setTaxRate(new BigDecimal(10));
//        secondOrder.setProductType("Tile");
//        secondOrder.setArea(new BigDecimal(249.00));
//        secondOrder.setCostPerSquareFoot(new BigDecimal(3.50));
//        secondOrder.setLaborCostPerSquareFoot(new BigDecimal(4.15));
//        secondOrder.setMaterialCost(new BigDecimal(871.50));
//        secondOrder.setLaborCost(new BigDecimal(1033.35));
//        secondOrder.setTax(new BigDecimal(476.21));
//        secondOrder.setTotal(new BigDecimal(2381.06));

        testOrdersDAO.addOrder(orderDate, 1, firstOrder, true);


        //assertEquals(removedOrder, firstOrder, "The removed order's customer name should be Ziggs.");
    }

    @Test
    void getAllOrders() throws Exception {
    }


    @Test
    void editOrder() throws Exception {
    }

    @Test
    void testRemoveOrder() throws Exception {
        String orderDate = "05052025";

        // create an order
        Orders order = new Orders();
        order.setCustomerName("ziggs");
        order.setState("texas");
        order.setTaxRate(new BigDecimal(25.00).setScale(2, RoundingMode.HALF_UP));
        order.setProductType("Carpet");
        order.setArea(new BigDecimal(249.00).setScale(2, RoundingMode.HALF_UP));
        order.setCostPerSquareFoot(new BigDecimal(3.50).setScale(2, RoundingMode.HALF_UP));
        order.setLaborCostPerSquareFoot(new BigDecimal(4.15).setScale(2, RoundingMode.HALF_UP));
        order.setMaterialCost(new BigDecimal(871.50));
        order.setLaborCost(new BigDecimal(1033.35).setScale(2, RoundingMode.HALF_UP));
        order.setTax(new BigDecimal(476.21).setScale(2, RoundingMode.HALF_UP));
        order.setTotal(new BigDecimal(2381.06).setScale(2, RoundingMode.HALF_UP));

        testOrdersDAO.addOrder(orderDate, 1, order, true);

        Orders removedOrder = testOrdersDAO.removeOrder(orderDate, 1, false);

        assertEquals(removedOrder, order, "The removed order's customer name should be Ziggs.");
    }

    @Test
    void exportData() throws Exception {
    }
}
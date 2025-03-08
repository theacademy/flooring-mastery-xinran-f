package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.model.Orders;
import org.junit.jupiter.api.*;
import org.springframework.core.annotation.Order;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrdersDAOFilelmplTest {

    OrdersDAO testOrdersDAO;

    public OrdersDAOFilelmplTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() throws IOException {
        String testFile = "./src/test/java/com/sg/flooringmastery/dao/Orders_01012025.txt";

        // use the FileWriter to quickly blank the file
        new FileWriter(testFile);
        testOrdersDAO = new OrdersDAOFilelmpl();
    }

    @AfterEach
    public void tearDown() {
        testOrdersDAO = null;
    }

    @Test
    void addOrder() throws Exception {
        //
    }

    @Test
    void getAllOrders() throws Exception {
    }

    @Test
    void getOrder() throws Exception{
    }

    @Test
    void editOrder() throws Exception {
    }

    @Test
    void testRemoveOrder() throws Exception {
        String orderDate = "05052025";

        // create two new orders
        Orders firstOrder = new Orders();
        firstOrder.setCustomerName("ziggs");
        firstOrder.setState("texas");
        firstOrder.setTaxRate(new BigDecimal(25.00));
        firstOrder.setProductType("Carpet");
        firstOrder.setArea(new BigDecimal(249.00));
        firstOrder.setCostPerSquareFoot(new BigDecimal(3.50));
        firstOrder.setLaborCostPerSquareFoot(new BigDecimal(4.15));
        firstOrder.setMaterialCost(new BigDecimal(871.50));
        firstOrder.setLaborCostPerSquareFoot(new BigDecimal(1033.35));
        firstOrder.setTax(new BigDecimal(476.21));
        firstOrder.setTotal(new BigDecimal(2381.06));

        Orders secondOrder = new Orders();
        secondOrder.setCustomerName("jhin");
        secondOrder.setState("calfornia");
        secondOrder.setTaxRate(new BigDecimal(10));
        secondOrder.setProductType("Tile");
        secondOrder.setArea(new BigDecimal(249.00));
        secondOrder.setCostPerSquareFoot(new BigDecimal(3.50));
        secondOrder.setLaborCostPerSquareFoot(new BigDecimal(4.15));
        secondOrder.setMaterialCost(new BigDecimal(871.50));
        secondOrder.setLaborCostPerSquareFoot(new BigDecimal(1033.35));
        secondOrder.setTax(new BigDecimal(476.21));
        secondOrder.setTotal(new BigDecimal(2381.06));

        testOrdersDAO.addOrder(orderDate, 1, firstOrder, true);
        testOrdersDAO.addOrder(orderDate, 2, secondOrder, true);

        Orders removedOrder = testOrdersDAO.removeOrder(orderDate, 1, false);

        assertEquals(removedOrder, firstOrder, "The removed order's customer name should be Ziggs.");
    }

    @Test
    void exportData() throws Exception {
    }
}
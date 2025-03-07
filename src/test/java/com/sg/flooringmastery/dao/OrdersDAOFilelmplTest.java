package com.sg.flooringmastery.dao;

import org.junit.jupiter.api.*;

import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

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
    void removeOrder() throws Exception {
    }

    @Test
    void exportData() throws Exception {
    }
}
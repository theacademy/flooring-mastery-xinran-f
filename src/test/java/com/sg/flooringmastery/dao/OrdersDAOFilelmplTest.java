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
        String testFile = "testOrders.txt";

        // use the FileWriter to quickly blank the file
        new FileWriter(testFile);
        testOrdersDAO = new OrdersDAOFilelmpl();
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    void addOrder() {
    }

    @Test
    void getAllOrders() {
    }

    @Test
    void getOrder() {
    }

    @Test
    void getOrderToBeEditedOrRemoved() {
    }

    @Test
    void editOrder() {
    }

    @Test
    void removeOrder() {
    }

    @Test
    void exportData() {
    }

    @Test
    void checkIfOrderDateFileExists() {
    }
}
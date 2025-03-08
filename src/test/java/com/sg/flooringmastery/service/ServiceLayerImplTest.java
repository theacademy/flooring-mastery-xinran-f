package com.sg.flooringmastery.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class ServiceLayerImplTest {

    ServiceLayer testService = new ServiceLayerImpl();

    @Test
    void testCalculateMaterialCost() {
        BigDecimal area = BigDecimal.valueOf(100);
        BigDecimal costPerSquareFoot = BigDecimal.valueOf(5);

        // MaterialCost = (Area * CostPerSquareFoot)
        BigDecimal expectedMaterialCost = BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_UP);

        BigDecimal result = testService.calculateMaterialCost(area, costPerSquareFoot);

        assertEquals(expectedMaterialCost, result, "Material cost calculation is incorrect");
    }

    @Test
    void testCalculateLaborCost() {
        BigDecimal area = BigDecimal.valueOf(200);
        BigDecimal laborCostPerSquareFoot = BigDecimal.valueOf(3);

        // LaborCost = (Area * LaborCostPerSquareFoot)
        BigDecimal expectedLaborCost = BigDecimal.valueOf(600).setScale(2, RoundingMode.HALF_UP);

        BigDecimal result = testService.calculateLaborCost(area, laborCostPerSquareFoot);

        assertEquals(expectedLaborCost, result, "Labor cost calculation is incorrect");
    }

    @Test
    void testCalculateTax() {
        BigDecimal materialCost = BigDecimal.valueOf(500);
        BigDecimal laborCost = BigDecimal.valueOf(500);
        BigDecimal taxRate = BigDecimal.valueOf(4);

        // Tax = (MaterialCost + LaborCost) * (TaxRate/100)
        BigDecimal expectedTax = BigDecimal.valueOf(40).setScale(2);

        BigDecimal result = testService.calculateTax(materialCost, laborCost, taxRate);

        assertEquals(expectedTax, result, "Tax calculation is incorrect");
    }

    @Test
    void testCalculateTotal() {
        BigDecimal materialCost = BigDecimal.valueOf(500);
        BigDecimal laborCost = BigDecimal.valueOf(300);
        BigDecimal tax = BigDecimal.valueOf(50);

        // Total = (MaterialCost + LaborCost + Tax)
        BigDecimal expectedTotal = BigDecimal.valueOf(850).setScale(2, RoundingMode.HALF_UP);

        BigDecimal result = testService.calculateTotal(materialCost, laborCost, tax);

        assertEquals(expectedTotal, result, "Total cost calculation is incorrect");
    }

    @Test
    void testValidateOrderDate_ValidDate() {
        String orderDate = "12312025";
        assertTrue(testService.validateOrderDate(orderDate));
    }

    @Test
    void testValidateOrderDate_InvalidDateFormat() {
        String orderDate = "20251231";
        assertFalse(testService.validateOrderDate(orderDate));
    }

    @Test
    void testValidateOrderDate_PastDate() {
        String orderDate = "12012021"; // December 1, 2021 (a past date)
        assertFalse(testService.validateOrderDate(orderDate));
    }

    @Test
    void testValidateOrderDateFormat_ValidFormat() {
        String orderDate = "12312025";
        assertTrue(testService.validateOrderDateFormat(orderDate));
    }

    @Test
    void testValidateOrderDateFormat_InvalidFormat() {
        String orderDate = "31-12-2025"; // Invalid format
        assertFalse(testService.validateOrderDateFormat(orderDate));
    }

    @Test
    void testValidateOrderCustomerName_ValidName() {
        String orderCustomerName = "annie";
        assertTrue(testService.validateOrderCustomerName(orderCustomerName));
    }

    @Test
    void testValidateOrderCustomerName_InvalidName() {
        String orderCustomerName = "A!";
        assertFalse(testService.validateOrderCustomerName(orderCustomerName));
    }

    @Test
    void testValidateOrderArea_ValidArea() {
        BigDecimal orderArea = BigDecimal.valueOf(150);
        assertTrue(testService.validateOrderArea(orderArea));
    }

    @Test
    void testValidateOrderArea_InvalidArea() {
        BigDecimal orderArea = BigDecimal.valueOf(50);
        assertFalse(testService.validateOrderArea(orderArea));
    }

    @Test
    void testValidatePlaceOrderSelection_ValidSelection() {
        String placeOrderSelection = "Y";
        assertTrue(testService.validatePlaceOrderSelection(placeOrderSelection));
    }

    @Test
    void testValidatePlaceOrderSelection_InvalidSelection() {
        String placeOrderSelection = "X";
        assertFalse(testService.validatePlaceOrderSelection(placeOrderSelection));
    }

    @Test
    void testCheckIfNewOrderIsReadyToBePlaced_Yes() {
        String placeOrderSelection = "Y";
        assertTrue(testService.checkIfNewOrderIsReadyToBePlaced(placeOrderSelection));
    }

    @Test
    void testCheckIfNewOrderIsReadyToBePlaced_No() {
        String placeOrderSelection = "N";
        assertFalse(testService.checkIfNewOrderIsReadyToBePlaced(placeOrderSelection));
    }

    @Test
    void testValidateEditOrRemoveOrderConfirmation_Yes() {
        String editOrderSelection = "Y";
        assertTrue(testService.validateEditOrRemoveOrderConfirmation(editOrderSelection));
    }

    @Test
    void testValidateEditOrRemoveOrderConfirmation_No() {
        String editOrderSelection = "N";
        assertFalse(testService.validateEditOrRemoveOrderConfirmation(editOrderSelection));
    }
}
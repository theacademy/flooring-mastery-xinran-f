package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dto.Products;

import java.math.BigDecimal;
import java.util.List;

public interface ServiceLayer {
    BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal costPerSquareFoot);

    BigDecimal calculateLaborCost(BigDecimal area, BigDecimal laborCostPerSquareFoot);

    BigDecimal calculateTax(BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate);

    BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax);

    boolean validateNewOrderDate(String newOrderDate);

    boolean validateNewOrderCustomerName(String newOrderCustomerName);

    boolean validateNewOrderState(String newOrderState);

    boolean validateNewOrderProductNumber(List<Products> productsList, int newOrderProductType);

    boolean validateNewOrderArea(BigDecimal newOrderArea);

    boolean validatePlaceOrderSelection(String placeOrderSelection);

    boolean checkIfNewOrderIsReadyToBePlaced(String placeOrderSelection);

    boolean checkIfNewOrderDateExists(String newOrderDate);
}

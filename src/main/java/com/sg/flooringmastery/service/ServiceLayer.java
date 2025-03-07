package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dto.Products;

import java.math.BigDecimal;
import java.util.List;

public interface ServiceLayer {
    BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal costPerSquareFoot);

    BigDecimal calculateLaborCost(BigDecimal area, BigDecimal laborCostPerSquareFoot);

    BigDecimal calculateTax(BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate);

    BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax);

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

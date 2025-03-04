package com.sg.flooringmastery.service;

import java.math.BigDecimal;

public interface ServiceLayer {
    BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal costPerSquareFoot);

    BigDecimal calculateLaborCost(BigDecimal area, BigDecimal laborCostPerSquareFoot);

    BigDecimal calculateTax(BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate);

    BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax);
}

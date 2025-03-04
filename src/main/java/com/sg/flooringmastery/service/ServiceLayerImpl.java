package com.sg.flooringmastery.service;

import java.math.BigDecimal;

public class ServiceLayerImpl implements ServiceLayer {

    @Override
    public BigDecimal calculateMaterialCost(BigDecimal area, BigDecimal costPerSquareFoot) {
        return area.multiply(costPerSquareFoot);
    }

    @Override
    public BigDecimal calculateLaborCost(BigDecimal area, BigDecimal laborCostPerSquareFoot) {
        return area.multiply(laborCostPerSquareFoot);
    }

    @Override
    public BigDecimal calculateTax(BigDecimal materialCost, BigDecimal laborCost, BigDecimal taxRate) {
        return (materialCost.add(laborCost)).multiply(taxRate.divide(BigDecimal.valueOf(100)));
    }

    @Override
    public BigDecimal calculateTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax) {
        return materialCost.add(laborCost).add(tax);
    }
}

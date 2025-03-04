package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.dto.Orders;

import java.math.BigDecimal;

public class View {
    private UserIO io = new UserIOConsoleImpl();

    public int printMenuAndGetSelection() {
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.print("* <<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Export All Data");
        io.print("* 6. Quit");
        io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");

        return io.readInt("Please select from the"
                + " above choices.", 1, 6);
    }

    public Orders getNewOrderInfo() {
        int orderNumber = io.readInt("Please enter the Order Number: ");
        String customerName = io.readString("Please enter the Customer Name: ");
        String state = io.readString("Please enter the State: ");
        BigDecimal taxRate = BigDecimal.valueOf(io.readDouble("Please enter the Tax Rate: "));
        String productType = io.readString("Please enter the Product Type: ");
        BigDecimal area = BigDecimal.valueOf(io.readDouble("Please enter the Area: "));
        BigDecimal costPerSquareFoot = BigDecimal.valueOf(io.readDouble("Please enter the Cost Per Square Foot: "));
        BigDecimal laborCostPerSquareFoot = BigDecimal.valueOf(io.readDouble("Please enter the Labor Cost Per Square Foot: " ));

        // TODO: call service layer?
        Orders currentOrder = new Orders(orderNumber);
        currentOrder.setCustomerName(customerName);
        currentOrder.setState(state);
        currentOrder.setTaxRate(taxRate);
        currentOrder.setProductType(productType);
        currentOrder.setArea(area);
        currentOrder.setCostPerSquareFoot(costPerSquareFoot);
        currentOrder.setLaborCostPerSquareFoot(laborCostPerSquareFoot);

        return currentOrder;
    }

    public void displayCreateOrderBanner() {
        io.print("=== Create Order ===");
    }

    public void displayCreateOrderSuccessBanner() {
        io.readString("Order successfully added. Please hit enter to continue.");
    }


}

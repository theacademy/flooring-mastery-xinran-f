package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.dto.Orders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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

    /* Display orders will ask the user for a date and then display the orders for that date.
    If no orders exist for that date, it will display an error message and return the user to the main menu.
     */
    public void displayOrdersList(List<Orders> ordersList) {
        for (Orders currentOrder : ordersList) {
            String orderInfo = String.format("Order #%d | Customer Name: %s | State: %s | Tax Rate: %.2f | " +
                            "Product Type: %s | Area: %.2f | Cost Per Square Foot: %.2f | Labor Cost Per Square Foot : %.2f | " +
                            "Material Cost: %.2f | Labor Cost: %.2f | Tax: %.2f | Total: $%.2f",
                    currentOrder.getOrderNumber(), currentOrder.getCustomerName(), currentOrder.getState(),
                    currentOrder.getTaxRate(), currentOrder.getProductType(), currentOrder.getArea(),
                    currentOrder.getCostPerSquareFoot(), currentOrder.getLaborCostPerSquareFoot(),
                    currentOrder.getMaterialCost(), currentOrder.getLaborCost(), currentOrder.getTax(),
                    currentOrder.getTotal());

            io.print(orderInfo);
        }
        io.readString("Please hit enter to continue.");
    }

    public void displayDisplayOrderListBanner() {
        io.print("=== Display All Orders ===");
    }

    public Orders getNewOrderInfo() {
        //String orderDate = io.readString("Please enter the order date (MMDDYYYY): ");

        String customerName = io.readString("Please enter the Customer Name: ");
        String state = io.readString("Please enter the State: ");
        String productType = io.readString("Please enter the Product Type: ");
        BigDecimal area = BigDecimal.valueOf(io.readDouble("Please enter the Area: "));

        // TODO: to delete
        int orderNumber = io.readInt("Please enter the Order Number: ");
        BigDecimal taxRate = BigDecimal.valueOf(io.readDouble("Please enter the Tax Rate: "));
        BigDecimal costPerSquareFoot = BigDecimal.valueOf(io.readDouble("Please enter the Cost Per Square Foot: "));
        BigDecimal laborCostPerSquareFoot = BigDecimal.valueOf(io.readDouble("Please enter the Labor Cost Per Square Foot: " ));

        Orders currentOrder = new Orders(orderNumber);
        currentOrder.setCustomerName(customerName);
        currentOrder.setState(state);
        currentOrder.setProductType(productType);
        currentOrder.setArea(area);

        // TODO: to delete
        currentOrder.setTaxRate(taxRate);
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

package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.dto.Orders;
import com.sg.flooringmastery.dto.Products;

import java.math.BigDecimal;
import java.util.List;

public class View {
    private UserIO io = new UserIOConsoleImpl();

    public int printMenuAndGetSelection() {
        io.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        io.println("* <<Flooring Program>>");
        io.println("* 1. Display Orders");
        io.println("* 2. Add an Order");
        io.println("* 3. Edit an Order");
        io.println("* 4. Remove an Order");
        io.println("* 5. Export All Data");
        io.println("* 6. Quit");
        io.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");

        return io.readInt("Please select from the"
                + " above choices.", 1, 6);
    }

    public void displayDisplayOrderListBanner() {
        io.println("=== Display All Orders ===");
    }

    public String getOrderDate() {
        return io.readString("Please enter the order date (MMDDYYYY): ");
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

            io.println(orderInfo);
        }

        io.readString("Please hit enter to continue.");
    }

    public void displayCreateOrderBanner() {
        io.println("=== Create Order ===");
    }

    public void displayCreateOrderSuccessBanner() {
        io.readString("Order successfully added. Please hit enter to continue.");
    }

    public String getNewOrderDate() {
        return io.readString("Please enter the order date (MMDDYYYY): ");
    }

    public void displayNewOrderDateErrorMessage() {
        io.println("Please enter a valid order date. It must be in the future and follow the format: MMDDYYYYY.");
    }

    public String getNewOrderCustomerName() {
        return io.readString("Please enter the customer name: ");
    }

    public void displayNewOrderCustomerNameErrorMessage() {
        io.println("Please enter a valid customer name that is not blank and only contains " +
                "lowercase letters (a-z), digits (0-9), periods (.), and commas (,).");
    }

    public String getNewOrderState() {
        return io.readString("Please enter the state: ");
    }

    public void displayNewOrderStateNoExistsMessage() {
        io.println("The state does not exist in the tax file, we cannot sell there.");
    }

    public int displayAvailableProductsAndGetNewOrderSelection(List<Products> productsList) {
        int numberOfProducts = 1;

        for (Products product : productsList) {
            String productInfo = String.format("#%d | Product Type: %s | " +
                    "Cost Per Square Foot: %.2f | Labor Cost Per Square Foot: %.2f",
                    numberOfProducts, product.getProductType(),
                    product.getCostPerSquareFoot(), product.getLaborCostPerSquareFoot());

            io.println(productInfo);
            numberOfProducts++;
        }

        return io.readInt("Please select the product: ");
    }

    public void displayAvailableProductTypesBanner() {
        io.println("=== Available Products ===");
    }

    public void displayNewOrderProductNumberErrorMessage() {
        io.println("Please enter a valid product number.");
    }

    public String getNewOrderArea() {
        return io.readString("Please enter the area: ");
    }

    public void displayNewOrderAreaErrorMessage(){
        io.println("The area must be a positive decimal. Minimum order size is 100 sq ft.");
    }

    public String displayCurrentOrderInfoAndGetSelection(String newOrderDate, String newOrderCustomerName, String newOrderState,
                                                         BigDecimal newOrderTaxRate, int newOrderProductNumber, BigDecimal newOrderArea, BigDecimal newOrderCostPerSquareFoot,
                                                         BigDecimal newOrderLaborCostPerSquareFoot, BigDecimal newOrderMaterialCost, BigDecimal newOrderLaborCost,
                                                         BigDecimal newOrderTax, BigDecimal newOrderTotal) {
        String currentOrderInfo = String.format("Order Date: %s | Customer Name: %s | State: %s | Tax Rate: %.2f | " +
                        "Product Type: %s | Area: %.2f | Cost Per Square Foot: %.2f | Labor Cost Per Square Foot : %.2f | " +
                        "Material Cost: %.2f | Labor Cost: %.2f | Tax: %.2f | Total: $%.2f",
                newOrderDate, newOrderCustomerName, newOrderState, newOrderTaxRate,
                newOrderProductNumber, newOrderArea, newOrderCostPerSquareFoot, newOrderLaborCostPerSquareFoot,
                newOrderMaterialCost, newOrderLaborCost, newOrderTax, newOrderTotal);

        io.println(currentOrderInfo);

        return io.readString("Do you want to place the order? (Y/N)");
    }

    public void displayPlaceOrderErrorMessage(){
        io.println("Please enter 'Y' or 'N' to make your selection.");
    }

}

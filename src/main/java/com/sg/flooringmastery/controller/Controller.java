package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.*;
import com.sg.flooringmastery.dto.Orders;
import com.sg.flooringmastery.dto.Products;
import com.sg.flooringmastery.dto.Tax;
import com.sg.flooringmastery.service.ServiceLayer;
import com.sg.flooringmastery.service.ServiceLayerImpl;
import com.sg.flooringmastery.ui.UserIO;
import com.sg.flooringmastery.ui.UserIOConsoleImpl;
import com.sg.flooringmastery.ui.View;

import java.math.BigDecimal;
import java.util.List;

public class Controller {
    private UserIO io = new UserIOConsoleImpl();
    private View view = new View();
    private OrdersDAO ordersDAO = new OrdersDAOFilelmpl();
    private ProductsDAO productsDAO = new ProductsDAOFilelmpl();
    private TaxDAO taxDAO = new TaxDAOFilelmpl();
    private ServiceLayer service = new ServiceLayerImpl(ordersDAO, productsDAO, taxDAO);

    // TODO
    public Controller() {}

    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;

        while (keepGoing) {
            menuSelection = getMenuSelection();

            switch (menuSelection) {
                case 1:
                    listOrders();
                    break;
                case 2:
                    createOrder();
                    break;
                case 3:
                    io.println("EDIT AN ORDER");
                    break;
                case 4:
                    io.println("REMOVE AN ORDER");
                    break;
                case 5:
                    io.println("EXPORT ALL DATA");
                    break;
                case 6:
                    keepGoing = false;
                    break;
                default:
                    io.println("UNKNOWN COMMAND");
            }
        }

        io.println("PROGRAM TERMINATED SUCCESSFULLY");
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    private void listOrders() {
        String orderDate = view.getOrderDate();
        List<Orders> orderList = ordersDAO.getAllOrders(orderDate);

        view.displayDisplayOrderListBanner();
        view.displayOrdersList(orderList);
    }

    public void createOrder() {
        view.displayCreateOrderBanner();

        String newOrderDate = "";
        int newOrderNumber = 0;
        String newOrderCustomerName = "";
        String newOrderStateNonFormatted = "";
        String newOrderStateFormatted = "";
        BigDecimal newOrderTaxRate = BigDecimal.ZERO;
        int newOrderProductNumber = 0;
        BigDecimal newOrderArea = BigDecimal.ZERO;
        BigDecimal newOrderCostPerSquareFoot = BigDecimal.ZERO;
        BigDecimal newOrderLaborCostPerSquareFoot = BigDecimal.ZERO;
        BigDecimal newOrderMaterialCost;
        BigDecimal newOrderLaborCost;
        BigDecimal newOrderTax;
        BigDecimal newOrderTotal;

        // validate new order date
        boolean isInputValid = false;

        while (!isInputValid) {
            newOrderDate = view.getNewOrderDate();
            isInputValid = service.validateNewOrderDate(newOrderDate);

            if (!isInputValid) {
                view.displayNewOrderDateErrorMessage();
            }
        }

        // validate new order customer name
        isInputValid = false;

        while (!isInputValid) {
            newOrderCustomerName = view.getNewOrderCustomerName();
            // capitalize the first letter of customer name
            newOrderCustomerName = newOrderCustomerName.substring(0, 1).toUpperCase() + newOrderCustomerName.substring(1);
            isInputValid = service.validateNewOrderCustomerName(newOrderCustomerName);

            if (!isInputValid) {
                view.displayNewOrderCustomerNameErrorMessage();
            }
        }

        // validate new order state
        isInputValid = false;

        while (!isInputValid) {
            newOrderStateNonFormatted = view.getNewOrderState();
            newOrderStateFormatted = newOrderStateNonFormatted.substring(0, 1).toUpperCase() +
                    newOrderStateNonFormatted.substring(1);
            isInputValid = service.validateNewOrderState(newOrderStateFormatted);

            if (!isInputValid) {
                view.displayNewOrderStateNoExistsMessage();
            }
        }

        // TODO: Tax rates are stored as whole numbers??
        // retrieve tax rate
        List<Tax> taxList = taxDAO.getAllTaxes();
        Tax taxSelected = new Tax();

        for (Tax tax : taxList) {
            if (newOrderStateFormatted.equals(tax.getState())) {
                taxSelected = tax;
            }
        }

        newOrderTaxRate = taxSelected.getTaxRate();

        // validate new order product type
        isInputValid = false;
        List<Products> productsList = productsDAO.getAllProducts();

        while (!isInputValid) {
            view.displayAvailableProductTypesBanner();

            newOrderProductNumber = view.displayAvailableProductsAndGetNewOrderSelection(productsList);
            isInputValid = service.validateNewOrderProductNumber(productsList, newOrderProductNumber);

            if (!isInputValid) {
                view.displayNewOrderProductNumberErrorMessage();
            }
        }

        // retrieve product's cost per square foot and labor cost per square foot
        int selectedProductIndex = newOrderProductNumber - 1;
        Products selectedProduct = productsList.get(selectedProductIndex);

        newOrderCostPerSquareFoot = selectedProduct.getCostPerSquareFoot();
        newOrderLaborCostPerSquareFoot = selectedProduct.getLaborCostPerSquareFoot();

        // validate new order area
        isInputValid = false;

        while (!isInputValid) {
            String newOrderAreaString = view.getNewOrderArea();
            newOrderArea = new BigDecimal(newOrderAreaString);
            isInputValid = service.validateNewOrderArea(newOrderArea);

            if (!isInputValid) {
                view.displayNewOrderAreaErrorMessage();
            }
        }

        newOrderMaterialCost = service.calculateMaterialCost(newOrderArea, newOrderCostPerSquareFoot);
        newOrderLaborCost = service.calculateLaborCost(newOrderArea, newOrderCostPerSquareFoot);
        newOrderTax = service.calculateTax(newOrderMaterialCost, newOrderLaborCost, newOrderTaxRate);
        newOrderTotal = service.calculateTotal(newOrderMaterialCost, newOrderLaborCost, newOrderTax);

        //
        isInputValid = false;
        String placeOrderSelection = "";

        while (!isInputValid) {
            placeOrderSelection = view.displayCurrentOrderInfoAndGetSelection(newOrderDate, newOrderCustomerName,
                    newOrderStateNonFormatted, newOrderTaxRate, newOrderProductNumber, newOrderArea, newOrderCostPerSquareFoot,
                    newOrderLaborCostPerSquareFoot, newOrderMaterialCost, newOrderLaborCost, newOrderTax, newOrderTotal);

            isInputValid = service.validatePlaceOrderSelection(placeOrderSelection);

            if (!isInputValid) {
                view.displayPlaceOrderErrorMessage();
            }
        }

        // return to the main menu if order not ready to be placed
        boolean isOrderReadyToBePlaced = service.checkIfNewOrderIsReadyToBePlaced(placeOrderSelection);

        if (!isOrderReadyToBePlaced) {
            return;
        }

        // if orders of this date exists, append new order to the existed file
        boolean isFileWithNewOrderDateExisted = service.checkIfNewOrderDateExists(newOrderDate);
        int numberOfExistingOrders = 0;
        List<Orders> orderList;

        // set new order
        Orders newOrder = new Orders();
        newOrder.setCustomerName(newOrderCustomerName);
        newOrder.setState(newOrderStateFormatted);
        newOrder.setTaxRate(newOrderTaxRate);
        newOrder.setProductType(selectedProduct.getProductType());
        newOrder.setArea(newOrderArea);
        newOrder.setCostPerSquareFoot(newOrderCostPerSquareFoot);
        newOrder.setLaborCostPerSquareFoot(newOrderLaborCostPerSquareFoot);
        newOrder.setMaterialCost(newOrderMaterialCost);
        newOrder.setLaborCost(newOrderLaborCost);
        newOrder.setTax(newOrderTax);
        newOrder.setTotal(newOrderTotal);

        // set new order number
        if (isFileWithNewOrderDateExisted) {
            // append
            orderList = ordersDAO.getAllOrders(newOrderDate);
            numberOfExistingOrders = orderList.size();
            newOrderNumber = numberOfExistingOrders + 1;
            newOrder.setOrderNumber(newOrderNumber);
        } else {
            // create a new file with new order starting at order number 1
            newOrder.setOrderNumber(1);
        }

        ordersDAO.addOrder(newOrderDate, newOrderNumber, newOrder);

        view.displayCreateOrderSuccessBanner();
    }


}

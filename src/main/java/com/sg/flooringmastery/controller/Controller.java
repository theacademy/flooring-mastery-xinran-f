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
                    editOrder();
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
        String newOrderProductType = "";
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
            isInputValid = service.validateOrderDate(newOrderDate);

            if (!isInputValid) {
                view.displayNewOrderDateErrorMessage();
            }
        }

        // validate new order customer name
        isInputValid = false;

        while (!isInputValid) {
            newOrderCustomerName = view.getNewOrderCustomerName();
            isInputValid = service.validateOrderCustomerName(newOrderCustomerName);

            if (!isInputValid) {
                view.displayOrderCustomerNameErrorMessage();
            }
        }

        // capitalize the first letter of customer name
        newOrderCustomerName = newOrderCustomerName.substring(0, 1).toUpperCase() + newOrderCustomerName.substring(1);

        // validate new order state
        isInputValid = false;

        while (!isInputValid) {
            newOrderStateNonFormatted = view.getNewOrderState();
            newOrderStateFormatted = newOrderStateNonFormatted.substring(0, 1).toUpperCase() +
                    newOrderStateNonFormatted.substring(1);
            isInputValid = service.validateOrderState(newOrderStateFormatted);

            if (!isInputValid) {
                view.displayOrderStateNoExistsMessage();
            }
        }

        // TODO: Tax rates are stored as whole numbers??
        // retrieve tax rate
        List<Tax> taxList = taxDAO.getAllTaxes();
        Tax selectedTax = new Tax();

        for (Tax tax : taxList) {
            if (newOrderStateFormatted.equals(tax.getState())) {
                selectedTax = tax;
            }
        }

        newOrderTaxRate = selectedTax.getTaxRate();

        // validate new order product type
        isInputValid = false;
        List<Products> productsList = productsDAO.getAllProducts();

        while (!isInputValid) {
            view.displayAvailableProductTypesBanner();

            newOrderProductNumber = view.displayAvailableProductsAndGetNewOrderSelection(productsList);
            isInputValid = service.validateOrderProductNumber(productsList, newOrderProductNumber);

            if (!isInputValid) {
                view.displayOrderProductNumberErrorMessage();
            }
        }

        // retrieve product's cost per square foot and labor cost per square foot
        int selectedProductIndex = newOrderProductNumber - 1;
        Products selectedProduct = productsList.get(selectedProductIndex);
        newOrderProductType = selectedProduct.getProductType();

        newOrderCostPerSquareFoot = selectedProduct.getCostPerSquareFoot();
        newOrderLaborCostPerSquareFoot = selectedProduct.getLaborCostPerSquareFoot();

        // validate new order area
        isInputValid = false;

        while (!isInputValid) {
            String newOrderAreaString = view.getOrderArea();
            newOrderArea = new BigDecimal(newOrderAreaString);
            isInputValid = service.validateOrderArea(newOrderArea);

            if (!isInputValid) {
                view.displayOrderAreaErrorMessage();
            }
        }

        newOrderMaterialCost = service.calculateMaterialCost(newOrderArea, newOrderCostPerSquareFoot);
        newOrderLaborCost = service.calculateLaborCost(newOrderArea, newOrderLaborCostPerSquareFoot);
        newOrderTax = service.calculateTax(newOrderMaterialCost, newOrderLaborCost, newOrderTaxRate);
        newOrderTotal = service.calculateTotal(newOrderMaterialCost, newOrderLaborCost, newOrderTax);

        //
        isInputValid = false;
        String placeOrderSelection = "";

        while (!isInputValid) {
            view.displayCurrentOrderInfo(newOrderDate, newOrderCustomerName,
                    newOrderStateNonFormatted, newOrderTaxRate, newOrderProductType, newOrderArea,
                    newOrderCostPerSquareFoot, newOrderLaborCostPerSquareFoot, newOrderMaterialCost,
                    newOrderLaborCost, newOrderTax, newOrderTotal);

            placeOrderSelection = view.getPlaceOrderSelection();

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
        //int numberOfExistingOrders = 0;
        int numberOfLastOrderInFile = 0;
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
            numberOfLastOrderInFile = orderList.get(orderList.size() - 1).getOrderNumber();
            newOrderNumber = numberOfLastOrderInFile + 1;

            //numberOfExistingOrders = orderList.size();
            //newOrderNumber = numberOfExistingOrders + 1;
            newOrder.setOrderNumber(newOrderNumber);
        } else {
            // create a new file with new order starting at order number 1
            newOrder.setOrderNumber(1);
        }

        ordersDAO.addOrder(newOrderDate, newOrderNumber, newOrder);

        view.displayCreateOrderSuccessBanner();
    }


    public void editOrder() {
        view.displayEditOrderBanner();
        view.diplayEditOrderMessage();

        String orderDateToEdit = view.getOrderDateToEdit();
        boolean isOrderDateToEditValid =  service.validateOrderDateFormat(orderDateToEdit);

        if (!isOrderDateToEditValid) {
            view.displayOrderDateToEditInvalidMessage();
            return;
        }

        int orderNumberToEdit = view.getOrderNumberToEdit();


        //boolean isOrderNumberToEditValid = service.validateOrderNumberToEdit();

//
//        if (!isOrderDateToEditValid) {
//            view.displayOrderDateToEditInexistMessage();
//        }


        Orders orderToEdit = ordersDAO.getOrderToBeEdited(orderDateToEdit, orderNumberToEdit);

        if (orderToEdit == null) {
            view.displayOrderDateToEditInexistMessage();
            return;
        }

        // asking user for each piece of order data but display the existing data
        view.displayOrder(orderToEdit);


        // edit customer name
        String updatedCustomerName = "";
        boolean isInputValid = false;

        while (!isInputValid) {
            updatedCustomerName = view.getUpdatedCustomerName(orderToEdit.getCustomerName());

            if (updatedCustomerName.isEmpty()) {
                break;
            }

            isInputValid = service.validateOrderCustomerName(updatedCustomerName);

            if (!isInputValid) {
                view.displayOrderCustomerNameErrorMessage();
                continue;
            }

            orderToEdit.setCustomerName(updatedCustomerName.substring(0, 1).toUpperCase() + updatedCustomerName.substring(1));
        }

        boolean needsRecalculation = false;

        // edit state
        String updatedState = "";
        isInputValid = false;

        while (!isInputValid) {
            updatedState = view.getUpdatedState(orderToEdit.getState());

            // if empty, leave the existing data in place
            if (updatedState.isEmpty()) {
                break;
            }

            isInputValid = service.validateOrderState(updatedState);

            if (!isInputValid) {
                view.displayOrderStateNoExistsMessage();
                continue;
            }

            orderToEdit.setState(updatedState);

            // update tax
            List<Tax> taxList = taxDAO.getAllTaxes();
            Tax selectedTax = new Tax();

            for (Tax tax : taxList) {
                if (updatedState.equals(tax.getState())) {
                    selectedTax = tax;
                }
            }

            BigDecimal updatedTaxRate = selectedTax.getTaxRate();
            orderToEdit.setTaxRate(updatedTaxRate);

            needsRecalculation = true;
        }



        // edit product type
        int updatedProductNumber = 0;
        isInputValid = false;

        List<Products> productsList = productsDAO.getAllProducts();
        String updatedProductNumberAsString = "";
        String updatedProductType = "";

        while (!isInputValid) {
            view.displayAvailableProductTypesBanner();
            view.displayAvailableProducts(productsList);

            updatedProductNumberAsString = view.getUpdatedProductNumberInString(orderToEdit.getProductType());

            // if empty, leave the existing data in place
            if (updatedProductNumberAsString.isEmpty()) {
                break;
            }

            updatedProductNumber = Integer.parseInt(updatedProductNumberAsString);
            isInputValid = service.validateOrderProductNumber(productsList, updatedProductNumber);

            if (!isInputValid) {
                view.displayOrderStateNoExistsMessage();
                continue;
            }

            int selectedProductIndex = updatedProductNumber - 1;
            Products updatedProduct = productsList.get(selectedProductIndex);
            orderToEdit.setProductType(updatedProduct.getProductType());

            // set product's cost per square foot and labor cost per square foot
            orderToEdit.setCostPerSquareFoot(updatedProduct.getCostPerSquareFoot());
            orderToEdit.setLaborCostPerSquareFoot(updatedProduct.getLaborCostPerSquareFoot());

            needsRecalculation = true;
        }


        // edit area
        String updatedAreaAsString = "";
        BigDecimal updatedArea = BigDecimal.ZERO;
        isInputValid = false;

        while (!isInputValid) {
            updatedAreaAsString = view.getUpdatedArea(orderToEdit.getArea().toString());

            // if empty, leave the existing data in place
            if (updatedAreaAsString.isEmpty()) {
                break;
            }

            updatedArea = new BigDecimal(updatedAreaAsString);
            isInputValid = service.validateOrderArea(updatedArea);

            if (!isInputValid) {
                view.displayOrderAreaErrorMessage();
                continue;
            }

            orderToEdit.setArea(updatedArea);
            needsRecalculation = true;
        }

        if (needsRecalculation) {
            BigDecimal updatedOrderMaterialCost = service.calculateMaterialCost(orderToEdit.getArea(), orderToEdit.getCostPerSquareFoot());
            orderToEdit.setMaterialCost(updatedOrderMaterialCost);
            BigDecimal updatedOrderLaborCost = service.calculateLaborCost(orderToEdit.getArea(), orderToEdit.getLaborCostPerSquareFoot());
            orderToEdit.setLaborCost(updatedOrderLaborCost);
            BigDecimal updatedOrderTax = service.calculateTax(updatedOrderMaterialCost, updatedOrderLaborCost, orderToEdit.getTaxRate());
            orderToEdit.setTax(updatedOrderTax);
            BigDecimal updatedOrderTotal = service.calculateTotal(updatedOrderMaterialCost, updatedOrderLaborCost, updatedOrderTax);
            orderToEdit.setTotal(updatedOrderTotal);
        }

        view.displayCurrentOrderInfo(orderDateToEdit, orderToEdit.getCustomerName(),
                orderToEdit.getState(), orderToEdit.getTaxRate(), orderToEdit.getProductType(),
                orderToEdit.getArea(), orderToEdit.getCostPerSquareFoot(), orderToEdit.getLaborCostPerSquareFoot(),
                orderToEdit.getMaterialCost(), orderToEdit.getLaborCost(), orderToEdit.getTax(), orderToEdit.getTotal());

        String editOrderSelection = view.getEditOrderSelection();

        // return to the main menu if order not ready to be updated
        boolean isOrderReadyToBeUpdated = service.checkIfOrderIsReadyToBeUpdated(editOrderSelection);

        if (!isOrderReadyToBeUpdated) {
            return;
        }

        // update order
        ordersDAO.editOrder(orderToEdit, orderDateToEdit);

        view.displayEditOrderSuccessBar();
    }
}

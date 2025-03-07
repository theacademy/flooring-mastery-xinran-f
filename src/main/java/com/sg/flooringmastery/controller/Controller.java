package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.*;
import com.sg.flooringmastery.model.Orders;
import com.sg.flooringmastery.model.Products;
import com.sg.flooringmastery.model.Tax;
import com.sg.flooringmastery.service.ServiceLayer;
import com.sg.flooringmastery.view.View;

import java.math.BigDecimal;
import java.util.List;

public class Controller {
    private ServiceLayer service;
    private View view;
    private OrdersDAO ordersDAO;
    private ProductsDAO productsDAO;
    private TaxDAO taxDAO;

    public Controller(ServiceLayer service, View view, OrdersDAO ordersDAO, ProductsDAO productsDAO, TaxDAO taxDAO) {
        this.service = service;
        this.view = view;
        this.ordersDAO = ordersDAO;
        this.productsDAO = productsDAO;
        this.taxDAO = taxDAO;
    }

    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;

        try {
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
                        removeOrder();
                        break;
                    case 5:
                        exportData();
                        break;
                    case 6:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }
            }

            exitMessage();

        } catch (OrdersDAOException e) {
            view.displayErrorMessageBanner();
            view.displayErrorMessage(e.getMessage());
        }
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    private void listOrders() throws OrdersDAOException {
        String orderDate = view.getOrderDate();

        boolean isOrderDateToEditValid =  service.validateOrderDateFormat(orderDate);

        if (!isOrderDateToEditValid) {
            view.displayErrorMessageBanner();
            view.displayErrorMessage("Order date is in an incorrect format. " +
                    "Please ensure the date is in the correct format (MMDDYYYY).");
            return;
        }

        List<Orders> orderList = service.getAllOrders(orderDate);

        if (orderList.isEmpty()) {
            view.displayErrorMessageBanner();
            view.displayErrorMessage("No orders found for date: " + orderDate);
            return;
        }

        view.displayDisplayOrderListBanner();
        view.displayOrdersList(orderList);
    }

    public void createOrder() throws OrdersDAOException {
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

            if (newOrderDate.equals("")) {
                view.displayErrorMessageBanner();
                view.displayNewOrderDateErrorMessage();
                continue;
            }

            isInputValid = service.validateOrderDate(newOrderDate);

            if (!isInputValid) {
                view.displayErrorMessageBanner();
                view.displayNewOrderDateErrorMessage();
            }
        }

        // validate new order customer name
        isInputValid = false;

        while (!isInputValid) {
            newOrderCustomerName = view.getNewOrderCustomerName();

            if (newOrderCustomerName.equals("")) {
                view.displayErrorMessageBanner();
                view.displayOrderCustomerNameErrorMessage();
                continue;
            }

            isInputValid = service.validateOrderCustomerName(newOrderCustomerName);

            if (!isInputValid) {
                view.displayErrorMessageBanner();
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
                view.displayErrorMessageBanner();
                view.displayOrderStateNoExistsMessage();
            }
        }

        // retrieve tax rate
        List<Tax> taxList = taxDAO.getAllTaxes();
        Tax selectedTax = new Tax();
        final String state = newOrderStateFormatted;

        selectedTax = taxList.stream()
                .filter(tax -> state.equals(tax.getStateName()))
                .findFirst()
                .orElse(null);

        newOrderTaxRate = selectedTax.getTaxRate();

        // validate new order product type
        isInputValid = false;
        List<Products> productsList = productsDAO.getAllProducts();

        while (!isInputValid) {
            view.displayAvailableProductTypesBanner();

            newOrderProductNumber = view.displayAvailableProductsAndGetNewOrderSelection(productsList);
            isInputValid = service.validateOrderProductNumber(productsList, newOrderProductNumber);

            if (!isInputValid) {
                view.displayErrorMessageBanner();
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

            if (newOrderAreaString.equals("")) {
                view.displayErrorMessageBanner();
                view.displayOrderAreaErrorMessage();
                continue;
            }

            newOrderArea = new BigDecimal(newOrderAreaString);
            isInputValid = service.validateOrderArea(newOrderArea);

            if (!isInputValid) {
                view.displayErrorMessageBanner();
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
                view.displayErrorMessageBanner();
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
            orderList = service.getAllOrders(newOrderDate);
            numberOfLastOrderInFile = orderList.get(orderList.size() - 1).getOrderNumber();
            newOrderNumber = numberOfLastOrderInFile + 1;

            //numberOfExistingOrders = orderList.size();
            //newOrderNumber = numberOfExistingOrders + 1;
            newOrder.setOrderNumber(newOrderNumber);
        } else {
            // create a new file with new order starting at order number 1
            newOrder.setOrderNumber(1);
        }

        service.addOrder(newOrderDate, newOrderNumber, newOrder);

        view.displayCreateOrderSuccessBanner();
    }


    public void editOrder() throws OrdersDAOException {
        view.displayEditOrderBanner();
        view.diplayEditOrderMessage();

        String orderDateToEdit = view.getOrderDateToEdit();
        boolean isOrderDateToEditValid =  service.validateOrderDateFormat(orderDateToEdit);

        if (!isOrderDateToEditValid) {
            view.displayOrderDateErrorMessage();
            return;
        }

        int orderNumberToEdit = view.getOrderNumberToEdit();
        Orders orderToEdit = service.getOrderToBeEditedOrRemoved(orderDateToEdit, orderNumberToEdit);

        if (orderToEdit == null) {
            view.displayOrderFileInexistMessage();
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
                view.displayErrorMessageBanner();
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
                view.displayErrorMessageBanner();
                view.displayOrderStateNoExistsMessage();
                continue;
            }

            orderToEdit.setState(updatedState.substring(0, 1).toUpperCase() + updatedState.substring(1));

            // update tax rate
            List<Tax> taxList = taxDAO.getAllTaxes();
            BigDecimal updatedTaxRate = BigDecimal.ZERO;

            for (Tax tax : taxList) {
                if (orderToEdit.getState().equals(tax.getStateName())) {
                    updatedTaxRate = tax.getTaxRate();
                    break;
                }
            }

            orderToEdit.setTaxRate(updatedTaxRate);

            needsRecalculation = true;
        }

        // edit product type
        int updatedProductNumber = 0;
        isInputValid = false;

        List<Products> productsList = productsDAO.getAllProducts();
        String updatedProductNumberAsString = "";

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
                view.displayErrorMessageBanner();
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
                view.displayErrorMessageBanner();
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
        boolean isOrderReadyToBeUpdated = service.validateEditOrRemoveOrderConfirmation(editOrderSelection);

        if (!isOrderReadyToBeUpdated) {
            view.displayEditOrderCancelledMessage();
            return;
        }

        // update order
        service.editOrder(orderToEdit, orderDateToEdit);

        view.displayEditOrderSuccessBar();
    }

    public void removeOrder() throws OrdersDAOException {
        view.displayRemoveOrderBanner();

        // get the order date and validate it
        String orderDate = view.getOrderDateToRemove();
        boolean isOrderDateValid = service.validateOrderDateFormat(orderDate);

        if (!isOrderDateValid) {
            view.displayErrorMessageBanner();
            view.displayOrderDateErrorMessage();
            return;
        }

        // get the order number
        int orderNumber = view.getOrderNumberToRemove();
        Orders orderToRemove = service.getOrderToBeEditedOrRemoved(orderDate, orderNumber);

        // check if the order exists
        if (orderToRemove == null) {
            view.displayErrorMessageBanner();
            view.displayOrderFileInexistMessage();
            return;
        }

        // display order details
        view.displayOrder(orderToRemove);

        String confirmation = view.getRemoveOrderConfirmation();

        boolean isOrderReadyToBeremoved = service.validateEditOrRemoveOrderConfirmation(confirmation);

        if (!isOrderReadyToBeremoved) {
            view.displayRemoveOrderCancelledMessage();
            return;
        }

        // remove the order
        service.removeOrder(orderDate, orderNumber);
        view.displayRemoveOrderSuccessMessage();
    }

    private void exportData() throws OrdersDAOException {
        try {
            service.exportData();
            view.displayExportSuccessMessage();
        } catch (OrdersPersistenceException e) {
            throw new OrdersPersistenceException("An error occurred while exporting the data.", e);
        }
    }

    private void unknownCommand() {
        view.displayErrorMessageBanner();
        view.displayUnknownCommandErrorMessage();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }
}

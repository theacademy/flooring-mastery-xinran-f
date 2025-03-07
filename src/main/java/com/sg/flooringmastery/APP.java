package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.Controller;
import com.sg.flooringmastery.dao.*;
import com.sg.flooringmastery.service.ServiceLayer;
import com.sg.flooringmastery.service.ServiceLayerImpl;
import com.sg.flooringmastery.ui.UserIO;
import com.sg.flooringmastery.ui.UserIOConsoleImpl;
import com.sg.flooringmastery.ui.View;

public class APP {
    public static void main(String[] args) {
        UserIO io = new UserIOConsoleImpl();
        View view = new View();
        OrdersDAO ordersDAO = new OrdersDAOFilelmpl();
        ProductsDAO productsDAO = new ProductsDAOFilelmpl();
        TaxDAO taxDAO = new TaxDAOFilelmpl();
        ServiceLayer service = new ServiceLayerImpl(ordersDAO, productsDAO, taxDAO);
        Controller controller = new Controller(service, view, ordersDAO, productsDAO, taxDAO);
        controller.run();
    }
}

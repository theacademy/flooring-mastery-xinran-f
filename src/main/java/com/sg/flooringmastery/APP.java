package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.Controller;
import com.sg.flooringmastery.dao.*;
import com.sg.flooringmastery.service.ServiceLayer;
import com.sg.flooringmastery.service.ServiceLayerImpl;
import com.sg.flooringmastery.ui.UserIO;
import com.sg.flooringmastery.ui.UserIOConsoleImpl;
import com.sg.flooringmastery.ui.View;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class APP {
    public static void main(String[] args) {
//        UserIO io = new UserIOConsoleImpl();
//        View view = new View(io);
//        OrdersDAO ordersDAO = new OrdersDAOFilelmpl();
//        ProductsDAO productsDAO = new ProductsDAOFilelmpl();
//        TaxDAO taxDAO = new TaxDAOFilelmpl();
//        ServiceLayer service = new ServiceLayerImpl(ordersDAO, productsDAO, taxDAO);
//        Controller controller = new Controller(service, view, ordersDAO, productsDAO, taxDAO);
//        controller.run();


        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        Controller controller =
                ctx.getBean("controller", Controller.class);
        controller.run();
    }
}

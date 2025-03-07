package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.Controller;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class APP {
    public static void main(String[] args) {
        ApplicationContext ctx =
                new ClassPathXmlApplicationContext("applicationContext.xml");
        Controller controller =
                ctx.getBean("controller", Controller.class);
        controller.run();
    }
}

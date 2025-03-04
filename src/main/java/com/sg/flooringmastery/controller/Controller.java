package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.ui.UserIO;
import com.sg.flooringmastery.ui.UserIOConsoleImpl;
import com.sg.flooringmastery.ui.View;

public class Controller {
    private UserIO io = new UserIOConsoleImpl();
    private View view = new View();

    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;

        while (keepGoing) {
            menuSelection = getMenuSelection();

            switch (menuSelection) {
                case 1:
                    io.print("DISPLAY ORDERS");
                    break;
                case 2:
                    io.print("ADD AN ORDER");
                    break;
                case 3:
                    io.print("EDIT AN ORDER");
                    break;
                case 4:
                    io.print("REMOVE AN ORDER");
                    break;
                case 5:
                    io.print("EXPORT ALL DATA");
                    break;
                case 6:
                    keepGoing = false;
                    break;
                default:
                    io.print("UNKNOWN COMMAND");
            }
        }

        io.print("PROGRAM TERMINATED SUCCESSFULLY");
    }

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }
}

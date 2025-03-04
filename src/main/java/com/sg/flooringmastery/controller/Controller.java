package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.ui.UserIO;
import com.sg.flooringmastery.ui.UserIOConsoleImpl;

public class Controller {
    private UserIO io = new UserIOConsoleImpl();

    public void run() {
        boolean keepGoing = true;
        int menuSelection = 0;

        while (keepGoing) {
            io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
            io.print("* <<Flooring Program>>");
            io.print("* 1. Display Orders");
            io.print("* 2. Add an Order");
            io.print("* 3. Edit an Order");
            io.print("* 4. Remove an Order");
            io.print("* 5. Export All Data");
            io.print("* 6. Quit");
            io.print("*");
            io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");

            menuSelection = io.readInt("Please select from the"
                    + " above choices.", 1, 6);

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
        
        io.print("PROGRAM TERMINATED SUCCESSFULLY.");
    }
}

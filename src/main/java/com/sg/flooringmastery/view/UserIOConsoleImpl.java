package com.sg.flooringmastery.view;

import java.util.Scanner;

public class UserIOConsoleImpl implements UserIO {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void print(String message) {
        System.out.print(message);
    }

    @Override
    public void println(String message) {
        System.out.println(message);
    }

    @Override
    public String readString(String prompt) {
        System.out.println(prompt);

        return scanner.nextLine();
    }

    @Override
    public int readInt(String prompt) {
        while (true) {
            try {
                String input = readString(prompt);
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                print("Invalid input. Please enter a valid integer.");
            }
        }
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        int value;

        do {
            value = readInt(prompt);

            if (value < min || value > max) {
                print("Input must be between " + min + " and " + max + ". Please try again.");
            }
        } while (value < min || value > max);

        return value;
    }

    @Override
    public double readDouble(String prompt) {
        while (true) {
            try {
                String input = readString(prompt);
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                print("Invalid input. Please enter a valid double.");
            }
        }
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        double value;

        do {
            value = readDouble(prompt);

            if (value < min || value > max) {
                print("Input must be between " + min + " and " + max + ". Please try again.");
            }
        } while (value < min || value > max);

        return value;
    }

    @Override
    public float readFloat(String prompt) {
        while (true) {
            try {
                String input = readString(prompt);

                return Float.parseFloat(input);
            } catch (NumberFormatException e) {
                print("Invalid input. Please enter a valid float.");
            }
        }
    }

    @Override
    public float readFloat(String prompt, float min, float max) {
        float value;

        do {
            value = readFloat(prompt);

            if (value < min || value > max) {
                print("Input must be between " + min + " and " + max + ". Please try again.");
            }
        } while (value < min || value > max);

        return value;
    }

    @Override
    public long readLong(String prompt) {
        while (true) {
            try {
                String input = readString(prompt);
                return Long.parseLong(input);
            } catch (NumberFormatException e) {
                print("Invalid input. Please enter a valid long integer.");
            }
        }
    }

    @Override
    public long readLong(String prompt, long min, long max) {
        long value;

        do {
            value = readLong(prompt);

            if (value < min || value > max) {
                print("Input must be between " + min + " and " + max + ". Please try again.");
            }
        } while (value < min || value > max);

        return value;
    }
}

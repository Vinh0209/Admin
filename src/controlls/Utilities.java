/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlls;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 *
 * @author BAO TRAN
 */
public class Utilities {

    private static Scanner sc = new Scanner(System.in);

    public static int getInt(String msg, int min, int max) {
        int number = 0;

        while (true) {
            try {
                System.out.print(msg + "  ");
                number = Integer.parseInt(sc.nextLine());
                if (number >= min && number < max) {
                    return number;
                }

            } catch (NumberFormatException e) {
                System.out.println();
            }
        }

    }

    public static double getDouble(String msg, double min, double max) {
        double number = 0;
        boolean isValid = false;

        do {
            try {
                System.out.println(msg);
                number = Double.parseDouble(sc.nextLine());

                if (number > min && number < max) {
                    isValid = true;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        } while (!isValid);

        return number;
    }

    public static String getString(String msg, int option, int min, int max) {

        String s = "";
        while (true) {
            try {
                System.out.println(msg);
                s = sc.nextLine();

                if (option == 1) {
                    if (!s.isEmpty() && s.length() > min && s.length() < max) {
                        return s;
                    }
                }
                if (option == 2) {

                    if (s.isEmpty()) {
                        System.out.println("Please enter a string of at least 1 character!!!");
                    }
                }
            } catch (Exception e) {
                System.out.println("PLease enter the valid information!");
            }

        }
    }

    public static String inputDate(String msg, int option) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate now = LocalDate.now();
        LocalDate check = null;
        String input = "";

        do {
            try {
                System.out.println(msg);
                input = sc.nextLine().trim();

                if (input.isEmpty() || input.equals("")) {
                    return null;
                }

                check = LocalDate.parse(input, formatter);

                if (option == 1) {
                    if (check.isAfter(now)) {
                        System.out.println("Please input a date before the current date!");
                        continue;
                    }
                }

                return check.format(formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Please input a valid date (dd/MM/yyyy).");
            }
        } while (true);
    }

}

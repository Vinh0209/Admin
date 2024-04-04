/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controlls.ProductManager;
import controlls.ReportManager;
import controlls.Utilities;
import java.io.IOException;
import java.text.ParseException;
import controlls.WarehouseManager;

/**
 *
 * @author BAO TRAN
 */
public class Main {

    int choice;
    int choice1;

    public static void main(String[] args) {
        Main main = new Main();
        main.showMenu();
    }

    public void showMenu() {
        ProductManager productManager = new ProductManager();
        WarehouseManager warehouseManager = new WarehouseManager();
        ReportManager reportManager = new ReportManager();

        do {
            Menu menu = new Menu("----------- Main Menu ------------");
            menu.addNewOptiont("1. Manage products");
            menu.addNewOptiont("2. Manage Warehouse");
            menu.addNewOptiont("3. Report");
            menu.addNewOptiont("4. Store data to files");
            menu.addNewOptiont("5. Close the application");
            menu.printMenu();

            choice = menu.getChoice();
            System.out.println("------------------------------");

            if (choice == 1) {

                do {
                    Menu menu1 = new Menu("1. Manage products");
                    menu1.addNewOptiont("     1.1. Add a product");
                    menu1.addNewOptiont("     1.2. Update product information");
                    menu1.addNewOptiont("     1.3. Delete product");
                    menu1.addNewOptiont("     1.4. Show all product");
                    menu1.addNewOptiont("     1.5. Back to main menu");
                    menu1.printMenu();

                    choice1 = menu1.getChoice();

                    if (choice1 == 1) {
                        productManager.addProduct();
                    }
                    if (choice1 == 2) {
                        productManager.updateProduct();
                    }
                    if (choice1 == 3) {
                        productManager.deleteProduct();
                    }
                    if (choice1 == 4) {

                        productManager.showAllProducts();
                    }

                } while (choice1 != 5);
            }

            if (choice == 2) {
                choice1 = -3;
                do {
                    Menu menu2 = new Menu("2. Manage Warehouse");
                    menu2.addNewOptiont("     2.1. Create an import receipt");
                    menu2.addNewOptiont("     2.2. Create an export receipt");
                    menu2.addNewOptiont("     2.3. Back to main menu");
                    menu2.printMenu();

                    choice1 = menu2.getChoice();

                    if (choice1 == 1) {
                        try {
                            warehouseManager.createImport();
                        } catch (IOException ex) {
                        }
                    }
                    if (choice1 == 2) {
                        try {
                            warehouseManager.createExport();
                        } catch (IOException ex) {
                        }
                    }

                } while (choice1 != 3);
            }

            if (choice == 3) {
                do {
                    choice1 = -3;
                    Menu menu3 = new Menu("3. Report");
                    menu3.addNewOptiont("     3.1. Products that have expired");
                    menu3.addNewOptiont("     3.2. The products that the store is selling");
                    menu3.addNewOptiont("     3.3. Products that are running out of stock (sorted in ascending order)");
                    menu3.addNewOptiont("     3.4. Import/export receipt of a product");
                    menu3.addNewOptiont("     3.5. Back to main menu");
                    menu3.printMenu();

                    choice1 = menu3.getChoice();

                    if (choice1 == 1) {
                        try {
                            reportManager.findExpiredProducts();
                        } catch (ParseException ex) {

                        }
                    }
                    if (choice1 == 2) {
                        try {
                            reportManager.findSellingProducts();
                        } catch (ParseException ex) {
                        }
                    }
                    if (choice1 == 3) {
                        reportManager.sort();
                    }
                    if (choice1 == 4) {
                        String ID;
                        do {
                            ID = Utilities.getString("Enter product ID (format: Pxxxx): ", 1, 0, 100);

                            if (!productManager.checkDuplicate(ID, 1)) {
                                System.out.println("No Product Found !!!");
                            }
                            if (!productManager.checkProductID(ID)) {
                                System.out.println("Rroduct ID has form Pxxxx (0 <= x < 10) !");
                            }
                        } while (!productManager.checkDuplicate(ID, 1) || !productManager.checkProductID(ID));
                        reportManager.displayAllsReceipt(ID);
                    }

                } while (choice1 != 5);
            }
            if (choice == 4) {
                productManager.printSave();
            }
        } while (choice != 5);

    }

}

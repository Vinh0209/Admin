/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlls;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import models.Products;

/**
 *
 * @author BAO TRAN
 */
public class ProductManager implements IProducts {

    HashMap<String, Products> productMap = new HashMap<String, Products>();

    static Scanner sc = new Scanner(System.in);

    @Override
    public boolean addProduct() {
        int getchoice = 9;

        do {

            String ID = "";

            do {
                ID = Utilities.getString("Enter product ID (format: Pxxxx) : ", 1, 0, 100);
                if (checkDuplicate(ID, 1)) {
                    System.out.println("Product ID cannot be duplicate. Please enter another one !");
                }
                if (!checkProductID(ID)) {
                    System.out.println("Product ID has form Pxxxx (0 <= x < 10) !");
                }
            } while (checkDuplicate(ID, 1) || !checkProductID(ID));
            String name = Utilities.getString("Enter product name: ", 1, 0, 100);
            String size = Utilities.getString("Enter product size: ", 1, 0, 100);
            String manufacturingDate = Utilities.inputDate("Enter manufacturing date: ", 1);
            String expirationDate = Utilities.inputDate("Enter expiration date: ", 0);

            double unitPrice = Utilities.getDouble("Enter product price: ", 0.0, 1000000000);
            int quantity = Utilities.getInt("Enter quantity: ", 0, 1000000);

            int isDaily = Utilities.getInt("Is daily product? (True:1 False:0): ", 0, 2);

            Products product = new Products(ID, name, size, manufacturingDate, expirationDate, unitPrice, quantity, isDaily);

            productMap = loadProduct();
            productMap.put(ID, product);

            saveProduct(productMap);

            getchoice = Utilities.getInt("Do you want to add more product ? (Yes: 1 , No: 0)", 0, 2);
            if (getchoice == 0) {
                System.out.println("----------Back to main menu-------------");
                break;
            }
        } while (getchoice != 0);
        for (Products object : productMap.values()) {
            System.out.println(object);
        }
        return true;
    }

    @Override
    public boolean deleteProduct() {
        productMap = loadProduct();
        String ID = "";
        do {
            ID = Utilities.getString("Enter product ID : ", 1, 0, 100);
            if (checkDuplicate(ID, 1) == false) {
                System.out.println("Product ID does not exist !");
            }
        } while (checkDuplicate(ID, 1) == false);
        int getChoice = Utilities.getInt("Do you want to delete this product? (Yes:1 , No:0)", 0, 2);
        if (getChoice == 0) {
            System.out.println("Delete Failed!!!");
            return true;
        }

        productMap.remove(ID);
        saveProduct(productMap);
        System.out.println("Delete Successfully!!!");

        return true;
    }

    @Override
    public boolean updateProduct() {
        Scanner input = new Scanner(System.in);
        List<Products> productList = new ArrayList<Products>();
        System.out.println("----Update product information----");

        String updateId = Utilities.getString("Enter ID of product that you want to update: ", 1, 0, 10000000);
        boolean productFound = false;
        productMap = loadProduct();

        for (Products object : productMap.values()) {
            productList.add(object);
        }
        for (Products product : productList) {
            if (product.getProductCode().equals(updateId)) {
                productFound = true;
                boolean OK = false;
                System.out.print("Enter name (current value: " + product.getProductName() + "): ");
                String newName = input.nextLine();
                if (newName.isEmpty()) {
                    product.setProductName(product.getProductName());
                } else {
                    product.setProductName(newName);
                }

                System.out.print("Enter size (current value: " + product.getSize() + "): ");
                String size = "";
                do {
                    size = input.nextLine();

                    if (size.isEmpty()) {
                        product.setSize(product.getSize());
                        OK = true;
                    } else {
                        product.setSize(size);
                        OK = true;
                    }

                    input.nextLine();
                } while (!OK);

                String newManufacturingDate = "";
                do {
                    newManufacturingDate = Utilities.inputDate("Enter manufacturing date (current value: " + product.getExpirationDate() + "): ", 0);
                    if (newManufacturingDate == null) {
                        product.setExpirationDate(product.getExpirationDate());
                        OK = true;
                    } else {
                        product.setExpirationDate(newManufacturingDate);
                        OK = true;
                    };
                } while (!OK);
                String newExpirationDate = "";
                do {
                    newExpirationDate = Utilities.inputDate("Enter expiration date (current value: " + product.getExpirationDate() + "): ", 0);
                    if (newExpirationDate == null) {
                        product.setExpirationDate(product.getExpirationDate());
                        OK = true;
                    } else {
                        product.setExpirationDate(newExpirationDate);
                        OK = true;
                    };
                } while (!OK);
                System.out.print("Enter new product price (current value: " + product.getPrice() + "): ");
                String price1 = "";

                double price = Utilities.getDouble("Enter new price: ", 0.0, 100000000000000.0);
                if (price == -1) {
                    product.setPrice(product.getPrice());
                } else {
                    product.setPrice(price);
                }

                int isDaily = Utilities.getInt("Is daily product? (True:1 False:0):  ", 0, 2);
                if (isDaily == -1) {
                    product.setIsDaily(product.getisIsDaily());
                } else {
                    product.setIsDaily(isDaily);
                }

                productMap = loadProduct();
                productMap.put(updateId, product);
                saveProduct(productMap);
                System.out.println("Product" + " after update: ");
                System.out.println(product.toString());
            }

        }
        if (!productFound) {
            System.out.println("ID Doesn't Exist!!!!");
        }

        return true;
    }

    @Override
    public void showAllProducts() {
        productMap = loadProduct();
        printAllsProduct(productMap);

    }

    public boolean sort() {
        return true;
    }

    public void saveProduct(HashMap<String, Products> studentMap) {
        ArrayList<Products> productList = new ArrayList<Products>(studentMap.values());

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/output/product.dat"));
            String line;
            for (Products student : productList) {
                line = student.toString() + "\n";
                bw.write(line);
            }
            bw.close();
            System.out.println("Saved Successfully!!!");
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public HashMap<String, Products> loadProduct() {
        HashMap<String, Products> studentMap = new HashMap<String, Products>();
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/output/product.dat"));
            String line;
            while ((line = br.readLine()) != null) {
                String parts[] = line.split(",");
                String id = parts[0].trim();
                String name = parts[1].trim();
                String size = parts[2].trim();
                String maDate = parts[3].trim();
                String exDate = parts[4].trim();
                double price = Double.parseDouble(parts[5].trim());
                int quantity = Integer.parseInt(parts[6].trim());
                int isDaily = Integer.parseInt(parts[7].trim());

                Products pro = new Products(id, name, size, maDate, exDate, price, quantity, isDaily);

                studentMap.put(id, pro);

            }
            br.close();
        } catch (Exception e) {
        }
        return studentMap;
    }

    public boolean checkProductID(String str) {
        if (str.length() != 5) {
            return false;
        }

        if (str.charAt(0) != 'P') {
            return false;
        }

        for (int i = 1; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public boolean checkDuplicate(String s, int option) {
        productMap = loadProduct();
        Products product = null;
        if (option == 1) {
            productMap = loadProduct();
            for (Products c : productMap.values()) {
                if (c.getProductCode().equalsIgnoreCase(s)) {
                    return true;
                }
            }
        }
        if (option == 2) {
            productMap = loadProduct();
            for (Products c : productMap.values()) {
                if (c.getProductName().equalsIgnoreCase(s)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void printSave() {
        System.out.println("Data has been added");

    }

    public double getPrice(String productCode) {
        productMap = loadProduct();

        if (productMap.containsKey(productCode)) {
            Products product = productMap.get(productCode);
            double price = product.getPrice();
            return price;
        } else {
            System.out.println("Product doesn't exist.");
            return 0.0;
        }
    }

    public void changeQuantityinStocks(String productCode, int quantity, int option) {
        productMap = loadProduct();

        if (productMap.containsKey(productCode)) {
            Products product = productMap.get(productCode);
            if (option == 1) {
                product.setQuantity(product.getQuantity() + quantity);
            }
            if (option == 2) {
                if (product.getQuantity() < quantity) {
                    System.out.println("Product does not have enough quantity to export " + quantity + " pieces");
                }
                if (product.getQuantity() > 0 && product.getQuantity() > quantity) {
                    product.setQuantity(product.getQuantity() - quantity);
                    if (product.getQuantity() < 0) {
                        product.setQuantity(0);
                    }

                }
            }
            saveProduct(productMap);
        } else {
            System.out.println("Product doesn't exist.");

        }
    }

    public void printAllsProduct(HashMap<String, Products> proMap) {

        System.out.println("_________________________________________________________________________________________________________________________");
        System.out.println("|ID        |Name               |Size       |Manufacturing Date   |Expiration Date      |Quantity        |Price          |");
        System.out.println("_________________________________________________________________________________________________________________________");

        for (Products p : proMap.values()) {
            String idstr = String.format("%-10s", "ID|");
            String id = String.format("%-10s", p.getProductCode());
            String name = String.format("%-18s", p.getProductName());
            String size = String.format("%-10s", p.getSize());
            String maDate = String.format("%-20s", p.getManufacturingDate());
            String exDate = String.format("%-20s", p.getExpirationDate());
            String quantity = String.format("%-15s", p.getQuantity());
            String price = String.format("%-15s", p.getPrice());
            String isDaily = String.format("%-15s", p.getisIsDaily());

            System.out.println("|" + id + "| " + name + "| " + size + "| " + maDate + "| " + exDate + "| " + quantity + "| " + price + "|");
        }

        System.out.println("_________________________________________________________________________________________________________________________");
        System.out.println("|                                                                                               TOTAL: " + proMap.size() + " product type[s]|");
        System.out.println("_________________________________________________________________________________________________________________________");

        System.out.println("");
        System.out.println("");
    }

}

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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import models.Products;
import models.ExportReceipt;
import models.ImportReceipt;
import models.ReceiptItem;

/**
 *
 * @author BAO TRAN
 */
public class WarehouseManager implements IReceipt {

    ProductManager productManager = new ProductManager();
    HashMap<String, Products> proMap = productManager.loadProduct();
    HashMap<String, ImportReceipt> program = new HashMap<String, ImportReceipt>();
    static Scanner sc = new Scanner(System.in);
    private int receiptCounter = 0;

    @Override

    public boolean createImport() throws IOException {
        List<ReceiptItem> receiptItems = new ArrayList<ReceiptItem>();
        List<ImportReceipt> importReceipts = new ArrayList<ImportReceipt>();
        int getchoice = 9;

        do {
            String receiptCode = generateImportReceiptCode();
            if (checkDuplicate(receiptCode, 1)) {
                System.out.println("Receipt code cannot be duplicate. Please enter another one !");
                continue;
            }

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = dateFormat.format(currentDate);

            int totalPrice = 0;
            do {
                String ID;
                do {
                    ID = Utilities.getString("Enter product ID (format: Pxxxx) : ", 1, 0, 100);

                    if (!productManager.checkDuplicate(ID, 1)) {
                        System.out.println("No Product Found !!!");
                    }
                    if (!productManager.checkProductID(ID)) {
                        System.out.println("Product ID has form Pxxxx (0 <= x < 10) !");
                    }
                } while (!productManager.checkDuplicate(ID, 1) || !productManager.checkProductID(ID));  //ID phải là duy nhất và đúng định dạng 
                String manufacturingDate = Utilities.inputDate("Enter manufacturingDate: ", 1);
                String expirationDate = Utilities.inputDate("Enter expirationDate: ", 0);
                int quantity = Utilities.getInt("Enter quantity: ", 0, 1000000000);

                totalPrice += quantity * productManager.getPrice(ID);
                productManager.changeQuantityinStocks(ID, quantity, 1);

                ReceiptItem receiptItem = new ReceiptItem(ID, manufacturingDate, expirationDate, quantity);
                receiptItems.add(receiptItem);

                getchoice = Utilities.getInt("Do you want to add more items? (Yes:1 No: 0): ", 0, 2);
                if (getchoice == 0) {

                    break;
                }
            } while (true);

            System.out.println("Items in the Receipt:");

            ImportReceipt importReceipt = new ImportReceipt(receiptCode, formattedDate, receiptItems, totalPrice);

            importReceipts = loadImportReceipt();
            importReceipts.add(importReceipt);
            saveImportReceipt(importReceipts);
            System.out.println(importReceipt.toString());

            getchoice = Utilities.getInt("Do you want to create another import receipt? (Yes: 1, No: 0): ", 0, 2);

        } while (getchoice != 0);

        return true;
    }

    @Override
    public boolean createExport() throws IOException {
        List<ReceiptItem> receiptItems = new ArrayList<ReceiptItem>();
        List<ExportReceipt> eReceipt1s = new ArrayList<ExportReceipt>();
        int getchoice = 9;

        do {
            String receiptCode = generateExportReceiptCode();
            if (checkDuplicate(receiptCode, 1)) {
                System.out.println("Receipt code cannot be duplicate. Please enter another one !");
                continue;
            }

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = dateFormat.format(currentDate);

            int totalPrice = 0;
            do {
                String ID;
                do {
                    ID = Utilities.getString("Enter product ID (format: Pxxxx) : ", 1, 0, 100);
                    if (!productManager.checkDuplicate(ID, 1)) {
                        System.out.println("No Product Found !!!");
                    }
                    if (!productManager.checkProductID(ID)) {
                        System.out.println("Product ID has form Pxxxx (0 <= x < 10) !");
                    }
                } while (!productManager.checkDuplicate(ID, 1) || !productManager.checkProductID(ID));
                String manufacturingDate = Utilities.inputDate("Enter manufacturingDate: ", 1);
                String expirationDate = Utilities.inputDate("Enter expirationDate: ", 0);
                int quantity = Utilities.getInt("Enter quantity: ", 0, 1000000000);
                totalPrice += quantity * productManager.getPrice(ID);
                productManager.changeQuantityinStocks(ID, quantity, 2);
                ReceiptItem receiptItem = new ReceiptItem(ID, manufacturingDate, expirationDate, quantity);
                receiptItems.add(receiptItem);

                getchoice = Utilities.getInt("Do you want to add more items? (Yes:1 No: 0): ", 0, 2);
                if (getchoice == 0) {
                    break;
                }
            } while (true);

            System.out.println("Items in the Receipt:");

            ExportReceipt exportReceipt = new ExportReceipt(receiptCode, formattedDate, receiptItems, totalPrice);

            eReceipt1s = loadExportReceipt();
            eReceipt1s.add(exportReceipt);
            saveExportReceipt(eReceipt1s);
            System.out.println(exportReceipt.toString());

            getchoice = Utilities.getInt("Do you want to create another export receipt? (Yes: 1, No: 0): ", 0, 2);

        } while (getchoice != 0);

        return true;
    }

    private boolean checkDuplicate(String ID, int i) {
        return false;
    }

    public String generateImportReceiptCode() throws IOException {
        List<ImportReceipt> orders = loadImportReceipt();
        int orderCount = orders.size();
        return "I" + String.format("%07d", orderCount);
    }

    public String generateExportReceiptCode() throws IOException {
        List<ExportReceipt> orders = loadExportReceipt();
        int orderCount = orders.size();
        return "E" + String.format("%07d", orderCount);
    }

    public void saveImportReceipt(List<ImportReceipt> programList) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src\\output\\importReceipt.dat"));

            String line;
            for (ImportReceipt program1 : programList) {
                line = program1.toString() + "\n";
                bw.write(line);
            }
            bw.close();
        } catch (Exception e) {
        }
    }

    public void saveExportReceipt(List<ExportReceipt> programList) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src\\output\\exportReceipt.dat"));

            String line;
            for (ExportReceipt program1 : programList) {
                line = program1.toString() + "\n";
                bw.write(line);
            }
            bw.close();
        } catch (Exception e) {
        }
    }

    public List<ImportReceipt> loadImportReceipt() {
        List<ImportReceipt> importReceipts = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader("src\\output\\importReceipt.dat"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] arr = line.split(";");
                String id = arr[0].trim();
                String date = arr[1].trim();
                String itemsStr = arr[2].trim();

                itemsStr = itemsStr.substring(1, itemsStr.length() - 1);
                String[] itemsArr = itemsStr.split(",");

                List<ReceiptItem> receiptItems = new ArrayList<>();
                for (String itemStr : itemsArr) {
                    String[] itemParts = itemStr.split(":");
                    String productCode = itemParts[0].trim();
                    String manufacturingDate = itemParts[1].trim();
                    String expirationDate = itemParts[2].trim();
                    int quantity = Integer.parseInt(itemParts[3].trim());
                    ReceiptItem receiptItem = new ReceiptItem(productCode, manufacturingDate, expirationDate, quantity);
                    receiptItems.add(receiptItem);
                }

                double cost = Double.parseDouble(arr[3].trim());
                ImportReceipt importReceipt = new ImportReceipt(id, date, receiptItems, cost);
                importReceipts.add(importReceipt);
            }

            br.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        receiptCounter = importReceipts.size();
        return importReceipts;
    }

    public List<ExportReceipt> loadExportReceipt() {
        List<ExportReceipt> exportReceipts = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader("src\\output\\exportReceipt.dat"));
            String line;

            while ((line = br.readLine()) != null) {
                String[] arr = line.split(";");
                String id = arr[0].trim();
                String date = arr[1].trim();
                String itemsStr = arr[2].trim();

                itemsStr = itemsStr.substring(1, itemsStr.length() - 1);
                String[] itemsArr = itemsStr.split(",");

                List<ReceiptItem> receiptItems = new ArrayList<>();
                for (String itemStr : itemsArr) {
                    String[] itemParts = itemStr.split(":");
                    String productCode = itemParts[0].trim();
                    String manufacturingDate = itemParts[1].trim();
                    String expirationDate = itemParts[2].trim();
                    int quantity = Integer.parseInt(itemParts[3].trim());
                    ReceiptItem receiptItem = new ReceiptItem(productCode, manufacturingDate, expirationDate, quantity);
                    receiptItems.add(receiptItem);
                }

                double cost = Double.parseDouble(arr[3].trim());
                ExportReceipt exportReceipt = new ExportReceipt(id, date, receiptItems, cost);
                exportReceipts.add(exportReceipt);
            }

            br.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return exportReceipts;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlls;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import models.ExportReceipt;
import models.ImportReceipt;
import models.Products;
import models.ReceiptItem;

/**
 *
 * @author BAO TRAN
 */
public class ReportManager implements IReport {

    WarehouseManager warehouseManager = new WarehouseManager();
    ProductManager productManager = new ProductManager();

    private HashMap<String, Products> productMap = productManager.loadProduct();
    HashMap<String, Products> proMap = new HashMap<String, Products>();

    @Override
    public void findExpiredProducts() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();

        System.out.println("Expired Products: ");
        for (Products product : productMap.values()) {
            if (currentDate.after(stringToDate(product.getExpirationDate()))) {
                proMap.put(product.getProductCode(), product);
            }
        }
        productManager.printAllsProduct(proMap);
    }

    @Override
    public void findSellingProducts() throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date currentDate = new Date();

        System.out.println("Selling Products: ");
        for (Products product : productMap.values()) {
            if (product.getQuantity() > 0 && !currentDate.after(stringToDate(product.getExpirationDate()))) {
                proMap.put(product.getProductCode(), product);
            }
        }
        productManager.printAllsProduct(proMap);
    }

    @Override
    public void findRunningOutProducts() {

        List<Products> list = new ArrayList<Products>();
        System.out.println("Running out products (quantity <= 3):");
        for (Products product : productMap.values()) {
            if (product.getQuantity() <= 3) {
                list.add(product);
                proMap.put(product.getProductCode(), product);
            }
        }
        displayDescendingByQuantity(list);
    }

    @Override
    public void displayAllsReceipt(String ID) {
        List<ExportReceipt> exportReceipts = warehouseManager.loadExportReceipt();
        List<ImportReceipt> importReceipts = warehouseManager.loadImportReceipt();

        System.out.println("Import Receipt: ");
        for (ImportReceipt importReceipt : importReceipts) {
            for (ReceiptItem item : importReceipt.items) {
                if (item.getProID().equalsIgnoreCase(ID)) {
                    System.out.println(importReceipt);
                }
            }
        }

        System.out.println("Export Receipt: ");
        for (ExportReceipt exportReceipt : exportReceipts) {
            for (ReceiptItem item : exportReceipt.items) {
                if (item.getProID().equalsIgnoreCase(ID)) {
                    System.out.println(exportReceipt);
                }
            }
        }
    }

    public static Date stringToDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.parse(dateString);
    }

    public void displayDescendingByQuantity(List<Products> list) {

        Collections.sort(list, new Comparator<Products>() {

            @Override
            public int compare(Products p, Products p1) {
                return Integer.compare(p.getQuantity(), p1.getQuantity());
            }
        });

        for (Products products : list) {
            System.out.println(products.productType());
        }
    }
    public void sort(){
        List<Products> list = new ArrayList<Products>();
        for(Products product : productMap.values()){
            list.add(product);           
        }
        Collections.sort(list, new Comparator<Products>() {
            @Override
            public int compare(Products p1, Products p2) {
                int quantityCompare = Integer.compare(p1.getQuantity(), p2.getQuantity());
                if(quantityCompare != 0){
                    return quantityCompare;
            }else{
                    return p2.getSize().compareTo(p1.getSize());
                }
            }
        });
            for (Products products : list) {
            System.out.println(products.productType());
        }  
    }

}


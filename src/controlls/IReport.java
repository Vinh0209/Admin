/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlls;

import java.text.ParseException;

/**
 *
 * @author BAO TRAN
 */
public interface IReport {

    public void findExpiredProducts() throws ParseException;

    public void findSellingProducts() throws ParseException;

    public void findRunningOutProducts();

    public void displayAllsReceipt(String ID);
}

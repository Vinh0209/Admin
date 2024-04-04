/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlls;

/**
 *
 * @author BAO TRAN
 */
public interface IReceipt {

    public boolean createExport() throws Exception;

    public boolean createImport() throws Exception;
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package threads;

import client.Components.MenuPanel;
import client.Dashboard;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Products;

/**
 *
 * @author zheng
 */
public class productsThreads implements Runnable{
    private List<Products> allProducts = new ArrayList<>();
    private List<Products> hotProducts = new ArrayList<>();
    private List<Products> newProducts = new ArrayList<>();

    
    @Override
    public void run() {
        Dashboard dashboard = new Dashboard();

        Thread fetchAll = new Thread(() -> {
            try {
                List<Products> result = dashboard.handleGetAllData();
                synchronized (this) {
                    allProducts = result;
                }
                System.out.println("Fetched ALL: " + allProducts.size());
            } catch (NotBoundException | MalformedURLException ex) {
                Logger.getLogger(MenuPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        Thread fetchHot = new Thread(() -> {
            try {
                List<Products> result = dashboard.handleGetHotProducts();
                synchronized (this) {
                    hotProducts = result;
                }
                System.out.println("Fetched HOT: " + hotProducts.size());
            } catch (NotBoundException | MalformedURLException ex) {
                Logger.getLogger(MenuPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        Thread fetchNew = new Thread(() -> {
            try {
                List<Products> result = dashboard.handleGetAllNewData();
                synchronized (this) {
                    newProducts = result;
                }
                System.out.println("Fetched NEW: " + newProducts.size());
            } catch (NotBoundException | MalformedURLException ex) {
                Logger.getLogger(MenuPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        fetchAll.start();
        fetchHot.start();
        fetchNew.start();
    }
}

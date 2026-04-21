/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author phuoc
 */
public class Satistic {
    private int totalOrders;
    private int totalSales;
    private int totalOrdersMonth;
    private int totalSalesMonth;

    public Satistic() {
    }

    public Satistic(int totalOrders, int totalSales, int totalOrdersMonth, int totalSalesMonth) {
        this.totalOrders = totalOrders;
        this.totalSales = totalSales;
        this.totalOrdersMonth = totalOrdersMonth;
        this.totalSalesMonth = totalSalesMonth;
    }

    public int getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(int totalOrders) {
        this.totalOrders = totalOrders;
    }

    public int getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(int totalSales) {
        this.totalSales = totalSales;
    }

    public int getTotalOrdersMonth() {
        return totalOrdersMonth;
    }

    public void setTotalOrdersMonth(int totalOrdersMonth) {
        this.totalOrdersMonth = totalOrdersMonth;
    }

    public int getTotalSalesMonth() {
        return totalSalesMonth;
    }

    public void setTotalSalesMonth(int totalSalesMonth) {
        this.totalSalesMonth = totalSalesMonth;
    }
    
}

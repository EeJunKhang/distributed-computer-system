package model;

import java.io.Serializable;

public class ReportData implements Serializable {
    private double totalSales;
    private int totalOrder;
    private int totalItems;
    private double averageSales;

    public ReportData(double totalSales, int totalOrder, int totalItems, double averageSales) {
        this.totalSales = totalSales;
        this.totalOrder = totalOrder;
        this.totalItems = totalItems;
        this.averageSales = averageSales;
    }

    public double getTotalSales() {
        return totalSales;
    }

    public int getTotalOrder() {
        return totalOrder;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public double getAverageSales() {
        return averageSales;
    }
    
    
}

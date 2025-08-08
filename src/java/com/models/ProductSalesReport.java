package com.models;

import java.math.BigDecimal;
import java.util.Date;

public class ProductSalesReport {
    private String productID;
    private String productName;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private int quantitySold;
    private BigDecimal unitPrice;
    private BigDecimal totalRevenue;
private Date orderDate;
private int completedOrders;

    private String orderStatus;
    private int orderCount;
    
    public ProductSalesReport() {
        this.productID = "";
        this.productName = "";
        this.customerName = "";
        this.customerEmail = "";
        this.customerPhone = "";
        this.quantitySold = 0;
        this.unitPrice = BigDecimal.ZERO;
        this.totalRevenue = BigDecimal.ZERO;
        this.orderDate = null;
        this.orderStatus = "";
        this.orderCount = 0;
    }
    

    public ProductSalesReport(String productID, String productName, String customerName, String customerEmail, String customerPhone, int quantitySold, BigDecimal unitPrice, BigDecimal totalRevenue, Date orderDate, int completedOrders, String orderStatus, int orderCount) {
        this.productID = productID;
        this.productName = productName;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
        this.quantitySold = quantitySold;
        this.unitPrice = unitPrice;
        this.totalRevenue = totalRevenue;
        this.orderDate = orderDate;
        this.completedOrders = completedOrders;
        this.orderStatus = orderStatus;
        this.orderCount = orderCount;
    }
    
    
    public String getProductID() {
        return productID;
    }
    
    public void setProductID(String productID) {
        this.productID = productID;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }
    
    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    
    public String getCustomerPhone() {
        return customerPhone;
    }
    
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    
    public int getQuantitySold() {
        return quantitySold;
    }
    
    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }
    
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
    
    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
    
    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }
    
    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
    
    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }
    
    public int getOrderCount() {
        return orderCount;
    }
    
    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public int getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(int completedOrders) {
        this.completedOrders = completedOrders;
    }
}
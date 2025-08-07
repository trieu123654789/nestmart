/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.models;

import com.models.ProductSalesReport;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface SalesReportDAO {

    List<ProductSalesReport> getSalesReport(LocalDateTime startDate, LocalDateTime endDate);

    List<ProductSalesReport> getSalesReportByProduct(String productID, LocalDateTime startDate, LocalDateTime endDate);

    List<ProductSalesReport> getSalesReportByCustomer(int customerID, LocalDateTime startDate, LocalDateTime endDate);

    List<ProductSalesReport> getTopSellingProducts(int limit, LocalDateTime startDate, LocalDateTime endDate);

    List<ProductSalesReport> getTopCustomers(int limit, LocalDateTime startDate, LocalDateTime endDate);

    BigDecimal getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate);

    int getTotalOrders(LocalDateTime startDate, LocalDateTime endDate);

    int getTotalProductsSold(LocalDateTime startDate, LocalDateTime endDate);

    Map<Integer, BigDecimal> getMonthlyRevenue(int year);

    Map<String, Integer> getOrderStatusReport(LocalDateTime startDate, LocalDateTime endDate);

    List<ProductSalesReport> getSalesReportPaginated(LocalDateTime startDate, LocalDateTime endDate, int offset, int limit);

    int countSalesReport(LocalDateTime startDate, LocalDateTime endDate);

    List<ProductSalesReport> getSalesReportByProductAndCustomer(String productID, int customerID, LocalDateTime start, LocalDateTime end);
}

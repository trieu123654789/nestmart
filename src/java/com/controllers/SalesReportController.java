package com.controllers;

import com.models.ProductSalesReport;
import com.models.SalesReportDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/admin")
public class SalesReportController {
    
   @Autowired
    private SalesReportDAO salesReportDAO; 

@RequestMapping(value = "salesReport", method = RequestMethod.GET)
public String showSalesReport(ModelMap model,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "20") int pageSize,
                            @RequestParam(required = false) String startDate,
                            @RequestParam(required = false) String endDate,
                            @RequestParam(required = false) String productID,
                            @RequestParam(required = false) String customerID,
                            RedirectAttributes redirectAttributes) {

    try {
        LocalDateTime start = null;
        LocalDateTime end = null;
        
        if (startDate != null && !startDate.trim().isEmpty()) {
            start = parseDate(startDate, null);
        }
        
        if (endDate != null && !endDate.trim().isEmpty()) {
            end = parseDate(endDate, null);
        }
        
        String cleanProductID = (productID != null && !productID.trim().isEmpty()) ? productID.trim() : null;
        String cleanCustomerID = (customerID != null && !customerID.trim().isEmpty()) ? customerID.trim() : null;
        
        List<ProductSalesReport> salesReports;
        int totalReports;
        int offset = (page - 1) * pageSize;
        
        if (cleanProductID != null && cleanCustomerID != null) {
            try {
                int custId = Integer.parseInt(cleanCustomerID);
                salesReports = salesReportDAO.getSalesReportByProductAndCustomer(cleanProductID, custId, start, end);
                totalReports = salesReports.size();
                salesReports = paginateList(salesReports, offset, pageSize);
            } catch (NumberFormatException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Customer ID must be a number");
                return "redirect:/admin/salesReport.htm";
            }
        } else if (cleanProductID != null) {
            salesReports = salesReportDAO.getSalesReportByProduct(cleanProductID, start, end);
            totalReports = salesReports.size();
            salesReports = paginateList(salesReports, offset, pageSize);
        } else if (cleanCustomerID != null) {
            try {
                int custId = Integer.parseInt(cleanCustomerID);
                salesReports = salesReportDAO.getSalesReportByCustomer(custId, start, end);
                totalReports = salesReports.size();
                salesReports = paginateList(salesReports, offset, pageSize);
            } catch (NumberFormatException e) {
                redirectAttributes.addFlashAttribute("errorMessage", "Customer ID must be a number");
                return "redirect:/admin/salesReport.htm";
            }
        } else {
            salesReports = salesReportDAO.getSalesReportPaginated(start, end, offset, pageSize);
            totalReports = salesReportDAO.countSalesReport(start, end);
        }
        
        int totalPages = (int) Math.ceil((double) totalReports / pageSize);
        
        BigDecimal totalRevenue;
        int totalOrders;
        int totalProductsSold;
        Map<String, Integer> orderStatusReport;
        BigDecimal averageOrderValue;
        List<ProductSalesReport> topProducts;
        List<ProductSalesReport> topCustomers;
        
        if (cleanProductID != null || cleanCustomerID != null) {
            List<ProductSalesReport> allFilteredReports;
            
            if (cleanProductID != null && cleanCustomerID != null) {
                int custId = Integer.parseInt(cleanCustomerID);
                allFilteredReports = salesReportDAO.getSalesReportByProductAndCustomer(cleanProductID, custId, start, end);
            } else if (cleanProductID != null) {
                allFilteredReports = salesReportDAO.getSalesReportByProduct(cleanProductID, start, end);
            } else {
                int custId = Integer.parseInt(cleanCustomerID);
                allFilteredReports = salesReportDAO.getSalesReportByCustomer(custId, start, end);
            }
            
            totalRevenue = calculateTotalRevenue(allFilteredReports);
            totalOrders = countUniqueOrders(allFilteredReports);
            totalProductsSold = calculateTotalProductsSold(allFilteredReports);
            orderStatusReport = calculateOrderStatusReport(allFilteredReports);
            averageOrderValue = calculateAverageOrderValue(totalRevenue, totalOrders);
            topProducts = getTopProductsFromReports(allFilteredReports, 3);
            topCustomers = getTopCustomersFromReports(allFilteredReports, 3);
            
        } else {
            totalRevenue = salesReportDAO.getTotalRevenue(start, end);
            totalOrders = salesReportDAO.getTotalOrders(start, end);
            totalProductsSold = salesReportDAO.getTotalProductsSold(start, end);
            orderStatusReport = salesReportDAO.getOrderStatusReport(start, end);
            averageOrderValue = calculateAverageOrderValue(totalRevenue, totalOrders);
            topProducts = salesReportDAO.getTopSellingProducts(3, start, end);
            topCustomers = salesReportDAO.getTopCustomers(3, start, end);
        }
        
        model.addAttribute("salesReports", salesReports);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("productID", cleanProductID);
        model.addAttribute("customerID", cleanCustomerID);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalProductsSold", totalProductsSold);
        model.addAttribute("orderStatusReport", orderStatusReport);
        model.addAttribute("totalReports", totalReports);
        
        model.addAttribute("averageOrderValue", averageOrderValue);
        model.addAttribute("topProducts", topProducts);
        model.addAttribute("topCustomers", topCustomers);
        
        return "/admin/salesReport";
        
    } catch (Exception e) {
        e.printStackTrace();
        redirectAttributes.addFlashAttribute("errorMessage", "Error loading sales report: " + e.getMessage());
        return "redirect:/admin/salesReport.htm";
    }
}
    
    @RequestMapping(value = "topSellingProducts", method = RequestMethod.GET)
    public String showTopSellingProducts(ModelMap model,
                                       @RequestParam(defaultValue = "10") int limit,
                                       @RequestParam(required = false) String startDate,
                                       @RequestParam(required = false) String endDate,
                                       RedirectAttributes redirectAttributes) {
        
        try {
            LocalDateTime start = parseDate(startDate, LocalDateTime.now().minusMonths(1));
            LocalDateTime end = parseDate(endDate, LocalDateTime.now());
            
            List<ProductSalesReport> topProducts = salesReportDAO.getTopSellingProducts(limit, start, end);
            
            model.addAttribute("topProducts", topProducts);
            model.addAttribute("limit", limit);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            
            return "/admin/topSellingProducts";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error loading top selling products: " + e.getMessage());
            return "redirect:/admin/salesReport.htm";
        }
    }
    
    @RequestMapping(value = "topCustomers", method = RequestMethod.GET)
    public String showTopCustomers(ModelMap model,
                                 @RequestParam(defaultValue = "10") int limit,
                                 @RequestParam(required = false) String startDate,
                                 @RequestParam(required = false) String endDate,
                                 RedirectAttributes redirectAttributes) {
        
        try {
            LocalDateTime start = parseDate(startDate, LocalDateTime.now().minusMonths(1));
            LocalDateTime end = parseDate(endDate, LocalDateTime.now());
            
            List<ProductSalesReport> topCustomers = salesReportDAO.getTopCustomers(limit, start, end);
            
            model.addAttribute("topCustomers", topCustomers);
            model.addAttribute("limit", limit);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            
            return "/admin/topCustomers";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error loading top customers: " + e.getMessage());
            return "redirect:/admin/salesReport.htm";
        }
    }
    
    @RequestMapping(value = "monthlyRevenue", method = RequestMethod.GET)
    public String showMonthlyRevenue(ModelMap model,
                                   @RequestParam(defaultValue = "2024") int year,
                                   RedirectAttributes redirectAttributes) {
        
        try {
            Map<Integer, BigDecimal> monthlyRevenue = salesReportDAO.getMonthlyRevenue(year);
            
            model.addAttribute("monthlyRevenue", monthlyRevenue);
            model.addAttribute("selectedYear", year);
            
            return "/admin/monthlyRevenue";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error loading monthly revenue: " + e.getMessage());
            return "redirect:/admin/salesReport.htm";
        }
    }
    
    @RequestMapping(value = "salesDashboard", method = RequestMethod.GET)
    public String showSalesDashboard(ModelMap model,@RequestParam(defaultValue = "10") int limit,
                                   @RequestParam(required = false) String startDate,
                                   @RequestParam(required = false) String endDate,HttpServletRequest request,
                                   RedirectAttributes redirectAttributes) {
         HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to access this page.");
            return "redirect:/login.htm";
        }
        if (session.getAttribute("role") == null || (Integer) session.getAttribute("role") != 1) {
            redirectAttributes.addFlashAttribute("error", "You do not have permission to access this page.");

            String currentUrl = request.getHeader("referer");
            if (currentUrl != null && !currentUrl.isEmpty()) {
                return "redirect:" + currentUrl;
            } else {
                return "redirect:/";
            }
        }
        try {
            LocalDateTime start = null;
            LocalDateTime end = null;
            
            if (startDate != null && !startDate.trim().isEmpty()) {
                start = parseDate(startDate, null);
            }
            
            if (endDate != null && !endDate.trim().isEmpty()) {
                end = parseDate(endDate, null);
            }
            
            BigDecimal totalRevenue = salesReportDAO.getTotalRevenue(start, end);
            int totalOrders = salesReportDAO.getTotalOrders(start, end);
            int totalProductsSold = salesReportDAO.getTotalProductsSold(start, end);
            Map<String, Integer> orderStatusReport = salesReportDAO.getOrderStatusReport(start, end);
            
            List<ProductSalesReport> topProducts = salesReportDAO.getTopSellingProducts(limit, start, end);
            
            List<ProductSalesReport> topCustomers = salesReportDAO.getTopCustomers(limit, start, end);
            
            Map<Integer, BigDecimal> monthlyRevenue = salesReportDAO.getMonthlyRevenue(LocalDateTime.now().getYear());
                        model.addAttribute("limit", limit);

            model.addAttribute("totalRevenue", totalRevenue);
            model.addAttribute("totalOrders", totalOrders);
            model.addAttribute("totalProductsSold", totalProductsSold);
            model.addAttribute("orderStatusReport", orderStatusReport);
            model.addAttribute("topProducts", topProducts);
            model.addAttribute("topCustomers", topCustomers);
            model.addAttribute("monthlyRevenue", monthlyRevenue);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("totalProductsSold", totalProductsSold);
            return "/admin/salesDashboard";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error loading sales dashboard: " + e.getMessage());
            return "redirect:/admin/dashboard.htm";
        }
    }
    
@RequestMapping(value = "exportSalesReport", method = RequestMethod.GET)
public void exportSalesReport(
        @RequestParam(required = false) String startDate,
        @RequestParam(required = false) String endDate,
        @RequestParam(required = false) String productID,
        @RequestParam(required = false) String customerID,
        HttpServletResponse response) throws IOException {
    
    try {
        LocalDateTime start = null;
        LocalDateTime end = null;
        
        if (startDate != null && !startDate.trim().isEmpty()) {
            start = parseDate(startDate, null);
        }
        
        if (endDate != null && !endDate.trim().isEmpty()) {
            end = parseDate(endDate, null);
        }
        
        String cleanProductID = (productID != null && !productID.trim().isEmpty()) ? productID.trim() : null;
        String cleanCustomerID = (customerID != null && !customerID.trim().isEmpty()) ? customerID.trim() : null;
        
        List<ProductSalesReport> salesReports;
        
        if (cleanProductID != null && cleanCustomerID != null) {
            int custId = Integer.parseInt(cleanCustomerID);
            salesReports = salesReportDAO.getSalesReportByProductAndCustomer(cleanProductID, custId, start, end);
        } else if (cleanProductID != null) {
            salesReports = salesReportDAO.getSalesReportByProduct(cleanProductID, start, end);
        } else if (cleanCustomerID != null) {
            int custId = Integer.parseInt(cleanCustomerID);
            salesReports = salesReportDAO.getSalesReportByCustomer(custId, start, end);
        } else {
            salesReports = salesReportDAO.getSalesReportPaginated(start, end, 0, Integer.MAX_VALUE);
        }
        
        BigDecimal totalRevenue;
        int totalOrders;
        int totalProductsSold;
        BigDecimal averageOrderValue;
        int totalRecords = salesReports.size();
        
        if (cleanProductID != null || cleanCustomerID != null) {
            totalRevenue = calculateTotalRevenue(salesReports);
            totalOrders = countUniqueOrders(salesReports);
            totalProductsSold = calculateTotalProductsSold(salesReports);
            averageOrderValue = calculateAverageOrderValue(totalRevenue, totalOrders);
        } else {
            totalRevenue = salesReportDAO.getTotalRevenue(start, end);
            totalOrders = salesReportDAO.getTotalOrders(start, end);
            totalProductsSold = salesReportDAO.getTotalProductsSold(start, end);
            averageOrderValue = calculateAverageOrderValue(totalRevenue, totalOrders);
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = "sales_report_" + dateFormat.format(new Date()) + ".csv";
        
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        
        PrintWriter writer = response.getWriter();
        
        writer.write('\ufeff');
        
        writer.println("SALES REPORT SUMMARY");
        writer.println("===================");
        writer.println();
        
        writer.println("FILTER INFORMATION:");
        if (start != null || end != null) {
            if (start != null && end != null) {
                writer.println("Period:,\"From " + start.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + 
                               " to " + end.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\"");
            } else if (start != null) {
                writer.println("Period:,\"From " + start.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " onwards\"");
            } else {
                writer.println("Period:,\"Up to " + end.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\"");
            }
        } else {
            writer.println("Period:,\"All Time\"");
        }
        
        if (cleanProductID != null) {
            writer.println("Product Filter:,\"" + cleanProductID + "\"");
        }
        if (cleanCustomerID != null) {
            writer.println("Customer Filter:,\"" + cleanCustomerID + "\"");
        }
        writer.println("Generated:,\"" + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "\"");
        writer.println();
        
        writer.println("SUMMARY STATISTICS:");
        writer.println("Total Revenue:,\"$" + String.format("%,.2f", totalRevenue) + "\"");
        writer.println("Total Orders:,\"" + String.format("%,d", totalOrders) + " orders\"");
        writer.println("Total Products Sold:,\"" + String.format("%,d", totalProductsSold) + " items\"");
        writer.println("Total Records:,\"" + String.format("%,d", totalRecords) + " records\"");
        writer.println("Average Order Value (AOV):,\"$" + String.format("%,.2f", averageOrderValue) + "\"");
        writer.println();
        
        writer.println("QUICK ANALYSIS:");
        if (totalOrders > 0) {
            double avgItemsPerOrder = (double) totalProductsSold / totalOrders;
            writer.println("Average Items per Order:,\"" + String.format("%.1f", avgItemsPerOrder) + " items/order\"");
        }
        if (totalRecords > totalOrders && totalOrders > 0) {
            double avgProductTypesPerOrder = (double) totalRecords / totalOrders;
            writer.println("Average Product Types per Order:,\"" + String.format("%.1f", avgProductTypesPerOrder) + " types/order\"");
        }
        writer.println();
        writer.println("DETAILED REPORT:");
        writer.println("================");
        writer.println();
        
        writer.println("Order Date,Product ID,Product Name,Customer Name,Customer Email,Customer Phone,Quantity,Unit Price,Total Revenue,Order Status");
        
        SimpleDateFormat csvDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        for (ProductSalesReport report : salesReports) {
            StringBuilder line = new StringBuilder();
            
            if (report.getOrderDate() != null) {
                line.append("\"").append(csvDateFormat.format(report.getOrderDate())).append("\"");
            } else {
                line.append("\"\"");
            }
            line.append(",");
            
            line.append("\"").append(escapeCSV(report.getProductID())).append("\"").append(",");
            
            line.append("\"").append(escapeCSV(report.getProductName())).append("\"").append(",");
            
            line.append("\"").append(escapeCSV(report.getCustomerName())).append("\"").append(",");
            
            line.append("\"").append(escapeCSV(report.getCustomerEmail())).append("\"").append(",");
            
            line.append("\"").append(escapeCSV(report.getCustomerPhone())).append("\"").append(",");
            
            line.append(report.getQuantitySold()).append(",");
            
            if (report.getUnitPrice() != null) {
                line.append("\"").append(report.getUnitPrice().toString()).append("\"");
            } else {
                line.append("\"0\"");
            }
            line.append(",");
            
            if (report.getTotalRevenue() != null) {
                line.append("\"").append(report.getTotalRevenue().toString()).append("\"");
            } else {
                line.append("\"0\"");
            }
            line.append(",");
            
            line.append("\"").append(escapeCSV(report.getOrderStatus())).append("\"");
            
            writer.println(line.toString());
        }
        
        writer.println();
        writer.println("===================");
        writer.println("FINAL SUMMARY:");
        writer.println("===================");
        writer.println("Total Records Exported:,\"" + String.format("%,d", totalRecords) + " records\"");
        writer.println("Total Revenue:,\"$" + String.format("%,.2f", totalRevenue) + "\"");
        writer.println("Total Orders:,\"" + String.format("%,d", totalOrders) + " orders\"");
        writer.println("Total Products Sold:,\"" + String.format("%,d", totalProductsSold) + " items\"");
        writer.println("Average Order Value:,\"$" + String.format("%,.2f", averageOrderValue) + "\"");
        
        if (totalOrders > 0) {
            double avgItemsPerOrder = (double) totalProductsSold / totalOrders;
            writer.println("Average Items per Order:,\"" + String.format("%.1f", avgItemsPerOrder) + " items/order\"");
            
            if (totalRecords > totalOrders) {
                double avgProductTypesPerOrder = (double) totalRecords / totalOrders;
                writer.println("Average Product Types per Order:,\"" + String.format("%.1f", avgProductTypesPerOrder) + " types/order\"");
            }
        }
        
        writer.println();
        writer.println("Report Generated:,\"" + new SimpleDateFormat("EEEE, dd/MM/yyyy 'at' HH:mm:ss").format(new Date()) + "\"");
        writer.println("System Info:,\"Sales Management System v1.0\"");
        
        writer.flush();
        
    } catch (Exception e) {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("Error generating CSV file: " + e.getMessage());
    }
}

    private String escapeCSV(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\"", "\"\"").replace("\n", " ").replace("\r", " ");
    }
    
    private LocalDateTime parseDate(String dateStr, LocalDateTime defaultValue) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return defaultValue;
        }
        
        try {
            return LocalDateTime.parse(dateStr + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException e1) {
            try {
                return LocalDateTime.parse(dateStr + "T00:00:00");
            } catch (DateTimeParseException e2) {
                try {
                    return LocalDateTime.parse(dateStr + " 00:00:00", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                } catch (DateTimeParseException e3) {
                    return defaultValue;
                }
            }
        }
    }
    
    private <T> List<T> paginateList(List<T> list, int offset, int pageSize) {
        if (list == null || list.isEmpty()) {
            return list;
        }
        
        int start = Math.min(offset, list.size());
        int end = Math.min(start + pageSize, list.size());
        
        return list.subList(start, end);
    }
    
    private BigDecimal calculateTotalRevenue(List<ProductSalesReport> reports) {
        return reports.stream()
            .map(report -> report.getTotalRevenue() != null ? report.getTotalRevenue() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    private int calculateTotalProductsSold(List<ProductSalesReport> reports) {
        return reports.stream()
            .mapToInt(report -> report.getQuantitySold())
            .sum();
    }
    
    private int countUniqueOrders(List<ProductSalesReport> reports) {
        return (int) reports.stream()
            .collect(Collectors.groupingBy(report -> 
                report.getCustomerName() + "_" + report.getOrderDate()
            ))
            .size();
    }
    
    private BigDecimal calculateAverageOrderValue(BigDecimal totalRevenue, int totalOrders) {
        if (totalOrders == 0) {
            return BigDecimal.ZERO;
        }
        return totalRevenue.divide(new BigDecimal(totalOrders), 2, RoundingMode.HALF_UP);
    }
    
    private Map<String, Integer> calculateOrderStatusReport(List<ProductSalesReport> reports) {
        return reports.stream()
            .collect(Collectors.groupingBy(
                report -> report.getOrderStatus() != null ? report.getOrderStatus() : "Unknown",
                Collectors.summingInt(report -> 1)
            ));
    }
    
    private List<ProductSalesReport> getTopProductsFromReports(List<ProductSalesReport> reports, int limit) {
        return reports.stream()
            .collect(Collectors.groupingBy(
                report -> report.getProductID(),
                Collectors.summingInt(ProductSalesReport::getQuantitySold)
            ))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(limit)
            .map(entry -> {
                ProductSalesReport topProduct = reports.stream()
                    .filter(r -> r.getProductID().equals(entry.getKey()))
                    .findFirst()
                    .orElse(new ProductSalesReport());
                
                topProduct.setQuantitySold(entry.getValue());
                
                BigDecimal productRevenue = reports.stream()
                    .filter(r -> r.getProductID().equals(entry.getKey()))
                    .map(r -> r.getTotalRevenue() != null ? r.getTotalRevenue() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                topProduct.setTotalRevenue(productRevenue);
                
                return topProduct;
            })
            .collect(Collectors.toList());
    }
    
    private List<ProductSalesReport> getTopCustomersFromReports(List<ProductSalesReport> reports, int limit) {
        return reports.stream()
            .filter(report -> "Completed".equals(report.getOrderStatus()))
            .collect(Collectors.groupingBy(
                report -> report.getCustomerName(),
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    customerReports -> {
                        ProductSalesReport result = customerReports.get(0);
                        
                        BigDecimal totalRevenue = customerReports.stream()
                            .map(r -> r.getTotalRevenue() != null ? r.getTotalRevenue() : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                        result.setTotalRevenue(totalRevenue);
                        
                        int completedOrderCount = (int) customerReports.stream()
                            .collect(Collectors.groupingBy(r -> r.getCustomerName() + "_" + r.getOrderDate()))
                            .size();
                        result.setQuantitySold(completedOrderCount);
                        
                        return result;
                    }
                )
            ))
            .values().stream()
            .sorted((c1, c2) -> Integer.compare(c2.getQuantitySold(), c1.getQuantitySold()))
            .limit(limit)
            .collect(Collectors.toList());
    }
}
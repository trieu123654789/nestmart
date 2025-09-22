/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.models;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SalesReportDAOImpl implements SalesReportDAO {
    
    private JdbcTemplate jdbcTemplate;
    
    // Default constructor
    public SalesReportDAOImpl() {
    }
    
    // Setter method for Spring dependency injection
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
   // RowMapper for ProductSalesReport
private static final RowMapper<ProductSalesReport> REPORT_ROW_MAPPER = new RowMapper<ProductSalesReport>() {
    @Override
    public ProductSalesReport mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductSalesReport report = new ProductSalesReport();
        report.setProductID(rs.getString("productID"));
        report.setProductName(rs.getString("productName"));
        report.setCustomerName(rs.getString("customerName"));
        report.setCustomerEmail(rs.getString("customerEmail"));
        report.setCustomerPhone(rs.getString("customerPhone"));
        report.setQuantitySold(rs.getInt("quantitySold"));
        report.setUnitPrice(rs.getBigDecimal("unitPrice"));
        report.setTotalRevenue(rs.getBigDecimal("totalRevenue"));

        Timestamp orderTimestamp = rs.getTimestamp("orderDate");
        if (orderTimestamp != null) {
            report.setOrderDate(orderTimestamp); // vì Timestamp extends java.util.Date
        }

        report.setOrderStatus(rs.getString("orderStatus"));
        report.setOrderCount(rs.getInt("orderCount"));

        return report;
    }
};

    @Override
public List<ProductSalesReport> getSalesReport(LocalDateTime startDate, LocalDateTime endDate) {
    String sql = 
        "SELECT " +
        "    COALESCE(p.productID, 'N/A') as productID, " +
        "    COALESCE(p.productName, 'No Product Data') as productName, " +
        "    a.fullName as customerName, " +
        "    a.email as customerEmail, " +
        "    a.phoneNumber as customerPhone, " +
        "    COALESCE(od.quantity, 1) as quantitySold, " +
        "    COALESCE(od.unitPrice, o.totalAmount) as unitPrice, " +
        "    COALESCE((od.quantity * od.unitPrice), o.totalAmount) as totalRevenue, " +
        "    o.orderDate, " +
        "    o.orderStatus, " +
        "    1 as orderCount " +
        "FROM Orders o " +
        "LEFT JOIN OrderDetails od ON o.orderID = od.orderID " +
        "LEFT JOIN Products p ON od.productID = p.productID " +
        "INNER JOIN Accounts a ON o.customerID = a.accountID " +
        "WHERE o.orderDate BETWEEN ? AND ? " +
        "ORDER BY o.orderDate DESC, COALESCE(p.productName, 'No Product Data')";
    
    return jdbcTemplate.query(sql, REPORT_ROW_MAPPER, 
                             Timestamp.valueOf(startDate), 
                             Timestamp.valueOf(endDate));
}
   
// You should also update the other filtering methods to handle null dates
@Override
public List<ProductSalesReport> getSalesReportByProduct(String productID, LocalDateTime startDate, LocalDateTime endDate) {
    StringBuilder sql = new StringBuilder(
        "SELECT " +
        "    COALESCE(p.productID, 'N/A') as productID, " +
        "    COALESCE(p.productName, 'No Product Data') as productName, " +
        "    a.fullName as customerName, " +
        "    a.email as customerEmail, " +
        "    a.phoneNumber as customerPhone, " +
        "    COALESCE(od.quantity, 1) as quantitySold, " +
        "    COALESCE(od.unitPrice, o.totalAmount) as unitPrice, " +
        "    COALESCE((od.quantity * od.unitPrice), o.totalAmount) as totalRevenue, " +
        "    o.orderDate, " +
        "    o.orderStatus, " +
        "    1 as orderCount " +
        "FROM Orders o " +
        "LEFT JOIN OrderDetails od ON o.orderID = od.orderID " +
        "LEFT JOIN Products p ON od.productID = p.productID " +
        "INNER JOIN Accounts a ON o.customerID = a.accountID " +
        "WHERE (p.productID = ? OR (p.productID IS NULL AND ? IS NOT NULL))");

    List<Object> params = new ArrayList<>();
    params.add(productID);
    params.add(productID);
    
    // Add date conditions
    if (startDate != null || endDate != null) {
        if (startDate != null && endDate != null) {
            sql.append(" AND o.orderDate BETWEEN ? AND ?");
            params.add(Timestamp.valueOf(startDate));
            params.add(Timestamp.valueOf(endDate));
        } else if (startDate != null) {
            sql.append(" AND o.orderDate >= ?");
            params.add(Timestamp.valueOf(startDate));
        } else {
            sql.append(" AND o.orderDate <= ?");
            params.add(Timestamp.valueOf(endDate));
        }
    }
    
    sql.append(" ORDER BY o.orderDate DESC");

    return jdbcTemplate.query(sql.toString(), REPORT_ROW_MAPPER, params.toArray());
}
    @Override
public List<ProductSalesReport> getSalesReportByProductAndCustomer(String productID, int customerID, LocalDateTime start, LocalDateTime end) {
    String sql = 
        "SELECT " +
        "    COALESCE(p.productID, 'N/A') as productID, " +
        "    COALESCE(p.productName, 'No Product Data') as productName, " +
        "    a.fullName as customerName, " +
        "    a.email as customerEmail, " +
        "    a.phoneNumber as customerPhone, " +
        "    COALESCE(od.quantity, 1) as quantitySold, " +
        "    COALESCE(od.unitPrice, o.totalAmount) as unitPrice, " +
        "    COALESCE((od.quantity * od.unitPrice), o.totalAmount) as totalRevenue, " +
        "    o.orderDate, " +
        "    o.orderStatus, " +
        "    1 as orderCount " +
        "FROM Orders o " +
        "LEFT JOIN OrderDetails od ON o.orderID = od.orderID " +
        "LEFT JOIN Products p ON od.productID = p.productID " +
        "INNER JOIN Accounts a ON o.customerID = a.accountID " +
        "WHERE (p.productID = ? OR (p.productID IS NULL AND ? IS NOT NULL)) " +
        "    AND o.customerID = ? AND o.orderDate BETWEEN ? AND ? " +
        "ORDER BY o.orderDate DESC";

    return jdbcTemplate.query(sql, REPORT_ROW_MAPPER, 
                             productID, productID, customerID,
                             Timestamp.valueOf(start), 
                             Timestamp.valueOf(end));
}
    
    @Override
    public List<ProductSalesReport> getSalesReportByCustomer(int customerID, LocalDateTime startDate, LocalDateTime endDate) {
        String sql = 
            "SELECT " +
            "    COALESCE(p.productID, 'N/A') as productID, " +
            "    COALESCE(p.productName, 'No Product Data') as productName, " +
            "    a.fullName as customerName, " +
            "    a.email as customerEmail, " +
            "    a.phoneNumber as customerPhone, " +
            "    COALESCE(od.quantity, 1) as quantitySold, " +
            "    COALESCE(od.unitPrice, o.totalAmount) as unitPrice, " +
            "    COALESCE((od.quantity * od.unitPrice), o.totalAmount) as totalRevenue, " +
            "    o.orderDate, " +
            "    o.orderStatus, " +
            "    1 as orderCount " +
            "FROM Orders o " +
            "LEFT JOIN OrderDetails od ON o.orderID = od.orderID " +
            "LEFT JOIN Products p ON od.productID = p.productID " +
            "INNER JOIN Accounts a ON o.customerID = a.accountID " +
            "WHERE o.customerID = ? AND o.orderDate BETWEEN ? AND ? " +
            "ORDER BY o.orderDate DESC, COALESCE(p.productName, 'No Product Data')";

        return jdbcTemplate.query(sql, REPORT_ROW_MAPPER, 
                                 customerID,
                                 Timestamp.valueOf(startDate), 
                                 Timestamp.valueOf(endDate));
    }
    
 @Override
    public List<ProductSalesReport> getTopSellingProducts(int limit, LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate == null) {
            startDate = LocalDateTime.of(2000, 1, 1, 0, 0);
        }
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }

        String sql
                = "SELECT TOP " + limit + " "
                + "COALESCE(p.productID, 'N/A') as productID, "
                + "COALESCE(p.productName, 'No Product Data') as productName, "
                + "'' as customerName, "
                + "'' as customerEmail, "
                + "'' as customerPhone, "
                + "SUM(COALESCE(od.quantity, 1)) as quantitySold, "
                + "AVG(COALESCE(od.unitPrice, o.totalAmount)) as unitPrice, "
                + "SUM(COALESCE((od.quantity * od.unitPrice), o.totalAmount)) as totalRevenue, "
                + "MAX(o.orderDate) as orderDate, "
                + "'COMPLETED' as orderStatus, "
                + "COUNT(DISTINCT o.orderID) as orderCount "
                + "FROM Orders o "
                + "LEFT JOIN OrderDetails od ON o.orderID = od.orderID "
                + "LEFT JOIN Products p ON od.productID = p.productID "
                + "WHERE o.orderDate BETWEEN ? AND ? "
                + "AND o.orderStatus = 'COMPLETED' "
                + "GROUP BY p.productID, p.productName "
                + "ORDER BY SUM(COALESCE(od.quantity, 1)) DESC";

        return jdbcTemplate.query(sql, REPORT_ROW_MAPPER,
                Timestamp.valueOf(startDate),
                Timestamp.valueOf(endDate));
    }

@Override
public List<ProductSalesReport> getTopCustomers(int limit, LocalDateTime startDate, LocalDateTime endDate) {
    StringBuilder sql = new StringBuilder(
        "SELECT TOP " + limit + " " +
        "    '' AS productID, " +
        "    '' AS productName, " +
        "    a.fullName AS customerName, " +
        "    a.email AS customerEmail, " +
        "    a.phoneNumber AS customerPhone, " +
        "    SUM(COALESCE(od.quantity, 0)) AS quantitySold, " +
        "    AVG(COALESCE(od.unitPrice, 0)) AS unitPrice, " +
        "    SUM(COALESCE(od.quantity * od.unitPrice, 0)) AS totalRevenue, " +
        "    MAX(o.orderDate) AS orderDate, " +
        "    'COMPLETED' AS orderStatus, " +
        "    COUNT(DISTINCT o.orderID) AS orderCount " +
        "FROM Orders o " +
        "INNER JOIN Accounts a ON o.customerID = a.accountID " +
        "LEFT JOIN OrderDetails od ON o.orderID = od.orderID " +
        "WHERE o.orderStatus = 'Completed'"
    );

    List<Object> params = new ArrayList<>();

    if (startDate != null || endDate != null) {
        if (startDate != null && endDate != null) {
            sql.append(" AND o.orderDate BETWEEN ? AND ?");
            params.add(Timestamp.valueOf(startDate));
            params.add(Timestamp.valueOf(endDate));
        } else if (startDate != null) {
            sql.append(" AND o.orderDate >= ?");
            params.add(Timestamp.valueOf(startDate));
        } else {
            sql.append(" AND o.orderDate <= ?");
            params.add(Timestamp.valueOf(endDate));
        }
    }

    sql.append(" GROUP BY a.accountID, a.fullName, a.email, a.phoneNumber " +
               "ORDER BY COUNT(DISTINCT o.orderID) DESC");

    return jdbcTemplate.query(sql.toString(), REPORT_ROW_MAPPER, params.toArray());
}

    @Override
public BigDecimal getTotalRevenue(LocalDateTime startDate, LocalDateTime endDate) {
    String sql;
    List<Object> params = new ArrayList<>();

    if (startDate == null && endDate == null) {
        sql = "SELECT COALESCE(SUM(COALESCE((od.quantity * od.unitPrice), o.totalAmount)), 0) as totalRevenue "
                + "FROM Orders o "
                + "LEFT JOIN OrderDetails od ON o.orderID = od.orderID "
                + "WHERE o.orderStatus = 'COMPLETED'";
    } else if (startDate == null) {
        sql = "SELECT COALESCE(SUM(COALESCE((od.quantity * od.unitPrice), o.totalAmount)), 0) as totalRevenue "
                + "FROM Orders o "
                + "LEFT JOIN OrderDetails od ON o.orderID = od.orderID "
                + "WHERE o.orderStatus = 'COMPLETED' AND o.orderDate <= ?";
        params.add(Timestamp.valueOf(endDate));
    } else if (endDate == null) {
        sql = "SELECT COALESCE(SUM(COALESCE((od.quantity * od.unitPrice), o.totalAmount)), 0) as totalRevenue "
                + "FROM Orders o "
                + "LEFT JOIN OrderDetails od ON o.orderID = od.orderID "
                + "WHERE o.orderStatus = 'COMPLETED' AND o.orderDate >= ?";
        params.add(Timestamp.valueOf(startDate));
    } else {
        sql = "SELECT COALESCE(SUM(COALESCE((od.quantity * od.unitPrice), o.totalAmount)), 0) as totalRevenue "
                + "FROM Orders o "
                + "LEFT JOIN OrderDetails od ON o.orderID = od.orderID "
                + "WHERE o.orderStatus = 'COMPLETED' AND o.orderDate BETWEEN ? AND ?";
        params.add(Timestamp.valueOf(startDate));
        params.add(Timestamp.valueOf(endDate));
    }

    BigDecimal result = jdbcTemplate.queryForObject(sql, BigDecimal.class, params.toArray());
    return result != null ? result : BigDecimal.ZERO;
}

   
// Also add similar methods for other statistics
@Override
public int getTotalOrders(LocalDateTime startDate, LocalDateTime endDate) {
    String sql;
    List<Object> params = new ArrayList<>();
    
    if (startDate == null && endDate == null) {
        sql = "SELECT COUNT(DISTINCT o.orderID) as totalOrders FROM Orders o";
    } else if (startDate == null) {
        sql = "SELECT COUNT(DISTINCT o.orderID) as totalOrders FROM Orders o WHERE o.orderDate <= ?";
        params.add(Timestamp.valueOf(endDate));
    } else if (endDate == null) {
        sql = "SELECT COUNT(DISTINCT o.orderID) as totalOrders FROM Orders o WHERE o.orderDate >= ?";
        params.add(Timestamp.valueOf(startDate));
    } else {
        sql = "SELECT COUNT(DISTINCT o.orderID) as totalOrders FROM Orders o WHERE o.orderDate BETWEEN ? AND ?";
        params.add(Timestamp.valueOf(startDate));
        params.add(Timestamp.valueOf(endDate));
    }

    Integer result = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());
    return result != null ? result : 0;
}
    
   @Override
public int getTotalProductsSold(LocalDateTime startDate, LocalDateTime endDate) {
    String sql;
    List<Object> params = new ArrayList<>();

    if (startDate == null && endDate == null) {
        sql = "SELECT COALESCE(SUM(od.quantity), 0) as totalProductsSold " +
              "FROM Orders o " +
              "INNER JOIN OrderDetails od ON o.orderID = od.orderID " +
              "WHERE o.OrderStatus = 'Completed'";
    } else if (startDate == null) {
        sql = "SELECT COALESCE(SUM(od.quantity), 0) as totalProductsSold " +
              "FROM Orders o " +
              "INNER JOIN OrderDetails od ON o.orderID = od.orderID " +
              "WHERE o.OrderStatus = 'Completed' AND o.orderDate <= ?";
        params.add(Timestamp.valueOf(endDate));
    } else if (endDate == null) {
        sql = "SELECT COALESCE(SUM(od.quantity), 0) as totalProductsSold " +
              "FROM Orders o " +
              "INNER JOIN OrderDetails od ON o.orderID = od.orderID " +
              "WHERE o.OrderStatus = 'Completed' AND o.orderDate >= ?";
        params.add(Timestamp.valueOf(startDate));
    } else {
        sql = "SELECT COALESCE(SUM(od.quantity), 0) as totalProductsSold " +
              "FROM Orders o " +
              "INNER JOIN OrderDetails od ON o.orderID = od.orderID " +
              "WHERE o.OrderStatus = 'Completed' AND o.orderDate BETWEEN ? AND ?";
        params.add(Timestamp.valueOf(startDate));
        params.add(Timestamp.valueOf(endDate));
    }

    Integer result = jdbcTemplate.queryForObject(sql, Integer.class, params.toArray());
    return result != null ? result : 0;
}

    
    @Override
    public Map<Integer, BigDecimal> getMonthlyRevenue(int year) {
        String sql = 
            "SELECT " +
            "    MONTH(o.orderDate) as month, " +
            "    COALESCE(SUM(COALESCE((od.quantity * od.unitPrice), o.totalAmount)), 0) as revenue " +
            "FROM Orders o " +
            "LEFT JOIN OrderDetails od ON o.orderID = od.orderID " +
            "WHERE YEAR(o.orderDate) = ? " +
            "GROUP BY MONTH(o.orderDate) " +
            "ORDER BY MONTH(o.orderDate)";

        Map<Integer, BigDecimal> monthlyRevenue = new HashMap<>();
        
        jdbcTemplate.query(sql, (rs) -> {
            int month = rs.getInt("month");
            BigDecimal revenue = rs.getBigDecimal("revenue");
            monthlyRevenue.put(month, revenue);
        }, year);
        
        return monthlyRevenue;
    }
    
    @Override
public Map<String, Integer> getOrderStatusReport(LocalDateTime startDate, LocalDateTime endDate) {
    String sql;
    List<Object> params = new ArrayList<>();
    
    if (startDate == null && endDate == null) {
        sql = "SELECT o.orderStatus, COUNT(*) as orderCount FROM Orders o " +
              "GROUP BY o.orderStatus ORDER BY o.orderStatus";
    } else if (startDate == null) {
        sql = "SELECT o.orderStatus, COUNT(*) as orderCount FROM Orders o " +
              "WHERE o.orderDate <= ? GROUP BY o.orderStatus ORDER BY o.orderStatus";
        params.add(Timestamp.valueOf(endDate));
    } else if (endDate == null) {
        sql = "SELECT o.orderStatus, COUNT(*) as orderCount FROM Orders o " +
              "WHERE o.orderDate >= ? GROUP BY o.orderStatus ORDER BY o.orderStatus";
        params.add(Timestamp.valueOf(startDate));
    } else {
        sql = "SELECT o.orderStatus, COUNT(*) as orderCount FROM Orders o " +
              "WHERE o.orderDate BETWEEN ? AND ? GROUP BY o.orderStatus ORDER BY o.orderStatus";
        params.add(Timestamp.valueOf(startDate));
        params.add(Timestamp.valueOf(endDate));
    }

    Map<String, Integer> statusReport = new HashMap<>();
    
    jdbcTemplate.query(sql, (rs) -> {
        String status = rs.getString("orderStatus");
        int count = rs.getInt("orderCount");
        statusReport.put(status, count);
    }, params.toArray());
    
    return statusReport;
}
  @Override
public List<ProductSalesReport> getSalesReportPaginated(LocalDateTime startDate, LocalDateTime endDate, int offset, int limit) {
    StringBuilder sql = new StringBuilder(
        "SELECT " +
        "    COALESCE(p.productID, 'N/A') as productID, " +
        "    COALESCE(p.productName, 'No Product Data') as productName, " +
        "    a.fullName as customerName, " +
        "    a.email as customerEmail, " +
        "    a.phoneNumber as customerPhone, " +
        "    COALESCE(od.quantity, 1) as quantitySold, " +
        "    COALESCE(od.unitPrice, o.totalAmount) as unitPrice, " +
        "    COALESCE((od.quantity * od.unitPrice), o.totalAmount) as totalRevenue, " +
        "    o.orderDate, " +
        "    o.orderStatus, " +
        "    1 as orderCount " +
        "FROM Orders o " +
        "LEFT JOIN OrderDetails od ON o.orderID = od.orderID " +
        "LEFT JOIN Products p ON od.productID = p.productID " +
        "INNER JOIN Accounts a ON o.customerID = a.accountID");

    List<Object> params = new ArrayList<>();
    
    // Add WHERE conditions based on available dates
    if (startDate != null || endDate != null) {
        sql.append(" WHERE ");
        if (startDate != null && endDate != null) {
            sql.append("o.orderDate BETWEEN ? AND ?");
            params.add(Timestamp.valueOf(startDate));
            params.add(Timestamp.valueOf(endDate));
        } else if (startDate != null) {
            sql.append("o.orderDate >= ?");
            params.add(Timestamp.valueOf(startDate));
        } else {
            sql.append("o.orderDate <= ?");
            params.add(Timestamp.valueOf(endDate));
        }
    }
    
    sql.append(" ORDER BY o.orderDate DESC, COALESCE(p.productName, 'No Product Data') " +
               "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
    
    params.add(offset);
    params.add(limit);

    return jdbcTemplate.query(sql.toString(), REPORT_ROW_MAPPER, params.toArray());
}
    
   @Override
public int countSalesReport(LocalDateTime startDate, LocalDateTime endDate) {
    StringBuilder sql = new StringBuilder(
        "SELECT COUNT(*) as totalCount " +
        "FROM Orders o " +
        "LEFT JOIN OrderDetails od ON o.orderID = od.orderID");

    List<Object> params = new ArrayList<>();
    
    // Add WHERE conditions based on available dates
    if (startDate != null || endDate != null) {
        sql.append(" WHERE ");
        if (startDate != null && endDate != null) {
            sql.append("o.orderDate BETWEEN ? AND ?");
            params.add(Timestamp.valueOf(startDate));
            params.add(Timestamp.valueOf(endDate));
        } else if (startDate != null) {
            sql.append("o.orderDate >= ?");
            params.add(Timestamp.valueOf(startDate));
        } else {
            sql.append("o.orderDate <= ?");
            params.add(Timestamp.valueOf(endDate));
        }
    }

    Integer result = jdbcTemplate.queryForObject(sql.toString(), Integer.class, params.toArray());
    return result != null ? result : 0;
}

}
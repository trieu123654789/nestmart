package com.models;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderDetailsDAOImpl implements OrderDetailsDAO {
    private JdbcTemplate jdbcTemplate;

    // Save a new order detail into the database
    @Override
    public void saveOrderDetail(String orderId, String productId, int quantity, BigDecimal unitPrice, BigDecimal totalPrice) {
        String sql = "INSERT INTO OrderDetails (OrderID, ProductID, Quantity, UnitPrice, TotalPrice) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, orderId, productId, quantity, unitPrice, totalPrice);
    }

    // Find all order details by order ID (int)
    @Override
    public List<OrderDetails> findByOrderId(int orderID) {
        String sql = "SELECT * FROM OrderDetails WHERE OrderID = ?";
        return jdbcTemplate.query(sql, new Object[]{orderID}, new BeanPropertyRowMapper<>(OrderDetails.class));
    }

    // Get JdbcTemplate instance
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    // Set JdbcTemplate instance
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Get order details by order ID (String), only selected fields
    @Override
    public List<OrderDetails> getOrderDetailsByOrderId(String orderId) {
        String sql = "SELECT OrderDetailID, ProductID, Quantity, UnitPrice FROM OrderDetails WHERE OrderID = ?";
        return jdbcTemplate.query(sql, new Object[]{orderId}, new BeanPropertyRowMapper<>(OrderDetails.class));
    }

    // Get product ID from a given order detail
    @Override
    public String getProductIdFromDetail(OrderDetails detail) {
        try {
            String sql = "SELECT ProductID FROM OrderDetails WHERE OrderDetailID = ?";
            return jdbcTemplate.queryForObject(sql, String.class, detail.getOrderDetailID());
        } catch (Exception e) {
            return null;
        }
    }
}

package com.models;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;

public class OrderDetailsDTODAOImpl implements OrderDetailsDTODAO {

    private JdbcTemplate jdbcTemplate;

    // Find order details by order ID and include product name & first product image
    @Override
    public List<OrderDetailsDTO> findByOrderId(String orderID) {
        String sql = "SELECT od.ProductID, od.Quantity, od.UnitPrice, od.TotalPrice, p.ProductName, pi.Image "
                   + "FROM OrderDetails od "
                   + "JOIN Products p ON od.ProductID = p.ProductID "
                   + "JOIN ProductImage pi ON p.ProductID = pi.ProductID "
                   + "WHERE pi.ProductImageID = ( "
                   + "    SELECT MIN(pi2.ProductImageID) "
                   + "    FROM ProductImage pi2 "
                   + "    WHERE pi2.ProductID = p.ProductID "
                   + ") "
                   + "AND od.OrderID = ?";

        return jdbcTemplate.query(sql, new Object[]{orderID}, (rs, rowNum) -> {
            OrderDetailsDTO dto = new OrderDetailsDTO();
            dto.setProductID(rs.getString("ProductID"));
            dto.setQuantity(rs.getInt("Quantity"));
            dto.setUnitPrice(rs.getDouble("UnitPrice"));
            dto.setTotalPrice(rs.getDouble("TotalPrice"));
            dto.setProductName(rs.getString("ProductName"));
            dto.setImage(rs.getString("Image"));
            return dto;
        });
    }

    // Get JdbcTemplate instance
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    // Set JdbcTemplate instance
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}

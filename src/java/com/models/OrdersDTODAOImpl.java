package com.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class OrdersDTODAOImpl implements OrdersDTODAO {

    private JdbcTemplate jdbcTemplate;

    // Finds an order by its ID
    @Override
    public OrdersDTO findByOrderId(String orderID) {
        String query = "SELECT OrderID, CustomerID, Name, Phone, ShippingAddress, PaymentMethod, Notes, "
                + "TotalAmount, OrderDate, OrderStatus, ShipperID "
                + "FROM Orders WHERE OrderID = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{orderID}, (rs, rowNum) ->
            new OrdersDTO(
                rs.getString("OrderID"),
                rs.getInt("CustomerID"),
                rs.getString("Name"),
                rs.getString("Phone"),
                rs.getString("ShippingAddress"),
                rs.getString("PaymentMethod"),
                rs.getString("Notes"),
                rs.getBigDecimal("TotalAmount"),
                rs.getObject("OrderDate", LocalDateTime.class),
                rs.getString("OrderStatus"),
                rs.getInt("ShipperID")
            )
        );
    }

    // Returns paginated list of orders
    @Override
    public List<OrdersDTO> findPaginated(int page, int pageSize) {
        if (page < 1 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid page number or page size");
        }

        String query = "SELECT OrderID, CustomerID, Name, Phone, ShippingAddress, PaymentMethod, Notes, TotalAmount, OrderDate, OrderStatus, ShipperID "
                + "FROM Orders "
                + "ORDER BY OrderID "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        int offset = (page - 1) * pageSize;

        return jdbcTemplate.query(query, new Object[]{offset, pageSize}, (rs, rowNum) ->
            new OrdersDTO(
                rs.getString("OrderID"),
                rs.getInt("CustomerID"),
                rs.getString("Name"),
                rs.getString("Phone"),
                rs.getString("ShippingAddress"),
                rs.getString("PaymentMethod"),
                rs.getString("Notes"),
                rs.getBigDecimal("TotalAmount"),
                rs.getObject("OrderDate", LocalDateTime.class),
                rs.getString("OrderStatus"),
                rs.getInt("ShipperID")
            )
        );
    }

    // Searches orders by keyword in Name, OrderID, or Phone
    @Override
    public List<OrdersDTO> searchByKeyword(String keyword) {
        String query = "SELECT OrderID, CustomerID, Name, Phone, ShippingAddress, PaymentMethod, Notes, TotalAmount, OrderDate, OrderStatus, ShipperID "
                + "FROM Orders "
                + "WHERE Name LIKE ? OR CAST(OrderID AS VARCHAR) LIKE ? OR Phone LIKE ? "
                + "ORDER BY OrderID";

        String searchKeyword = "%" + keyword + "%";

        return jdbcTemplate.query(query, new Object[]{searchKeyword, searchKeyword, searchKeyword}, (rs, rowNum) ->
            new OrdersDTO(
                rs.getString("OrderID"),
                rs.getInt("CustomerID"),
                rs.getString("Name"),
                rs.getString("Phone"),
                rs.getString("ShippingAddress"),
                rs.getString("PaymentMethod"),
                rs.getString("Notes"),
                rs.getBigDecimal("TotalAmount"),
                rs.getObject("OrderDate", LocalDateTime.class),
                rs.getString("OrderStatus"),
                rs.getInt("ShipperID")
            )
        );
    }

    // Finds orders by customer ID
    @Override
    public List<OrdersDTO> findOrdersByCustomerId(int customerId) {
        String query = "SELECT OrderID, CustomerID, Name, Phone, ShippingAddress, PaymentMethod, Notes, TotalAmount, OrderDate, OrderStatus, ShipperID "
                + "FROM Orders "
                + "WHERE CustomerID = ? "
                + "ORDER BY OrderID";

        return jdbcTemplate.query(query, new Object[]{customerId}, (rs, rowNum) ->
            new OrdersDTO(
                rs.getString("OrderID"),
                rs.getInt("CustomerID"),
                rs.getString("Name"),
                rs.getString("Phone"),
                rs.getString("ShippingAddress"),
                rs.getString("PaymentMethod"),
                rs.getString("Notes"),
                rs.getBigDecimal("TotalAmount"),
                rs.getObject("OrderDate", LocalDateTime.class),
                rs.getString("OrderStatus"),
                rs.getInt("ShipperID")
            )
        );
    }

    // Returns total number of orders
    @Override
    public int getTotalOrders() {
        String query = "SELECT COUNT(*) FROM Orders";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    // Counts orders by keyword matching Name field
    @Override
    public int countByKeyword(String keyword) {
        String query = "SELECT COUNT(*) FROM Orders WHERE Name LIKE ?";
        return jdbcTemplate.queryForObject(query, new Object[]{"%" + keyword + "%"}, Integer.class);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}

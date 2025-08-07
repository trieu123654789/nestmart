package com.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class ReturnRequestResponseDAOImpl implements ReturnRequestResponseDAO {

    private JdbcTemplate jdbcTemplate;

    // Default constructor
    public ReturnRequestResponseDAOImpl() {
    }

    // Constructor with DataSource
    public ReturnRequestResponseDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        System.out.println("JdbcTemplate initialized: " + (this.jdbcTemplate != null));
    }

    // Get orders that have return requests
    @Override
    public List<Orders> getOrdersWithReturnRequests() {
        String sql = "SELECT o.*, c.FullName AS CustomerName, c.Email AS CustomerEmail, "
                   + "c.PhoneNumber AS CustomerPhone, c.Address AS CustomerAddress, "
                   + "s.FullName AS ShipperName, s.Email AS ShipperEmail, "
                   + "s.PhoneNumber AS ShipperPhone, s.Address AS ShipperAddress, "
                   + "od.ProductID, p.ProductName, od.Quantity, od.UnitPrice, od.TotalPrice "
                   + "FROM Orders o "
                   + "JOIN Accounts c ON o.CustomerID = c.AccountID "
                   + "LEFT JOIN Accounts s ON o.ShipperID = s.AccountID "
                   + "JOIN OrderDetails od ON o.OrderID = od.OrderID "
                   + "JOIN Products p ON od.ProductID = p.ProductID "
                   + "WHERE o.OrderStatus IN ('Return Requested')";
        return jdbcTemplate.query(sql, new OrderRowMapper());
    }

    // Add a new return request response
    @Override
    public void addReturnRequestResponse(ReturnRequestResponse response) {
        String sql = "INSERT INTO ReturnRequestResponse (OrderID, EmployeeID, ReturnRequestResponseDate, Message) "
                   + "VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, response.getOrderID(), response.getEmployeeID(),
                Timestamp.valueOf(response.getReturnRequestResponseDate()), response.getMessage());
        System.out.println("Inserted into ReturnRequestResponse table successfully");
    }

    // Get completed return orders
    @Override
    public List<Orders> getCompletedReturnOrders() {
        String sql = "SELECT o.*, c.FullName AS CustomerName, c.Email AS CustomerEmail, "
                   + "c.PhoneNumber AS CustomerPhone, c.Address AS CustomerAddress, "
                   + "s.FullName AS ShipperName, s.Email AS ShipperEmail, "
                   + "s.PhoneNumber AS ShipperPhone, s.Address AS ShipperAddress, "
                   + "od.ProductID, p.ProductName, od.Quantity, od.UnitPrice, od.TotalPrice "
                   + "FROM Orders o "
                   + "JOIN Accounts c ON o.CustomerID = c.AccountID "
                   + "LEFT JOIN Accounts s ON o.ShipperID = s.AccountID "
                   + "JOIN OrderDetails od ON o.OrderID = od.OrderID "
                   + "JOIN Products p ON od.ProductID = p.ProductID "
                   + "WHERE o.OrderStatus IN ('Return Completed')";
        return jdbcTemplate.query(sql, new OrderRowMapper());
    }

    // Delete a completed return order and related data
    @Override
    public void deleteCompletedReturnOrder(String orderID) {
        try {
            String deleteReturnRequestResponseSql = "DELETE FROM ReturnRequestResponse WHERE OrderID = ?";
            int returnRequestResponseDeleted = jdbcTemplate.update(deleteReturnRequestResponseSql, orderID);
            System.out.println("Deleted " + returnRequestResponseDeleted + " return request responses for orderID: " + orderID);

            String deleteOrderDetailsSql = "DELETE FROM OrderDetails WHERE OrderID = ?";
            int orderDetailsDeleted = jdbcTemplate.update(deleteOrderDetailsSql, orderID);
            System.out.println("Deleted " + orderDetailsDeleted + " order details for orderID: " + orderID);

            String deleteOrderSql = "DELETE FROM Orders WHERE OrderID = ? AND OrderStatus = 'Return Completed'";
            int rowsAffected = jdbcTemplate.update(deleteOrderSql, orderID);

            if (rowsAffected > 0) {
                System.out.println("Successfully deleted completed return order with ID: " + orderID);
            } else {
                System.out.println("No completed return order found with ID: " + orderID + " or order is not in 'Return Completed' status");
            }
        } catch (Exception e) {
            System.err.println("Error deleting completed return order " + orderID + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to delete completed return order: " + e.getMessage(), e);
        }
    }

    // Update order status and return request status
    @Override
    public void updateOrderStatus(String orderID, String newOrderStatus, String newReturnRequestStatus) {
        String sql = "UPDATE Orders SET OrderStatus = ?, ReturnRequestStatus = ? WHERE OrderID = ?";
        jdbcTemplate.update(sql, newOrderStatus, newReturnRequestStatus, orderID);
        System.out.println("Updated Order status and Return Request status successfully");
    }

    // RowMapper for Orders including order details and product
    private static class OrderRowMapper implements RowMapper<Orders> {
        @Override
        public Orders mapRow(ResultSet rs, int rowNum) throws SQLException {
            Orders order = new Orders();
            order.setOrderID(rs.getString("OrderID"));
            order.setCustomerName(rs.getString("CustomerName"));
            order.setCustomerEmail(rs.getString("CustomerEmail"));

            Timestamp orderTimestamp = rs.getTimestamp("OrderDate");
            if (orderTimestamp != null) {
                order.setOrderDate(orderTimestamp.toLocalDateTime());
            }
            order.setFormattedOrderDate(rs.getString("OrderDate"));

            Timestamp returnRequestTimestamp = rs.getTimestamp("ReturnRequestDate");
            if (returnRequestTimestamp != null) {
                order.setReturnRequestDate(returnRequestTimestamp.toLocalDateTime());
            }
            order.setFormattedReturnRequestDate(rs.getString("ReturnRequestDate"));

            order.setTotalAmount(rs.getBigDecimal("TotalAmount"));
            order.setPaymentMethod(rs.getString("PaymentMethod"));
            order.setShippingAddress(rs.getString("ShippingAddress"));
            order.setReturnRequestReason(rs.getString("ReturnRequestReason"));

            List<OrderDetails> orderDetailsList = new ArrayList<>();
            OrderDetails orderDetail = new OrderDetails();
            orderDetail.setProductID(rs.getString("ProductID"));
            orderDetail.setQuantity(rs.getInt("Quantity"));
            orderDetail.setUnitPrice(rs.getBigDecimal("UnitPrice"));
            orderDetail.setTotalPrice(rs.getBigDecimal("TotalPrice"));

            Products product = new Products();
            product.setProductID(rs.getString("ProductID"));
            product.setProductName(rs.getString("ProductName"));

            orderDetail.setProduct(product);
            orderDetailsList.add(orderDetail);

            order.setOrderDetails(orderDetailsList);

            return order;
        }
    }

    // Get return request responses by order ID
    @Override
    public List<ReturnRequestResponse> getReturnResponses(String orderId) {
        String sql = "SELECT ReturnRequestResponseID, OrderID, EmployeeID, ReturnRequestResponseDate, Message "
                   + "FROM ReturnRequestResponse "
                   + "WHERE OrderID = ? "
                   + "ORDER BY ReturnRequestResponseDate ASC";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, orderId);
        List<ReturnRequestResponse> responses = new ArrayList<>();

        for (Map<String, Object> rs : rows) {
            ReturnRequestResponse resp = new ReturnRequestResponse();
            resp.setReturnRequestResponseID((int) rs.get("ReturnRequestResponseID"));
            resp.setOrderID((String) rs.get("OrderID"));
            resp.setEmployeeID((int) rs.get("EmployeeID"));

            Timestamp ts = (Timestamp) rs.get("ReturnRequestResponseDate");
            if (ts != null) {
                resp.setReturnRequestResponseDate(ts.toLocalDateTime());
            }

            resp.setMessage((String) rs.get("Message"));
            responses.add(resp);
        }
        return responses;
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

package com.models;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class OrdersDAOImpl implements OrdersDAO {

    private JdbcTemplate jdbcTemplate;

    public OrdersDAOImpl() {
    }

    public OrdersDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Get all orders assigned to a specific shipper
    @Override
    public List<Orders> getOrdersByShipper(int shipperID) {
        String query = "SELECT o.*, c.FullName AS CustomerName, c.Email AS CustomerEmail, c.PhoneNumber AS CustomerPhone, c.Address AS CustomerAddress, "
                + "s.FullName AS ShipperName, s.Email AS ShipperEmail, s.PhoneNumber AS ShipperPhone, s.Address AS ShipperAddress "
                + "FROM Orders o "
                + "JOIN Accounts c ON o.CustomerID = c.AccountID "
                + "JOIN Accounts s ON o.ShipperID = s.AccountID "
                + "WHERE o.ShipperID = ?";
        return jdbcTemplate.query(query, new Object[]{shipperID}, new OrderRowMapper());
    }

    // Get voucher information used for a specific order
    @Override
    public Map<String, Object> getUsedVoucherInfo(String orderId) {
        String sql = "SELECT v.Code, v.DiscountType, v.DiscountValue, v.MaxDiscount " +
                     "FROM AccountVouchers av " +
                     "INNER JOIN Vouchers v ON av.VoucherID = v.VoucherID " +
                     "WHERE av.OrderID = ? AND av.IsUsed = 1";

        try {
            return jdbcTemplate.queryForMap(sql, orderId);
        } catch (EmptyResultDataAccessException e) {
            return null; 
        }
    }

    // Update order status and related transaction status
  @Override
public void updateOrder(String orderId, String newStatus, String paymentMethod) {
    String query = "UPDATE Orders SET OrderStatus = ?, "
            + "TransactionStatus = CASE "
            + "    WHEN ? = 'Approved' THEN 'Refunded' "
            + "    WHEN ? = 'Return Completed' THEN 'Refunded' "
            + "    WHEN PaymentMethod = 'Bank Transfer' AND ? IN ('Confirmed', 'On Delivering', 'Completed') THEN 'Paid' "
            + "    WHEN PaymentMethod = 'Cash' AND ? = 'Completed' THEN 'Paid' "
            + "    WHEN PaymentMethod = 'Cash on Delivery' AND ? = 'Completed' THEN 'Paid' "
            + "    WHEN PaymentMethod = 'Cash' AND ? IN ('Confirmed', 'On Delivering') THEN 'Unpaid' "
            + "    WHEN PaymentMethod = 'Cash on Delivery' AND ? IN ('Confirmed', 'On Delivering') THEN 'Unpaid' "
            + "    WHEN ? = 'Cancelled' THEN 'Cancelled' "
            + "    ELSE 'Unpaid' "
            + "END, "
            + "ShipperId = CASE WHEN ? = 'Confirmed' THEN ShipperId ELSE ShipperId END, "
            + "DeliveryDate = CASE WHEN ? = 'Completed' THEN CURRENT_TIMESTAMP ELSE DeliveryDate END "
            + "WHERE OrderId = ?";
    
    jdbcTemplate.update(query, 
        newStatus, newStatus, newStatus, newStatus, newStatus, newStatus, 
        newStatus, newStatus, newStatus, newStatus, newStatus, orderId);
}    
    // Search and filter orders for a specific shipper with multiple criteria
    @Override
    public List<Orders> searchAndFilterOrders(int shipperID, String orderIdQuery, String searchQuery, String status) {
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT o.*, c.FullName AS CustomerName, c.Email AS CustomerEmail, c.PhoneNumber AS CustomerPhone, c.Address AS CustomerAddress, "
                + "s.FullName AS ShipperName, s.Email AS ShipperEmail, s.PhoneNumber AS ShipperPhone, s.Address AS ShipperAddress "
                + "FROM Orders o "
                + "JOIN Accounts c ON o.CustomerID = c.AccountID "
                + "JOIN Accounts s ON o.ShipperID = s.AccountID "
                + "WHERE o.ShipperID = ? ");

        List<Object> params = new ArrayList<>();
        params.add(shipperID);

        if (orderIdQuery != null && !orderIdQuery.isEmpty()) {
            queryBuilder.append("AND o.OrderID = ? ");
            params.add(orderIdQuery);
        }

        if (status != null && !status.isEmpty()) {
            queryBuilder.append("AND o.OrderStatus = ? ");
            params.add(status);
        }

        if (searchQuery != null && !searchQuery.isEmpty()) {
            queryBuilder.append("AND c.FullName LIKE ? ");
            params.add("%" + searchQuery + "%");
        }

        String query = queryBuilder.toString();
        return jdbcTemplate.query(query, params.toArray(), new OrderRowMapper());
    }

    // Map database rows to Orders object
    private static class OrderRowMapper implements RowMapper<Orders> {

        @Override
        public Orders mapRow(ResultSet rs, int rowNum) throws SQLException {
            Orders order = new Orders();
            order.setOrderID(rs.getString("OrderID"));
            order.setCustomerID(rs.getInt("CustomerID"));
            order.setShipperID(rs.getInt("ShipperID"));
            order.setOrderDate(rs.getObject("OrderDate", LocalDateTime.class));
            order.setFormattedOrderDate(rs.getString("OrderDate"));
            order.setOrderStatus(rs.getString("OrderStatus"));
            order.setTotalAmount(rs.getBigDecimal("TotalAmount"));
            order.setPaymentMethod(rs.getString("PaymentMethod"));
            order.setShippingAddress(rs.getString("ShippingAddress"));
            order.setTransactionDate(rs.getObject("TransactionDate", LocalDateTime.class));
            order.setFormattedTransactionDate(rs.getString("TransactionDate"));
            order.setTransactionStatus(rs.getString("TransactionStatus"));
            order.setTransactionDescription(rs.getString("TransactionDescription"));
            order.setReturnRequestDate(rs.getObject("ReturnRequestDate", LocalDateTime.class));
            order.setFormattedReturnRequestDate(rs.getString("ReturnRequestDate"));
            order.setReturnRequestStatus(rs.getString("ReturnRequestStatus"));
            order.setReturnRequestReason(rs.getString("ReturnRequestReason"));
            order.setDeliveryDate(rs.getDate("DeliveryDate"));
            order.setNotes(rs.getString("Notes"));
            order.setCustomerName(rs.getString("CustomerName"));
            order.setCustomerEmail(rs.getString("CustomerEmail"));
            order.setCustomerPhone(rs.getString("CustomerPhone"));
            order.setCustomerAddress(rs.getString("CustomerAddress"));
            order.setShipperName(rs.getString("ShipperName"));
            order.setShipperEmail(rs.getString("ShipperEmail"));
            order.setShipperPhone(rs.getString("ShipperPhone"));
            order.setShipperAddress(rs.getString("ShipperAddress"));

            return order;
        }
    }

    // Get all orders for a specific customer with order details and product information
    @Override
    public List<Orders> findAllOrders(int customerID) {
        String query = "SELECT O.OrderID, O.CustomerID, O.OrderDate, O.OrderStatus,\n"
                + "       O.TotalAmount, O.PaymentMethod, O.ShippingAddress, O.TransactionDate, O.TransactionStatus,\n"
                + "       O.DeliveryDate, O.Notes, P.ProductID, P.ProductName, \n"
                + "       (SELECT TOP 1 PI2.Image \n"
                + "        FROM ProductImage PI2 \n"
                + "        WHERE PI2.ProductID = P.ProductID \n"
                + "        ORDER BY PI2.ProductImageID ASC) AS Image,\n"
                + "       OD.Quantity, OD.UnitPrice, OD.TotalPrice, P.Discount, \n"
                + "       COUNT(F.FeedbackID) AS FeedbackCount\n"
                + "FROM Orders O \n"
                + "JOIN Accounts A ON O.CustomerID = A.AccountID\n"
                + "JOIN OrderDetails OD ON O.OrderID = OD.OrderID \n"
                + "JOIN Products P ON OD.ProductID = P.ProductID\n"
                + "LEFT JOIN Feedback F ON F.ProductID = OD.ProductID\n"
                + "WHERE O.CustomerID = ?\n"
                + "GROUP BY O.OrderID, O.CustomerID, O.OrderDate, O.OrderStatus, O.TotalAmount, \n"
                + "         O.PaymentMethod, O.ShippingAddress, O.TransactionDate, O.TransactionStatus, \n"
                + "         O.DeliveryDate, O.Notes, P.ProductID, P.ProductName, OD.Quantity, \n"
                + "         OD.UnitPrice, OD.TotalPrice, P.Discount\n"
                + "ORDER BY O.OrderDate DESC, O.OrderID, P.ProductID;";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, customerID);

        Map<String, Orders> orderMap = new HashMap<>();

        for (Map<String, Object> rs : rows) {
            String orderId = (String) rs.get("OrderID");
            Orders order = orderMap.get(orderId);
            if (order == null) {
                order = new Orders();
                order.setOrderID(orderId);
                order.setCustomerID((int) rs.get("CustomerID"));

                Timestamp timestampOrderDate = (Timestamp) rs.get("OrderDate");
                LocalDateTime dateTimeOrderDate = timestampOrderDate != null ? timestampOrderDate.toLocalDateTime() : null;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTimeOrderDate = (dateTimeOrderDate != null) ? dateTimeOrderDate.format(formatter) : null;
                order.setFormattedOrderDate(formattedDateTimeOrderDate);
                order.setOrderDate(dateTimeOrderDate);

                order.setOrderStatus((String) rs.get("OrderStatus"));
                order.setTotalAmount((BigDecimal) rs.get("TotalAmount"));
                order.setPaymentMethod((String) rs.get("PaymentMethod"));
                order.setShippingAddress((String) rs.get("ShippingAddress"));

                Timestamp timestampTransactionDate = (Timestamp) rs.get("TransactionDate");
                LocalDateTime dateTimeTransactionDate = timestampTransactionDate != null ? timestampTransactionDate.toLocalDateTime() : null;
                String formattedDateTimeTransactionDate = (dateTimeTransactionDate != null) ? dateTimeTransactionDate.format(formatter) : null;
                order.setFormattedTransactionDate(formattedDateTimeTransactionDate);
                order.setTransactionDate(dateTimeTransactionDate);

                order.setTransactionStatus((String) rs.get("TransactionStatus"));
                order.setDeliveryDate((Date) rs.get("DeliveryDate"));
                order.setNotes((String) rs.get("Notes"));

                orderMap.put(orderId, order);
            }

            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setQuantity((int) rs.get("Quantity"));
            orderDetails.setUnitPrice((BigDecimal) rs.get("UnitPrice"));
            orderDetails.setTotalPrice((BigDecimal) rs.get("TotalPrice"));

            Products product = new Products();
            product.setProductID((String) rs.get("ProductID"));
            product.setProductName((String) rs.get("ProductName"));
            product.setDiscount((BigDecimal) rs.get("Discount"));

            List<ProductImage> images = new ArrayList<>();
            String imagePath = (String) rs.get("Image");
            if (imagePath != null) {
                ProductImage image = new ProductImage();
                image.setImages(imagePath);
                images.add(image);
            }
            orderDetails.setImages(images);
            orderDetails.setProduct(product);

            List<OrderDetails> orderDetailsList = order.getOrderDetails();
            if (orderDetailsList == null) {
                orderDetailsList = new ArrayList<>();
                order.setOrderDetails(orderDetailsList);
            }
            orderDetailsList.add(orderDetails);
        }

        return new ArrayList<>(orderMap.values());
    }

    // Get orders filtered by status for a specific customer
    @Override
    public List<Orders> getOrdersByStatus(String status, int customerID) {
        String query = "SELECT O.OrderID, O.CustomerID, O.OrderDate, O.OrderStatus,\n"
                + "       O.TotalAmount, O.PaymentMethod, O.ShippingAddress, O.TransactionDate, O.TransactionStatus,\n"
                + "       O.DeliveryDate, O.Notes, P.ProductID, P.ProductName, \n"
                + "       (SELECT TOP 1 PI2.Image \n"
                + "        FROM ProductImage PI2 \n"
                + "        WHERE PI2.ProductID = P.ProductID \n"
                + "        ORDER BY PI2.ProductImageID ASC) AS Image,\n"
                + "       OD.Quantity, OD.UnitPrice, OD.TotalPrice, P.Discount, \n"
                + "       COUNT(F.FeedbackID) AS FeedbackCount\n"
                + "FROM Orders O \n"
                + "JOIN Accounts A ON O.CustomerID = A.AccountID\n"
                + "JOIN OrderDetails OD ON O.OrderID = OD.OrderID \n"
                + "JOIN Products P ON OD.ProductID = P.ProductID\n"
                + "LEFT JOIN Feedback F ON F.ProductID = OD.ProductID\n"
                + "WHERE O.CustomerID = ? AND O.OrderStatus = ?\n"
                + "GROUP BY O.OrderID, O.CustomerID, O.OrderDate, O.OrderStatus, O.TotalAmount, O.PaymentMethod, O.ShippingAddress,\n"
                + "         O.TransactionDate, O.TransactionStatus, O.DeliveryDate, O.Notes, P.ProductID, P.ProductName, OD.Quantity, OD.UnitPrice, OD.TotalPrice, P.Discount\n"
                + "ORDER BY O.OrderDate DESC, O.OrderID, P.ProductID";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, customerID, status);

        Map<String, Orders> orderMap = new HashMap<>();

        for (Map<String, Object> rs : rows) {
            String orderId = (String) rs.get("OrderID");
            Orders order = orderMap.get(orderId);
            if (order == null) {
                order = new Orders();
                order.setOrderID(orderId);
                order.setCustomerID((int) rs.get("CustomerID"));

                Timestamp timestampOrderDate = (Timestamp) rs.get("OrderDate");
                LocalDateTime dateTimeOrderDate = timestampOrderDate != null ? timestampOrderDate.toLocalDateTime() : null;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTimeOrderDate = (dateTimeOrderDate != null) ? dateTimeOrderDate.format(formatter) : null;
                order.setFormattedOrderDate(formattedDateTimeOrderDate);
                order.setOrderDate(dateTimeOrderDate);

                order.setOrderStatus((String) rs.get("OrderStatus"));
                order.setTotalAmount((BigDecimal) rs.get("TotalAmount"));
                order.setPaymentMethod((String) rs.get("PaymentMethod"));
                order.setShippingAddress((String) rs.get("ShippingAddress"));

                Timestamp timestampTransactionDate = (Timestamp) rs.get("TransactionDate");
                LocalDateTime dateTimeTransactionDate = timestampTransactionDate != null ? timestampTransactionDate.toLocalDateTime() : null;
                String formattedDateTimeTransactionDate = (dateTimeTransactionDate != null) ? dateTimeTransactionDate.format(formatter) : null;
                order.setFormattedTransactionDate(formattedDateTimeTransactionDate);
                order.setTransactionDate(dateTimeTransactionDate);

                order.setTransactionStatus((String) rs.get("TransactionStatus"));
                order.setDeliveryDate((Date) rs.get("DeliveryDate"));
                order.setNotes((String) rs.get("Notes"));

                orderMap.put(orderId, order);
            }

            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setQuantity((int) rs.get("Quantity"));
            orderDetails.setUnitPrice((BigDecimal) rs.get("UnitPrice"));
            orderDetails.setTotalPrice((BigDecimal) rs.get("TotalPrice"));

            Products product = new Products();
            product.setProductID((String) rs.get("ProductID"));
            product.setProductName((String) rs.get("ProductName"));
            product.setDiscount((BigDecimal) rs.get("Discount"));

            List<ProductImage> images = new ArrayList<>();
            String imagePath = (String) rs.get("Image");
            if (imagePath != null) {
                ProductImage image = new ProductImage();
                image.setImages(imagePath);
                images.add(image);
            }
            orderDetails.setImages(images);
            orderDetails.setProduct(product);

            List<OrderDetails> orderDetailsList = order.getOrderDetails();
            if (orderDetailsList == null) {
                orderDetailsList = new ArrayList<>();
                order.setOrderDetails(orderDetailsList);
            }
            orderDetailsList.add(orderDetails);
        }

        return new ArrayList<>(orderMap.values());
    }

    // Get detailed information for a specific order
    @Override
    public Orders viewOrderInfo(String orderId) {
        String query =
            "SELECT \n" +
            "    O.OrderID,\n" +
            "    A.FullName AS CustomerName,\n" +
            "    O.OrderDate,\n" +
            "    O.OrderStatus,\n" +
            "    O.TotalAmount,\n" +
            "    O.PaymentMethod,\n" +
            "    O.ShippingAddress,\n" +
            "    O.TransactionDate,\n" +
            "    O.TransactionStatus,\n" +
            "    O.DeliveryDate,\n" +
            "    O.Notes,\n" +
            "    O.ReturnRequestDate,\n" +
            "    O.ReturnRequestStatus,\n" +
            "    O.ReturnRequestReason,\n" +
            "    P.ProductID,\n" +
            "    P.ProductName,\n" +
            "    (SELECT TOP 1 PI2.Image\n" +
            "     FROM ProductImage PI2\n" +
            "     WHERE PI2.ProductID = P.ProductID\n" +
            "     ORDER BY PI2.ProductImageID ASC) AS Image,\n" +
            "    OD.Quantity,\n" +
            "    OD.UnitPrice,\n" +
            "    OD.TotalPrice\n" +
            "FROM Orders O\n" +
            "JOIN Accounts A ON O.CustomerID = A.AccountID\n" +
            "JOIN OrderDetails OD ON O.OrderID = OD.OrderID\n" +
            "JOIN Products P ON OD.ProductID = P.ProductID\n" +
            "WHERE O.OrderID = ?\n" +
            "GROUP BY \n" +
            "    O.OrderID, A.FullName, O.OrderDate, O.OrderStatus,\n" +
            "    O.TotalAmount, O.PaymentMethod, O.ShippingAddress,\n" +
            "    O.TransactionDate, O.TransactionStatus, O.DeliveryDate, O.Notes,\n" +
            "    O.ReturnRequestDate, O.ReturnRequestStatus, O.ReturnRequestReason,\n" +
            "    P.ProductID, P.ProductName, OD.Quantity, OD.UnitPrice, OD.TotalPrice\n" +
            "ORDER BY O.OrderDate DESC, O.OrderID, P.ProductID;";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, orderId);

        if (rows.isEmpty()) {
            return null; 
        }

        Orders order = new Orders();

        for (Map<String, Object> rs : rows) {
            if (order.getOrderID() == null) {
                order.setOrderID((String) rs.get("OrderID"));
                order.setCustomerName((String) rs.get("CustomerName"));

                Timestamp tsOrderDate = (Timestamp) rs.get("OrderDate");
                if (tsOrderDate != null) {
                    order.setOrderDate(tsOrderDate.toLocalDateTime());
                }

                order.setOrderStatus((String) rs.get("OrderStatus"));
                order.setTotalAmount((BigDecimal) rs.get("TotalAmount"));
                order.setPaymentMethod((String) rs.get("PaymentMethod"));
                order.setShippingAddress((String) rs.get("ShippingAddress"));

                Timestamp tsTransactionDate = (Timestamp) rs.get("TransactionDate");
                if (tsTransactionDate != null) {
                    order.setTransactionDate(tsTransactionDate.toLocalDateTime());
                }

                order.setTransactionStatus((String) rs.get("TransactionStatus"));
                order.setDeliveryDate((Date) rs.get("DeliveryDate"));
                order.setNotes((String) rs.get("Notes"));

                Timestamp tsReturnRequestDate = (Timestamp) rs.get("ReturnRequestDate");
                if (tsReturnRequestDate != null) {
                    order.setReturnRequestDate(tsReturnRequestDate.toLocalDateTime());
                }
                order.setReturnRequestStatus((String) rs.get("ReturnRequestStatus"));
                order.setReturnRequestReason((String) rs.get("ReturnRequestReason"));
            }

            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setQuantity((int) rs.get("Quantity"));
            orderDetails.setUnitPrice((BigDecimal) rs.get("UnitPrice"));
            orderDetails.setTotalPrice((BigDecimal) rs.get("TotalPrice"));

            Products product = new Products();
            product.setProductID((String) rs.get("ProductID"));
            product.setProductName((String) rs.get("ProductName"));
            orderDetails.setProduct(product);

            List<ProductImage> images = new ArrayList<>();
            String imagePath = (String) rs.get("Image");
            if (imagePath != null) {
                ProductImage image = new ProductImage();
                image.setImages(imagePath);
                images.add(image);
            }
            orderDetails.setImages(images);

            List<OrderDetails> orderDetailsList = order.getOrderDetails();
            if (orderDetailsList == null) {
                orderDetailsList = new ArrayList<>();
                order.setOrderDetails(orderDetailsList);
            }
            orderDetailsList.add(orderDetails);
        }

        return order;
    }

    // Check if feedback exists for a specific order, product, and customer
    @Override
    public Integer getFeedback(String orderID, String productID, int customerID) {
        String query = "SELECT TOP 1 f.FeedbackID\n"
                + "FROM Feedback f\n"
                + "JOIN Orders o \n"
                + "    ON f.CustomerID = o.CustomerID\n"
                + "JOIN OrderDetails od \n"
                + "    ON od.OrderID = o.OrderID \n"
                + "   AND od.ProductID = f.ProductID\n"
                + "WHERE f.ProductID = ?\n"
                + "  AND f.CustomerID = ?\n"
                + "  AND o.OrderID = ?;";
        try {
            return jdbcTemplate.queryForObject(query, new Object[]{productID, customerID, orderID}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Get single order by ID with customer and shipper details
    @Override
    public Orders get(String id) {
        String sql = "SELECT o.*, c.FullName AS CustomerName, c.Email AS CustomerEmail, "
                + "c.PhoneNumber AS CustomerPhone, c.Address AS CustomerAddress, "
                + "s.FullName AS ShipperName, s.Email AS ShipperEmail, "
                + "s.PhoneNumber AS ShipperPhone, s.Address AS ShipperAddress "
                + "FROM Orders o "
                + "JOIN Accounts c ON o.CustomerID = c.AccountID "
                + "LEFT JOIN Accounts s ON o.ShipperID = s.AccountID "
                + "WHERE o.OrderID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, new OrderRowMapper());
    }

    // Update order with return request information
    @Override
    public void update(Orders order) {
        String sql = "UPDATE Orders SET OrderStatus = ?, ReturnRequestDate = ?, ReturnRequestStatus = ?, ReturnRequestReason = ? WHERE OrderID = ?";
        jdbcTemplate.update(sql,
                order.getOrderStatus(),
                order.getReturnRequestDate(),
                order.getReturnRequestStatus(),
                order.getReturnRequestReason(),
                order.getOrderID());
    }

    // Save new order to database
    @Override
    public void saveOrder(int customerId, String shippingAddress, String notes, String paymentMethod,
            String name, String phone, BigDecimal totalAmount, String orderID) {

        String query = "INSERT INTO Orders (OrderID, CustomerID, OrderDate, TotalAmount, ShippingAddress, Notes, PaymentMethod, Name, Phone, OrderStatus, ShipperID) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(query,
                orderID,
                customerId,
                new Timestamp(new Date().getTime()),
                totalAmount,
                shippingAddress,
                notes,
                paymentMethod,
                name,
                phone,
                "Pending",
                null);
    }

    // Get all orders with basic information
    @Override
    public List<Orders> findAll() {
        String query = "SELECT OrderID, Name, Phone, ShippingAddress, PaymentMethod, Notes, TotalAmount, OrderDate, OrderStatus, ShipperID FROM Orders";
        List<Orders> orderList = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

        for (Map<String, Object> row : rows) {
            Orders order = new Orders();
            order.setOrderID((String) row.get("OrderID"));
            order.setName((String) row.get("Name"));
            order.setPhone((String) row.get("Phone"));
            order.setShippingAddress((String) row.get("ShippingAddress"));
            order.setPaymentMethod((String) row.get("PaymentMethod"));
            order.setNotes((String) row.get("Notes"));
            order.setTotalAmount((BigDecimal) row.get("TotalAmount"));

            Timestamp timestamp = (Timestamp) row.get("OrderDate");
            if (timestamp != null) {
                order.setOrderDate(timestamp.toLocalDateTime());
            }

            order.setOrderStatus((String) row.get("OrderStatus"));
            order.setShipperID((Integer) row.get("ShipperID"));

            orderList.add(order);
        }

        return orderList;
    }

    // Assign shipper to order and update status to confirmed
    @Override
    public void updateShipperAndStatus(String orderID, int shipperID) {
        String query = "UPDATE Orders SET ShipperID = ?, OrderStatus = 'Confirmed' WHERE OrderID = ?";
        jdbcTemplate.update(query, shipperID, orderID);
    }

    // Update order status to cancelled
    @Override
    public void updateCancalledStatus(String orderID) {
        String query = "UPDATE Orders SET OrderStatus = 'Cancelled' WHERE OrderID = ?";
        jdbcTemplate.update(query, orderID);
    }

    // Search orders for a customer with product name, order ID, and status filters
    @Override
    public List<Orders> searchOrders(int customerID, String searchQuery, String status) {

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT ")
                    .append("O.OrderID, O.CustomerID, O.OrderDate, O.OrderStatus, ")
                    .append("O.TotalAmount, O.PaymentMethod, O.ShippingAddress, O.TransactionDate, O.TransactionStatus, ")
                    .append("O.DeliveryDate, O.Notes, ")
                    .append("P.ProductID, P.ProductName, ")
                    .append("(SELECT TOP 1 PI2.Image FROM ProductImage PI2 WHERE PI2.ProductID = P.ProductID ORDER BY PI2.ProductImageID ASC) AS Image, ")
                    .append("OD.Quantity, OD.UnitPrice, OD.TotalPrice, P.Discount, ")
                    .append("COUNT(F.FeedbackID) AS FeedbackCount ")
                    .append("FROM Orders O ")
                    .append("JOIN Accounts A ON O.CustomerID = A.AccountID ")
                    .append("JOIN OrderDetails OD ON O.OrderID = OD.OrderID ")
                    .append("JOIN Products P ON OD.ProductID = P.ProductID ")
                    .append("LEFT JOIN Feedback F ON F.ProductID = OD.ProductID ")
                    .append("WHERE O.CustomerID = ? ");

        List<Object> params = new ArrayList<>();
        params.add(customerID);

        if (searchQuery != null && !searchQuery.isEmpty()) {
            queryBuilder.append(" AND O.OrderID IN (")
                        .append("SELECT DISTINCT OD2.OrderID ")
                        .append("FROM OrderDetails OD2 ")
                        .append("JOIN Products P2 ON OD2.ProductID = P2.ProductID ")
                        .append("WHERE P2.ProductName LIKE ? ")
                        .append("OR OD2.OrderID LIKE ?")
                        .append(") ");
            params.add("%" + searchQuery + "%");
            params.add("%" + searchQuery + "%");
        }

        if (status != null && !status.isEmpty()) {
            queryBuilder.append(" AND O.OrderStatus = ? ");
            params.add(status);
        }

        queryBuilder.append("GROUP BY ")
                    .append("O.OrderID, O.CustomerID, O.OrderDate, O.OrderStatus, O.TotalAmount, ")
                    .append("O.PaymentMethod, O.ShippingAddress, O.TransactionDate, O.TransactionStatus, ")
                    .append("O.DeliveryDate, O.Notes, P.ProductID, P.ProductName, OD.Quantity, OD.UnitPrice, OD.TotalPrice, P.Discount ")
                    .append("ORDER BY O.OrderDate DESC, O.OrderID, P.ProductID");

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(queryBuilder.toString(), params.toArray());

        Map<String, Orders> orderMap = new HashMap<>();

        for (Map<String, Object> rs : rows) {
            String orderId = (String) rs.get("OrderID");
            Orders order = orderMap.get(orderId);
            if (order == null) {
                order = new Orders();
                order.setOrderID(orderId);
                order.setCustomerID((int) rs.get("CustomerID"));

                Timestamp tsOrderDate = (Timestamp) rs.get("OrderDate");
                LocalDateTime orderDate = tsOrderDate != null ? tsOrderDate.toLocalDateTime() : null;
                order.setOrderDate(orderDate);
                order.setFormattedOrderDate(orderDate != null ? orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null);

                order.setOrderStatus((String) rs.get("OrderStatus"));
                order.setTotalAmount((BigDecimal) rs.get("TotalAmount"));
                order.setPaymentMethod((String) rs.get("PaymentMethod"));
                order.setShippingAddress((String) rs.get("ShippingAddress"));

                Timestamp tsTransaction = (Timestamp) rs.get("TransactionDate");
                LocalDateTime transactionDate = tsTransaction != null ? tsTransaction.toLocalDateTime() : null;
                order.setTransactionDate(transactionDate);
                order.setFormattedTransactionDate(transactionDate != null ? transactionDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null);

                order.setTransactionStatus((String) rs.get("TransactionStatus"));
                order.setDeliveryDate((Date) rs.get("DeliveryDate"));
                order.setNotes((String) rs.get("Notes"));

                order.setOrderDetails(new ArrayList<>());
                orderMap.put(orderId, order);
            }

            OrderDetails details = new OrderDetails();
            details.setQuantity((int) rs.get("Quantity"));
            details.setUnitPrice((BigDecimal) rs.get("UnitPrice"));
            details.setTotalPrice((BigDecimal) rs.get("TotalPrice"));

            Products product = new Products();
            product.setProductID((String) rs.get("ProductID"));
            product.setProductName((String) rs.get("ProductName"));
            product.setDiscount((BigDecimal) rs.get("Discount"));

            List<ProductImage> images = new ArrayList<>();
            String imagePath = (String) rs.get("Image");
            if (imagePath != null) {
                ProductImage img = new ProductImage();
                img.setImages(imagePath);
                images.add(img);
            }
            details.setImages(images);
            details.setProduct(product);

            order.getOrderDetails().add(details);
        }

        return new ArrayList<>(orderMap.values());
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    } 
}
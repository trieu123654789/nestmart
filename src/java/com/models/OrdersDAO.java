package com.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OrdersDAO {

    public List<Orders> findAllOrders(int customerID);

    public Integer getFeedback(String orderID, String productID, int customerID);

    public List<Orders> getOrdersByStatus(String status, int customerID);

    public Orders get(String id);

    public void update(Orders order);

    List<Orders> getOrdersByShipper(int shipperID);

    void updateOrder(String orderID, String newStatus, String paymentMethod);

    List<Orders> searchAndFilterOrders(int shipperID, String orderIdQuery, String searchQuery, String status);

    public void saveOrder(int customerId, String shippingAddress, String notes, String paymentMethod,
            String name, String phone, BigDecimal totalAmount, String orderId);

    public void updateShipperAndStatus(String orderID, int shipperID);

    public List<Orders> findAll();

    public Orders viewOrderInfo(String orderId);

    public void updateCancalledStatus(String orderID);

    List<Orders> searchOrders(int customerID, String searchQuery, String status);
    public Map<String, Object> getUsedVoucherInfo(String orderId);
}

package com.models;

import java.math.BigDecimal;
import java.util.List;

public interface OrderDetailsDAO {

    void saveOrderDetail(String orderId, String productId, int quantity, BigDecimal unitPrice, BigDecimal totalPrice);

    public List<OrderDetails> findByOrderId(int orderID);

    public String getProductIdFromDetail(OrderDetails detail);

    public List<OrderDetails> getOrderDetailsByOrderId(String orderId);
}

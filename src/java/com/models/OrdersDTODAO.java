/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.models;

import java.util.List;

public interface OrdersDTODAO {

    public OrdersDTO findByOrderId(String orderID);

    int getTotalOrders();

    int countByKeyword(String keyword);

    List<OrdersDTO> searchByKeyword(String keyword);

    List<OrdersDTO> findPaginated(int page, int pageSize);

    public List<OrdersDTO> findOrdersByCustomerId(int customerId);
}

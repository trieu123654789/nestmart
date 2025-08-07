/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.models;

import java.util.List;

public interface ReturnRequestResponseDAO {

    List<Orders> getOrdersWithReturnRequests();

    void addReturnRequestResponse(ReturnRequestResponse response);

    void updateOrderStatus(String orderID, String newOrderStatus, String newReturnRequestStatus);

    void deleteCompletedReturnOrder(String orderID);

    List<Orders> getCompletedReturnOrders();

    public List<ReturnRequestResponse> getReturnResponses(String orderId);

}

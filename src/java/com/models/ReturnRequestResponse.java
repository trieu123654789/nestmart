// ReturnRequestResponse.java
package com.models;

import java.time.LocalDateTime;
import java.util.Date;

public class ReturnRequestResponse {
    private int returnRequestResponseID; 
    private String orderID; 
    private int employeeID;
    private LocalDateTime returnRequestResponseDate; 
    private String message; 
    
    public ReturnRequestResponse() {}

    public ReturnRequestResponse(int returnRequestResponseID, String orderID, int employeeID, LocalDateTime  returnRequestResponseDate, String message) {
        this.returnRequestResponseID = returnRequestResponseID;
        this.orderID = orderID;
        this.employeeID = employeeID;
        this.returnRequestResponseDate = returnRequestResponseDate;
        this.message = message;
    }

    public int getReturnRequestResponseID() {
        return returnRequestResponseID;
    }

    public void setReturnRequestResponseID(int returnRequestResponseID) {
        this.returnRequestResponseID = returnRequestResponseID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

  
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getReturnRequestResponseDate() {
        return returnRequestResponseDate;
    }

    public void setReturnRequestResponseDate(LocalDateTime returnRequestResponseDate) {
        this.returnRequestResponseDate = returnRequestResponseDate;
    }
}

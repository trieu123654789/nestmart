package com.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Support {

    private int supportID;
    private Integer customerID;
    private Integer employeeID;
    private String message;
    private String status;
    private LocalDateTime sendDate;
    private String fullName;
    private String phoneNumber;

    public Support() {
        this.supportID = 0;
        this.customerID = null;
        this.employeeID = null;
        this.message = "";
        this.status = "Pending";
        this.sendDate = LocalDateTime.now();
    }

    public Support(int supportID, Integer customerID, Integer employeeID, String message, String status, LocalDateTime sendDate) {
        this.supportID = supportID;
        this.customerID = customerID;
        this.employeeID = employeeID;
        this.message = message;
        this.status = status;
        this.sendDate = sendDate;
    }

    public String getSendDateFormatted() {
        if (sendDate == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return sendDate.format(formatter);
    }

    public int getSupportID() {
        return supportID;
    }

    public void setSupportID(int supportID) {
        this.supportID = supportID;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getSendDate() {
        return sendDate;
    }

    public void setSendDate(LocalDateTime sendDate) {
        this.sendDate = sendDate;
    }
}

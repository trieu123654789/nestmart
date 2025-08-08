package com.models;

import java.time.LocalDateTime;

public class AccountVoucher {

    private int accountVoucherID;
    private int accountID;
    private int voucherID;
    private boolean isUsed;
    private LocalDateTime assignedDate;
    private LocalDateTime expiryDate;
    private String orderID;
    private String formattedExpiryDate;
    private String formattedAssignedDate;
    private String fullName;
    private String voucherCode;

    public AccountVoucher() {
    }

    public AccountVoucher(int accountID, int voucherID, boolean isUsed, LocalDateTime assignedDate, LocalDateTime expiryDate, String orderID) {
        this.accountID = accountID;
        this.voucherID = voucherID;
        this.isUsed = isUsed;
        this.assignedDate = assignedDate;
        this.expiryDate = expiryDate;
        this.orderID = orderID;
    }

    public int getAccountVoucherID() {
        return accountVoucherID;
    }

    public void setAccountVoucherID(int accountVoucherID) {
        this.accountVoucherID = accountVoucherID;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public int getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(int voucherID) {
        this.voucherID = voucherID;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public LocalDateTime getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(LocalDateTime assignedDate) {
        this.assignedDate = assignedDate;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    @Override
    public String toString() {
        return "AccountVoucher{"
                + "accountVoucherID=" + accountVoucherID
                + ", accountID=" + accountID
                + ", voucherID=" + voucherID
                + ", isUsed=" + isUsed
                + ", assignedDate=" + assignedDate
                + ", expiryDate=" + expiryDate
                + ", orderID=" + orderID
                + ", fullName='" + fullName + '\''
                + ", voucherCode='" + voucherCode + '\''
                + '}';
    }

    public boolean isIsUsed() {
        return isUsed;
    }

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public String getFormattedExpiryDate() {
        return formattedExpiryDate;
    }

    public void setFormattedExpiryDate(String formattedExpiryDate) {
        this.formattedExpiryDate = formattedExpiryDate;
    }

    public String getFormattedAssignedDate() {
        return formattedAssignedDate;
    }

    public void setFormattedAssignedDate(String formattedAssignedDate) {
        this.formattedAssignedDate = formattedAssignedDate;
    }

  

  
}

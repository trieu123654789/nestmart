package com.models;

import java.math.BigDecimal;

public class EmployeeSalaryDTO {

    private int accountID;
    private String fullName;
    private BigDecimal hourlyRate;
    private int totalHours;        
    private BigDecimal totalSalary; 
    private int overtimeHours;      
    private BigDecimal overtimeSalary; 
    private java.sql.Timestamp paymentDate;
    private String status; 

    public EmployeeSalaryDTO() {
    }

    public EmployeeSalaryDTO(int accountID, String fullName, BigDecimal hourlyRate, int totalHours, 
                             BigDecimal totalSalary, int overtimeHours, BigDecimal overtimeSalary) {
        this.accountID = accountID;
        this.fullName = fullName;
        this.hourlyRate = hourlyRate;
        this.totalHours = totalHours;
        this.totalSalary = totalSalary;
        this.overtimeHours = overtimeHours;
        this.overtimeSalary = overtimeSalary;
    }

    public EmployeeSalaryDTO(String fullName, BigDecimal hourlyRate, int totalHours, 
                             BigDecimal totalSalary, int overtimeHours, BigDecimal overtimeSalary) {
        this.fullName = fullName;
        this.hourlyRate = hourlyRate;
        this.totalHours = totalHours;
        this.totalSalary = totalSalary;
        this.overtimeHours = overtimeHours;
        this.overtimeSalary = overtimeSalary;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public BigDecimal getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(BigDecimal totalSalary) {
        this.totalSalary = totalSalary;
    }

    public int getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(int overtimeHours) {
        this.overtimeHours = overtimeHours;
    }

    public BigDecimal getOvertimeSalary() {
        return overtimeSalary;
    }

    public void setOvertimeSalary(BigDecimal overtimeSalary) {
        this.overtimeSalary = overtimeSalary;
    }
    
    public java.sql.Timestamp getPaymentDate() {
        return paymentDate;
    }
    
    public void setPaymentDate(java.sql.Timestamp paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}

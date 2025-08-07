/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.models;

import java.math.BigDecimal;
import java.util.List;


public interface SalaryDAO {

    public boolean exists(Salary salary);

    public void updateSalaryAfterShiftChange(int accountID, int weekScheduleID, BigDecimal newTotalSalary);

    public BigDecimal calculateNewSalary(int accountID, int shiftID);

    public BigDecimal calculateTotalSalaryByWeek(int weekScheduleID);

    public List<EmployeeSalaryDTO> findSalariesByWeek(int weekScheduleID);

    public List<WeekSalaryDTO> calculateWeeklySalaries();

    public List<EmployeeSalaryDTO> calculateSalariesByWeek(int weekScheduleID);

    public List<EmployeeSalaryDTO> findSalariesWithHoursByWeek(int weekScheduleID);

    public List<EmployeeSalaryDTO> findTotalSalary(int weekScheduleID);
    
    // Method to update salary with complete information
    public void updateSalaryWithShiftInfo(int accountID, int weekScheduleID, int shiftID, BigDecimal totalSalary);

//    public List<WeekSalaryDTO> getPagedSalaries(int page, int pageSize);
//
//    public int getTotalSalaries();

}

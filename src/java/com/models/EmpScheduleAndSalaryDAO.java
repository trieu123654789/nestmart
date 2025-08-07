/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.models;

import java.util.Date;
import java.util.List;

public interface EmpScheduleAndSalaryDAO {

    List<WeekSchedule> getEmployeeWeekSchedules(int employeeID);

    List<WeekSchedule> getEmployeeWeekSchedulesByDateRange(int employeeID, java.sql.Date startDate, java.sql.Date endDate);

    WeekSchedule getCurrentWeekSchedule(int employeeID);

    List<WeekSchedule> getUpcomingWeekSchedules(int employeeID, int limit);

    List<WeekDetails> getEmployeeWeekDetails(int employeeID, int weekScheduleID);

    WeekSalaryDTO getEmployeeWeekSalary(int employeeID, int weekScheduleID);

    List<WeekSalaryDTO> getEmployeeSalaryHistory(int employeeID);

    List<WeekSalaryDTO> getEmployeeSalaryByDateRange(int employeeID, java.sql.Date startDate, java.sql.Date endDate);

    int getTotalWorkingHours(int employeeID, int weekScheduleID);

    boolean hasScheduleOnDate(int employeeID, java.sql.Date date);

    WeekSchedule getWeekScheduleById(int weekScheduleID);

}

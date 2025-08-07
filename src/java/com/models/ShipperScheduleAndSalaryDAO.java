package com.models;

import java.sql.Date;
import java.util.List;

public interface ShipperScheduleAndSalaryDAO {

    List<WeekDetails> getShipperWeekDetails(int shipperID, int weekScheduleID);

    List<WeekSchedule> getShipperWeekSchedules(int shipperID);

    List<WeekSchedule> getShipperWeekSchedulesByDateRange(int shipperID, Date startDate, Date endDate);

    WeekSchedule getCurrentWeekSchedule(int shipperID);

    List<WeekSchedule> getUpcomingWeekSchedules(int shipperID, int limit);

    WeekSalaryDTO getShipperWeekSalary(int shipperID, int weekScheduleID);

    List<WeekSalaryDTO> getShipperSalaryHistory(int shipperID);

    List<WeekSalaryDTO> getShipperSalaryByDateRange(int shipperID, Date startDate, Date endDate);

    int getTotalWorkingHours(int shipperID, int weekScheduleID);

    boolean hasScheduleOnDate(int shipperID, Date date);

    WeekSchedule getWeekScheduleById(int weekScheduleID);

    int getTotalOrdersDelivered(int shipperID, int weekScheduleID);

}

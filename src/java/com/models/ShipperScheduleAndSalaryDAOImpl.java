package com.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * Implementation of ShipperScheduleAndSalaryDAO
 */
@Repository
public class ShipperScheduleAndSalaryDAOImpl implements ShipperScheduleAndSalaryDAO {
    
    private JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    private static final RowMapper<WeekSchedule> WEEK_SCHEDULE_ROW_MAPPER = new RowMapper<WeekSchedule>() {
        @Override
        public WeekSchedule mapRow(ResultSet rs, int rowNum) throws SQLException {
            WeekSchedule schedule = new WeekSchedule();
            schedule.setWeekScheduleID(rs.getInt("weekScheduleID"));
            schedule.setWeekStartDate(rs.getDate("weekStartDate"));
            schedule.setWeekEndDate(rs.getDate("weekEndDate"));
            return schedule;
        }
    };

    private static final RowMapper<WeekDetails> WEEK_DETAILS_ROW_MAPPER = new RowMapper<WeekDetails>() {
        @Override
        public WeekDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
            WeekDetails detail = new WeekDetails();
            detail.setWeekDetailID(rs.getInt("weekDetailID"));
            detail.setWeekScheduleID(rs.getInt("weekScheduleID"));
            detail.setEmployeeID(rs.getInt("employeeID"));
            detail.setDayID(rs.getInt("dayID"));
            detail.setShiftID(rs.getInt("shiftID"));
            detail.setOvertimeHours(rs.getBigDecimal("overtimeHours"));
            detail.setStatus(rs.getString("status"));
            return detail;
        }
    };

    private static final RowMapper<WeekSalaryDTO> WEEK_SALARY_ROW_MAPPER = new RowMapper<WeekSalaryDTO>() {
        @Override
        public WeekSalaryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            try {
                return new WeekSalaryDTO(
                    rs.getInt("weekScheduleID"),
                    rs.getDate("weekStartDate"),
                    rs.getDate("weekEndDate"),
                    rs.getBigDecimal("totalSalary"),
                    rs.getInt("totalHoursWorked"),
                    rs.getBigDecimal("totalOvertimeHours"),
                    rs.getBigDecimal("totalOvertimeSalary"),
                    rs.getDate("salaryPaymentDate"),
                    rs.getString("status")
                );
            } catch (SQLException e) {
                return new WeekSalaryDTO(
                    rs.getInt("weekScheduleID"),
                    rs.getDate("weekStartDate"),
                    rs.getDate("weekEndDate"),
                    rs.getBigDecimal("totalSalary"),
                    rs.getInt("totalHoursWorked"),
                    rs.getBigDecimal("totalOvertimeHours"),
                    rs.getBigDecimal("totalOvertimeSalary"),
                    rs.getDate("salaryPaymentDate")
                );
            }
        }
    };

    // Get all week schedules for a specific shipper
    @Override
    public List<WeekSchedule> getShipperWeekSchedules(int shipperID) {
        String sql =
            "SELECT DISTINCT ws.weekScheduleID, ws.weekStartDate, ws.weekEndDate " +
            "FROM WeekSchedule ws " +
            "INNER JOIN WeekDetails wd ON ws.weekScheduleID = wd.weekScheduleID " +
            "WHERE wd.employeeID = ? " +
            "ORDER BY ws.weekStartDate DESC";

        try {
            return jdbcTemplate.query(sql, WEEK_SCHEDULE_ROW_MAPPER, shipperID);
        } catch (Exception e) {
            throw new RuntimeException("Error getting shipper week schedules", e);
        }
    }

    // Get shipper week schedules within a specific date range
    @Override
    public List<WeekSchedule> getShipperWeekSchedulesByDateRange(int shipperID, Date startDate, Date endDate) {
        String sql = 
            "SELECT DISTINCT ws.weekScheduleID, ws.weekStartDate, ws.weekEndDate " +
            "FROM WeekSchedule ws " +
            "INNER JOIN WeekDetails wd ON ws.weekScheduleID = wd.weekScheduleID " +
            "WHERE wd.employeeID = ? " +
            "AND ws.weekStartDate >= ? " +
            "AND ws.weekEndDate <= ? " +
            "ORDER BY ws.weekStartDate DESC";

        try {
            return jdbcTemplate.query(sql, WEEK_SCHEDULE_ROW_MAPPER, shipperID, startDate, endDate);
        } catch (Exception e) {
            throw new RuntimeException("Error getting shipper week schedules by date range", e);
        }
    }

    // Get current week schedule for a shipper
    @Override
    public WeekSchedule getCurrentWeekSchedule(int shipperID) {
        String sql = 
            "SELECT DISTINCT ws.weekScheduleID, ws.weekStartDate, ws.weekEndDate " +
            "FROM WeekSchedule ws " +
            "INNER JOIN WeekDetails wd ON ws.weekScheduleID = wd.weekScheduleID " +
            "WHERE wd.employeeID = ? " +
            "AND CAST(GETDATE() AS DATE) BETWEEN ws.weekStartDate AND ws.weekEndDate";

        try {
            List<WeekSchedule> schedules = jdbcTemplate.query(sql, WEEK_SCHEDULE_ROW_MAPPER, shipperID);
            return schedules.isEmpty() ? null : schedules.get(0);
        } catch (Exception e) {
            throw new RuntimeException("Error getting current week schedule for shipper", e);
        }
    }

    // Get upcoming week schedules for a shipper with limit
    @Override
    public List<WeekSchedule> getUpcomingWeekSchedules(int shipperID, int limit) {
        String sql =
            "SELECT DISTINCT ws.weekScheduleID, ws.weekStartDate, ws.weekEndDate " +
            "FROM WeekSchedule ws " +
            "INNER JOIN WeekDetails wd ON ws.weekScheduleID = wd.weekScheduleID " +
            "WHERE wd.employeeID = ? " +
            "AND ws.weekStartDate > CAST(GETDATE() AS DATE) " +
            "ORDER BY ws.weekStartDate ASC " +
            "OFFSET 0 ROWS FETCH NEXT " + limit + " ROWS ONLY";

        try {
            return jdbcTemplate.query(sql, WEEK_SCHEDULE_ROW_MAPPER, shipperID);
        } catch (Exception e) {
            throw new RuntimeException("Error getting upcoming week schedules for shipper", e);
        }
    }

    // Get detailed schedule information for a shipper's specific week
    @Override
    public List<WeekDetails> getShipperWeekDetails(int shipperID, int weekScheduleID) {
        String sql = 
            "SELECT wd.*, dow.dayName, s.shiftName, s.startTime, s.endTime " +
            "FROM WeekDetails wd " +
            "LEFT JOIN DayOfWeek dow ON wd.dayID = dow.dayID " +
            "LEFT JOIN Shift s ON wd.shiftID = s.shiftID " +
            "WHERE wd.employeeID = ? AND wd.weekScheduleID = ? " +
            "ORDER BY dow.dayID, s.startTime";

        try {
            return jdbcTemplate.query(sql, WEEK_DETAILS_ROW_MAPPER, shipperID, weekScheduleID);
        } catch (Exception e) {
            throw new RuntimeException("Error getting shipper week details", e);
        }
    }

    // Get salary information for a shipper's specific week
    @Override
    public WeekSalaryDTO getShipperWeekSalary(int shipperID, int weekScheduleID) {
        String salaryCheckSql = "SELECT * FROM Salary WHERE AccountID = ? AND WeekScheduleID = ?";
        
        try {
            List<Map<String, Object>> salaryCheck = jdbcTemplate.queryForList(salaryCheckSql, shipperID, weekScheduleID);
            
            if (!salaryCheck.isEmpty()) {
                String sql = 
                    "SELECT ws.WeekScheduleID, ws.WeekStartDate, ws.WeekEndDate, " +
                    "       s.TotalSalary as totalSalary, " +
                    "       s.TotalHours as totalHoursWorked, " +
                    "       SUM(COALESCE(wd.OvertimeHours, 0)) as totalOvertimeHours, " +
                    "       SUM(COALESCE(wd.OvertimeHours, 0)) * s.HourlyRate * 1.5 as totalOvertimeSalary, " +
                    "       s.SalaryPaymentDate as salaryPaymentDate, " +
                    "       CASE " +
                    "           WHEN s.SalaryPaymentDate IS NOT NULL THEN 'PAID' " +
                    "           ELSE 'PENDING' " +
                    "       END as status " +
                    "FROM WeekSchedule ws " +
                    "INNER JOIN WeekDetails wd ON ws.WeekScheduleID = wd.WeekScheduleID " +
                    "INNER JOIN Salary s ON ws.WeekScheduleID = s.WeekScheduleID AND s.AccountID = wd.EmployeeID " +
                    "WHERE wd.EmployeeID = ? AND ws.WeekScheduleID = ? " +
                    "GROUP BY ws.WeekScheduleID, ws.WeekStartDate, ws.WeekEndDate, " +
                    "         s.TotalSalary, s.TotalHours, s.SalaryPaymentDate, s.HourlyRate";
                
                List<WeekSalaryDTO> results = jdbcTemplate.query(sql, WEEK_SALARY_ROW_MAPPER, shipperID, weekScheduleID);
                return results.isEmpty() ? null : results.get(0);
            } else {
                String calcSql = 
                    "SELECT ws.WeekScheduleID, ws.WeekStartDate, ws.WeekEndDate, " +
                    "       (SUM(DATEDIFF(HOUR, sh.StartTime, sh.EndTime)) * a.HourlyRate + " +
                    "        SUM(COALESCE(wd.OvertimeHours, 0)) * a.HourlyRate * 1.5) as totalSalary, " +
                    "       SUM(DATEDIFF(HOUR, sh.StartTime, sh.EndTime)) as totalHoursWorked, " +
                    "       SUM(COALESCE(wd.OvertimeHours, 0)) as totalOvertimeHours, " +
                    "       SUM(COALESCE(wd.OvertimeHours, 0)) * a.HourlyRate * 1.5 as totalOvertimeSalary, " +
                    "       NULL as salaryPaymentDate, " +
                    "       'PENDING' as status " +
                    "FROM WeekSchedule ws " +
                    "INNER JOIN WeekDetails wd ON ws.WeekScheduleID = wd.WeekScheduleID " +
                    "INNER JOIN Accounts a ON wd.EmployeeID = a.AccountID " +
                    "INNER JOIN Shift sh ON wd.ShiftID = sh.ShiftID " +
                    "WHERE wd.EmployeeID = ? AND ws.WeekScheduleID = ? " +
                    "GROUP BY ws.WeekScheduleID, ws.WeekStartDate, ws.WeekEndDate, a.HourlyRate, a.Role";
                
                List<WeekSalaryDTO> results = jdbcTemplate.query(calcSql, WEEK_SALARY_ROW_MAPPER, shipperID, weekScheduleID);
                return results.isEmpty() ? null : results.get(0);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Error getting shipper week salary: " + e.getMessage(), e);
        }
    }

    // Get complete salary history for a shipper
    @Override
    public List<WeekSalaryDTO> getShipperSalaryHistory(int shipperID) {
        String sql = 
            "SELECT ws.WeekScheduleID, ws.WeekStartDate, ws.WeekEndDate, " +
            "       CASE " +
            "           WHEN COUNT(s.SalaryID) > 0 THEN SUM(s.TotalSalary) " +
            "           ELSE (SUM(DATEDIFF(HOUR, sh.StartTime, sh.EndTime)) * a.HourlyRate + " +
            "                 SUM(COALESCE(wd.OvertimeHours, 0)) * a.HourlyRate * 1.5) " +
            "       END as totalSalary, " +
            "       CASE " +
            "           WHEN COUNT(s.SalaryID) > 0 THEN SUM(CAST(s.TotalHours as decimal(10,2))) " +
            "           ELSE SUM(DATEDIFF(HOUR, sh.StartTime, sh.EndTime)) " +
            "       END as totalHoursWorked, " +
            "       COALESCE(SUM(wd.OvertimeHours), 0) as totalOvertimeHours, " +
            "       COALESCE(SUM(CASE WHEN wd.OvertimeHours > 0 THEN wd.OvertimeHours * a.HourlyRate * 1.5 ELSE 0 END), 0) as totalOvertimeSalary, " +
            "       MAX(s.SalaryPaymentDate) as salaryPaymentDate, " +
            "       CASE " +
            "           WHEN MAX(s.SalaryPaymentDate) IS NOT NULL THEN 'PAID' " +
            "           ELSE 'PENDING' " +
            "       END as status " +
            "FROM WeekSchedule ws " +
            "INNER JOIN WeekDetails wd ON ws.WeekScheduleID = wd.WeekScheduleID " +
            "INNER JOIN Accounts a ON wd.EmployeeID = a.AccountID " +
            "INNER JOIN Shift sh ON wd.ShiftID = sh.ShiftID " +
            "LEFT JOIN Salary s ON (s.AccountID = wd.EmployeeID AND s.WeekScheduleID = ws.WeekScheduleID) " +
            "WHERE wd.EmployeeID = ? " +
            "GROUP BY ws.WeekScheduleID, ws.WeekStartDate, ws.WeekEndDate, a.HourlyRate, a.Role " +
            "ORDER BY ws.WeekStartDate DESC";
            
        try {
            return jdbcTemplate.query(sql, WEEK_SALARY_ROW_MAPPER, shipperID);
        } catch (Exception e) {
            throw new RuntimeException("Error getting shipper salary history", e);
        }
    }

    // Get shipper salary information within a specific date range
    @Override
    public List<WeekSalaryDTO> getShipperSalaryByDateRange(int shipperID, Date startDate, Date endDate) {
        String sql = 
            "SELECT ws.WeekScheduleID, ws.WeekStartDate, ws.WeekEndDate, " +
            "       CASE " +
            "           WHEN COUNT(s.SalaryID) > 0 THEN SUM(s.TotalSalary) " +
            "           ELSE (SUM(DATEDIFF(HOUR, sh.StartTime, sh.EndTime)) * a.HourlyRate + " +
            "                 SUM(COALESCE(wd.OvertimeHours, 0)) * a.HourlyRate * 1.5) " +
            "       END as totalSalary, " +
            "       CASE " +
            "           WHEN COUNT(s.SalaryID) > 0 THEN SUM(CAST(s.TotalHours as decimal(10,2))) " +
            "           ELSE SUM(DATEDIFF(HOUR, sh.StartTime, sh.EndTime)) " +
            "       END as totalHoursWorked, " +
            "       COALESCE(SUM(wd.OvertimeHours), 0) as totalOvertimeHours, " +
            "       COALESCE(SUM(CASE WHEN wd.OvertimeHours > 0 THEN wd.OvertimeHours * a.HourlyRate * 1.5 ELSE 0 END), 0) as totalOvertimeSalary, " +
            "       MAX(s.SalaryPaymentDate) as salaryPaymentDate, " +
            "       CASE " +
            "           WHEN MAX(s.SalaryPaymentDate) IS NOT NULL THEN 'PAID' " +
            "           ELSE 'PENDING' " +
            "       END as status " +
            "FROM WeekSchedule ws " +
            "INNER JOIN WeekDetails wd ON ws.WeekScheduleID = wd.WeekScheduleID " +
            "INNER JOIN Accounts a ON wd.EmployeeID = a.AccountID " +
            "INNER JOIN Shift sh ON wd.ShiftID = sh.ShiftID " +
            "LEFT JOIN Salary s ON (s.AccountID = wd.EmployeeID AND s.WeekScheduleID = ws.WeekScheduleID) " +
            "WHERE wd.EmployeeID = ? " +
            "AND ws.WeekStartDate <= ? AND ws.WeekEndDate >= ? " +
            "GROUP BY ws.WeekScheduleID, ws.WeekStartDate, ws.WeekEndDate, a.HourlyRate, a.Role " +
            "ORDER BY ws.WeekStartDate DESC";
            
        try {
            return jdbcTemplate.query(sql, WEEK_SALARY_ROW_MAPPER, shipperID, endDate, startDate);
        } catch (Exception e) {
            throw new RuntimeException("Error getting shipper salary by date range for shipperID: " + shipperID, e);
        }
    }

    // Calculate total working hours for a shipper in a specific week
    @Override
    public int getTotalWorkingHours(int shipperID, int weekScheduleID) {
        String sql = 
            "SELECT COALESCE(SUM( " +
            "    DATEDIFF(HOUR, s.startTime, s.endTime) + " +
            "    COALESCE(wd.overtimeHours, 0) " +
            "), 0) as totalHours " +
            "FROM WeekDetails wd " +
            "LEFT JOIN Shift s ON wd.shiftID = s.shiftID " +
            "WHERE wd.employeeID = ? AND wd.weekScheduleID = ?";

        try {
            Integer result = jdbcTemplate.queryForObject(sql, Integer.class, shipperID, weekScheduleID);
            return result != null ? result : 0;
        } catch (Exception e) {
            throw new RuntimeException("Error getting total working hours for shipper", e);
        }
    }

    // Check if a shipper has any schedule on a specific date
    @Override
    public boolean hasScheduleOnDate(int shipperID, Date date) {
        String sql = 
            "SELECT COUNT(*) as count " +
            "FROM WeekSchedule ws " +
            "INNER JOIN WeekDetails wd ON ws.weekScheduleID = wd.weekScheduleID " +
            "WHERE wd.employeeID = ? " +
            "AND ? BETWEEN ws.weekStartDate AND ws.weekEndDate";

        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, shipperID, date);
            return count != null && count > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error checking shipper schedule on date", e);
        }
    }

    // Get week schedule by its ID
    @Override
    public WeekSchedule getWeekScheduleById(int weekScheduleID) {
        String sql = "SELECT weekScheduleID, weekStartDate, weekEndDate FROM WeekSchedule WHERE weekScheduleID = ?";
        
        try {
            List<WeekSchedule> schedules = jdbcTemplate.query(sql, WEEK_SCHEDULE_ROW_MAPPER, weekScheduleID);
            return schedules.isEmpty() ? null : schedules.get(0);
        } catch (Exception e) {
            throw new RuntimeException("Error getting week schedule by ID", e);
        }
    }

    // Get total number of orders delivered by a shipper in a specific week
    @Override
    public int getTotalOrdersDelivered(int shipperID, int weekScheduleID) {
        String sql = 
            "SELECT COUNT(*) as totalOrders " +
            "FROM Orders o " +
            "INNER JOIN WeekSchedule ws ON o.OrderDate BETWEEN ws.WeekStartDate AND ws.WeekEndDate " +
            "WHERE o.ShipperID = ? AND ws.WeekScheduleID = ? " +
            "AND o.OrderStatus IN ('Completed', 'Delivered')";

        try {
            Integer result = jdbcTemplate.queryForObject(sql, Integer.class, shipperID, weekScheduleID);
            return result != null ? result : 0;
        } catch (Exception e) {
            throw new RuntimeException("Error getting total orders delivered", e);
        }
    }
}
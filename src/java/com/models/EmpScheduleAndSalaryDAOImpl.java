/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.models;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


@Repository
public class EmpScheduleAndSalaryDAOImpl implements EmpScheduleAndSalaryDAO {
    
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
    };

    // Get all week schedules of an employee
    @Override
    public List<WeekSchedule> getEmployeeWeekSchedules(int employeeID) {
        String sql =
            "SELECT DISTINCT ws.weekScheduleID, ws.weekStartDate, ws.weekEndDate " +
            "FROM WeekSchedule ws " +
            "INNER JOIN WeekDetails wd ON ws.weekScheduleID = wd.weekScheduleID " +
            "WHERE wd.employeeID = ? " +
            "ORDER BY ws.weekStartDate DESC";

        try {
            return jdbcTemplate.query(sql, WEEK_SCHEDULE_ROW_MAPPER, employeeID);
        } catch (Exception e) {
            throw new RuntimeException("Error getting employee week schedules", e);
        }
    }

    // Get week schedules of an employee within a date range
    @Override
    public List<WeekSchedule> getEmployeeWeekSchedulesByDateRange(int employeeID, Date startDate, Date endDate) {
        String sql = 
            "SELECT DISTINCT ws.weekScheduleID, ws.weekStartDate, ws.weekEndDate " +
            "FROM WeekSchedule ws " +
            "INNER JOIN WeekDetails wd ON ws.weekScheduleID = wd.weekScheduleID " +
            "WHERE wd.employeeID = ? " +
            "AND ws.weekStartDate >= ? " +
            "AND ws.weekEndDate <= ? " +
            "ORDER BY ws.weekStartDate DESC";

        try {
            return jdbcTemplate.query(sql, WEEK_SCHEDULE_ROW_MAPPER, employeeID, startDate, endDate);
        } catch (Exception e) {
            throw new RuntimeException("Error getting employee week schedules by date range", e);
        }
    }

    // Get current week schedule of an employee
    @Override
    public WeekSchedule getCurrentWeekSchedule(int employeeID) {
        String sql = 
            "SELECT DISTINCT ws.weekScheduleID, ws.weekStartDate, ws.weekEndDate " +
            "FROM WeekSchedule ws " +
            "INNER JOIN WeekDetails wd ON ws.weekScheduleID = wd.weekScheduleID " +
            "WHERE wd.employeeID = ? " +
            "AND CAST(GETDATE() AS DATE) BETWEEN ws.weekStartDate AND ws.weekEndDate";

        try {
            List<WeekSchedule> schedules = jdbcTemplate.query(sql, WEEK_SCHEDULE_ROW_MAPPER, employeeID);
            return schedules.isEmpty() ? null : schedules.get(0);
        } catch (Exception e) {
            throw new RuntimeException("Error getting current week schedule", e);
        }
    }

    // Get upcoming week schedules of an employee
    @Override
    public List<WeekSchedule> getUpcomingWeekSchedules(int employeeID, int limit) {
        String sql =
            "SELECT DISTINCT ws.weekScheduleID, ws.weekStartDate, ws.weekEndDate " +
            "FROM WeekSchedule ws " +
            "INNER JOIN WeekDetails wd ON ws.weekScheduleID = wd.weekScheduleID " +
            "WHERE wd.employeeID = ? " +
            "AND ws.weekStartDate > CAST(GETDATE() AS DATE) " +
            "ORDER BY ws.weekStartDate ASC " +
            "OFFSET 0 ROWS FETCH NEXT " + limit + " ROWS ONLY";

        try {
            return jdbcTemplate.query(sql, WEEK_SCHEDULE_ROW_MAPPER, employeeID);
        } catch (Exception e) {
            throw new RuntimeException("Error getting upcoming week schedules", e);
        }
    }

    // Get week details of an employee for a specific week
    @Override
    public List<WeekDetails> getEmployeeWeekDetails(int employeeID, int weekScheduleID) {
        String sql = 
            "SELECT wd.*, dow.dayName, s.shiftName, s.startTime, s.endTime " +
            "FROM WeekDetails wd " +
            "LEFT JOIN DayOfWeek dow ON wd.dayID = dow.dayID " +
            "LEFT JOIN Shift s ON wd.shiftID = s.shiftID " +
            "WHERE wd.employeeID = ? AND wd.weekScheduleID = ? " +
            "ORDER BY dow.dayID, s.startTime";

        try {
            return jdbcTemplate.query(sql, WEEK_DETAILS_ROW_MAPPER, employeeID, weekScheduleID);
        } catch (Exception e) {
            throw new RuntimeException("Error getting employee week details", e);
        }
    }

    // Get salary of an employee for a specific week
    @Override
    public WeekSalaryDTO getEmployeeWeekSalary(int employeeID, int weekScheduleID) {
        String salaryCheckSql = "SELECT * FROM Salary WHERE AccountID = ? AND WeekScheduleID = ?";
        
        try {
            List<Map<String, Object>> salaryCheck = jdbcTemplate.queryForList(salaryCheckSql, employeeID, weekScheduleID);
            
            if (!salaryCheck.isEmpty()) {
                String sql = 
                    "SELECT ws.WeekScheduleID, ws.WeekStartDate, ws.WeekEndDate, " +
                    "       s.TotalSalary as totalSalary, " +
                    "       s.TotalHours as totalHoursWorked, " +
                    "       SUM(COALESCE(wd.OvertimeHours, 0)) as totalOvertimeHours, " +
                    "       SUM(COALESCE(wd.OvertimeHours, 0)) * a.HourlyRate * 1.5 as totalOvertimeSalary, " +
                    "       s.SalaryPaymentDate as salaryPaymentDate " +
                    "FROM WeekSchedule ws " +
                    "INNER JOIN WeekDetails wd ON ws.WeekScheduleID = wd.WeekScheduleID " +
                    "INNER JOIN Accounts a ON wd.EmployeeID = a.AccountID " +
                    "INNER JOIN Salary s ON ws.WeekScheduleID = s.WeekScheduleID AND s.AccountID = a.AccountID " +
                    "WHERE wd.EmployeeID = ? AND ws.WeekScheduleID = ? " +
                    "GROUP BY ws.WeekScheduleID, ws.WeekStartDate, ws.WeekEndDate, " +
                    "         s.TotalSalary, s.TotalHours, s.SalaryPaymentDate, a.HourlyRate";
                
                List<WeekSalaryDTO> results = jdbcTemplate.query(sql, WEEK_SALARY_ROW_MAPPER, employeeID, weekScheduleID);
                return results.isEmpty() ? null : results.get(0);
            } else {
                String calcSql = 
                    "SELECT ws.WeekScheduleID, ws.WeekStartDate, ws.WeekEndDate, " +
                    "       (SUM(DATEDIFF(HOUR, sh.StartTime, sh.EndTime)) * a.HourlyRate + " +
                    "        SUM(COALESCE(wd.OvertimeHours, 0)) * a.HourlyRate * 1.5) as totalSalary, " +
                    "       SUM(DATEDIFF(HOUR, sh.StartTime, sh.EndTime)) as totalHoursWorked, " +
                    "       SUM(COALESCE(wd.OvertimeHours, 0)) as totalOvertimeHours, " +
                    "       SUM(COALESCE(wd.OvertimeHours, 0)) * a.HourlyRate * 1.5 as totalOvertimeSalary, " +
                    "       NULL as salaryPaymentDate " +
                    "FROM WeekSchedule ws " +
                    "INNER JOIN WeekDetails wd ON ws.WeekScheduleID = wd.WeekScheduleID " +
                    "INNER JOIN Accounts a ON wd.EmployeeID = a.AccountID " +
                    "INNER JOIN Shift sh ON wd.ShiftID = sh.ShiftID " +
                    "WHERE wd.EmployeeID = ? AND ws.WeekScheduleID = ? " +
                    "GROUP BY ws.WeekScheduleID, ws.WeekStartDate, ws.WeekEndDate, a.HourlyRate";
                
                List<WeekSalaryDTO> results = jdbcTemplate.query(calcSql, WEEK_SALARY_ROW_MAPPER, employeeID, weekScheduleID);
                return results.isEmpty() ? null : results.get(0);
            }
            
        } catch (Exception e) {
            throw new RuntimeException("Error getting employee week salary: " + e.getMessage(), e);
        }
    }

    // Get salary history of an employee
    @Override
    public List<WeekSalaryDTO> getEmployeeSalaryHistory(int employeeID) {
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
            "       MAX(s.SalaryPaymentDate) as salaryPaymentDate " +
            "FROM WeekSchedule ws " +
            "INNER JOIN WeekDetails wd ON ws.WeekScheduleID = wd.WeekScheduleID " +
            "INNER JOIN Accounts a ON wd.EmployeeID = a.AccountID " +
            "INNER JOIN Shift sh ON wd.ShiftID = sh.ShiftID " +
            "LEFT JOIN Salary s ON (s.AccountID = wd.EmployeeID AND " +
            "                      (s.WeekScheduleID = ws.WeekScheduleID OR " +
            "                       (s.WeekScheduleID IS NULL AND s.ShiftID = wd.ShiftID))) " +
            "WHERE wd.EmployeeID = ? " +
            "GROUP BY ws.WeekScheduleID, ws.WeekStartDate, ws.WeekEndDate, a.HourlyRate " +
            "ORDER BY ws.WeekStartDate DESC";
            
        try {
            return jdbcTemplate.query(sql, WEEK_SALARY_ROW_MAPPER, employeeID);
        } catch (Exception e) {
            throw new RuntimeException("Error getting employee salary history", e);
        }
    }

    // Get salary of an employee within a date range
    @Override
    public List<WeekSalaryDTO> getEmployeeSalaryByDateRange(int employeeID, Date startDate, Date endDate) {
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
            "       MAX(s.SalaryPaymentDate) as salaryPaymentDate " +
            "FROM WeekSchedule ws " +
            "INNER JOIN WeekDetails wd ON ws.WeekScheduleID = wd.WeekScheduleID " +
            "INNER JOIN Accounts a ON wd.EmployeeID = a.AccountID " +
            "INNER JOIN Shift sh ON wd.ShiftID = sh.ShiftID " +
            "LEFT JOIN Salary s ON (s.AccountID = wd.EmployeeID AND " +
            "                      (s.WeekScheduleID = ws.WeekScheduleID OR " +
            "                       (s.WeekScheduleID IS NULL AND s.ShiftID = wd.ShiftID))) " +
            "WHERE wd.EmployeeID = ? " +
            "AND ws.WeekStartDate <= ? AND ws.WeekEndDate >= ? " +
            "GROUP BY ws.WeekScheduleID, ws.WeekStartDate, ws.WeekEndDate, a.HourlyRate " +
            "ORDER BY ws.WeekStartDate DESC";
            
        try {
            return jdbcTemplate.query(sql, WEEK_SALARY_ROW_MAPPER, employeeID, endDate, startDate);
        } catch (Exception e) {
            throw new RuntimeException("Error getting employee salary by date range for employeeID: " + employeeID, e);
        }
    }

    // Get total working hours of an employee for a specific week
    @Override
    public int getTotalWorkingHours(int employeeID, int weekScheduleID) {
        String sql = 
            "SELECT COALESCE(SUM( " +
            "    DATEDIFF(HOUR, s.startTime, s.endTime) + " +
            "    COALESCE(wd.overtimeHours, 0) " +
            "), 0) as totalHours " +
            "FROM WeekDetails wd " +
            "LEFT JOIN Shift s ON wd.shiftID = s.shiftID " +
            "WHERE wd.employeeID = ? AND wd.weekScheduleID = ?";

        try {
            Integer result = jdbcTemplate.queryForObject(sql, Integer.class, employeeID, weekScheduleID);
            return result != null ? result : 0;
        } catch (Exception e) {
            throw new RuntimeException("Error getting total working hours", e);
        }
    }

    // Check if employee has schedule on a specific date
    @Override
    public boolean hasScheduleOnDate(int employeeID, Date date) {
        String sql = 
            "SELECT COUNT(*) as count " +
            "FROM WeekSchedule ws " +
            "INNER JOIN WeekDetails wd ON ws.weekScheduleID = wd.weekScheduleID " +
            "WHERE wd.employeeID = ? " +
            "AND ? BETWEEN ws.weekStartDate AND ws.weekEndDate";

        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, employeeID, date);
            return count != null && count > 0;
        } catch (Exception e) {
            throw new RuntimeException("Error checking schedule on date", e);
        }
    }

    // Get week schedule by ID
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

}

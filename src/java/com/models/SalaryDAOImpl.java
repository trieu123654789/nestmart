package com.models;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class SalaryDAOImpl implements SalaryDAO {

    private JdbcTemplate jdbcTemplate;

    public SalaryDAOImpl() {
    }

    public SalaryDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        System.out.println("JdbcTemplate initialized: " + (this.jdbcTemplate != null));
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Check if salary record exists
    @Override
    public boolean exists(Salary salary) {
        String sql = "SELECT COUNT(*) FROM Salary WHERE accountID = ? AND weekScheduleID = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{salary.getAccountID(), salary.getWeekScheduleID()}, Integer.class);
        return count > 0;
    }

    // Update salary after shift change
    @Override
    public void updateSalaryAfterShiftChange(int accountID, int weekScheduleID, BigDecimal newTotalSalary) {
        String sql = "UPDATE Salary SET totalSalary = ?, weekScheduleID = ? WHERE accountID = ? AND (weekScheduleID = ? OR weekScheduleID IS NULL)";
        jdbcTemplate.update(sql, newTotalSalary, weekScheduleID, accountID, weekScheduleID);
    }

    // Calculate new salary for a specific shift
    @Override
    public BigDecimal calculateNewSalary(int accountID, int shiftID) {
        String sql = "SELECT COALESCE(hourlyRate, 0) FROM Accounts WHERE accountID = ?";
        BigDecimal hourlyRate = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountID);

        if (hourlyRate == null || hourlyRate.compareTo(BigDecimal.ZERO) == 0) {
            System.err.println("Hourly rate not found or is zero for accountID: " + accountID);
            return BigDecimal.ZERO;
        }

        int hoursWorked = 0;
        switch (shiftID) {
            case 1: hoursWorked = 5; break;
            case 2: hoursWorked = 4; break;
            case 3: hoursWorked = 5; break;
            default:
                System.err.println("Invalid shiftID: " + shiftID);
                return BigDecimal.ZERO;
        }

        BigDecimal salaryForShift = hourlyRate.multiply(new BigDecimal(hoursWorked));
        System.out.println("Calculated salary for accountID " + accountID + " and shiftID " + shiftID + " is: " + salaryForShift);
        return salaryForShift;
    }

    // Calculate total salary for a week
    @Override
    public BigDecimal calculateTotalSalaryByWeek(int weekScheduleID) {
        String sql = "SELECT SUM(totalSalary) AS totalSalary FROM Salary WHERE weekScheduleID = ?";
        BigDecimal totalSalary = jdbcTemplate.queryForObject(sql, new Object[]{weekScheduleID}, BigDecimal.class);
        return totalSalary != null ? totalSalary : BigDecimal.ZERO;
    }

    // Calculate salaries by week with details
    @Override
    public List<EmployeeSalaryDTO> calculateSalariesByWeek(int weekScheduleID) {
        String sql = "SELECT " +
                "a.accountID AS accountID, " +
                "a.fullName AS employeeName, " +
                "COALESCE(SUM(s.totalHours), 0) AS totalHours, " +
                "COALESCE(a.hourlyRate, 0) AS hourlyRate, " +
                "COALESCE(SUM(s.totalSalary), 0) AS totalSalary " +
                "FROM Accounts a " +
                "LEFT JOIN Salary s ON a.accountID = s.accountID AND s.weekScheduleID = ? " +
                "WHERE a.role IN (2, 3) " +
                "GROUP BY a.accountID, a.fullName, a.hourlyRate " +
                "ORDER BY a.fullName";

        return jdbcTemplate.query(sql, new Object[]{weekScheduleID}, (rs, rowNum) -> {
            EmployeeSalaryDTO dto = new EmployeeSalaryDTO();
            dto.setAccountID(rs.getInt("accountID"));
            dto.setFullName(rs.getString("employeeName"));
            dto.setHourlyRate(rs.getBigDecimal("hourlyRate"));
            dto.setTotalHours(rs.getInt("totalHours"));
            dto.setTotalSalary(rs.getBigDecimal("totalSalary"));
            // Set default values for overtime since they're not in the query
            dto.setOvertimeHours(0);
            dto.setOvertimeSalary(BigDecimal.ZERO);
            return dto;
        });
    }

    // Find salaries by week
    @Override
    public List<EmployeeSalaryDTO> findSalariesByWeek(int weekScheduleID) {
        String sql = "SELECT a.accountID, a.fullName, a.hourlyRate, " +
                "COALESCE(SUM(s.totalHours), 0) AS totalHours, " +
                "COALESCE(SUM(s.totalSalary), 0) AS totalSalary " +
                "FROM Accounts a " +
                "LEFT JOIN Salary s ON a.accountID = s.accountID AND s.weekScheduleID = ? " +
                "WHERE a.role IN (2, 3) " +
                "GROUP BY a.accountID, a.fullName, a.hourlyRate";

        return jdbcTemplate.query(sql, new Object[]{weekScheduleID}, (rs, rowNum) -> {
            EmployeeSalaryDTO dto = new EmployeeSalaryDTO();
            dto.setAccountID(rs.getInt("accountID"));
            dto.setFullName(rs.getString("fullName"));
            dto.setHourlyRate(rs.getBigDecimal("hourlyRate"));
            dto.setTotalHours(rs.getInt("totalHours"));
            dto.setTotalSalary(rs.getBigDecimal("totalSalary"));
            // Set default values for overtime since they're not in the query
            dto.setOvertimeHours(0);
            dto.setOvertimeSalary(BigDecimal.ZERO);
            return dto;
        });
    }

    // Calculate weekly salaries summary
    public List<WeekSalaryDTO> calculateWeeklySalaries() {
        String sql = "SELECT " +
                " ws.weekScheduleID, " +
                " ws.weekStartDate, " +
                " ws.weekEndDate, " +
                " COALESCE(SUM(CASE " +
                " WHEN wd.shiftID = 1 THEN 5 " +
                " WHEN wd.shiftID = 2 THEN 4 " +
                " WHEN wd.shiftID = 3 THEN 5 " +
                " ELSE 0 END), 0) AS totalHoursWorked, " +
                " COALESCE(SUM(CASE " +
                " WHEN wd.shiftID = 1 THEN 5 * COALESCE(a.HourlyRate, 0) " +
                " WHEN wd.shiftID = 2 THEN 4 * COALESCE(a.HourlyRate, 0) " +
                " WHEN wd.shiftID = 3 THEN 5 * COALESCE(a.HourlyRate, 0) " +
                " ELSE 0 END), 0) AS regularSalary, " +
                " COALESCE(SUM(wd.overtimeHours), 0) AS totalOvertimeHours, " +
                " COALESCE(SUM(wd.overtimeHours * COALESCE(a.HourlyRate, 0) * 1), 0) AS overtimeSalary, " +
                " COALESCE(SUM(CASE " +
                " WHEN wd.shiftID = 1 THEN 5 * COALESCE(a.HourlyRate, 0) " +
                " WHEN wd.shiftID = 2 THEN 4 * COALESCE(a.HourlyRate, 0) " +
                " WHEN wd.shiftID = 3 THEN 5 * COALESCE(a.HourlyRate, 0) " +
                " ELSE 0 END), 0) + " +
                " COALESCE(SUM(wd.overtimeHours * COALESCE(a.HourlyRate, 0) * 1), 0) AS totalSalary, " +
                " COALESCE(MAX(s.SalaryPaymentDate), NULL) AS salaryPaymentDate " +
                "FROM WeekSchedule ws " +
                "LEFT JOIN WeekDetails wd ON ws.weekScheduleID = wd.weekScheduleID " +
                "LEFT JOIN Accounts a ON wd.EmployeeID = a.AccountID " +
                "LEFT JOIN Salary s ON ws.weekScheduleID = s.WeekScheduleID AND wd.shiftID = s.shiftID " +
                "GROUP BY ws.weekScheduleID, ws.weekStartDate, ws.weekEndDate, s.SalaryPaymentDate " +
                "ORDER BY ws.weekStartDate;";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new WeekSalaryDTO(
                    rs.getInt("weekScheduleID"),
                    rs.getDate("weekStartDate"),
                    rs.getDate("weekEndDate"),
                    rs.getBigDecimal("totalSalary"),
                    rs.getInt("totalHoursWorked") + rs.getInt("totalOvertimeHours"),
                    rs.getBigDecimal("totalOvertimeHours"),
                    rs.getBigDecimal("overtimeSalary"),
                    rs.getDate("salaryPaymentDate")
            );
        });
    }

    // Find salaries with hours and payment status
    @Override
    public List<EmployeeSalaryDTO> findSalariesWithHoursByWeek(int weekScheduleID) {
        String sql = "SELECT " +
                " a.AccountID AS employeeID, " +
                " a.FullName, " +
                " COALESCE(a.HourlyRate, 0) AS HourlyRate, " +
                " COALESCE(SUM(CASE " +
                " WHEN wd.shiftID = 1 THEN 5 " +
                " WHEN wd.shiftID = 2 THEN 4 " +
                " WHEN wd.shiftID = 3 THEN 5 " +
                " ELSE 0 END), 0) + COALESCE(SUM(wd.overtimeHours), 0) AS totalHours, " +
                " COALESCE(SUM(s.totalSalary), COALESCE(SUM(CASE " +
                " WHEN wd.shiftID = 1 THEN 5 * COALESCE(a.HourlyRate, 0) " +
                " WHEN wd.shiftID = 2 THEN 4 * COALESCE(a.HourlyRate, 0) " +
                " WHEN wd.shiftID = 3 THEN 5 * COALESCE(a.HourlyRate, 0) " +
                " ELSE 0 END), 0) + COALESCE(SUM(wd.overtimeHours * COALESCE(a.HourlyRate, 0)), 0)) AS totalSalary, " +
                " CASE WHEN COUNT(CASE WHEN s.status = 'PENDING' THEN 1 END) > 0 THEN NULL " +
                " ELSE MAX(s.salaryPaymentDate) END AS paymentDate, " +
                " CASE WHEN COUNT(CASE WHEN s.status = 'PENDING' THEN 1 END) > 0 THEN 'PENDING' " +
                " WHEN COUNT(CASE WHEN s.status = 'PAID' THEN 1 END) = COUNT(s.salaryID) AND COUNT(s.salaryID) > 0 THEN 'PAID' " +
                " ELSE 'PENDING' END AS status " +
                "FROM WeekDetails wd " +
                "LEFT JOIN Accounts a ON wd.EmployeeID = a.AccountID " +
                "LEFT JOIN Salary s ON a.AccountID = s.accountID AND wd.weekScheduleID = s.weekScheduleID " +
                "WHERE wd.weekScheduleID = ? " +
                "GROUP BY a.AccountID, a.FullName, a.HourlyRate " +
                "ORDER BY a.FullName;";

        return jdbcTemplate.query(sql, new Object[]{weekScheduleID}, new EmployeeSalaryWithPaymentRowMapper());
    }

    private static class EmployeeSalaryRowMapper implements RowMapper<EmployeeSalaryDTO> {
        @Override
        public EmployeeSalaryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeSalaryDTO employeeSalary = new EmployeeSalaryDTO();
            employeeSalary.setAccountID(rs.getInt("employeeID"));
            employeeSalary.setFullName(rs.getString("FullName"));
            employeeSalary.setHourlyRate(rs.getBigDecimal("HourlyRate"));
            employeeSalary.setTotalHours(rs.getInt("totalHours"));
            employeeSalary.setTotalSalary(rs.getBigDecimal("totalSalary"));
            return employeeSalary;
        }
    }

    private static class EmployeeSalaryWithPaymentRowMapper implements RowMapper<EmployeeSalaryDTO> {
        @Override
        public EmployeeSalaryDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            EmployeeSalaryDTO employeeSalary = new EmployeeSalaryDTO();
            employeeSalary.setAccountID(rs.getInt("employeeID"));
            employeeSalary.setFullName(rs.getString("FullName"));
            employeeSalary.setHourlyRate(rs.getBigDecimal("HourlyRate"));
            employeeSalary.setTotalHours(rs.getInt("totalHours"));
            employeeSalary.setTotalSalary(rs.getBigDecimal("totalSalary"));
            employeeSalary.setPaymentDate(rs.getTimestamp("paymentDate"));
            employeeSalary.setStatus(rs.getString("status"));
            return employeeSalary;
        }
    }

    // Find total salary including overtime
    @Override
    public List<EmployeeSalaryDTO> findTotalSalary(int weekScheduleID) {
        String sql = "SELECT " +
                "wd.EmployeeID AS accountID, " +
                "CONCAT(a.FirstName, ' ', a.LastName) AS fullName, " +
                "COALESCE(a.HourlyRate, 0) AS hourlyRate, " +
                "SUM(CASE WHEN wd.shiftID = 1 THEN 5 " +
                "WHEN wd.shiftID = 2 THEN 4 " +
                "WHEN wd.shiftID = 3 THEN 5 " +
                "ELSE 0 END) AS totalHours, " +
                "SUM((CASE WHEN wd.shiftID = 1 THEN 5 " +
                "WHEN wd.shiftID = 2 THEN 4 " +
                "WHEN wd.shiftID = 3 THEN 5 ELSE 0 END) * COALESCE(a.HourlyRate, 0)) AS totalSalary, " +
                "GREATEST(0, SUM(CASE WHEN wd.shiftID = 1 THEN 5 " +
                "WHEN wd.shiftID = 2 THEN 4 " +
                "WHEN wd.shiftID = 3 THEN 5 ELSE 0 END) - 40) AS overtimeHours, " +
                "GREATEST(0, SUM(CASE WHEN wd.shiftID = 1 THEN 5 " +
                "WHEN wd.shiftID = 2 THEN 4 " +
                "WHEN wd.shiftID = 3 THEN 5 ELSE 0 END) - 40) * COALESCE(a.HourlyRate, 0) * 1 AS overtimeSalary " +
                "FROM Accounts a " +
                "JOIN WeekDetails wd ON a.AccountID = wd.EmployeeID " +
                "WHERE wd.weekScheduleID = ? " +
                "GROUP BY wd.EmployeeID, a.FirstName, a.LastName, a.HourlyRate";

        return jdbcTemplate.query(sql, new Object[]{weekScheduleID}, (rs, rowNum) -> {
            int regularHours = rs.getInt("totalHours");
            int overtimeHours = rs.getInt("overtimeHours");
            int totalHours = regularHours + overtimeHours;

            return new EmployeeSalaryDTO(
                    rs.getInt("accountID"),
                    rs.getString("fullName"),
                    rs.getBigDecimal("hourlyRate"),
                    totalHours,
                    rs.getBigDecimal("totalSalary").add(rs.getBigDecimal("overtimeSalary")),
                    overtimeHours,
                    rs.getBigDecimal("overtimeSalary")
            );
        });
    }

    // Update or insert salary record with shift info
    public void updateSalaryWithShiftInfo(int accountID, int weekScheduleID, int shiftID, BigDecimal totalSalary) {
        String checkSql = "SELECT COUNT(*) FROM Salary WHERE accountID = ? AND weekScheduleID = ? AND shiftID = ?";
        int existingCount = jdbcTemplate.queryForObject(checkSql, Integer.class, accountID, weekScheduleID, shiftID);

        if (existingCount > 0) {
            String updateSql = "UPDATE Salary SET totalSalary = ?, status = 'PENDING', salaryPaymentDate = NULL " +
                    "WHERE accountID = ? AND weekScheduleID = ? AND shiftID = ?";
            jdbcTemplate.update(updateSql, totalSalary, accountID, weekScheduleID, shiftID);
            System.out.println("Updated existing salary record for employee " + accountID + " (weekScheduleID: " + weekScheduleID + ", shiftID: " + shiftID + ") with PENDING status");
        } else {
            try {
                String getHourlyRateSql = "SELECT HourlyRate FROM Accounts WHERE AccountID = ?";
                BigDecimal hourlyRate = jdbcTemplate.queryForObject(getHourlyRateSql, BigDecimal.class, accountID);

                int totalHours = 0;
                switch (shiftID) {
                    case 1: totalHours = 5; break;
                    case 2: totalHours = 4; break;
                    case 3: totalHours = 5; break;
                    default: totalHours = 0; break;
                }

                String insertSql = "INSERT INTO Salary (accountID, weekScheduleID, shiftID, totalSalary, totalHours, hourlyRate, salaryPaymentDate, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, NULL, 'PENDING')";
                jdbcTemplate.update(insertSql, accountID, weekScheduleID, shiftID, totalSalary, new BigDecimal(totalHours), hourlyRate);
                System.out.println("Created new PENDING salary record for employee " + accountID + " (weekScheduleID: " + weekScheduleID + ", shiftID: " + shiftID + ")");
            } catch (Exception e) {
                System.err.println("Error creating new salary record: " + e.getMessage());
            }
        }
    }

    // Mark all salaries in a week as paid
    public int markWeekSalariesAsPaid(int weekScheduleID, java.sql.Timestamp paymentDateTime) {
        String sql = "UPDATE Salary SET salaryPaymentDate = ?, status = 'PAID' WHERE weekScheduleID = ? AND (status = 'PENDING' OR status IS NULL OR salaryPaymentDate IS NULL)";
        return jdbcTemplate.update(sql, paymentDateTime, weekScheduleID);
    }

    // Mark specific employee's salaries as paid
    public int markEmployeeSalaryAsPaid(int accountID, int weekScheduleID, java.sql.Timestamp paymentDateTime) {
        String sql = "UPDATE Salary SET salaryPaymentDate = ?, status = 'PAID' WHERE accountID = ? AND weekScheduleID = ? AND (status = 'PENDING' OR status IS NULL OR salaryPaymentDate IS NULL)";
        return jdbcTemplate.update(sql, paymentDateTime, accountID, weekScheduleID);
    }

    // Get unpaid salaries by week
    public List<EmployeeSalaryDTO> getUnpaidSalariesByWeek(int weekScheduleID) {
        String sql = "SELECT s.accountID, a.FullName, s.hourlyRate, s.totalHours, s.totalSalary, s.status " +
                "FROM Salary s " +
                "LEFT JOIN Accounts a ON s.accountID = a.AccountID " +
                "WHERE s.weekScheduleID = ? AND (s.status = 'PENDING' OR s.status IS NULL OR s.salaryPaymentDate IS NULL) " +
                "ORDER BY a.FullName";

        return jdbcTemplate.query(sql, new Object[]{weekScheduleID}, (rs, rowNum) -> {
            EmployeeSalaryDTO salary = new EmployeeSalaryDTO();
            salary.setAccountID(rs.getInt("accountID"));
            salary.setFullName(rs.getString("FullName"));
            salary.setHourlyRate(rs.getBigDecimal("hourlyRate"));
            salary.setTotalHours(rs.getInt("totalHours"));
            salary.setTotalSalary(rs.getBigDecimal("totalSalary"));
            salary.setStatus(rs.getString("status"));
            return salary;
        });
    }
}

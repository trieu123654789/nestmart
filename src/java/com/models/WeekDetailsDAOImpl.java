package com.models;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

public class WeekDetailsDAOImpl implements WeekDetailsDAO {

    private JdbcTemplate jdbcTemplate;

    public WeekDetailsDAOImpl() {
    }

    public WeekDetailsDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Getter for JdbcTemplate
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    // Setter for JdbcTemplate
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Save or update WeekDetails based on weekDetailID value
    @Override
    public void save(WeekDetails weekDetails) {
        String sql;
        if (weekDetails.getWeekDetailID() == 0) {
            sql = "INSERT INTO WeekDetails (weekScheduleID, employeeID, dayID, shiftID) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, weekDetails.getWeekScheduleID(), weekDetails.getEmployeeID(), weekDetails.getDayID(), weekDetails.getShiftID());
        } else {
            sql = "UPDATE WeekDetails SET weekScheduleID = ?, employeeID = ?, dayID = ?, shiftID = ? WHERE weekDetailID = ?";
            jdbcTemplate.update(sql, weekDetails.getWeekScheduleID(), weekDetails.getEmployeeID(), weekDetails.getDayID(), weekDetails.getShiftID(), weekDetails.getWeekDetailID());
        }
    }

    // Find all WeekDetails by weekScheduleID
    @Override
    public List<WeekDetails> findByWeekScheduleID(int weekScheduleID) {
        String sql = "SELECT * FROM WeekDetails WHERE WeekScheduleID =?";
        return jdbcTemplate.query(sql, new Object[]{weekScheduleID}, (rs, rowNum) -> {
            WeekDetails weekDetails = new WeekDetails();
            weekDetails.setWeekDetailID(rs.getInt("weekDetailID"));
            weekDetails.setWeekScheduleID(rs.getInt("weekScheduleID"));
            weekDetails.setEmployeeID(rs.getInt("employeeID"));
            weekDetails.setDayID(rs.getInt("dayID"));
            weekDetails.setShiftID(rs.getInt("shiftID"));
            weekDetails.setOvertimeHours(rs.getBigDecimal("overtimeHours"));
            weekDetails.setStatus(rs.getString("status"));
            weekDetails.setIsUpdated(rs.getBoolean("isUpdated"));
            weekDetails.setUpdateDate(rs.getDate("updateDate"));
            weekDetails.setNotes(rs.getString("notes"));

            return weekDetails;
        });
    }

    // Get detailed WeekDetails with employee, day, and shift information
    @Override
    public List<WeekDetails> getWeekDetailsByScheduleID(int weekScheduleID) {
        String sql = "SELECT a.fullName AS employeeName, d.dayName AS dayName, s.shiftName, s.startTime, "
                + "s.endTime, wd.overtimeHours, wd.status, wd.notes "
                + "FROM WeekDetails wd "
                + "JOIN Accounts a ON wd.employeeID = a.accountID "
                + "JOIN DayOfWeek d ON wd.dayID = d.dayID "
                + "JOIN Shift s ON wd.shiftID = s.shiftID "
                + "WHERE wd.weekScheduleID = ?";

        return jdbcTemplate.query(sql, new Object[]{weekScheduleID}, new RowMapper<WeekDetails>() {
            @Override
            public WeekDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                WeekDetails weekDetails = new WeekDetails();

                Accounts account = new Accounts();
                account.setFullName(rs.getString("employeeName"));
                weekDetails.setEmployeeID(account.getAccountID());

                DayOfWeek dayOfWeek = new DayOfWeek();
                dayOfWeek.setDayName(rs.getString("dayName"));
                weekDetails.setDayID(dayOfWeek.getDayID());

                Shift shift = new Shift();
                shift.setShiftName(rs.getString("shiftName"));
                shift.setStartTime(rs.getTime("startTime"));
                shift.setEndTime(rs.getTime("endTime"));
                weekDetails.setShiftID(shift.getShiftID());

                weekDetails.setOvertimeHours(rs.getBigDecimal("overtimeHours"));
                weekDetails.setStatus(rs.getString("status"));
                weekDetails.setNotes(rs.getString("notes"));

                return weekDetails;
            }
        });
    }

    // Find WeekDetails by schedule, day, shift, and employee
    @Override
    public WeekDetails findByScheduleAndDayAndShift(int weekScheduleID, int dayID, int shiftID, int employeeID) {
        String sql = "SELECT * FROM WeekDetails WHERE weekScheduleID = ? AND dayID = ? AND shiftID = ? AND employeeID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{weekScheduleID, dayID, shiftID, employeeID}, (rs, rowNum) -> {
                WeekDetails weekDetails = new WeekDetails();
                weekDetails.setWeekDetailID(rs.getInt("weekDetailID"));
                weekDetails.setWeekScheduleID(rs.getInt("weekScheduleID"));
                weekDetails.setEmployeeID(rs.getInt("employeeID"));
                weekDetails.setDayID(rs.getInt("dayID"));
                weekDetails.setShiftID(rs.getInt("shiftID"));
                return weekDetails;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching WeekDetails for ScheduleID: " + weekScheduleID, e);
        }
    }

    // Find WeekDetails by weekDetailID
    @Override
    public WeekDetails findByID(int weekDetailID) {
        String sql = "SELECT * FROM WeekDetails WHERE weekDetailID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{weekDetailID}, (rs, rowNum) -> {
                WeekDetails weekDetails = new WeekDetails();
                weekDetails.setWeekDetailID(rs.getInt("weekDetailID"));
                weekDetails.setWeekScheduleID(rs.getInt("weekScheduleID"));
                weekDetails.setEmployeeID(rs.getInt("employeeID"));
                weekDetails.setDayID(rs.getInt("dayID"));
                weekDetails.setShiftID(rs.getInt("shiftID"));
                weekDetails.setOvertimeHours(rs.getBigDecimal("overtimeHours"));
                weekDetails.setStatus(rs.getString("status"));
                weekDetails.setIsUpdated(rs.getBoolean("isUpdated"));
                weekDetails.setUpdateDate(rs.getDate("updateDate"));
                weekDetails.setNotes(rs.getString("notes"));
                return weekDetails;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching WeekDetails with ID: " + weekDetailID, e);
        }
    }

    // Update existing WeekDetails record
    @Override
    public void update(WeekDetails weekDetails) {
        String sql = "UPDATE WeekDetails SET weekScheduleID = ?, employeeID = ?, dayID = ?, shiftID = ?, "
                + "overtimeHours = ?, status = ?, isUpdated = ?, updateDate = ?, notes = ? WHERE weekDetailID = ?";

        try {
            jdbcTemplate.update(sql, weekDetails.getWeekScheduleID(), weekDetails.getEmployeeID(), weekDetails.getDayID(),
                    weekDetails.getShiftID(), weekDetails.getOvertimeHours(), weekDetails.getStatus(), weekDetails.isIsUpdated(),
                    weekDetails.getUpdateDate(), weekDetails.getNotes(), weekDetails.getWeekDetailID());
        } catch (Exception e) {
            throw new RuntimeException("Error updating WeekDetails for ID: " + weekDetails.getWeekDetailID(), e);
        }
    }

    // Find all WeekDetails by employeeID
    @Override
    public List<WeekDetails> findByEmployeeID(int employeeID) {
        String sql = "SELECT * FROM WeekDetails WHERE employeeID = ?";
        return jdbcTemplate.query(sql, new Object[]{employeeID}, (rs, rowNum) -> {
            WeekDetails weekDetails = new WeekDetails();
            weekDetails.setWeekDetailID(rs.getInt("weekDetailID"));
            weekDetails.setWeekScheduleID(rs.getInt("weekScheduleID"));
            weekDetails.setEmployeeID(rs.getInt("employeeID"));
            weekDetails.setDayID(rs.getInt("dayID"));
            weekDetails.setShiftID(rs.getInt("shiftID"));
            weekDetails.setOvertimeHours(rs.getBigDecimal("overtimeHours"));
            weekDetails.setStatus(rs.getString("status"));
            weekDetails.setIsUpdated(rs.getBoolean("isUpdated"));
            weekDetails.setUpdateDate(rs.getDate("updateDate"));
            weekDetails.setNotes(rs.getString("notes"));
            return weekDetails;
        });
    }

    // Find WeekDetails by schedule and employee
    @Override
    public List<WeekDetails> findByScheduleAndEmployee(int weekScheduleID, int employeeID) {
        String sql = "SELECT * FROM WeekDetails WHERE weekScheduleID = ? AND employeeID = ?";
        return jdbcTemplate.query(sql, new Object[]{weekScheduleID, employeeID}, (rs, rowNum) -> {
            WeekDetails weekDetails = new WeekDetails();
            weekDetails.setWeekDetailID(rs.getInt("weekDetailID"));
            weekDetails.setWeekScheduleID(rs.getInt("weekScheduleID"));
            weekDetails.setEmployeeID(rs.getInt("employeeID"));
            weekDetails.setDayID(rs.getInt("dayID"));
            weekDetails.setShiftID(rs.getInt("shiftID"));
            weekDetails.setOvertimeHours(rs.getBigDecimal("overtimeHours"));
            weekDetails.setStatus(rs.getString("status"));
            weekDetails.setIsUpdated(rs.getBoolean("isUpdated"));
            weekDetails.setUpdateDate(rs.getDate("updateDate"));
            weekDetails.setNotes(rs.getString("notes"));
            return weekDetails;
        });
    }

    // Check if shift is duplicated for employee on specific day
    @Override
    public boolean isShiftDuplicated(int employeeID, int dayID, int shiftID) {
        String sql = "SELECT COUNT(*) FROM WeekDetails WHERE employeeID = ? AND dayID = ? AND shiftID = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{employeeID, dayID, shiftID}, Integer.class);
        return count > 0;
    }

    // Find WeekDetails by shift and day
    @Override
    public List<WeekDetails> findByShiftAndDay(int shiftID, int dayID) {
        String sql = "SELECT * FROM WeekDetails WHERE shiftID = ? AND dayID = ?";

        RowMapper<WeekDetails> rowMapper = new RowMapper<WeekDetails>() {
            @Override
            public WeekDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                WeekDetails weekDetail = new WeekDetails();
                weekDetail.setWeekDetailID(rs.getInt("weekDetailID"));
                weekDetail.setWeekScheduleID(rs.getInt("weekScheduleID"));
                weekDetail.setEmployeeID(rs.getInt("employeeID"));
                weekDetail.setDayID(rs.getInt("dayID"));
                weekDetail.setShiftID(rs.getInt("shiftID"));
                weekDetail.setOvertimeHours(rs.getBigDecimal("overtimeHours"));
                weekDetail.setStatus(rs.getString("status"));
                weekDetail.setIsUpdated(rs.getBoolean("isUpdated"));
                weekDetail.setUpdateDate(rs.getDate("updateDate"));
                weekDetail.setNotes(rs.getString("notes"));
                return weekDetail;
            }
        };

        return jdbcTemplate.query(sql, new Object[]{shiftID, dayID}, rowMapper);
    }

    // Delete WeekDetails by weekDetailID
    @Override
    public void delete(int weekDetailID) {
        String sql = "DELETE FROM WeekDetails WHERE weekDetailID = ?";
        jdbcTemplate.update(sql, weekDetailID);
    }

    // Get total overtime hours for a specific week schedule
    @Override
    public int getOvertimeHoursByWeekScheduleID(int weekScheduleID) {
        String sql = "SELECT SUM(overtimeHours) FROM WeekDetails WHERE weekScheduleID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{weekScheduleID}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        } catch (DataAccessException e) {
            return 0;
        }
    }

}
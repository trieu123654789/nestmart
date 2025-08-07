package com.models;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class WeekScheduleDAOImpl implements WeekScheduleDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public WeekScheduleDAOImpl() {
    }

    public WeekScheduleDAOImpl(DataSource dataSource) {
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

    // Retrieve all week schedules from database
    @Override
    public List<WeekSchedule> findAll() {
        String sql = "SELECT * FROM WeekSchedule";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            WeekSchedule weekSchedule = new WeekSchedule();
            weekSchedule.setWeekScheduleID(rs.getInt("weekScheduleID"));
            weekSchedule.setWeekStartDate(rs.getDate("weekStartDate"));
            weekSchedule.setWeekEndDate(rs.getDate("weekEndDate"));
            return weekSchedule;
        });
    }

    // Find week schedule by specific ID
    @Override
    public WeekSchedule findByID(int weekScheduleID) {
        String sql = "SELECT * FROM WeekSchedule WHERE weekScheduleID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{weekScheduleID}, (rs, rowNum) -> {
            WeekSchedule weekSchedule = new WeekSchedule();
            weekSchedule.setWeekScheduleID(rs.getInt("weekScheduleID"));
            weekSchedule.setWeekStartDate(rs.getDate("weekStartDate"));
            weekSchedule.setWeekEndDate(rs.getDate("weekEndDate"));
            return weekSchedule;
        });
    }

    // Save new week schedule to database
    @Override
    public void save(WeekSchedule weekSchedule) {
        String sql = "INSERT INTO WeekSchedule (weekStartDate, weekEndDate) VALUES (?,?)";
        jdbcTemplate.update(sql, weekSchedule.getWeekStartDate(), weekSchedule.getWeekEndDate());
    }

    // Check if week schedule already exists for given date range
    @Override
    public boolean isWeekScheduleExists(Date weekStartDate, Date weekEndDate) {
        String sql = "SELECT COUNT(*) FROM WeekSchedule WHERE (weekStartDate <= ? AND weekEndDate >=?)";
        return jdbcTemplate.queryForObject(sql, new Object[]{weekEndDate, weekStartDate}, Integer.class) > 0;
    }

    // Check if week schedule has assigned week details
    @Override
    public boolean hasAssignedWeekDetails(int weekScheduleID) {
        String sql = "SELECT COUNT(*) FROM WeekDetails WHERE weekScheduleID = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{weekScheduleID}, Integer.class);
        return count > 0;
    }

    // Find week schedules that have associated salaries
    @Override
    public List<WeekSchedule> findWeeksWithSalaries() {
        String sql = "SELECT ws.* FROM WeekSchedule ws "
                + "JOIN Salary s ON ws.weekScheduleID = s.weekScheduleID "
                + "GROUP BY ws.weekScheduleID, ws.weekStartDate, ws.weekEndDate";

        return jdbcTemplate.query(sql, new Object[]{}, new RowMapper<WeekSchedule>() {
            @Override
            public WeekSchedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                WeekSchedule weekSchedule = new WeekSchedule();
                weekSchedule.setWeekScheduleID(rs.getInt("weekScheduleID"));
                weekSchedule.setWeekStartDate(rs.getDate("weekStartDate"));
                weekSchedule.setWeekEndDate(rs.getDate("weekEndDate"));
                return weekSchedule;
            }
        });
    }

    // Delete week schedule if no week details are assigned
    @Override
    public void delete(int weekScheduleID) {
        if (!hasAssignedWeekDetails(weekScheduleID)) {
            String sql = "DELETE FROM WeekSchedule WHERE weekScheduleID = ?";
            jdbcTemplate.update(sql, weekScheduleID);
        } else {
            throw new IllegalStateException("Cannot delete week schedule with assigned week details.");
        }
    }

    // Get paginated list of week schedules
    @Override
    public List<WeekSchedule> getPagedWeekSchedules(int page, int pageSize) {
        String query = "SELECT * FROM WeekSchedule ORDER BY WeekScheduleID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        int offset = (page - 1) * pageSize;

        return jdbcTemplate.query(query, new Object[]{offset, pageSize}, new RowMapper<WeekSchedule>() {
            @Override
            public WeekSchedule mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new WeekSchedule(
                        rs.getInt("WeekScheduleID"),
                        rs.getDate("WeekStartDate"),
                        rs.getDate("WeekEndDate")
                );
            }
        });
    }

    // Get total count of week schedules
    @Override
    public int getTotalWeekSchedules() {
        String query = "SELECT COUNT(*) FROM WeekSchedule";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

}
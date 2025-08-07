/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.models;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DayOfWeekDAOImpl implements DayOfWeekDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public DayOfWeekDAOImpl() {
    }

    public DayOfWeekDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Inserts a new day of week into the database
    @Override
    public void save(DayOfWeek dayOfWeek) {
        String sql = "INSERT INTO DayOfWeek (DayName) VALUES (?)";
        jdbcTemplate.update(sql, dayOfWeek.getDayName());
    }

    // Retrieves a day of week by its ID
    @Override
    public DayOfWeek findById(int dayID) {
        String sql = "SELECT * FROM DayOfWeek WHERE DayID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{dayID}, new BeanPropertyRowMapper<>(DayOfWeek.class));
    }

    // Retrieves all days of the week
    @Override
    public List<DayOfWeek> findAll() {
        String sql = "SELECT * FROM DayOfWeek";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(DayOfWeek.class));
    }

    // Updates an existing day of week
    @Override
    public void update(DayOfWeek dayOfWeek) {
        String sql = "UPDATE DayOfWeek SET DayName = ? WHERE DayID = ?";
        jdbcTemplate.update(sql, dayOfWeek.getDayName(), dayOfWeek.getDayID());
    }

    // Deletes a day of week by its ID
    @Override
    public void delete(int dayID) {
        String sql = "DELETE FROM DayOfWeek WHERE DayID = ?";
        jdbcTemplate.update(sql, dayID);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}

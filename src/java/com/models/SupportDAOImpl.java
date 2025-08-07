package com.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class SupportDAOImpl implements SupportDAO {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public SupportDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        System.out.println("JdbcTemplate initialized: " + (this.jdbcTemplate != null));
    }

    // Save a new support message
    @Override
    public void saveMessage(Support support) {
        String sql = "INSERT INTO Support (customerID, employeeID, message, status, sendDate) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                support.getCustomerID(),
                support.getEmployeeID(),
                support.getMessage(),
                support.getStatus(),
                Timestamp.valueOf(support.getSendDate())
        );
    }

    // Get all messages by customerId
    @Override
    public List<Support> getMessagesByCustomerId(int customerId) {
        String sql = "SELECT * FROM Support WHERE customerID = ? ORDER BY sendDate ASC";
        return jdbcTemplate.query(sql, new Object[]{customerId}, new SupportRowMapper());
    }

    // Get all messages by customerId after a given messageId
    @Override
    public List<Support> getMessagesByCustomerIdAfterId(int customerId, int lastMessageId) {
        String sql = "SELECT * FROM Support WHERE customerID = ? AND supportID > ? ORDER BY sendDate ASC";
        return jdbcTemplate.query(sql, new Object[]{customerId, lastMessageId}, new SupportRowMapper());
    }

    // Get the latest messages of customers with new messages
    @Override
    public List<Support> getCustomersWithNewMessages() {
        String sql = "SELECT s.CustomerID, s.EmployeeID, s.Status, "
                + "s.SendDate, s.Message, s.SupportID, "
                + "a.FullName, a.PhoneNumber "
                + "FROM Support s "
                + "JOIN Accounts a ON s.CustomerID = a.AccountID "
                + "WHERE (a.Role = 4) "
                + "AND s.SupportID IN ("
                + "    SELECT MAX(SupportID) "
                + "    FROM Support "
                + "    GROUP BY CustomerID"
                + ") "
                + "ORDER BY s.SendDate DESC";

        return jdbcTemplate.query(sql, new SupportRowMapperWithCustomerInfo());
    }

    // Get messages by customerId and optional employeeId
    @Override
    public List<Support> getMessagesByCustomerIdAndEmployeeId(Integer customerId, Integer employeeId) {
        String sql = "SELECT * FROM Support WHERE CustomerID = ? "
                + (employeeId != null ? "AND (EmployeeID = ? OR EmployeeID IS NULL) " : "")
                + "ORDER BY SendDate";

        Object[] params = (employeeId != null) ? new Object[]{customerId, employeeId} : new Object[]{customerId};
        return jdbcTemplate.query(sql, params, new SupportRowMapper());
    }

    // Get a message by its ID
    @Override
    public Support getMessageById(int messageId) {
        String sql = "SELECT * FROM Support WHERE supportID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{messageId}, new SupportRowMapper());
    }

    // Update an existing message
    @Override
    public void updateMessage(Support support) {
        String sql = "UPDATE Support SET customerID = ?, employeeID = ?, message = ?, status = ?, sendDate = ? WHERE supportID = ?";
        jdbcTemplate.update(sql,
                support.getCustomerID(),
                support.getEmployeeID(),
                support.getMessage(),
                support.getStatus(),
                Timestamp.valueOf(support.getSendDate()),
                support.getSupportID()
        );
    }

    // Get all new or processing messages
    @Override
    public List<Support> getNewMessages() {
        String sql = "SELECT * FROM Support WHERE status = 'New' OR status = 'Processing' ORDER BY sendDate ASC";
        return jdbcTemplate.query(sql, new SupportRowMapper());
    }

    private static class SupportRowMapper implements RowMapper<Support> {
        @Override
        public Support mapRow(ResultSet rs, int rowNum) throws SQLException {
            Support support = new Support();
            support.setSupportID(rs.getInt("supportID"));
            support.setCustomerID(rs.getObject("customerID", Integer.class));
            support.setEmployeeID(rs.getObject("employeeID", Integer.class));
            support.setMessage(rs.getString("message"));
            support.setStatus(rs.getString("status"));

            Timestamp ts = rs.getTimestamp("sendDate");
            support.setSendDate(ts != null ? ts.toLocalDateTime() : null);

            return support;
        }
    }

    private static class SupportRowMapperWithCustomerInfo implements RowMapper<Support> {
        @Override
        public Support mapRow(ResultSet rs, int rowNum) throws SQLException {
            Support support = new SupportRowMapper().mapRow(rs, rowNum);
            support.setFullName(rs.getString("FullName"));
            support.setPhoneNumber(rs.getString("PhoneNumber"));
            return support;
        }
    }

    // Get unread message count for a customer
    @Override
    public int getUnreadMessageCount(int customerId) {
        String sql = "SELECT COUNT(*) FROM Support WHERE customerID = ? AND status = 'Sent'";
        return jdbcTemplate.queryForObject(sql, new Object[]{customerId}, Integer.class);
    }

    // Delete messages by customerId (ignores employeeId)
    @Override
    public int deleteMessagesByCustomerIdAndEmployeeId(int customerId, int employeeId) {
        String sql = "DELETE FROM Support WHERE customerID = ? ";
        return jdbcTemplate.update(sql, customerId);
    }
}

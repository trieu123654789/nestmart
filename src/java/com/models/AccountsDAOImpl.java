package com.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.RowMapper;

@Repository
public class AccountsDAOImpl implements AccountsDAO {

    private JdbcTemplate jdbcTemplate;

    public AccountsDAOImpl() {
    }

    public AccountsDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        System.out.println("JdbcTemplate initialized: " + (this.jdbcTemplate != null));
    }

    // Retrieve all accounts
    @Override
    public List<Accounts> findAll() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
        }

        String query = "SELECT AccountID, PhoneNumber, Password, Email, Role, Gender, FullName, Birthday, Address, HourlyRate FROM Accounts";
        List<Accounts> accountList = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

        for (Map<String, Object> row : rows) {
            Accounts a = new Accounts(
                    (int) row.get("AccountID"),
                    (String) row.get("PhoneNumber"),
                    (String) row.get("Password"),
                    (String) row.get("Email"),
                    (int) row.get("Role"),
                    (String) row.get("Gender"),
                    (String) row.get("FullName"),
                    (Date) row.get("Birthday"),
                    (String) row.get("Address"),
                    (BigDecimal) row.get("HourlyRate"));
            accountList.add(a);
        }
        return accountList;
    }

    // Getter for JdbcTemplate
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    // Setter for JdbcTemplate
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Save a new account
    @Override
    public void save(Accounts account) {
        String query = "INSERT INTO Accounts (FullName, Password, PhoneNumber, Email, Role, Gender, Birthday, Address, HourlyRate) VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            jdbcTemplate.update(query, account.getFullName(), account.getPassword(), account.getPhoneNumber(),
                    account.getEmail(), account.getRole(), account.getGender(), account.getBirthday(),
                    account.getAddress(), account.getHourlyRate());
        } catch (DataAccessException e) {
            throw new RuntimeException("Failed to save account", e);
        }
    }

    // Find account by ID
    @Override
    public Accounts findById(int id) {
        String query = "SELECT * FROM Accounts WHERE AccountID = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, new RowMapper<Accounts>() {
            @Override
            public Accounts mapRow(ResultSet rs, int i) throws SQLException {
                return new Accounts(
                        rs.getInt("AccountID"),
                        rs.getString("PhoneNumber"),
                        rs.getString("Password"),
                        rs.getString("Email"),
                        rs.getInt("Role"),
                        rs.getString("Gender"),
                        rs.getString("FullName"),
                        rs.getDate("Birthday"),
                        rs.getString("Address"),
                        rs.getBigDecimal("HourlyRate")
                );
            }
        });
    }

    // Search accounts by keyword
    @Override
    public List<Accounts> searchByKeyword(String keyword) {
        String query = "SELECT AccountID, PhoneNumber, Email, FullName, Address, Role, Gender, Birthday FROM Accounts "
                + "WHERE PhoneNumber COLLATE Latin1_General_CI_AI LIKE ? "
                + "OR Email COLLATE Latin1_General_CI_AI LIKE ? "
                + "OR FullName COLLATE Latin1_General_CI_AI LIKE ? "
                + "OR Address COLLATE Latin1_General_CI_AI LIKE ?";
        return jdbcTemplate.query(query, new Object[]{
            "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%", "%" + keyword + "%"
        }, (rs, rowNum) -> {
            Accounts account = new Accounts();
            account.setAccountID(rs.getInt("AccountID"));
            account.setPhoneNumber(rs.getString("PhoneNumber"));
            account.setEmail(rs.getString("Email"));
            account.setFullName(rs.getString("FullName"));
            account.setAddress(rs.getString("Address"));
            account.setRole(rs.getInt("Role"));
            account.setBirthday(rs.getDate("Birthday"));
            account.setGender(rs.getString("Gender"));
            return account;
        });
    }

    // Update account
    @Override
    public void update(Accounts account) {
        String sql = "UPDATE Accounts SET FullName = ?, Password = ?, PhoneNumber = ?, Email = ?, Role = ?, Gender = ?, Birthday = ?, Address = ?, HourlyRate = ? WHERE AccountID = ?";
        jdbcTemplate.update(sql, account.getFullName(), account.getPassword(), account.getPhoneNumber(),
                account.getEmail(), account.getRole(), account.getGender(), account.getBirthday(),
                account.getAddress(), account.getHourlyRate(), account.getAccountID());
    }

    // Delete account by ID
    @Override
    public void deleteById(int id) {
        String query = "DELETE FROM Accounts WHERE AccountID = ?";
        jdbcTemplate.update(query, id);
    }

    // Check if phone number exists
    @Override
    public boolean existsByPhoneNumber(String phoneNumber) {
        String query = "SELECT COUNT(*) FROM Accounts WHERE PhoneNumber = ?";
        Integer count = jdbcTemplate.queryForObject(query, new Object[]{phoneNumber}, Integer.class);
        return count != null && count > 0;
    }

    // Check if email exists
    @Override
    public boolean existsByEmail(String email) {
        String query = "SELECT COUNT(*) FROM Accounts WHERE Email = ?";
        Integer count = jdbcTemplate.queryForObject(query, new Object[]{email}, Integer.class);
        return count != null && count > 0;
    }

    // Find account by email
    @Override
    public Accounts findByEmail(String email) {
        String sql = "SELECT * FROM Accounts WHERE Email = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{email}, new AccountRowMapper());
    }

    // Validate user by email and password
    @Override
    public boolean validateUser(String email, String password) {
        String sql = "SELECT Password FROM Accounts WHERE Email = ?";
        try {
            String storedPassword = jdbcTemplate.queryForObject(sql, new Object[]{email}, String.class);
            return storedPassword != null && password.equals(storedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    // Register a new user
    @Override
    public boolean registerUser(Accounts account) {
        try {
            String sql = "INSERT INTO Accounts (FullName, Email, Password, PhoneNumber, Address, Gender, Birthday, Role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            int rowsAffected = jdbcTemplate.update(sql, account.getFullName(), account.getEmail(),
                    account.getPassword(), account.getPhoneNumber(), account.getAddress(),
                    account.getGender(), account.getBirthday(), account.getRole());
            return rowsAffected > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // Get role by email
    @Override
    public Integer getRoleByEmail(String email) {
        String sql = "SELECT Role FROM Accounts WHERE Email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class);
        } catch (Exception e) {
            return null;
        }
    }

    // Get password by email
    @Override
    public String getPasswordByEmail(String email) {
        String sql = "SELECT Password FROM Accounts WHERE Email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, String.class);
        } catch (Exception e) {
            return null;
        }
    }

    // Update password by email
    @Override
    public void updatePassword(String email, String newPassword) {
        String sql = "UPDATE Accounts SET Password = ? WHERE Email = ?";
        jdbcTemplate.update(sql, newPassword, email);
    }

    // Find account by phone number
    @Override
    public Accounts findByPhoneNumber(String phoneNumber) {
        String sql = "SELECT * FROM Accounts WHERE PhoneNumber = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{phoneNumber}, (rs, rowNum) -> {
                Accounts account = new Accounts();
                account.setAccountID(rs.getInt("AccountID"));
                account.setEmail(rs.getString("Email"));
                account.setFullName(rs.getString("FullName"));
                account.setPhoneNumber(rs.getString("PhoneNumber"));
                account.setGender(rs.getString("Gender"));
                account.setAddress(rs.getString("Address"));
                account.setBirthday(rs.getDate("Birthday"));
                account.setRole(rs.getInt("Role"));
                return account;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Get accounts by list of roles
    @Override
    public List<Accounts> getAccountsByRoles(List<Integer> roles) {
        StringBuilder sql = new StringBuilder("SELECT * FROM Accounts WHERE Role IN (");
        for (int i = 0; i < roles.size(); i++) {
            sql.append("?");
            if (i < roles.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");
        Object[] params = roles.toArray();
        return jdbcTemplate.query(sql.toString(), params, (rs, rowNum) -> {
            Accounts account = new Accounts();
            account.setAccountID(rs.getInt("accountID"));
            account.setFullName(rs.getString("fullName"));
            account.setPhoneNumber(rs.getString("phoneNumber"));
            account.setEmail(rs.getString("email"));
            account.setRole(rs.getInt("role"));
            account.setGender(rs.getString("gender"));
            account.setBirthday(rs.getDate("birthday"));
            account.setAddress(rs.getString("address"));
            account.setHourlyRate(rs.getBigDecimal("hourlyRate"));
            return account;
        });
    }

    // Check if phone number is registered
    @Override
    public boolean isPhoneNumberRegistered(String phone) {
        String sql = "SELECT COUNT(*) FROM Accounts WHERE PhoneNumber = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{phone}, Integer.class);
        return count != null && count > 0;
    }

    // Search accounts with pagination
    @Override
    public List<Accounts> searchAccounts(String keyword, int page, int pageSize) {
        if (page < 1) page = 1;
        String query = "SELECT * FROM Accounts WHERE FullName LIKE ? OR Email LIKE ? ORDER BY AccountID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        String searchKeyword = "%" + keyword + "%";
        int offset = (page - 1) * pageSize;
        return jdbcTemplate.query(query, new Object[]{searchKeyword, searchKeyword, offset, pageSize}, (rs, rowNum) -> {
            return new Accounts(
                    rs.getInt("AccountID"),
                    rs.getString("PhoneNumber"),
                    rs.getString("Password"),
                    rs.getString("Email"),
                    rs.getInt("Role"),
                    rs.getString("Gender"),
                    rs.getString("FullName"),
                    rs.getDate("Birthday"),
                    rs.getString("Address"),
                    rs.getBigDecimal("HourlyRate")
            );
        });
    }

    // Get paginated accounts by keyword
    @Override
    public List<Accounts> getPagedAccounts(String keyword, int page, int pageSize) {
        String query = "SELECT * FROM Accounts WHERE FullName LIKE ? OR Email LIKE ? ORDER BY AccountID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        String searchKeyword = "%" + keyword + "%";
        int offset = (page - 1) * pageSize;
        return jdbcTemplate.query(query, new Object[]{searchKeyword, searchKeyword, offset, pageSize}, (rs, rowNum) -> {
            return new Accounts(
                    rs.getInt("AccountID"),
                    rs.getString("PhoneNumber"),
                    rs.getString("Password"),
                    rs.getString("Email"),
                    rs.getInt("Role"),
                    rs.getString("Gender"),
                    rs.getString("FullName"),
                    rs.getDate("Birthday"),
                    rs.getString("Address"),
                    rs.getBigDecimal("HourlyRate")
            );
        });
    }

    // Get total number of accounts by keyword
    @Override
    public int getTotalAccounts(String keyword) {
        String query = "SELECT COUNT(*) FROM Accounts WHERE FullName LIKE ? OR Email LIKE ? OR PhoneNumber LIKE ? OR Address LIKE ?";
        String searchKeyword = "%" + keyword + "%";
        return jdbcTemplate.queryForObject(query, new Object[]{searchKeyword, searchKeyword, searchKeyword, searchKeyword}, Integer.class);
    }

    // Find employees by roles
    @Override
    public List<Accounts> findEmployeesByRoles(List<Integer> roles) {
        StringBuilder sql = new StringBuilder("SELECT * FROM Accounts WHERE Role IN (");
        for (int i = 0; i < roles.size(); i++) {
            sql.append("?");
            if (i < roles.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(")");
        Object[] params = roles.toArray();
        return jdbcTemplate.query(sql.toString(), params, (rs, rowNum) -> {
            return new Accounts(
                    rs.getInt("AccountID"),
                    rs.getString("PhoneNumber"),
                    rs.getString("Password"),
                    rs.getString("Email"),
                    rs.getInt("Role"),
                    rs.getString("Gender"),
                    rs.getString("FullName"),
                    rs.getDate("Birthday"),
                    rs.getString("Address"),
                    rs.getBigDecimal("HourlyRate")
            );
        });
    }

    // Find shipper by order ID
    @Override
    public Accounts findShipperById(String orderID) {
        String sql = "SELECT a.AccountID, a.FullName FROM Accounts a JOIN Orders o ON a.AccountID = o.ShipperID WHERE o.OrderID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{orderID}, (rs, rowNum) -> {
            Accounts account = new Accounts();
            account.setAccountID(rs.getInt("AccountID"));
            account.setFullName(rs.getString("FullName"));
            return account;
        });
    }

    // Find available shippers based on current schedule
    @Override
    public List<Accounts> findShippers() {
        String sql = "SELECT DISTINCT a.AccountID, a.FullName FROM WeekDetails wd "
                + "JOIN WeekSchedule ws ON wd.WeekScheduleID = ws.WeekScheduleID "
                + "JOIN DayOfWeek d ON wd.DayID = d.DayID "
                + "JOIN Shift s ON wd.ShiftID = s.ShiftID "
                + "JOIN Accounts a ON wd.EmployeeID = a.AccountID "
                + "WHERE a.Role = 3 "
                + "AND CAST(GETDATE() AS DATE) BETWEEN ws.WeekStartDate AND ws.WeekEndDate "
                + "AND DATENAME(WEEKDAY, GETDATE()) = d.DayName "
                + "AND CONVERT(TIME, GETDATE()) BETWEEN s.StartTime AND s.EndTime";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Accounts account = new Accounts();
            account.setAccountID(rs.getInt("AccountID"));
            account.setFullName(rs.getString("FullName"));
            return account;
        });
    }

    // RowMapper for Accounts
    private static class AccountRowMapper implements RowMapper<Accounts> {
        @Override
        public Accounts mapRow(ResultSet rs, int rowNum) throws SQLException {
            Accounts account = new Accounts();
            account.setAccountID(rs.getInt("accountID"));
            account.setPhoneNumber(rs.getString("phoneNumber"));
            account.setPassword(rs.getString("password"));
            account.setEmail(rs.getString("email"));
            account.setRole(rs.getInt("role"));
            account.setGender(rs.getString("gender"));
            account.setFullName(rs.getString("fullName"));
            account.setBirthday(rs.getDate("birthday"));
            account.setAddress(rs.getString("address"));
            account.setHourlyRate(rs.getBigDecimal("hourlyRate"));
            return account;
        }
    }
}


package com.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AccountVoucherDAOImpl implements AccountVoucherDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<AccountVoucher> rowMapper = (rs, rowNum) -> {
        AccountVoucher av = new AccountVoucher();
        av.setAccountVoucherID(rs.getInt("AccountVoucherID"));
        av.setAccountID(rs.getInt("AccountID"));
        av.setVoucherID(rs.getInt("VoucherID"));
        av.setUsed(rs.getBoolean("IsUsed"));
        av.setAssignedDate(rs.getObject("AssignedDate", LocalDateTime.class));
        av.setExpiryDate(rs.getObject("ExpiryDate", LocalDateTime.class));
        av.setOrderID(rs.getObject("OrderID") != null ? rs.getString("OrderID") : null);
        av.setFullName(rs.getString("FullName"));
        av.setVoucherCode(rs.getString("Code"));
        return av;
    };

    // Retrieve paginated list of account vouchers
    @Override
    public List<AccountVoucher> findPaginated(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String sql = "SELECT av.*, a.FullName, v.Code " +
                     "FROM AccountVouchers av " +
                     "JOIN Accounts a ON av.AccountID = a.AccountID " +
                     "JOIN Vouchers v ON av.VoucherID = v.VoucherID " +
                     "ORDER BY av.AccountVoucherID " +
                     "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        return jdbcTemplate.query(sql, rowMapper, offset, pageSize);
    }

    // Get total number of account vouchers
    @Override
    public int getTotalAccountVouchers() {
        String sql = "SELECT COUNT(*) FROM AccountVouchers";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    // Search account vouchers by keyword with pagination
    @Override
    public List<AccountVoucher> searchByKeyword(String keyword, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String sql = "SELECT av.*, a.FullName, v.Code " +
                     "FROM AccountVouchers av " +
                     "JOIN Accounts a ON av.AccountID = a.AccountID " +
                     "JOIN Vouchers v ON av.VoucherID = v.VoucherID " +
                     "WHERE CAST(av.AccountID AS VARCHAR) LIKE ? " +
                     "   OR CAST(av.VoucherID AS VARCHAR) LIKE ? " +
                     "   OR a.FullName LIKE ? " +
                     "   OR v.Code LIKE ? " +
                     "ORDER BY av.AccountVoucherID " +
                     "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        String kw = "%" + keyword + "%";
        return jdbcTemplate.query(sql, rowMapper, kw, kw, kw, kw, offset, pageSize);
    }

    // Create a new account voucher
    @Override
    public void create(AccountVoucher av) throws Exception {
        String sql = "INSERT INTO AccountVouchers (AccountID, VoucherID, IsUsed, AssignedDate, ExpiryDate, OrderID) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, av.getAccountID(), av.getVoucherID(), av.isUsed(), av.getAssignedDate(), av.getExpiryDate(), null);
    }

    // Update an existing account voucher
    @Override
    public void update(AccountVoucher av) throws Exception {
        String sql = "UPDATE AccountVouchers SET AccountID = ?, VoucherID = ?, IsUsed = ?, AssignedDate = ?, ExpiryDate = ? WHERE AccountVoucherID = ?";
        jdbcTemplate.update(sql, av.getAccountID(), av.getVoucherID(), av.isUsed(), av.getAssignedDate(), av.getExpiryDate(), av.getAccountVoucherID());
    }

    // Delete an account voucher by ID
    @Override
    public void deleteById(int id) throws Exception {
        jdbcTemplate.update("DELETE FROM AccountVouchers WHERE AccountVoucherID = ?", id);
    }

    // Find account voucher by ID
    @Override
    public AccountVoucher findById(int id) {
        String sql = "SELECT av.*, a.FullName, v.Code " +
                     "FROM AccountVouchers av " +
                     "JOIN Accounts a ON av.AccountID = a.AccountID " +
                     "JOIN Vouchers v ON av.VoucherID = v.VoucherID " +
                     "WHERE av.AccountVoucherID = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    // Get accounts for dropdown (only role = 4)
    @Override
    public List<Map<String, Object>> findAccountsForDropdown() {
        String sql = "SELECT AccountID, FullName FROM Accounts WHERE Role = 4";
        return jdbcTemplate.queryForList(sql);
    }

    // Get vouchers for dropdown
    @Override
    public List<Map<String, Object>> findVouchersForDropdown() {
        String sql = "SELECT VoucherID, Code FROM Vouchers";
        return jdbcTemplate.queryForList(sql);
    }

    // Getter for JdbcTemplate
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    // Setter for JdbcTemplate
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}

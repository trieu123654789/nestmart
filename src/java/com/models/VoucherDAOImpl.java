package com.models;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class VoucherDAOImpl implements VoucherDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public VoucherDAOImpl() {
    }

    public VoucherDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        System.out.println("JdbcTemplate initialized: " + (this.jdbcTemplate != null));
    }

    // Search vouchers by keyword with pagination
    @Override
    public List<Voucher> searchByKeyword(String keyword, int page, int pageSize) {
        if (page < 1 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid page number or page size");
        }

        String query = "SELECT VoucherID, Code, DiscountType, DiscountValue, MinOrderValue, MaxDiscount, "
                + "StartDate, EndDate, UsageLimit, UsedCount, Status "
                + "FROM Vouchers "
                + "WHERE Code LIKE ? "
                + "ORDER BY VoucherID "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        String searchKeyword = "%" + keyword + "%";
        int offset = (page - 1) * pageSize;

        return jdbcTemplate.query(query, new Object[]{searchKeyword, offset, pageSize}, new RowMapper<Voucher>() {
            @Override
            public Voucher mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Voucher(
                        rs.getInt("VoucherID"),
                        rs.getString("Code"),
                        rs.getString("DiscountType"),
                        rs.getBigDecimal("DiscountValue"),
                        rs.getBigDecimal("MinOrderValue"),
                        rs.getBigDecimal("MaxDiscount"),
                        rs.getObject("StartDate", LocalDateTime.class),
                        rs.getObject("EndDate", LocalDateTime.class),
                        rs.getInt("UsageLimit"),
                        rs.getInt("UsedCount"),
                        rs.getBoolean("Status")
                );
            }
        });
    }

    // Check if voucher code already exists
    @Override
    public boolean existsByCode(String code) {
        String sql = "SELECT COUNT(*) FROM Vouchers WHERE Code = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, code);
        return count != null && count > 0;
    }

    // Add a new voucher
    @Override
    public void addVoucher(Voucher voucher) {
        String sql = "INSERT INTO Vouchers "
                + "(Code, DiscountType, DiscountValue, MinOrderValue, MaxDiscount, StartDate, EndDate, UsageLimit, UsedCount, Status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                voucher.getCode(),
                voucher.getDiscountType(),
                voucher.getDiscountValue(),
                voucher.getMinOrderValue(),
                voucher.getMaxDiscount(),
                voucher.getStartDate(),
                voucher.getEndDate(),
                voucher.getUsageLimit(),
                voucher.getUsedCount(),
                voucher.isStatus()
        );
    }

    // Find voucher by ID
    @Override
    public Voucher findById(int voucherID) {
        String sql = "SELECT * FROM Vouchers WHERE VoucherID = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Voucher.class), voucherID);
    }

    // Update an existing voucher
    @Override
    public void update(Voucher voucher) throws Exception {
        String sql = "UPDATE Vouchers SET code = ?, discountType = ?, discountValue = ?, minOrderValue = ?, maxDiscount = ?, startDate = ?, endDate = ?, usageLimit = ?, status = ? WHERE VoucherID = ?";
        jdbcTemplate.update(sql, voucher.getCode(), voucher.getDiscountType(), voucher.getDiscountValue(),
                voucher.getMinOrderValue(), voucher.getMaxDiscount(), voucher.getStartDate(),
                voucher.getEndDate(), voucher.getUsageLimit(), voucher.isStatus(), voucher.getVoucherID());
    }

    // Delete voucher by ID (including relations in AccountVouchers)
    @Override
    public void deleteById(int voucherID) {
        String sqlDeleteAccountVouchers = "DELETE FROM AccountVouchers WHERE VoucherID = ?";
        jdbcTemplate.update(sqlDeleteAccountVouchers, voucherID);

        String sqlDeleteVoucher = "DELETE FROM Vouchers WHERE VoucherID = ?";
        jdbcTemplate.update(sqlDeleteVoucher, voucherID);
    }

    // Check if voucher code exists excluding a specific ID
    @Override
    public boolean existsByCodeExceptId(String code, int voucherID) {
        String sql = "SELECT COUNT(*) FROM Vouchers WHERE code = ? AND VoucherID <> ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, code, voucherID);
        return count != null && count > 0;
    }

    // Get total number of vouchers
    @Override
    public int getTotalVouchers() {
        String query = "SELECT COUNT(*) FROM Vouchers";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    // Find vouchers with pagination
    @Override
    public List<Voucher> findPaginated(int page, int pageSize) {
        if (page < 1 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid page number or page size");
        }

        String query = "SELECT VoucherID, Code, DiscountType, DiscountValue, MinOrderValue, MaxDiscount, "
                + "StartDate, EndDate, UsageLimit, UsedCount, Status "
                + "FROM Vouchers "
                + "ORDER BY VoucherID "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        int offset = (page - 1) * pageSize;

        return jdbcTemplate.query(query, new Object[]{offset, pageSize}, new RowMapper<Voucher>() {
            @Override
            public Voucher mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Voucher(
                        rs.getInt("VoucherID"),
                        rs.getString("Code"),
                        rs.getString("DiscountType"),
                        rs.getBigDecimal("DiscountValue"),
                        rs.getBigDecimal("MinOrderValue"),
                        rs.getBigDecimal("MaxDiscount"),
                        rs.getObject("StartDate", LocalDateTime.class),
                        rs.getObject("EndDate", LocalDateTime.class),
                        rs.getInt("UsageLimit"),
                        rs.getInt("UsedCount"),
                        rs.getBoolean("Status")
                );
            }
        });
    }

    // Save a voucher
    @Override
    public void save(Voucher voucher) {
        String sql = "INSERT INTO Vouchers (Code, DiscountType, DiscountValue, MinOrderValue, MaxDiscount, StartDate, EndDate, UsageLimit, UsedCount, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, voucher.getCode(), voucher.getDiscountType(), voucher.getDiscountValue(),
                voucher.getMinOrderValue(), voucher.getMaxDiscount(), voucher.getStartDate(),
                voucher.getEndDate(), voucher.getUsageLimit(), voucher.getUsedCount(), voucher.isStatus());
    }

    // Find all vouchers
    @Override
    public List<Voucher> findAll() {
        String sql = "SELECT * FROM Vouchers ORDER BY VoucherID";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Voucher.class));
    }

    // Delete voucher by ID
    @Override
    public void delete(int voucherID) {
        String sql = "DELETE FROM Vouchers WHERE VoucherID = ?";
        jdbcTemplate.update(sql, voucherID);
    }

    // Find voucher by code
    @Override
    public Voucher findByCode(String code) {
        try {
            String sql = "SELECT * FROM Vouchers WHERE Code = ?";
            return jdbcTemplate.queryForObject(sql, new Object[]{code}, new BeanPropertyRowMapper<>(Voucher.class));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // Assign voucher to an account
    @Override
    public void assignVoucherToAccount(int accountID, int voucherID) {
        String sql = "INSERT INTO AccountVouchers (AccountID, VoucherID, IsUsed, AssignedDate) VALUES (?, ?, 0, GETDATE())";
        jdbcTemplate.update(sql, accountID, voucherID);
    }

    // Mark voucher as used for an account
    @Override
    public void markVoucherAsUsed(int accountID, int voucherID) {
        String sql = "UPDATE AccountVouchers SET IsUsed = 1 WHERE AccountID = ? AND VoucherID = ?";
        jdbcTemplate.update(sql, accountID, voucherID);
    }

    // Get vouchers of an account
    @Override
    public List<Voucher> getAccountVouchers(int accountID, BigDecimal orderValue) {
        String sql = "SELECT v.*, av.IsUsed, av.AssignedDate, av.ExpiryDate, "
                + "CASE WHEN av.AccountVoucherID IS NULL THEN 'GLOBAL' ELSE 'PERSONAL' END AS VoucherType "
                + "FROM Vouchers v "
                + "LEFT JOIN AccountVouchers av ON v.VoucherID = av.VoucherID AND av.AccountID = ? "
                + "WHERE v.Status = 1 "
                + "AND (av.AccountID = ? OR NOT EXISTS (SELECT 1 FROM AccountVouchers av2 WHERE av2.VoucherID = v.VoucherID)) "
                + "ORDER BY "
                + "CASE WHEN (av.IsUsed = 0 OR av.IsUsed IS NULL) "
                + "AND (av.ExpiryDate IS NULL OR av.ExpiryDate >= GETDATE()) "
                + "AND (v.EndDate >= GETDATE()) "
                + "AND (v.UsageLimit IS NULL OR v.UsedCount < v.UsageLimit) "
                + "AND (v.MinOrderValue IS NULL OR v.MinOrderValue <= ?) "
                + "THEN 0 ELSE 1 END, v.VoucherID";

        return jdbcTemplate.query(sql, new Object[]{accountID, accountID, orderValue}, new VoucherWithStatusRowMapper(orderValue));
    }

    // Get available vouchers for an account
    @Override
    public List<Voucher> getAvailableVouchersForAccount(int accountID, BigDecimal orderValue) {
        String sql = "SELECT v.*, av.IsUsed, av.AssignedDate, av.ExpiryDate "
                + "FROM Vouchers v "
                + "LEFT JOIN AccountVouchers av ON v.VoucherID = av.VoucherID AND av.AccountID = ? "
                + "WHERE v.Status = 1 "
                + "AND (NOT EXISTS (SELECT 1 FROM AccountVouchers av2 WHERE av2.VoucherID = v.VoucherID) OR av.AccountID = ?) "
                + "AND (av.IsUsed = 0 OR av.IsUsed IS NULL) "
                + "AND (av.ExpiryDate IS NULL OR av.ExpiryDate >= GETDATE()) "
                + "AND v.EndDate >= GETDATE() "
                + "AND (v.UsageLimit IS NULL OR v.UsedCount < v.UsageLimit) "
                + "AND (v.MinOrderValue IS NULL OR ? >= v.MinOrderValue) "
                + "ORDER BY v.VoucherID";

        return jdbcTemplate.query(sql, new Object[]{accountID, accountID, orderValue},
                new VoucherWithStatusRowMapper(orderValue));
    }

    // Get unavailable vouchers for an account
    @Override
    public List<Voucher> getUnavailableVouchersForAccount(int accountID, BigDecimal orderValue) {
        String sql = "SELECT v.*, av.IsUsed, av.AssignedDate, av.ExpiryDate, "
                + "CASE WHEN av.AccountVoucherID IS NULL THEN 'GLOBAL' ELSE 'PERSONAL' END AS VoucherType "
                + "FROM Vouchers v "
                + "LEFT JOIN AccountVouchers av ON v.VoucherID = av.VoucherID AND av.AccountID = ? "
                + "WHERE (av.AccountID = ? OR NOT EXISTS (SELECT 1 FROM AccountVouchers av2 WHERE av2.VoucherID = v.VoucherID)) "
                + "AND (v.Status = 0 OR av.IsUsed = 1 OR (av.ExpiryDate IS NOT NULL AND av.ExpiryDate < GETDATE()) OR v.EndDate < GETDATE() OR (v.UsageLimit IS NOT NULL AND v.UsedCount >= v.UsageLimit) OR (v.MinOrderValue IS NOT NULL AND ? < v.MinOrderValue)) "
                + "ORDER BY v.VoucherID";

        return jdbcTemplate.query(sql, new Object[]{accountID, accountID, orderValue},
                new VoucherWithStatusRowMapper(orderValue));
    }

    // Check if voucher can be used by account with order value
    @Override
    public boolean canUseVoucher(int accountID, int voucherID, BigDecimal orderValue) {
        String sql = "SELECT COUNT(*) FROM Vouchers v "
                + "LEFT JOIN AccountVouchers av ON v.VoucherID = av.VoucherID AND av.AccountID = ? "
                + "WHERE v.VoucherID = ? "
                + "AND (av.AccountID = ? OR NOT EXISTS (SELECT 1 FROM AccountVouchers av2 WHERE av2.VoucherID = v.VoucherID)) "
                + "AND v.Status = 1 "
                + "AND (av.IsUsed = 0 OR av.IsUsed IS NULL) "
                + "AND (av.ExpiryDate IS NULL OR av.ExpiryDate >= GETDATE()) "
                + "AND (v.EndDate >= GETDATE()) "
                + "AND (v.UsageLimit IS NULL OR v.UsedCount < v.UsageLimit) "
                + "AND (v.MinOrderValue IS NULL OR v.MinOrderValue <= ?)";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, accountID, voucherID, accountID, orderValue);
        return count != null && count > 0;
    }

    // Validate and return voucher by code
    @Override
    public Voucher validateAndGetVoucher(int accountID, String voucherCode, BigDecimal orderValue) {
        String sql = "SELECT v.*, av.IsUsed, av.AssignedDate, av.ExpiryDate, "
                + "CASE WHEN av.AccountVoucherID IS NULL THEN 'GLOBAL' ELSE 'PERSONAL' END AS VoucherType "
                + "FROM Vouchers v "
                + "LEFT JOIN AccountVouchers av ON v.VoucherID = av.VoucherID AND av.AccountID = ? "
                + "WHERE v.Code = ? "
                + "AND (av.AccountID = ? OR NOT EXISTS (SELECT 1 FROM AccountVouchers av2 WHERE av2.VoucherID = v.VoucherID)) "
                + "AND v.Status = 1 "
                + "AND (av.IsUsed = 0 OR av.IsUsed IS NULL) "
                + "AND (av.ExpiryDate IS NULL OR av.ExpiryDate >= GETDATE()) "
                + "AND (v.EndDate >= GETDATE()) "
                + "AND (v.UsageLimit IS NULL OR v.UsedCount < v.UsageLimit) "
                + "AND (v.MinOrderValue IS NULL OR v.MinOrderValue <= ?)";

        List<Voucher> vouchers = jdbcTemplate.query(sql, new Object[]{accountID, voucherCode, accountID, orderValue},
                new VoucherWithStatusRowMapper(orderValue));
        return vouchers.isEmpty() ? null : vouchers.get(0);
    }

    // Increment voucher usage count
    @Override
    public void incrementUsageCount(int voucherID) {
        String sql = "UPDATE Vouchers SET UsedCount = UsedCount + 1 WHERE VoucherID = ?";
        jdbcTemplate.update(sql, voucherID);
    }

    // Get voucher used count
    @Override
    public int getVoucherUsageCount(int voucherID) {
        String sql = "SELECT UsedCount FROM Vouchers WHERE VoucherID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{voucherID}, Integer.class);
    }

    // Check if account already has voucher
    @Override
    public boolean accountHasVoucher(int accountID, int voucherID) {
        try {
            String sql = "SELECT COUNT(*) FROM AccountVouchers WHERE AccountID = ? AND VoucherID = ?";
            int count = jdbcTemplate.queryForObject(sql, new Object[]{accountID, voucherID}, Integer.class);
            return count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // Mark voucher as used with order ID
    @Override
    public boolean markVoucherAsUsed(Integer accountId, int voucherCode, String orderId) {
        try {
            String checkSql = "SELECT COUNT(*) FROM AccountVouchers WHERE AccountID = ? AND VoucherID = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, accountId, voucherCode);

            if (count != null && count > 0) {
                String sql = "UPDATE AccountVouchers SET IsUsed = 1, OrderID = ? WHERE AccountID = ? AND VoucherID = ? AND IsUsed = 0";
                return jdbcTemplate.update(sql, orderId, accountId, voucherCode) > 0;
            } else {
                String insertSql = "INSERT INTO AccountVouchers (AccountID, VoucherID, IsUsed, AssignedDate, ExpiryDate, OrderID) VALUES (?, ?, 1, GETDATE(), NULL, ?)";
                return jdbcTemplate.update(insertSql, accountId, voucherCode, orderId) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Increment used count of voucher by code
    @Override
    public boolean incrementVoucherUsedCount(String voucherCode) {
        String sql = "UPDATE Vouchers SET UsedCount = UsedCount + 1 WHERE Code = ?";

        try {
            int rowsAffected = jdbcTemplate.update(sql, voucherCode);
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if account can use voucher by code
    @Override
    public boolean canUseVoucher(Integer accountId, String voucherCode) {
        String sql = "SELECT COUNT(*) FROM Vouchers v "
                + "LEFT JOIN AccountVouchers av ON v.VoucherID = av.VoucherID AND av.AccountID = ? "
                + "WHERE v.Code = ? "
                + "AND (av.AccountID = ? OR NOT EXISTS (SELECT 1 FROM AccountVouchers av2 WHERE av2.VoucherID = v.VoucherID)) "
                + "AND v.Status = 1 "
                + "AND (av.IsUsed = 0 OR av.IsUsed IS NULL) "
                + "AND (av.ExpiryDate IS NULL OR av.ExpiryDate >= GETDATE()) "
                + "AND (v.EndDate >= GETDATE()) "
                + "AND (v.UsageLimit IS NULL OR v.UsedCount < v.UsageLimit)";

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, accountId, voucherCode, accountId);
        return count != null && count > 0;
    }

    // Get account voucher by voucher code
    @Override
    public AccountVoucher getAccountVoucher(Integer accountId, String voucherCode) {
        String sql = "SELECT av.AccountVoucherID, av.AccountID, av.VoucherID, av.IsUsed, av.AssignedDate, av.ExpiryDate "
                + "FROM Vouchers v "
                + "LEFT JOIN AccountVouchers av ON v.VoucherID = av.VoucherID AND av.AccountID = ? "
                + "WHERE v.Code = ? AND (av.AccountID = ? OR NOT EXISTS (SELECT 1 FROM AccountVouchers av2 WHERE av2.VoucherID = v.VoucherID))";

        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(AccountVoucher.class),
                    accountId, voucherCode, accountId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Custom RowMapper with voucher validation logic
    private class VoucherWithStatusRowMapper implements RowMapper<Voucher> {

        private final BigDecimal orderValue;

        public VoucherWithStatusRowMapper(BigDecimal orderValue) {
            this.orderValue = orderValue;
        }

        @Override
        public Voucher mapRow(ResultSet rs, int rowNum) throws SQLException {
            Voucher voucher = new Voucher();
            voucher.setVoucherID(rs.getInt("VoucherID"));
            voucher.setCode(rs.getString("Code"));
            voucher.setDiscountType(rs.getString("DiscountType"));
            voucher.setDiscountValue(rs.getBigDecimal("DiscountValue"));
            voucher.setMinOrderValue(rs.getBigDecimal("MinOrderValue"));
            voucher.setMaxDiscount(rs.getBigDecimal("MaxDiscount"));
            voucher.setStartDate(rs.getObject("StartDate", LocalDateTime.class));
            voucher.setEndDate(rs.getObject("EndDate", LocalDateTime.class));
            voucher.setUsageLimit((Integer) rs.getObject("UsageLimit"));
            voucher.setUsedCount(rs.getInt("UsedCount"));

            Boolean status = (Boolean) rs.getObject("Status");
            voucher.setStatus(status != null && status);

            Boolean isUsed = (Boolean) rs.getObject("IsUsed");
            voucher.setIsUsed(isUsed != null && isUsed);

            voucher.setAssignedDate(rs.getDate("AssignedDate"));
            voucher.setExpiryDate(rs.getDate("ExpiryDate"));

            boolean canUse = true;
            StringBuilder reasons = new StringBuilder();

            if (!voucher.isStatus()) {
                canUse = false;
                reasons.append("Voucher is inactive. ");
            }
            if (voucher.isUsed()) {
                canUse = false;
                reasons.append("Voucher already used. ");
            }
            LocalDateTime currentDate = LocalDateTime.now();

            if (voucher.getEndDate() != null && voucher.getEndDate().isBefore(currentDate)) {
                canUse = false;
                reasons.append("Voucher has expired. ");
            }
            if (voucher.getUsageLimit() != null && voucher.getUsedCount() >= voucher.getUsageLimit()) {
                canUse = false;
                reasons.append("Usage limit reached. ");
            }
            if (voucher.getMinOrderValue() != null && orderValue != null && orderValue.compareTo(voucher.getMinOrderValue()) < 0) {
                canUse = false;
                reasons.append("Minimum order value not met (requires $" + voucher.getMinOrderValue() + "). ");
            }

            voucher.setCanUse(canUse);
            voucher.setReasonCannotUse(reasons.toString().trim());
            return voucher;
        }
    }
}

package com.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class BrandsDAOImpl implements BrandsDAO {

    private JdbcTemplate jdbcTemplate;

    public BrandsDAOImpl() {
    }

    public BrandsDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        System.out.println("JdbcTemplate initialized: " + (this.jdbcTemplate != null));
    }

    // Get all brands
    @Override
    public List<Brands> findAll() {
        String query = "SELECT b.BrandID, b.BrandName, b.Description, "
                + "CASE WHEN EXISTS (SELECT 1 FROM Products p WHERE p.BrandID = b.BrandID) "
                + "THEN 1 ELSE 0 END as HasProducts "
                + "FROM Brands b";

        List<Brands> brandList = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

        for (Map row : rows) {
            Brands b = new Brands(
                    (int) row.get("BrandID"),
                    (String) row.get("BrandName"),
                    (String) row.get("Description")
            );
            b.setHasProducts(((Number) row.get("HasProducts")).intValue() == 1);
            brandList.add(b);
        }
        return brandList;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Save new brand
    @Override
    public void save(Brands brand) {
        String query = "INSERT INTO Brands (BrandName, Description) VALUES (?, ?)";
        jdbcTemplate.update(query, brand.getBrandName(), brand.getDescription());
    }

    // Update brand by ID
    @Override
    public void update(Brands brand) {
        System.out.println("Executing update for brand: " + brand.getBrandID());
        String query = "UPDATE Brands SET BrandName = ?, Description = ? WHERE BrandID = ?";
        int rowsAffected = jdbcTemplate.update(query, brand.getBrandName(), brand.getDescription(), brand.getBrandID());
        System.out.println("Rows affected: " + rowsAffected);
    }

    // Delete brand by ID (with product check)
    @Override
    public void deleteById(int id) {
        String checkProductsQuery = "SELECT COUNT(*) FROM Products WHERE BrandID = ?";
        int productCount = jdbcTemplate.queryForObject(checkProductsQuery, Integer.class, id);

        if (productCount > 0) {
            throw new IllegalStateException("Cannot delete brand with ID " + id + " because it has associated products");
        }

        String deleteBrandQuery = "DELETE FROM Brands WHERE BrandID = ?";
        int brandDeleted = jdbcTemplate.update(deleteBrandQuery, id);

        if (brandDeleted == 0) {
            throw new IllegalStateException("Brand with ID " + id + " not found");
        }

        System.out.println("Brand deleted successfully: " + brandDeleted);
    }

    // Find brand by ID
    @Override
    public Brands findById(int id) {
        String query = "SELECT BrandID, BrandName, Description FROM Brands WHERE BrandID = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, new RowMapper<Brands>() {
            @Override
            public Brands mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Brands(
                        rs.getInt("BrandID"),
                        rs.getString("BrandName"),
                        rs.getString("Description")
                );
            }
        });
    }

    // Get brand name by ID
    @Override
    public String getBrandNameById(int id) {
        String query = "SELECT BrandName FROM Brands WHERE BrandID = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, String.class);
    }

    // Get total brand count
    @Override
    public int getTotalBrands() {
        String query = "SELECT COUNT(*) FROM Brands";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    // Get paginated brands
    @Override
    public List<Brands> findPaginated(int page, int pageSize) {
        String query = "SELECT b.BrandID, b.BrandName, b.Description, "
                + "CASE WHEN EXISTS (SELECT 1 FROM Products p WHERE p.BrandID = b.BrandID) "
                + "THEN 1 ELSE 0 END as HasProducts "
                + "FROM Brands b "
                + "ORDER BY b.BrandID "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        int offset = (page - 1) * pageSize;

        return jdbcTemplate.query(query, new Object[]{offset, pageSize}, (rs, rowNum) -> {
            Brands brand = new Brands(
                    rs.getInt("BrandID"),
                    rs.getString("BrandName"),
                    rs.getString("Description")
            );
            brand.setHasProducts(rs.getInt("HasProducts") == 1);
            return brand;
        });
    }

    // Search brands by keyword
    @Override
    public List<Brands> searchByKeyword(String keyword) {
        String query = "SELECT BrandID, BrandName, Description "
                + "FROM Brands "
                + "WHERE BrandName LIKE ?";

        return jdbcTemplate.query(
                query,
                new Object[]{"%" + keyword + "%"},
                (rs, rowNum) -> new Brands(
                        rs.getInt("BrandID"),
                        rs.getString("BrandName"),
                        rs.getString("Description")
                )
        );
    }
}

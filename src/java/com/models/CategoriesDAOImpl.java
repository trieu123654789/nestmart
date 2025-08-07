package com.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class CategoriesDAOImpl implements CategoriesDAO {

    private JdbcTemplate jdbcTemplate;

    public CategoriesDAOImpl() {
    }

    public CategoriesDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        System.out.println("JdbcTemplate initialized: " + (this.jdbcTemplate != null));
    }

    // Get all categories with HasProducts flag
    @Override
    public List<Categories> findAll() {
        String query = "SELECT c.CategoryID, c.CategoryName, c.Description, " +
                       "CASE WHEN EXISTS (SELECT 1 FROM Products p WHERE p.CategoryID = c.CategoryID) " +
                       "THEN 1 ELSE 0 END as HasProducts " +
                       "FROM Categories c";
        return jdbcTemplate.query(query, new RowMapper<Categories>() {
            @Override
            public Categories mapRow(ResultSet rs, int rowNum) throws SQLException {
                Categories category = new Categories(
                    rs.getInt("CategoryID"),
                    rs.getString("CategoryName"),
                    rs.getString("Description")
                );
                category.setHasProducts(rs.getInt("HasProducts") == 1);
                return category;
            }
        });
    }

    // Get categories that have products
    @Override
    public List<Categories> findCategoriesWithProducts() {
        String query = "SELECT c.CategoryID, c.CategoryName, c.Description " +
                       "FROM Categories c " +
                       "WHERE EXISTS (SELECT 1 FROM Products p WHERE p.CategoryID = c.CategoryID)";

        return jdbcTemplate.query(query, new RowMapper<Categories>() {
            @Override
            public Categories mapRow(ResultSet rs, int rowNum) throws SQLException {
                Categories category = new Categories(
                    rs.getInt("CategoryID"),
                    rs.getString("CategoryName"),
                    rs.getString("Description")
                );
                category.setHasProducts(true);
                return category;
            }
        });
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Save new category
    @Override
    public void save(Categories category) {
        String query = "INSERT INTO Categories (CategoryName, Description) VALUES (?, ?)";
        jdbcTemplate.update(query, category.getCategoryName(), category.getDescription());
    }

    // Update category by ID
    @Override
    public void update(Categories category) {
        System.out.println("Executing update for category: " + category.getCategoryID());
        String query = "UPDATE Categories SET CategoryName = ?, Description = ? WHERE CategoryID = ?";
        int rowsAffected = jdbcTemplate.update(query, category.getCategoryName(), category.getDescription(), category.getCategoryID());
        System.out.println("Rows affected: " + rowsAffected);
    }

    // Delete category by ID (with product check)
    @Override
    public void deleteById(int id) {
        String checkProductsQuery = "SELECT COUNT(*) FROM Products WHERE CategoryID = ?";
        int productCount = jdbcTemplate.queryForObject(checkProductsQuery, Integer.class, id);

        if (productCount > 0) {
            throw new IllegalStateException("Cannot delete category with ID " + id + " because it has associated products");
        }

        String deleteCategoryQuery = "DELETE FROM Categories WHERE CategoryID = ?";
        int categoryDeleted = jdbcTemplate.update(deleteCategoryQuery, id);

        if (categoryDeleted == 0) {
            throw new IllegalStateException("Category with ID " + id + " not found");
        }
    }

    // Find category by ID
    @Override
    public Categories findById(int id) {
        String query = "SELECT CategoryID, CategoryName, Description FROM Categories WHERE CategoryID = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, new RowMapper<Categories>() {
            @Override
            public Categories mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Categories(
                        rs.getInt("CategoryID"),
                        rs.getString("CategoryName"),
                        rs.getString("Description")
                );
            }
        });
    }

    // Get category name by ID
    @Override
    public String getCategoryNameById(int id) {
        String query = "SELECT CategoryName FROM Categories WHERE CategoryID = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, String.class);
    }

    // Get multiple category names by list of IDs
    @Override
    public Map<Integer, String> getCategoryNames(List<Integer> categoryIds) {
        if (categoryIds.isEmpty()) {
            return Collections.emptyMap();
        }

        String placeholders = String.join(",", Collections.nCopies(categoryIds.size(), "?"));
        String query = "SELECT CategoryID, CategoryName FROM Categories WHERE CategoryID IN (" + placeholders + ")";

        return jdbcTemplate.query(query, categoryIds.toArray(), rs -> {
            Map<Integer, String> categoryNames = new HashMap<>();
            while (rs.next()) {
                categoryNames.put(rs.getInt("CategoryID"), rs.getString("CategoryName"));
            }
            return categoryNames;
        });
    }

    // Get paginated categories
    @Override
    public List<Categories> findPaginated(int page, int pageSize) {
        String query = "SELECT c.CategoryID, c.CategoryName, c.Description, " +
                       "CASE WHEN EXISTS (SELECT 1 FROM Products p WHERE p.CategoryID = c.CategoryID) " +
                       "THEN 1 ELSE 0 END as HasProducts " +
                       "FROM Categories c " +
                       "ORDER BY c.CategoryID " +
                       "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        int offset = (page - 1) * pageSize;

        return jdbcTemplate.query(query, new Object[]{offset, pageSize}, new RowMapper<Categories>() {
            @Override
            public Categories mapRow(ResultSet rs, int rowNum) throws SQLException {
                Categories category = new Categories(
                    rs.getInt("CategoryID"),
                    rs.getString("CategoryName"),
                    rs.getString("Description")
                );
                category.setHasProducts(rs.getInt("HasProducts") == 1);
                return category;
            }
        });
    }

    // Search categories by keyword
    @Override
    public List<Categories> searchByKeyword(String keyword) {
        String query = "SELECT DISTINCT c.CategoryID, c.CategoryName, c.Description " +
                       "FROM Categories c " +
                       "JOIN Products p ON p.CategoryID = c.CategoryID " +
                       "JOIN CategoryDetails cd ON cd.ProductID = p.ProductID " +
                       "WHERE p.ProductName COLLATE Latin1_General_CI_AI LIKE ? " +
                       "   OR cd.AttributeName COLLATE Latin1_General_CI_AI LIKE ? " +
                       "   OR cd.AttributeValue COLLATE Latin1_General_CI_AI LIKE ? " +
                       "   OR CAST(p.ProductID AS VARCHAR) LIKE ?";

        String kw = "%" + keyword + "%";

        return jdbcTemplate.query(query, new Object[]{kw, kw, kw, kw}, 
            (rs, rowNum) -> new Categories(
                rs.getInt("CategoryID"),
                rs.getString("CategoryName"),
                rs.getString("Description")
            ));
    }

    // Get total categories count
    @Override
    public int getTotalCategories() {
        String query = "SELECT COUNT(*) FROM Categories";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }
}

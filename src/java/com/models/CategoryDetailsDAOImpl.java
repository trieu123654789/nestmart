package com.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class CategoryDetailsDAOImpl implements CategoryDetailsDAO {

    private JdbcTemplate jdbcTemplate;

    public CategoryDetailsDAOImpl() {
    }

    public CategoryDetailsDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        System.out.println("JdbcTemplate initialized: " + (this.jdbcTemplate != null));
    }

    // Retrieves all category details with joined product and category information
    @Override
    public List<CategoryDetails> findAll() {
        String query = "SELECT c.CategoryDetailID, c.CategoryID, c.ProductID, c.AttributeName, c.AttributeValue, p.ProductName, ca.CategoryName FROM CategoryDetails c join Products p on c.productID = p.productID join Categories ca on c.categoryID = ca.categoryID ";
        List<CategoryDetails> categorydetailList = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

        for (Map<String, Object> row : rows) {
            CategoryDetails p = new CategoryDetails(
                    (Integer) row.get("CategoryDetailID"),
                    (Integer) row.get("CategoryID"),
                    (String) row.get("ProductID"),
                    (String) row.get("AttributeName"),
                    (String) row.get("AttributeValue"),
                    (String) row.get("ProductName"),
                    (String) row.get("CategoryName"),
                    0,
                    new ArrayList<>()
            );
            categorydetailList.add(p);
        }
        return categorydetailList;
    }

    // Retrieves a category detail by its ID
    @Override
    public CategoryDetails findById(int categoryDetailID) {
        String query = "SELECT c.CategoryDetailID, c.CategoryID, c.ProductID, c.AttributeName, c.AttributeValue, "
                + "p.ProductName, ca.CategoryName "
                + "FROM CategoryDetails c "
                + "JOIN Products p ON c.ProductID = p.ProductID "
                + "JOIN Categories ca ON c.CategoryID = ca.CategoryID "
                + "WHERE c.CategoryDetailID = ?";

        return jdbcTemplate.queryForObject(query, new Object[]{categoryDetailID}, new RowMapper<CategoryDetails>() {
            @Override
            public CategoryDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new CategoryDetails(
                        rs.getInt("CategoryDetailID"),
                        rs.getInt("CategoryID"),
                        rs.getString("ProductID"),
                        rs.getString("AttributeName"),
                        rs.getString("AttributeValue"),
                        rs.getString("ProductName"),
                        rs.getString("CategoryName"),
                        0,
                        new ArrayList<>()
                );
            }
        });
    }

    // Deletes a category detail by its ID
    @Override
    public void deleteById(int categoryDetailID) {
        String query = "DELETE FROM CategoryDetails WHERE CategoryDetailID = ?";
        jdbcTemplate.update(query, categoryDetailID);
    }

    // Retrieves category details by category ID
    @Override
    public List<CategoryDetails> findByCategoryId(int categoryId) {
        String query = "SELECT CD.CategoryDetailID, CD.CategoryID, CD.ProductID, CD.AttributeName, CD.AttributeValue "
                + "FROM CategoryDetails CD "
                + "WHERE CD.CategoryID = ?";
        return jdbcTemplate.query(query, new Object[]{categoryId}, new BeanPropertyRowMapper<>(CategoryDetails.class));
    }

    // Saves a new category detail to the database
    @Override
    public void save(CategoryDetails categoryDetails) {
        String query = "INSERT INTO CategoryDetails (CategoryID, ProductID, AttributeName, AttributeValue) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(query, categoryDetails.getCategoryID(), categoryDetails.getProductID(),
                categoryDetails.getAttributeName(), categoryDetails.getAttributeValue());
    }

    // Updates an existing category detail
    @Override
    public void update(CategoryDetails categoryDetails) {
        String query = "UPDATE CategoryDetails SET CategoryID = ?, ProductID = ?, AttributeName = ?, AttributeValue = ? WHERE CategoryDetailID = ?";
        jdbcTemplate.update(query,
                categoryDetails.getCategoryID(),
                categoryDetails.getProductID(),
                categoryDetails.getAttributeName(),
                categoryDetails.getAttributeValue(),
                categoryDetails.getCategoryDetailID());
    }

    // Retrieves products by category ID
    @Override
    public List<Products> findProductsByCategoryId(int categoryId) {
        String query = "SELECT ProductID, ProductName FROM Products WHERE CategoryID = ?";
        return jdbcTemplate.query(query, new Object[]{categoryId}, new RowMapper<Products>() {
            @Override
            public Products mapRow(ResultSet rs, int rowNum) throws SQLException {
                Products product = new Products();
                product.setProductID(rs.getString("ProductID"));
                product.setProductName(rs.getString("ProductName"));
                return product;
            }
        });
    }

    // Returns the total number of category details
    @Override
    public int getTotalCategoryDetails() {
        String query = "SELECT COUNT(*) FROM CategoryDetails";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    // Retrieves paginated category details
    @Override
    public List<CategoryDetails> findPaginated(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        String query = "SELECT c.CategoryDetailID, c.CategoryID, c.ProductID, c.AttributeName, c.AttributeValue, "
                + "p.ProductName, ca.CategoryName "
                + "FROM CategoryDetails c "
                + "JOIN Products p ON c.ProductID = p.ProductID "
                + "JOIN Categories ca ON c.CategoryID = ca.CategoryID "
                + "ORDER BY c.CategoryDetailID "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        return jdbcTemplate.query(query, new Object[]{offset, pageSize}, new RowMapper<CategoryDetails>() {
            @Override
            public CategoryDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new CategoryDetails(
                        rs.getInt("CategoryDetailID"),
                        rs.getInt("CategoryID"),
                        rs.getString("ProductID"),
                        rs.getString("AttributeName"),
                        rs.getString("AttributeValue"),
                        rs.getString("ProductName"),
                        rs.getString("CategoryName"),
                        0,
                        new ArrayList<>()
                );
            }
        });
    }

    // Searches category details using a keyword across multiple fields
    @Override
    public List<CategoryDetails> searchByKeyword(String keyword) {
        String query = "SELECT CD.CategoryDetailID, CD.CategoryID, CD.ProductID, CD.AttributeName, CD.AttributeValue, "
                + "P.ProductName, CA.CategoryName "
                + "FROM CategoryDetails CD "
                + "JOIN Products P ON CD.ProductID = P.ProductID "
                + "JOIN Categories CA ON CD.CategoryID = CA.CategoryID "
                + "WHERE CAST(CD.ProductID AS VARCHAR(10)) LIKE ? "
                + "   OR CAST(CD.CategoryID AS VARCHAR(10)) LIKE ? "
                + "   OR P.ProductName COLLATE Latin1_General_CI_AI LIKE ? "
                + "   OR CD.AttributeName COLLATE Latin1_General_CI_AI LIKE ? "
                + "   OR CD.AttributeValue COLLATE Latin1_General_CI_AI LIKE ?";

        String kw = "%" + keyword.trim() + "%";
        return jdbcTemplate.query(query, new Object[]{kw, kw, kw, kw, kw}, (rs, rowNum) -> {
            return new CategoryDetails(
                    rs.getInt("CategoryDetailID"),
                    rs.getInt("CategoryID"),
                    rs.getString("ProductID"),
                    rs.getString("AttributeName"),
                    rs.getString("AttributeValue"),
                    rs.getString("ProductName"),
                    rs.getString("CategoryName"),
                    0,
                    new ArrayList<>()
            );
        });
    }

    // Retrieves attribute details by product ID
    @Override
    public List<AttributeDetail> findByProductId(String productId) {
        String query = "SELECT c.AttributeName, c.AttributeValue FROM CategoryDetails c JOIN Products p ON c.ProductID = p.ProductID WHERE p.ProductID = ?";

        return jdbcTemplate.query(query, new Object[]{productId}, new RowMapper<AttributeDetail>() {
            @Override
            public AttributeDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new AttributeDetail(
                        rs.getString("AttributeName"),
                        rs.getString("AttributeValue")
                );
            }
        });
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}

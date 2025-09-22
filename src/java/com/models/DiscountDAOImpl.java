package com.models;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.annotation.MultipartConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
@MultipartConfig
public class DiscountDAOImpl implements DiscountDAO {

    private JdbcTemplate jdbcTemplate;

    public DiscountDAOImpl() {
    }

    @Autowired
    public DiscountDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Retrieves all discounts from the database
    @Override
    public List<Discount> findAll() {
        String query = "SELECT DiscountID, DiscountName, Description, StartDate, EndDate, Image FROM Discounts";
        List<Discount> discountList = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

        for (Map<String, Object> row : rows) {
            Discount discount = new Discount(
                    (int) row.get("DiscountID"),
                    (String) row.get("DiscountName"),
                    (String) row.get("Description"),
                    ((Timestamp) row.get("StartDate")).toLocalDateTime(),
                    ((Timestamp) row.get("EndDate")).toLocalDateTime(),
                    (String) row.get("Image")
            );
            discountList.add(discount);
        }
        return discountList;
    }

    // Saves a new discount including file upload for the image
    @Override
    public void save(Discount discount, MultipartFile imageFile, ServletContext servletContext) {
        String fileName = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                fileName = Paths.get(imageFile.getOriginalFilename()).getFileName().toString();
                // Fix path resolution for Docker container
                String webAppPath = servletContext.getRealPath("");
                String uploadPath;
                if (webAppPath != null) {
                    // Running in container or deployed environment
                    uploadPath = webAppPath + "/assets/admin/images/uploads/discount/";
                } else {
                    // Fallback for development
                    String relativePath = "/web/assets/admin/images/uploads/discount/";
                    uploadPath = new File(servletContext.getRealPath("")).getParentFile().getParentFile().getAbsolutePath() + relativePath;
                }
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                String filePath = Paths.get(uploadPath, fileName).toString();
                imageFile.transferTo(new File(filePath));
            } catch (IOException e) {
                throw new RuntimeException("Error when saving file: " + e.getMessage());
            }
        }

        String query = "INSERT INTO Discounts (DiscountName, Description, StartDate, EndDate, Image) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(query,
                discount.getDiscountName(),
                discount.getDescription(),
                discount.getStartDate() != null ? Timestamp.valueOf(discount.getStartDate()) : null,
                discount.getEndDate() != null ? Timestamp.valueOf(discount.getEndDate()) : null,
                fileName
        );
    }

    // Updates an existing discount including image file replacement if provided
    @Override
    public void update(Discount discount, MultipartFile imageFile, ServletContext servletContext) {
        if (discount.getDiscountName() == null || discount.getDiscountName().isEmpty()) {
            throw new RuntimeException("DiscountName cannot be null");
        }

        String currentImageName = getCurrentImageName(discount.getDiscountID());
        String fileName = currentImageName;

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                fileName = Paths.get(imageFile.getOriginalFilename()).getFileName().toString();
                // Fix path resolution for Docker container
                String webAppPath = servletContext.getRealPath("");
                String uploadPath;
                if (webAppPath != null) {
                    // Running in container or deployed environment
                    uploadPath = webAppPath + "/assets/admin/images/uploads/discount/";
                } else {
                    // Fallback for development
                    String relativePath = "/web/assets/admin/images/uploads/discount/";
                    uploadPath = new File(servletContext.getRealPath("")).getParentFile().getParentFile().getAbsolutePath() + relativePath;
                }
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }
                String filePath = Paths.get(uploadPath, fileName).toString();

                File oldFile = new File(Paths.get(uploadPath, currentImageName).toString());
                if (oldFile.exists() && !oldFile.getName().equals(fileName)) {
                    oldFile.delete();
                }

                imageFile.transferTo(new File(filePath));
            } catch (IOException e) {
                throw new RuntimeException("Error when saving file: " + e.getMessage());
            }
        }

        String query = "UPDATE Discounts SET DiscountName = ?, Description = ?, StartDate = ?, EndDate = ?, Image = ? WHERE DiscountID = ?";
        jdbcTemplate.update(query,
                discount.getDiscountName(),
                discount.getDescription(),
                discount.getStartDate() != null ? Timestamp.valueOf(discount.getStartDate()) : null,
                discount.getEndDate() != null ? Timestamp.valueOf(discount.getEndDate()) : null,
                fileName,
                discount.getDiscountID()
        );
    }

    // Retrieves the image name of the current discount
    private String getCurrentImageName(int discountID) {
        String query = "SELECT Image FROM Discounts WHERE DiscountID = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{discountID}, String.class);
    }

    // Updates discounted prices for products linked to a discount
    private void updateProductPrices(int discountID, int discountValue) {
        String productQuery = "SELECT p.ProductID, p.UnitPrice FROM Products p JOIN Offers o ON p.ProductID = o.ProductID WHERE o.DiscountID = ?";
        List<Map<String, Object>> products = jdbcTemplate.queryForList(productQuery, discountID);

        BigDecimal discountPercentage = new BigDecimal(discountValue).divide(new BigDecimal(100));

        for (Map<String, Object> product : products) {
            String productId = (String) product.get("ProductID");
            BigDecimal unitPrice = (BigDecimal) product.get("UnitPrice");
            BigDecimal discountedPrice = unitPrice.multiply(discountPercentage);

            String updatePriceQuery = "UPDATE Products SET Discount = ? WHERE ProductID = ?";
            jdbcTemplate.update(updatePriceQuery, discountedPrice, productId);
        }
    }

    // Deletes a discount and resets any linked product discounts
    @Override
    public void deleteById(int discountId) {
        String updateProductsQuery = "UPDATE Products SET Discount = 0 WHERE ProductID IN (SELECT ProductID FROM Offers WHERE DiscountID = ?)";
        jdbcTemplate.update(updateProductsQuery, discountId);

        String deleteOffersQuery = "DELETE FROM Offers WHERE DiscountID = ?";
        jdbcTemplate.update(deleteOffersQuery, discountId);

        String deleteDiscountQuery = "DELETE FROM Discounts WHERE DiscountID = ?";
        jdbcTemplate.update(deleteDiscountQuery, discountId);
    }

    // Retrieves a discount by its ID
    @Override
    public Discount findById(int id) {
        String query = "SELECT * FROM Discounts WHERE DiscountID = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{id}, new RowMapper<Discount>() {
            @Override
            public Discount mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Discount(
                        rs.getInt("DiscountID"),
                        rs.getString("DiscountName"),
                        rs.getString("Description"),
                        rs.getObject("StartDate", LocalDateTime.class),
                        rs.getObject("EndDate", LocalDateTime.class),
                        rs.getString("Image")
                );
            }
        });
    }

    // Retrieves paginated discounts
    @Override
    public List<Discount> findPaginated(int page, int pageSize) {
        if (page < 1 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid page number or page size");
        }

        String query = "SELECT DiscountID, DiscountName, Description, StartDate, EndDate, Image "
                + "FROM Discounts "
                + "ORDER BY DiscountID "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        int offset = (page - 1) * pageSize;

        return jdbcTemplate.query(query, new Object[]{offset, pageSize}, new RowMapper<Discount>() {
            @Override
            public Discount mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Discount(
                        rs.getInt("DiscountID"),
                        rs.getString("DiscountName"),
                        rs.getString("Description"),
                        rs.getObject("StartDate", LocalDateTime.class),
                        rs.getObject("EndDate", LocalDateTime.class),
                        rs.getString("Image")
                );
            }
        });
    }

    // Searches discounts by keyword with pagination
    @Override
    public List<Discount> searchByKeyword(String keyword, int page, int pageSize) {
        if (page < 1 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid page number or page size");
        }

        String query = "SELECT DiscountID, DiscountName, Description, StartDate, EndDate, Image "
                + "FROM Discounts "
                + "WHERE DiscountName LIKE ? "
                + "ORDER BY DiscountID "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        String searchKeyword = "%" + keyword + "%";
        int offset = (page - 1) * pageSize;

        return jdbcTemplate.query(query, new Object[]{searchKeyword, offset, pageSize}, new RowMapper<Discount>() {
            @Override
            public Discount mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Discount(
                        rs.getInt("DiscountID"),
                        rs.getString("DiscountName"),
                        rs.getString("Description"),
                        rs.getObject("StartDate", LocalDateTime.class),
                        rs.getObject("EndDate", LocalDateTime.class),
                        rs.getString("Image")
                );
            }
        });
    }

    // Counts total discounts matching a keyword
    @Override
    public int countByKeyword(String keyword) {
        String query = "SELECT COUNT(*) FROM Discounts WHERE DiscountName LIKE ?";
        return jdbcTemplate.queryForObject(query, new Object[]{"%" + keyword + "%"}, Integer.class);
    }

    // Returns total number of discounts
    @Override
    public int getTotalDiscounts() {
        String query = "SELECT COUNT(*) FROM Discounts";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}

package com.models;

import java.math.BigDecimal;
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

public class ProductsClientDAOImpl implements ProductsClientDAO {

    private JdbcTemplate jdbcTemplate;

    public ProductsClientDAOImpl() {
    }

    public ProductsClientDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        System.out.println("JdbcTemplate initialized: " + (this.jdbcTemplate != null));
    }

    // Retrieves first 8 products with ratings and sales data, out-of-stock items last
    @Override
    public List<ProductsClient> findAll() {
        String query = "SELECT \n"
                + "    p.ProductID, \n"
                + "    p.CategoryID, \n"
                + "    p.BrandID, \n"
                + "    p.ProductName, \n"
                + "    p.ProductDescription,\n"
                + "    p.UnitPrice, \n"
                + "    p.DateAdded, \n"
                + "    p.Discount,\n"
                + "    p.Quantity,\n"
                + "    COALESCE(avgFeedback.AverageRating, 0) AS AverageRating,\n"
                + "    COALESCE(soldQuantity.TotalQuantitySold, 0) AS TotalQuantitySold\n"
                + "FROM Products p\n"
                + "LEFT JOIN (\n"
                + "    SELECT f.ProductID, AVG(CAST(f.Rating AS FLOAT)) AS AverageRating\n"
                + "    FROM Feedback f\n"
                + "    GROUP BY f.ProductID\n"
                + ") AS avgFeedback \n"
                + "    ON p.ProductID = avgFeedback.ProductID\n"
                + "LEFT JOIN (\n"
                + "    SELECT od.ProductID, SUM(od.Quantity) AS TotalQuantitySold\n"
                + "    FROM OrderDetails od\n"
                + "    INNER JOIN Orders o ON od.OrderID = o.OrderID\n"
                + "    WHERE o.OrderStatus = 'Completed'\n"
                + "    GROUP BY od.ProductID\n"
                + ") AS soldQuantity \n"
                + "    ON p.ProductID = soldQuantity.ProductID\n"
                + "ORDER BY \n"
                + "    CASE WHEN p.Quantity = 0 THEN 1 ELSE 0 END, \n"
                + "    p.ProductID\n"
                + "OFFSET 0 ROWS FETCH NEXT 8 ROWS ONLY;";

        Map<String, ProductsClient> productMap = new HashMap<>();
        jdbcTemplate.query(query, rs -> {
            String productId = rs.getString("ProductID");
            ProductsClient product = new ProductsClient();
            product.setProductID(productId);
            product.setCategoryID(rs.getInt("CategoryID"));
            product.setBrandID(rs.getInt("BrandID"));
            product.setProductName(rs.getString("ProductName"));
            product.setProductDescription(rs.getString("ProductDescription"));
            product.setUnitPrice(rs.getBigDecimal("UnitPrice"));
            product.setDateAdded(rs.getDate("DateAdded"));
            product.setDiscount(rs.getBigDecimal("Discount"));
            product.setAverageRating(rs.getBigDecimal("AverageRating") != null ? rs.getBigDecimal("AverageRating") : BigDecimal.ZERO);
            product.setTotalQuantitySold(rs.getInt("TotalQuantitySold"));
            productMap.put(productId, product);
            product.setQuantity(rs.getInt("Quantity"));
        });

        for (ProductsClient product : productMap.values()) {
            String imageQuery = "SELECT ProductImageID, ProductID, Image FROM ProductImage WHERE ProductID = ?";
            List<ProductImage> images = jdbcTemplate.query(imageQuery, new Object[]{product.getProductID()},
                    (rs, rowNum) -> new ProductImage(
                            rs.getString("ProductImageID"),
                            rs.getString("ProductID"),
                            rs.getString("Image")
                    )
            );
            product.setImages(images);
        }
        return new ArrayList<>(productMap.values());
    }

    // Retrieves 10 random products that are in stock with ratings and sales data
    @Override
    public List<ProductsClient> select10random() {
        String query
                = " SELECT  \n"
                + "    p.ProductID,  \n"
                + "    p.CategoryID,  \n"
                + "    p.BrandID,  \n"
                + "    p.ProductName,  \n"
                + "    p.ProductDescription,  \n"
                + "    p.UnitPrice,  \n"
                + "    p.DateAdded,  \n"
                + "    p.Discount,  \n"
                + "    p.Quantity,  \n"
                + "    COALESCE(avgFeedback.AverageRating, 0) AS AverageRating,   \n"
                + "    COALESCE(SUM(CASE WHEN o.OrderStatus = 'Completed' THEN od.Quantity ELSE 0 END), 0) AS TotalQuantitySold \n"
                + "FROM Products p  \n"
                + "LEFT JOIN (  \n"
                + "    SELECT  \n"
                + "        f.ProductID,  \n"
                + "        AVG(CAST(f.Rating AS FLOAT)) AS AverageRating  \n"
                + "    FROM Feedback f  \n"
                + "    GROUP BY f.ProductID  \n"
                + ") AS avgFeedback ON p.ProductID = avgFeedback.ProductID  \n"
                + "LEFT JOIN OrderDetails od  \n"
                + "    ON p.ProductID = od.ProductID  \n"
                + "LEFT JOIN Orders o  \n"
                + "    ON od.OrderID = o.OrderID  \n"
                + "WHERE p.Quantity > 0  \n"
                + "GROUP BY  \n"
                + "    p.ProductID,  \n"
                + "    p.CategoryID,  \n"
                + "    p.BrandID,  \n"
                + "    p.ProductName,  \n"
                + "    p.ProductDescription,  \n"
                + "    p.UnitPrice,  \n"
                + "    p.DateAdded,  \n"
                + "    p.Discount,  \n"
                + "    p.Quantity,  \n"
                + "    avgFeedback.AverageRating  \n"
                + "ORDER BY NEWID()  \n"
                + "OFFSET 0 ROWS FETCH NEXT 10 ROWS ONLY ;";

        Map<String, ProductsClient> productMap = new HashMap<>();

        jdbcTemplate.query(query, rs -> {
            String productId = rs.getString("ProductID");

            ProductsClient product = productMap.get(productId);
            if (product == null) {
                product = new ProductsClient();
                product.setProductID(productId);
                product.setCategoryID(rs.getInt("CategoryID"));
                product.setBrandID(rs.getInt("BrandID"));
                product.setProductName(rs.getString("ProductName"));
                product.setProductDescription(rs.getString("ProductDescription"));
                product.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                product.setDateAdded(rs.getDate("DateAdded"));
                product.setDiscount(rs.getBigDecimal("Discount"));
                product.setAverageRating(rs.getBigDecimal("AverageRating") != null ? rs.getBigDecimal("AverageRating") : BigDecimal.ZERO);
                product.setTotalQuantitySold(rs.getInt("TotalQuantitySold"));

                productMap.put(productId, product);
            }
        });

        for (ProductsClient product : productMap.values()) {
            String imageQuery = "SELECT ProductImageID, ProductID, Image "
                    + "FROM ProductImage WHERE ProductID = ?";

            List<ProductImage> images = jdbcTemplate.query(imageQuery, new Object[]{product.getProductID()}, new RowMapper<ProductImage>() {
                @Override
                public ProductImage mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new ProductImage(
                            rs.getString("ProductImageID"),
                            rs.getString("ProductID"),
                            rs.getString("Image")
                    );
                }
            });

            product.setImages(images);
        }

        return new ArrayList<>(productMap.values());
    }

    // Retrieves 5 random products that are in stock with ratings and sales data
    @Override
    public List<ProductsClient> select5random() {
        String query
                = " SELECT  \n"
                + "    p.ProductID,  \n"
                + "    p.CategoryID,  \n"
                + "    p.BrandID,  \n"
                + "    p.ProductName,  \n"
                + "    p.ProductDescription,  \n"
                + "    p.UnitPrice,  \n"
                + "    p.DateAdded,  \n"
                + "    p.Discount,  \n"
                + "    p.Quantity,  \n"
                + "    COALESCE(avgFeedback.AverageRating, 0) AS AverageRating,   \n"
                + "    COALESCE(SUM(CASE WHEN o.OrderStatus = 'Completed' THEN od.Quantity ELSE 0 END), 0) AS TotalQuantitySold \n"
                + "FROM Products p  \n"
                + "LEFT JOIN (  \n"
                + "    SELECT  \n"
                + "        f.ProductID,  \n"
                + "        AVG(CAST(f.Rating AS FLOAT)) AS AverageRating  \n"
                + "    FROM Feedback f  \n"
                + "    GROUP BY f.ProductID  \n"
                + ") AS avgFeedback ON p.ProductID = avgFeedback.ProductID  \n"
                + "LEFT JOIN OrderDetails od  \n"
                + "    ON p.ProductID = od.ProductID  \n"
                + "LEFT JOIN Orders o  \n"
                + "    ON od.OrderID = o.OrderID  \n"
                + "WHERE p.Quantity > 0  \n"
                + "GROUP BY  \n"
                + "    p.ProductID,  \n"
                + "    p.CategoryID,  \n"
                + "    p.BrandID,  \n"
                + "    p.ProductName,  \n"
                + "    p.ProductDescription,  \n"
                + "    p.UnitPrice,  \n"
                + "    p.DateAdded,  \n"
                + "    p.Discount,  \n"
                + "    p.Quantity,  \n"
                + "    avgFeedback.AverageRating  \n"
                + "ORDER BY NEWID()  \n"
                + "OFFSET 0 ROWS FETCH NEXT 5 ROWS ONLY ;";

        Map<String, ProductsClient> productMap = new HashMap<>();

        jdbcTemplate.query(query, rs -> {
            String productId = rs.getString("ProductID");

            ProductsClient product = productMap.get(productId);
            if (product == null) {
                product = new ProductsClient();
                product.setProductID(productId);
                product.setCategoryID(rs.getInt("CategoryID"));
                product.setBrandID(rs.getInt("BrandID"));
                product.setProductName(rs.getString("ProductName"));
                product.setProductDescription(rs.getString("ProductDescription"));
                product.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                product.setDateAdded(rs.getDate("DateAdded"));
                product.setDiscount(rs.getBigDecimal("Discount"));
                product.setAverageRating(rs.getBigDecimal("AverageRating") != null ? rs.getBigDecimal("AverageRating") : BigDecimal.ZERO);
                product.setTotalQuantitySold(rs.getInt("TotalQuantitySold"));

                productMap.put(productId, product);
            }
        });

        for (ProductsClient product : productMap.values()) {
            String imageQuery = "SELECT ProductImageID, ProductID, Image "
                    + "FROM ProductImage WHERE ProductID = ?";

            List<ProductImage> images = jdbcTemplate.query(imageQuery, new Object[]{product.getProductID()}, new RowMapper<ProductImage>() {
                @Override
                public ProductImage mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new ProductImage(
                            rs.getString("ProductImageID"),
                            rs.getString("ProductID"),
                            rs.getString("Image")
                    );
                }
            });

            product.setImages(images);
        }

        return new ArrayList<>(productMap.values());
    }

    // Gets the newest discount based on start date
    @Override
    public DiscountClient getNewestDiscountSingle() {
        String sql = "SELECT TOP 1 DiscountID, DiscountName, Image, StartDate, EndDate, Description "
                + "FROM Discounts "
                + "ORDER BY StartDate DESC";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            DiscountClient discount = new DiscountClient();
            discount.setDiscountID(rs.getInt("DiscountID"));
            discount.setDiscountName(rs.getString("DiscountName"));
            discount.setImage(rs.getString("Image"));
            discount.setStartDate(rs.getDate("StartDate"));
            discount.setEndDate(rs.getDate("EndDate"));
            discount.setDescription(rs.getString("Description"));
            return discount;
        });
    }

    // Finds 10 random products associated with a specific discount
    @Override
    public List<ProductsClient> findRandom10ProductsByDiscount(int discountID) {
        String sql = "SELECT TOP 10 p.ProductID, p.ProductName, p.UnitPrice, p.Discount, "
                + "       (SELECT MIN(Image) FROM ProductImage WHERE ProductID = p.ProductID) AS ProductImage "
                + "FROM Products p "
                + "JOIN Offers o ON p.ProductID = o.ProductID "
                + "WHERE o.DiscountID = ? "
                + "ORDER BY NEWID()";

        return jdbcTemplate.query(sql, new Object[]{discountID}, (rs, rowNum) -> {
            ProductsClient product = new ProductsClient();
            product.setProductID(rs.getString("ProductID"));
            product.setProductName(rs.getString("ProductName"));
            product.setUnitPrice(rs.getBigDecimal("UnitPrice"));
            product.setDiscount(rs.getBigDecimal("Discount"));

            ProductImage productImage = new ProductImage();
            productImage.setImages(rs.getString("ProductImage"));
            List<ProductImage> images = new ArrayList<>();
            images.add(productImage);
            product.setImages(images);

            return product;
        });
    }

    // Finds a product by its ID with associated images
    @Override
    public ProductsClient findById(String id) {
        String productQuery = "SELECT ProductID, CategoryID, BrandID, ProductName, ProductDescription, UnitPrice, DateAdded, Discount, Quantity\n"
                + "FROM Products WHERE ProductID = ?";

        ProductsClient product = jdbcTemplate.queryForObject(productQuery, new Object[]{id}, new RowMapper<ProductsClient>() {
            @Override
            public ProductsClient mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ProductsClient(
                        rs.getString("ProductID"),
                        rs.getInt("CategoryID"),
                        rs.getInt("BrandID"),
                        rs.getString("ProductName"),
                        rs.getString("ProductDescription"),
                        rs.getBigDecimal("UnitPrice"),
                        new ArrayList<>(),
                        rs.getDate("DateAdded"),
                        rs.getBigDecimal("Discount"),
                        null,
                        0,
                        rs.getInt("Quantity")
                );
            }
        });

        String imageQuery = "SELECT ProductImageID, ProductID, Image "
                + "FROM ProductImage WHERE ProductID = ?";

        List<ProductImage> images = jdbcTemplate.query(imageQuery, new Object[]{id}, new RowMapper<ProductImage>() {
            @Override
            public ProductImage mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ProductImage(
                        rs.getString("ProductImageID"),
                        rs.getString("ProductID"),
                        rs.getString("Image")
                );
            }
        });

        product.setImages(images);

        return product;
    }

    // Finds products by category ID (deprecated - use findByCategory with pagination instead)
    @Override
    public List<ProductsClient> findByCategoryId(int categoryId) {
        String query = "SELECT * FROM Products WHERE CategoryID = ?";
        return jdbcTemplate.query(query, new Object[]{categoryId}, new RowMapper<ProductsClient>() {
            @Override
            public ProductsClient mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ProductsClient();
            }
        });
    }

    // Gets product names for a list of product IDs
    @Override
    public Map<String, String> getProductNames(List<String> productIds) {
        if (productIds.isEmpty()) {
            return Collections.emptyMap();
        }

        String placeholders = String.join(",", Collections.nCopies(productIds.size(), "?"));
        String query = "SELECT ProductID, ProductName FROM Products WHERE ProductID IN (" + placeholders + ")";

        return jdbcTemplate.query(query, productIds.toArray(), rs -> {
            Map<String, String> productNames = new HashMap<>();
            while (rs.next()) {
                productNames.put(rs.getString("ProductID"), rs.getString("ProductName"));
            }
            return productNames;
        });
    }

    // Gets total count of products in database
    @Override
    public int getTotalProducts() {
        String query = "SELECT COUNT(*) FROM Products";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    // Retrieves products with pagination, out-of-stock items appear last
    @Override
    public List<ProductsClient> findPaginated(int page, int pageSize) {
        if (page < 1 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid page number or page size");
        }

        String query = "SELECT \n"
                + "    p.ProductID, \n"
                + "    p.CategoryID, \n"
                + "    p.BrandID, \n"
                + "    p.ProductName, \n"
                + "    p.ProductDescription,\n"
                + "    p.UnitPrice, \n"
                + "    p.DateAdded, \n"
                + "    p.Discount,\n"
                + "    p.Quantity,\n"
                + "    COALESCE(avgFeedback.AverageRating, 0) AS AverageRating,\n"
                + "    COALESCE(soldQuantity.TotalQuantitySold, 0) AS TotalQuantitySold\n"
                + "FROM Products p\n"
                + "LEFT JOIN (\n"
                + "    SELECT f.ProductID, AVG(CAST(f.Rating AS FLOAT)) AS AverageRating\n"
                + "    FROM Feedback f\n"
                + "    GROUP BY f.ProductID\n"
                + ") AS avgFeedback \n"
                + "    ON p.ProductID = avgFeedback.ProductID\n"
                + "LEFT JOIN (\n"
                + "    SELECT od.ProductID, SUM(od.Quantity) AS TotalQuantitySold\n"
                + "    FROM OrderDetails od\n"
                + "    INNER JOIN Orders o ON od.OrderID = o.OrderID\n"
                + "    WHERE o.OrderStatus = 'Completed'\n"
                + "    GROUP BY od.ProductID\n"
                + ") AS soldQuantity \n"
                + "    ON p.ProductID = soldQuantity.ProductID\n"
                + "ORDER BY \n"
                + "    CASE WHEN p.Quantity = 0 THEN 1 ELSE 0 END,\n"
                + "    p.ProductID\n"
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";

        int offset = (page - 1) * pageSize;

        Map<String, ProductsClient> productMap = new HashMap<>();

        jdbcTemplate.query(query, new Object[]{offset, pageSize}, rs -> {
            String productId = rs.getString("ProductID");

            ProductsClient product = new ProductsClient();
            product.setProductID(productId);
            product.setCategoryID(rs.getInt("CategoryID"));
            product.setBrandID(rs.getInt("BrandID"));
            product.setProductName(rs.getString("ProductName"));
            product.setProductDescription(rs.getString("ProductDescription"));
            product.setUnitPrice(rs.getBigDecimal("UnitPrice"));
            product.setDateAdded(rs.getDate("DateAdded"));
            product.setDiscount(rs.getBigDecimal("Discount"));
            product.setAverageRating(rs.getBigDecimal("averageRating") != null ? rs.getBigDecimal("averageRating") : BigDecimal.ZERO);
            product.setTotalQuantitySold(rs.getInt("TotalQuantitySold"));
            product.setQuantity(rs.getInt("Quantity"));
            productMap.put(productId, product);
        });

        for (ProductsClient product : productMap.values()) {
            String imageQuery = "SELECT ProductImageID, ProductID, Image "
                    + "FROM ProductImage WHERE ProductID = ?";

            List<ProductImage> images = jdbcTemplate.query(imageQuery, new Object[]{product.getProductID()},
                    (rs, rowNum) -> new ProductImage(
                            rs.getString("ProductImageID"),
                            rs.getString("ProductID"),
                            rs.getString("Image")
                    )
            );

            product.setImages(images);
        }

        return new ArrayList<>(productMap.values());
    }

    // Finds products by category with pagination and ratings
    @Override
    public List<ProductsClient> findByCategory(int categoryID, int page, int pageSize) {
        if (page < 1 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid page number or page size");
        }

        String query = "SELECT \n"
                + "    p.ProductID,\n"
                + "    p.CategoryID,\n"
                + "    p.BrandID,\n"
                + "    p.ProductName,\n"
                + "    p.ProductDescription,\n"
                + "    p.UnitPrice,\n"
                + "    p.DateAdded,\n"
                + "    p.Discount,\n"
                + "    p.Quantity,\n"
                + "    COALESCE(avgFeedback.AverageRating, 0) AS AverageRating,\n"
                + "    COALESCE(SUM(od.Quantity), 0) AS TotalQuantitySold\n"
                + "FROM Products p\n"
                + "LEFT JOIN (\n"
                + "    SELECT \n"
                + "        f.ProductID,\n"
                + "        AVG(CAST(f.Rating AS FLOAT)) AS AverageRating\n"
                + "    FROM Feedback f\n"
                + "    GROUP BY f.ProductID\n"
                + ") AS avgFeedback ON p.ProductID = avgFeedback.ProductID\n"
                + "LEFT JOIN OrderDetails od ON p.ProductID = od.ProductID\n"
                + "LEFT JOIN Orders o ON od.OrderID = o.OrderID\n"
                + "WHERE (o.OrderStatus = 'Completed' OR o.OrderStatus IS NULL)\n"
                + "  AND p.CategoryID = ?\n"
                + "GROUP BY \n"
                + "    p.ProductID,\n"
                + "    p.CategoryID,\n"
                + "    p.BrandID,\n"
                + "    p.ProductName,\n"
                + "    p.ProductDescription,\n"
                + "    p.UnitPrice,\n"
                + "    p.DateAdded,\n"
                + "    p.Discount,\n"
                + "    p.Quantity,\n"
                + "    avgFeedback.AverageRating\n"
                + "ORDER BY p.ProductID\n"
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";

        int offset = (page - 1) * pageSize;

        List<ProductsClient> products = jdbcTemplate.query(query, new Object[]{categoryID, offset, pageSize}, new RowMapper<ProductsClient>() {
            @Override
            public ProductsClient mapRow(ResultSet rs, int rowNum) throws SQLException {
                ProductsClient product = new ProductsClient();
                product.setProductID(rs.getString("ProductID"));
                product.setCategoryID(rs.getInt("CategoryID"));
                product.setBrandID(rs.getInt("BrandID"));
                product.setProductName(rs.getString("ProductName"));
                product.setProductDescription(rs.getString("ProductDescription"));
                product.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                product.setDateAdded(rs.getDate("DateAdded"));
                product.setDiscount(rs.getBigDecimal("Discount"));
                product.setAverageRating(rs.getBigDecimal("averageRating") != null ? rs.getBigDecimal("averageRating") : BigDecimal.ZERO);
                product.setTotalQuantitySold(rs.getInt("TotalQuantitySold"));
                product.setImages(new ArrayList<>());
                product.setQuantity(rs.getInt("Quantity"));

                return product;
            }
        });

        for (ProductsClient product : products) {
            String imageQuery = "SELECT ProductImageID, ProductID, Image FROM ProductImage WHERE ProductID = ?";

            List<ProductImage> images = jdbcTemplate.query(imageQuery, new Object[]{product.getProductID()}, new RowMapper<ProductImage>() {
                @Override
                public ProductImage mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new ProductImage(
                            rs.getString("ProductImageID"),
                            rs.getString("ProductID"),
                            rs.getString("Image")
                    );
                }
            });

            product.setImages(images);
        }

        return products;
    }

    // Counts total products in a specific category
    @Override
    public int countByCategory(int categoryID) {
        String query = "SELECT COUNT(*) FROM Products WHERE CategoryID = ?";
        return jdbcTemplate.queryForObject(query, new Object[]{categoryID}, Integer.class);
    }

    // Search products by keyword using LIKE and SOUNDEX matching
    @Override
    public List<ProductsClient> searchByKeyword(String keyword) {
        String query = "SELECT \n"
                + "    p.ProductID, \n"
                + "    p.CategoryID, \n"
                + "    p.BrandID, \n"
                + "    p.ProductName, \n"
                + "    p.ProductDescription,  \n"
                + "    p.UnitPrice, \n"
                + "    p.DateAdded, \n"
                + "    p.Discount,  \n"
                + "    p.Quantity,  \n"
                + "    COALESCE(AVG(CAST(f.Rating AS FLOAT)), 0) AS AverageRating,   \n"
                + "    COUNT(f.ProductID) AS FeedbackCount,  \n"
                + "    COALESCE(SUM(od.Quantity), 0) AS TotalQuantitySold,\n"
                + "    (SELECT STRING_AGG(pi.Image, ', ')  \n"
                + "     FROM ProductImage pi \n"
                + "     WHERE pi.ProductID = p.ProductID) AS Images  \n"
                + "FROM \n"
                + "    Products p  \n"
                + "LEFT JOIN \n"
                + "    Feedback f ON p.ProductID = f.ProductID   \n"
                + "LEFT JOIN \n"
                + "    OrderDetails od ON p.ProductID = od.ProductID\n"
                + "LEFT JOIN \n"
                + "    Orders o ON od.OrderID = o.OrderID AND o.OrderStatus = 'Completed'\n"
                + "WHERE \n"
                + "    p.ProductName COLLATE Latin1_General_CI_AI LIKE ? \n"
                + "    OR SOUNDEX(p.ProductName COLLATE Latin1_General_CI_AI) = SOUNDEX(?)\n"
                + "GROUP BY \n"
                + "    p.ProductID, \n"
                + "    p.CategoryID, \n"
                + "    p.BrandID, \n"
                + "    p.ProductName, \n"
                + "    p.ProductDescription,  \n"
                + "    p.UnitPrice, \n"
                + "    p.DateAdded, \n"
                + "    p.Discount";

        List<ProductsClient> products = jdbcTemplate.query(query, new Object[]{"%" + keyword + "%", keyword}, new RowMapper<ProductsClient>() {
            @Override
            public ProductsClient mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new ProductsClient(
                        rs.getString("ProductID"),
                        rs.getInt("CategoryID"),
                        rs.getInt("BrandID"),
                        rs.getString("ProductName"),
                        rs.getString("ProductDescription"),
                        rs.getBigDecimal("UnitPrice"),
                        new ArrayList<>(),
                        rs.getDate("DateAdded"),
                        rs.getBigDecimal("Discount"),
                        rs.getBigDecimal("averageRating") != null ? rs.getBigDecimal("averageRating") : BigDecimal.ZERO,
                        rs.getInt("TotalQuantitySold"),
                        rs.getInt("Quantity")
                );
            }
        });

        for (ProductsClient product : products) {
            String imageQuery = "SELECT ProductImageID, ProductID, Image "
                    + "FROM ProductImage WHERE ProductID = ?";

            List<ProductImage> images = jdbcTemplate.query(imageQuery, new Object[]{product.getProductID()}, new RowMapper<ProductImage>() {
                @Override
                public ProductImage mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new ProductImage(
                            rs.getString("ProductImageID"),
                            rs.getString("ProductID"),
                            rs.getString("Image")
                    );
                }
            });

            product.setImages(images);
        }

        return products;
    }

    // Find the closest matching product name using fuzzy string similarity
    @Override
    public String findClosestMatch(String keyword) {
        String query = "SELECT ProductName FROM Products";
        List<String> allProductNames = jdbcTemplate.queryForList(query, String.class);
        String closestMatch = null;
        double maxSimilarity = 0.0;

        String normalizedKeyword = removeAccents(keyword.toLowerCase());

        for (String productName : allProductNames) {
            String normalizedProductName = removeAccents(productName.toLowerCase());

            double similarity = calculateFuzzySimilarity(normalizedKeyword, normalizedProductName);

            if (similarity > maxSimilarity) {
                maxSimilarity = similarity;
                closestMatch = productName;
            }
        }

        double threshold = 0.4;
        if (maxSimilarity >= threshold) {
            return closestMatch;
        } else {
            return null;
        }
    }

    // Calculate fuzzy similarity between keyword and product name using weighted algorithm
    private double calculateFuzzySimilarity(String keyword, String productName) {
        String[] productWords = productName.split("\\s+");

        double bestWordSimilarity = 0.0;
        for (String word : productWords) {
            double wordSimilarity = wordSimilarity(keyword, word);
            if (wordSimilarity > bestWordSimilarity) {
                bestWordSimilarity = wordSimilarity;
            }
        }

        double overallSimilarity = 0.7 * bestWordSimilarity + 0.3 * stringSimilarity(keyword, productName);

        return overallSimilarity;
    }

    // Calculate similarity between two words using Levenshtein distance
    private double wordSimilarity(String s1, String s2) {
        int distance = levenshteinDistance(s1, s2);
        int maxLength = Math.max(s1.length(), s2.length());
        return 1.0 - ((double) distance / maxLength);
    }

    // Calculate similarity between two strings using Levenshtein distance
    private double stringSimilarity(String s1, String s2) {
        int distance = levenshteinDistance(s1, s2);
        int maxLength = Math.max(s1.length(), s2.length());
        return 1.0 - ((double) distance / maxLength);
    }

    // Remove diacritical marks from input string for normalization
    public String removeAccents(String input) {
        return java.text.Normalizer.normalize(input, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    // Calculate Levenshtein distance between two strings
    @Override
    public int levenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1),
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1));
                }
            }
        }
        return dp[a.length()][b.length()];
    }

    // Getter for JdbcTemplate
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    // Setter for JdbcTemplate
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Search products by name with pagination support
    @Override
    public List<ProductsClient> searchByProductName(String productName, int page, int pageSize) {
        if (page < 1 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid page number or page size");
        }

        String query = "SELECT \n"
                + "    p.ProductID, \n"
                + "    p.CategoryID, \n"
                + "    p.BrandID, \n"
                + "    p.ProductName, \n"
                + "    p.ProductDescription,\n"
                + "    p.UnitPrice, \n"
                + "    p.DateAdded, \n"
                + "    p.Discount,\n"
                + "    p.Quantity,\n"
                + "    COALESCE(avgFeedback.AverageRating, 0) AS AverageRating,\n"
                + "    COALESCE(soldQuantity.TotalQuantitySold, 0) AS TotalQuantitySold\n"
                + "FROM Products p\n"
                + "LEFT JOIN (\n"
                + "    SELECT f.ProductID, AVG(CAST(f.Rating AS FLOAT)) AS AverageRating\n"
                + "    FROM Feedback f\n"
                + "    GROUP BY f.ProductID\n"
                + ") AS avgFeedback \n"
                + "    ON p.ProductID = avgFeedback.ProductID\n"
                + "LEFT JOIN (\n"
                + "    SELECT od.ProductID, SUM(od.Quantity) AS TotalQuantitySold\n"
                + "    FROM OrderDetails od\n"
                + "    INNER JOIN Orders o ON od.OrderID = o.OrderID\n"
                + "    WHERE o.OrderStatus = 'Completed'\n"
                + "    GROUP BY od.ProductID\n"
                + ") AS soldQuantity \n"
                + "    ON p.ProductID = soldQuantity.ProductID\n"
                + "WHERE p.ProductName LIKE ?\n"
                + "ORDER BY \n"
                + "    CASE WHEN p.Quantity = 0 THEN 1 ELSE 0 END,  \n"
                + "    p.ProductID\n"
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";

        String searchKeyword = "%" + productName + "%";
        int offset = (page - 1) * pageSize;

        List<ProductsClient> products = jdbcTemplate.query(query, new Object[]{searchKeyword, offset, pageSize},
                (rs, rowNum) -> new ProductsClient(
                        rs.getString("ProductID"),
                        rs.getInt("CategoryID"),
                        rs.getInt("BrandID"),
                        rs.getString("ProductName"),
                        rs.getString("ProductDescription"),
                        rs.getBigDecimal("UnitPrice"),
                        new ArrayList<>(),
                        rs.getDate("DateAdded"),
                        rs.getBigDecimal("Discount"),
                        rs.getBigDecimal("averageRating") != null ? rs.getBigDecimal("averageRating") : BigDecimal.ZERO,
                        rs.getInt("TotalQuantitySold"),
                        rs.getInt("Quantity")
                )
        );

        for (ProductsClient product : products) {
            String imageQuery = "SELECT ProductImageID, ProductID, Image "
                    + "FROM ProductImage WHERE ProductID = ?";

            List<ProductImage> images = jdbcTemplate.query(imageQuery,
                    new Object[]{product.getProductID()},
                    (rs, rowNum) -> new ProductImage(
                            rs.getString("ProductImageID"),
                            rs.getString("ProductID"),
                            rs.getString("Image")
                    )
            );

            product.setImages(images);
        }

        return products;
    }

    // Count total products matching the given keyword
    @Override
    public int countByKeyword(String keyword) {
        String query = "SELECT COUNT(*) FROM Products WHERE ProductName LIKE ?";
        return jdbcTemplate.queryForObject(query, new Object[]{"%" + keyword + "%"}, Integer.class);
    }
}

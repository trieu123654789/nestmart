package com.models;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.multipart.MultipartFile;

public class ProductsDAOImpl implements ProductsDAO {

    private JdbcTemplate jdbcTemplate;

    public ProductsDAOImpl() {
    }

    public ProductsDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Format BigDecimal price to string without trailing zeros
    @Override
    public String formatPrice(BigDecimal price) {
        if (price == null) {
            return "";
        }
        BigDecimal stripped = price.stripTrailingZeros();
        if (stripped.scale() <= 0) {
            return String.valueOf(stripped.intValue());
        } else {
            return stripped.toPlainString();
        }
    }

    // Retrieve all products with their associated images
    @Override
    public List<Products> findAll() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        } catch (Exception e) {
            // Database connection failed
        }

        String query = "SELECT p.ProductID, p.CategoryID, p.BrandID, p.ProductName, p.ProductDescription, p.UnitPrice, \n"
                + "       pi.Image AS ProductImage, p.DateAdded, p.Discount, p.Quantity\n"
                + "FROM Products p\n"
                + "LEFT JOIN ProductImage pi ON p.ProductID = pi.ProductID";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

        Map<String, Products> productMap = new HashMap<>();

        for (Map<String, Object> row : rows) {
            String productId = (String) row.get("ProductID");

            Products product = productMap.get(productId);
            if (product == null) {
                product = new Products(
                        productId,
                        (Integer) row.get("CategoryID"),
                        (Integer) row.get("BrandID"),
                        (String) row.get("ProductName"),
                        (String) row.get("ProductDescription"),
                        (BigDecimal) row.get("UnitPrice"),
                        new ArrayList<>(),
                        ((Timestamp) row.get("DateAdded")).toLocalDateTime(),
                        (BigDecimal) row.get("Discount"),
                        (Integer) row.get("Quantity")
                );
                productMap.put(productId, product);
            }

            String images = (String) row.get("ProductImage");
            if (images != null) {
                ProductImage productImage = new ProductImage();
                productImage.setImages(images);

                product.getImages().add(productImage);
            }
        }

        List<Products> productList = new ArrayList<>(productMap.values());
        return productList;
    }

    // Save new product with validation and image upload handling
    @Override
    public void save(Products product, List<MultipartFile> imageFiles, ServletContext servletContext) {
        if (product.getProductID() == null || product.getProductID().isEmpty()) {
            throw new RuntimeException("ProductID cannot be empty.");
        }

        BigDecimal unitPrice;
        try {
            unitPrice = new BigDecimal(product.getUnitPrice().toString().replace(",", ""));
            if (unitPrice.compareTo(BigDecimal.ZERO) <= 0 || unitPrice.compareTo(new BigDecimal("1000000000")) > 0) {
                throw new RuntimeException("UnitPrice must be greater than 0 and less than or equal to 1,000,000,000.");
            }
            product.setUnitPrice(unitPrice);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid UnitPrice format.");
        }

        if (product.getDiscount() == null || product.getDiscount().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Discount cannot be negative.");
        }

        String query = "SELECT ProductID FROM Products WHERE ProductID = ?";
        List<String> existingIds = jdbcTemplate.query(query, new Object[]{product.getProductID()},
                (rs, rowNum) -> rs.getString("ProductID"));

        if (!existingIds.isEmpty()) {
            throw new RuntimeException("A product with this ID already exists.");
        }

        List<ProductImage> image = new ArrayList<>();
        String uploadPath = new File(servletContext.getRealPath("")).getParentFile().getParentFile()
                .getAbsolutePath() + "/web/assets/admin/images/uploads/products/";

        try {
            if (imageFiles != null && !imageFiles.isEmpty()) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                    throw new RuntimeException("Unable to create directory: " + uploadDir.getAbsolutePath());
                }

                for (MultipartFile imageFile : imageFiles) {
                    if (!imageFile.isEmpty()) {
                        String fileName = Paths.get(imageFile.getOriginalFilename()).getFileName().toString();
                        if (fileName == null || fileName.isEmpty()) {
                            throw new RuntimeException("Invalid file name.");
                        }

                        String filePath = uploadPath + File.separator + fileName;
                        imageFile.transferTo(new File(filePath));

                        ProductImage productImage = new ProductImage();
                        productImage.setProductImageID(UUID.randomUUID().toString());
                        productImage.setProductID(product.getProductID());
                        productImage.setImages(fileName);
                        image.add(productImage);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error while saving image: " + e.getMessage());
        }

        String insertProductQuery = "INSERT INTO Products (ProductID, CategoryID, BrandID, ProductName, ProductDescription, UnitPrice, DateAdded, Discount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(insertProductQuery,
                    product.getProductID(),
                    product.getCategoryID(),
                    product.getBrandID(),
                    product.getProductName(),
                    product.getProductDescription(),
                    product.getUnitPrice(),
                    Timestamp.valueOf(product.getDateAdded()),
                    product.getDiscount()
            );

            String insertImageQuery = "INSERT INTO ProductImage (ProductID, Image) VALUES (?, ?)";
            for (ProductImage img : image) {
                jdbcTemplate.update(insertImageQuery,
                        img.getProductID(),
                        img.getImages()
                );
            }

        } catch (Exception e) {
            for (ProductImage img : image) {
                String filePath = uploadPath + File.separator + img.getImages();
                File fileToDelete = new File(filePath);
                if (fileToDelete.exists() && !fileToDelete.delete()) {
                    // Unable to delete image file
                }
            }
            throw e;
        }
    }

    // Update existing product with image management and validation
    @Override
    public void update(Products product, List<MultipartFile> imageFiles, List<String> imagesToDelete, ServletContext servletContext) {
        if (product == null || product.getProductID() == null || product.getProductID().isEmpty()) {
            throw new RuntimeException("ProductID cannot be empty.");
        }
        if (product.getProductName() == null || product.getProductName().isEmpty()) {
            throw new RuntimeException("ProductName cannot be empty.");
        }

        BigDecimal unitPrice;
        try {
            unitPrice = new BigDecimal(product.getUnitPrice().toString().replace(",", ""));
            if (unitPrice.compareTo(BigDecimal.ZERO) <= 0 || unitPrice.compareTo(new BigDecimal("1000000000")) > 0) {
                throw new RuntimeException("UnitPrice must be greater than 0 and less than or equal to 1,000,000,000.");
            }
            product.setUnitPrice(unitPrice);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid UnitPrice format.");
        }

        if (product.getDiscount() == null || product.getDiscount().compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Discount cannot be negative.");
        }

        String relativePath = "/web/assets/admin/images/uploads/products/";
        String uploadPath = new File(servletContext.getRealPath("")).getParentFile().getParentFile().getAbsolutePath() + relativePath;

        List<String> existingImages = jdbcTemplate.queryForList(
                "SELECT Image FROM ProductImage WHERE ProductID = ?",
                new Object[]{product.getProductID()},
                String.class
        );

        if (imagesToDelete != null && !imagesToDelete.isEmpty()) {
            for (String imageToDelete : imagesToDelete) {
                jdbcTemplate.update("DELETE FROM ProductImage WHERE ProductID = ? AND Image = ?",
                        product.getProductID(), imageToDelete);

                File fileToDelete = new File(uploadPath + File.separator + imageToDelete);
                if (fileToDelete.exists() && !fileToDelete.delete()) {
                    System.err.println("Failed to delete file: " + fileToDelete.getAbsolutePath());
                }
                existingImages.remove(imageToDelete);
            }
        }

        List<ProductImage> newImages = new ArrayList<>();
        try {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                throw new RuntimeException("Failed to create directory: " + uploadDir.getAbsolutePath());
            }

            for (MultipartFile imageFile : imageFiles) {
                if (!imageFile.isEmpty()) {
                    String originalFileName = Paths.get(imageFile.getOriginalFilename()).getFileName().toString();
                    if (originalFileName == null || originalFileName.isEmpty()) {
                        throw new RuntimeException("Invalid file name.");
                    }

                    String uniqueFileName = UUID.randomUUID().toString() + "_" + originalFileName;
                    String filePath = uploadPath + File.separator + uniqueFileName;
                    imageFile.transferTo(new File(filePath));

                    ProductImage productImage = new ProductImage();
                    productImage.setProductImageID(UUID.randomUUID().toString());
                    productImage.setProductID(product.getProductID());
                    productImage.setImages(uniqueFileName);
                    newImages.add(productImage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving images: " + e.getMessage());
        }

        String insertImageQuery = "INSERT INTO ProductImage (ProductID, Image) VALUES (?, ?)";
        for (ProductImage img : newImages) {
            jdbcTemplate.update(insertImageQuery, img.getProductID(), img.getImages());
        }

        String updateProductQuery = "UPDATE Products SET CategoryID = ?, BrandID = ?, ProductName = ?, ProductDescription = ?, UnitPrice = ?, DateAdded = ?, Discount = ? WHERE ProductID = ?";
        jdbcTemplate.update(updateProductQuery,
                product.getCategoryID(),
                product.getBrandID(),
                product.getProductName(),
                product.getProductDescription(),
                product.getUnitPrice(),
                product.getDateAdded(),
                product.getDiscount(),
                product.getProductID()
        );
    }

    // Delete product by ID with dependency checks and cleanup
    @Override
    public void deleteById(String id) {
        String checkSoldQuery = "SELECT COUNT(*) FROM OrderDetails WHERE ProductID = ?";
        int count = jdbcTemplate.queryForObject(checkSoldQuery, new Object[]{id}, Integer.class);

        if (count > 0) {
            throw new RuntimeException("Product has been sold and cannot be deleted.");
        }

        String deleteInventoryQuery = "DELETE FROM Inventory WHERE ProductID = ?";
        jdbcTemplate.update(deleteInventoryQuery, id);

        String deleteCategoryDetailsQuery = "DELETE FROM CategoryDetails WHERE ProductID = ?";
        jdbcTemplate.update(deleteCategoryDetailsQuery, id);

        String deleteImagesQuery = "DELETE FROM ProductImage WHERE ProductID = ?";
        jdbcTemplate.update(deleteImagesQuery, id);

        String deleteProductQuery = "DELETE FROM Products WHERE ProductID = ?";
        jdbcTemplate.update(deleteProductQuery, id);
    }

    // Get product name by product ID
    @Override
    public String getProductNameById(String productId) {
        String sql = "SELECT ProductName FROM Products WHERE ProductID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, String.class);
    }

    // Find product by ID with associated images
    @Override
    public Products findById(String id) {
        String productQuery = "SELECT ProductID, CategoryID, BrandID, ProductName, ProductDescription, UnitPrice, DateAdded, Discount, Quantity "
                + "FROM Products WHERE ProductID = ?";

        Products product = jdbcTemplate.queryForObject(productQuery, new Object[]{id}, new RowMapper<Products>() {
            @Override
            public Products mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Products(
                        rs.getString("ProductID"),
                        rs.getInt("CategoryID"),
                        rs.getInt("BrandID"),
                        rs.getString("ProductName"),
                        rs.getString("ProductDescription"),
                        rs.getBigDecimal("UnitPrice"),
                        new ArrayList<>(),
                        rs.getTimestamp("DateAdded").toLocalDateTime(),
                        rs.getBigDecimal("Discount"),
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

    // Find products by category ID
    @Override
    public List<Products> findByCategoryId(int categoryId) {
        String query = "SELECT * FROM Products WHERE CategoryID = ?";
        return jdbcTemplate.query(query, new Object[]{categoryId}, new BeanPropertyRowMapper<>(Products.class));
    }

    // Update product quantity after sale
    @Override
    public void updateProductQuantity(String productId, int quantitySold) {
        String sql = "UPDATE Products SET Quantity = Quantity - ? WHERE ProductID = ?";
        jdbcTemplate.update(sql, quantitySold, productId);
    }

    // Restore product quantity with validation and debugging
    @Override
    public void restoreProductQuantity(String productId, int quantityToAdd) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("ProductID cannot be null or empty");
        }

        if (quantityToAdd <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        String trimmedProductId = productId.trim();

        try {
            String checkSql = "SELECT COUNT(*) FROM Products WHERE ProductID = ?";
            int productCount = jdbcTemplate.queryForObject(checkSql, Integer.class, trimmedProductId);

            if (productCount == 0) {
                throw new RuntimeException("Product not found: " + trimmedProductId);
            }

            String getCurrentSql = "SELECT Quantity FROM Products WHERE ProductID = ?";
            Integer currentQuantity = jdbcTemplate.queryForObject(getCurrentSql, Integer.class, trimmedProductId);

            String updateSql = "UPDATE Products SET Quantity = Quantity + ? WHERE ProductID = ?";
            int rowsAffected = jdbcTemplate.update(updateSql, quantityToAdd, trimmedProductId);

            Integer newQuantity = jdbcTemplate.queryForObject(getCurrentSql, Integer.class, trimmedProductId);

            if (newQuantity.intValue() != currentQuantity.intValue() + quantityToAdd) {
                throw new RuntimeException("Quantity update failed - expected: " + (currentQuantity + quantityToAdd) + ", actual: " + newQuantity);
            }

        } catch (Exception e) {
            throw e;
        }
    }

    // Find products by category ID with price filter (incomplete implementation)
    public List<Products> findByCategoryIdwithPrice(int categoryId) {
        String query = "SELECT * FROM Products WHERE CategoryID = ? AND unitPrice = ?";
        return jdbcTemplate.query(query, new Object[]{categoryId}, new BeanPropertyRowMapper<>(Products.class));
    }

    // Get product names map from list of product IDs
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

    // Get total count of products
    @Override
    public int getTotalProducts() {
        String query = "SELECT COUNT(*) FROM Products";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }

    // Find products with pagination
    @Override
    public List<Products> findPaginated(int page, int pageSize) {
        if (page < 1 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid page number or page size");
        }

        String query = "SELECT ProductID, CategoryID, BrandID, ProductName, ProductDescription, UnitPrice, DateAdded, Discount "
                + "FROM Products "
                + "ORDER BY ProductID "
                + "OFFSET ? ROWS "
                + "FETCH NEXT ? ROWS ONLY";
        int offset = (page - 1) * pageSize;

        List<Products> products = jdbcTemplate.query(query, new Object[]{offset, pageSize}, new RowMapper<Products>() {
            @Override
            public Products mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Products(
                        rs.getString("ProductID"),
                        rs.getInt("CategoryID"),
                        rs.getInt("BrandID"),
                        rs.getString("ProductName"),
                        rs.getString("ProductDescription"),
                        rs.getBigDecimal("UnitPrice"),
                        new ArrayList<>(),
                        rs.getTimestamp("DateAdded").toLocalDateTime(),
                        rs.getBigDecimal("Discount"),
                        rs.getInt("Quantity")
                );
            }
        });

        for (Products product : products) {
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

    // Get product quantity by product ID
    @Override
    public int getProductQuantity(String productID) {
        String sql = "SELECT Quantity FROM Products WHERE ProductID = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{productID}, Integer.class);
    }

    // Search products by keyword across multiple fields
    @Override
    public List<Products> searchByKeyword(String keyword) {
        String query = "SELECT p.ProductID, p.CategoryID, p.BrandID, p.ProductName, p.ProductDescription, "
                + "       p.UnitPrice, p.DateAdded, p.Discount, p.Quantity, "
                + "       c.CategoryName, b.BrandName "
                + "FROM Products p "
                + "JOIN Categories c ON p.CategoryID = c.CategoryID "
                + "JOIN Brands b ON p.BrandID = b.BrandID "
                + "WHERE p.ProductName COLLATE Latin1_General_CI_AI LIKE ? "
                + "   OR p.ProductID COLLATE Latin1_General_CI_AI LIKE ? "
                + "   OR c.CategoryName COLLATE Latin1_General_CI_AI LIKE ? "
                + "   OR b.BrandName COLLATE Latin1_General_CI_AI LIKE ? "
                + "ORDER BY p.ProductID";

        List<Products> products = jdbcTemplate.query(
                query,
                new Object[]{
                    "%" + keyword + "%",
                    "%" + keyword + "%",
                    "%" + keyword + "%",
                    "%" + keyword + "%"
                },
                (rs, rowNum) -> {
                    return new Products(
                            rs.getString("ProductID"),
                            rs.getInt("CategoryID"),
                            rs.getInt("BrandID"),
                            rs.getString("ProductName"),
                            rs.getString("ProductDescription"),
                            rs.getBigDecimal("UnitPrice"),
                            new ArrayList<>(),
                            rs.getTimestamp("DateAdded").toLocalDateTime(),
                            rs.getBigDecimal("Discount"),
                            rs.getInt("Quantity")
                    );
                }
        );

        for (Products product : products) {
            String imageQuery = "SELECT ProductImageID, ProductID, Image FROM ProductImage WHERE ProductID = ?";
            List<ProductImage> images = jdbcTemplate.query(
                    imageQuery,
                    new Object[]{product.getProductID()},
                    (rs, rowNum) -> {
                        return new ProductImage(
                                rs.getString("ProductImageID"),
                                rs.getString("ProductID"),
                                rs.getString("Image")
                        );
                    }
            );
            product.setImages(images);
        }
        return products;
    }

    // Find all products for offers management
    @Override
    public List<Products> findAllProductsForOffers() {
        String query = "SELECT ProductID, CategoryID, BrandID, ProductName, ProductDescription, UnitPrice, DateAdded, Discount, Quantity "
                + "FROM Products";

        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Products product = new Products();
            product.setProductID(rs.getString("ProductID"));
            product.setCategoryID(rs.getInt("CategoryID"));
            product.setBrandID(rs.getInt("BrandID"));
            product.setProductName(rs.getString("ProductName"));
            product.setProductDescription(rs.getString("ProductDescription"));
            product.setUnitPrice(rs.getBigDecimal("UnitPrice"));
            product.setDateAdded(rs.getTimestamp("DateAdded").toLocalDateTime());
            product.setDiscount(rs.getBigDecimal("Discount"));
            product.setQuantity(rs.getInt("Quantity"));
            product.setImages(new ArrayList<>());
            return product;
        });
    }

    // Get paginated products with keyword search
    @Override
    public List<Products> getPagedProducts(String keyword, int page, int pageSize) {
        String query = "SELECT "
                + "    p.ProductID, "
                + "    p.CategoryID, "
                + "    p.BrandID, "
                + "    p.ProductName, "
                + "    p.ProductDescription, "
                + "    p.UnitPrice, "
                + "    p.DateAdded, "
                + "    p.Discount, "
                + "    p.Quantity "
                + "FROM Products p "
                + "WHERE p.ProductName LIKE ? OR p.ProductID LIKE ? "
                + "ORDER BY p.ProductID "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        int offset = (page - 1) * pageSize;
        String like = "%" + keyword + "%";

        Map<String, Products> productMap = new HashMap<>();

        jdbcTemplate.query(query, new Object[]{like, like, offset, pageSize}, rs -> {
            String productId = rs.getString("ProductID");
            Products product = new Products(
                    rs.getString("ProductID"),
                    rs.getInt("CategoryID"),
                    rs.getInt("BrandID"),
                    rs.getString("ProductName"),
                    rs.getString("ProductDescription"),
                    rs.getBigDecimal("UnitPrice"),
                    new ArrayList<>(),
                    rs.getTimestamp("DateAdded").toLocalDateTime(),
                    rs.getBigDecimal("Discount"),
                    rs.getInt("Quantity")
            );
            productMap.put(productId, product);
        });

        for (Products product : productMap.values()) {
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

    // Check if product has been sold
    @Override
    public boolean isProductSold(String productId) {
        String sql = "SELECT COUNT(*) FROM OrderDetails WHERE ProductID = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{productId}, Integer.class);
        return count != null && count > 0;
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
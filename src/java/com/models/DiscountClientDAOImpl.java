package com.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class DiscountClientDAOImpl implements DiscountClientDAO {

    private JdbcTemplate jdbcTemplate;

    public DiscountClientDAOImpl() {
    }

    public DiscountClientDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Get all discounts with products (limit 10 products per discount)
    @Override
    public List<DiscountClient> getAllDiscountsWithProducts() {
        String sql = "WITH DiscountProducts AS (\n" +
                "    SELECT \n" +
                "        d.DiscountID,\n" +
                "        d.DiscountName,\n" +
                "        d.Image AS DiscountImage,\n" +
                "        p.ProductID,\n" +
                "        p.ProductName,\n" +
                "        p.UnitPrice,\n" +
                "        p.Quantity,\n" +
                "        pi.Image AS ProductImage,\n" +
                "        p.Discount AS ProductDiscount,\n" +
                "        d.StartDate,\n" +
                "        d.EndDate,\n" +
                "        d.Description,\n" +
                "        COALESCE(avgFeedback.AverageRating, 0) AS AverageRating,\n" +
                "        COALESCE(SUM(CASE WHEN o2.OrderStatus = 'Completed' THEN od.Quantity ELSE 0 END), 0) AS TotalQuantitySold\n" +
                "    FROM Discounts d\n" +
                "    INNER JOIN Offers o ON d.DiscountID = o.DiscountID\n" +
                "    INNER JOIN Products p ON o.ProductID = p.ProductID AND p.Quantity > 0\n" +
                "    LEFT JOIN (\n" +
                "        SELECT ProductID, Image\n" +
                "        FROM (\n" +
                "            SELECT ProductID, Image,\n" +
                "                   ROW_NUMBER() OVER (PARTITION BY ProductID ORDER BY ProductImageID ASC) AS rn\n" +
                "            FROM ProductImage\n" +
                "        ) t\n" +
                "        WHERE rn = 1\n" +
                "    ) pi ON p.ProductID = pi.ProductID\n" +
                "    LEFT JOIN (\n" +
                "        SELECT ProductID, AVG(CAST(Rating AS FLOAT)) AS AverageRating\n" +
                "        FROM Feedback\n" +
                "        GROUP BY ProductID\n" +
                "    ) avgFeedback ON p.ProductID = avgFeedback.ProductID\n" +
                "    LEFT JOIN OrderDetails od ON p.ProductID = od.ProductID\n" +
                "    LEFT JOIN Orders o2 ON od.OrderID = o2.OrderID\n" +
                "    WHERE GETDATE() BETWEEN d.StartDate AND d.EndDate\n" +
                "    GROUP BY d.DiscountID, d.DiscountName, d.Image,\n" +
                "             p.ProductID, p.ProductName, p.UnitPrice, p.Quantity, pi.Image, p.Discount,\n" +
                "             d.StartDate, d.EndDate, d.Description, avgFeedback.AverageRating\n" +
                "),\n" +
                "RankedProducts AS (\n" +
                "    SELECT dp.*, ROW_NUMBER() OVER (PARTITION BY dp.DiscountID ORDER BY dp.ProductID) AS RowNum\n" +
                "    FROM DiscountProducts dp\n" +
                ")\n" +
                "SELECT * FROM RankedProducts WHERE RowNum <= 10 ORDER BY DiscountID, ProductID;";

        return jdbcTemplate.query(sql, (ResultSet rs) -> {
            List<DiscountClient> results = new ArrayList<>();
            DiscountClient currentDiscountClient = null;
            List<ProductsClient> productList = null;

            while (rs.next()) {
                int discountID = rs.getInt("DiscountID");

                if (currentDiscountClient == null || currentDiscountClient.getDiscountID() != discountID) {
                    if (currentDiscountClient != null) {
                        currentDiscountClient.setProducts(productList);
                        results.add(currentDiscountClient);
                    }

                    currentDiscountClient = new DiscountClient();
                    currentDiscountClient.setDiscountID(discountID);
                    currentDiscountClient.setDiscountName(rs.getString("DiscountName"));
                    currentDiscountClient.setImage(rs.getString("DiscountImage"));
                    currentDiscountClient.setStartDate(rs.getDate("StartDate"));
                    currentDiscountClient.setEndDate(rs.getDate("EndDate"));
                    currentDiscountClient.setDescription(rs.getString("Description"));

                    productList = new ArrayList<>();
                }

                String productID = rs.getString("ProductID");
                if (productID != null) {
                    ProductsClient product = new ProductsClient();
                    product.setProductID(productID);
                    product.setProductName(rs.getString("ProductName"));
                    product.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                    product.setDiscount(rs.getBigDecimal("ProductDiscount"));
                    product.setQuantity(rs.getInt("Quantity"));

                    ProductImage productImage = new ProductImage();
                    productImage.setImages(rs.getString("ProductImage"));
                    List<ProductImage> images = new ArrayList<>();
                    images.add(productImage);
                    product.setImages(images);

                    BigDecimal averageRating = rs.getBigDecimal("AverageRating");
                    if (averageRating != null && averageRating.compareTo(BigDecimal.ZERO) > 0) {
                        averageRating = averageRating.setScale(1, RoundingMode.HALF_UP);
                    } else {
                        averageRating = BigDecimal.ZERO;
                    }

                    int totalQuantitySold = rs.getInt("TotalQuantitySold");

                    product.setAverageRating(averageRating);
                    product.setTotalQuantitySold(totalQuantitySold);

                    productList.add(product);
                }
            }

            if (currentDiscountClient != null) {
                currentDiscountClient.setProducts(productList);
                results.add(currentDiscountClient);
            }

            return results;
        });
    }

    // Get newest active discount
    @Override
    public DiscountClient getNewestDiscountSingle() {
        String sql = "SELECT TOP 1 DiscountID, DiscountName, Image, StartDate, EndDate, Description " +
                     "FROM Discounts " +
                     "WHERE GETDATE() BETWEEN StartDate AND EndDate " +
                     "ORDER BY StartDate DESC;";

        List<DiscountClient> discounts = jdbcTemplate.query(sql, (rs, rowNum) -> {
            DiscountClient discount = new DiscountClient();
            discount.setDiscountID(rs.getInt("DiscountID"));
            discount.setDiscountName(rs.getString("DiscountName"));
            discount.setImage(rs.getString("Image"));
            discount.setStartDate(rs.getDate("StartDate"));
            discount.setEndDate(rs.getDate("EndDate"));
            discount.setDescription(rs.getString("Description"));
            return discount;
        });

        return discounts.isEmpty() ? null : discounts.get(0);
    }

    // Find random 10 products by discount ID
    @Override
    public List<ProductsClient> findRandom10ProductsByDiscount(int discountID) {
        String sql = "SELECT TOP 10 p.ProductID, p.ProductName, p.UnitPrice, p.Discount, p.Quantity,\n" +
                "       (SELECT MIN(Image) FROM ProductImage WHERE ProductID = p.ProductID) AS ProductImage,\n" +
                "       COALESCE(f.AvgRating, 0) AS AverageRating,\n" +
                "       COALESCE(s.TotalQuantitySold, 0) AS TotalQuantitySold\n" +
                "FROM Products p\n" +
                "JOIN Offers o ON p.ProductID = o.ProductID\n" +
                "LEFT JOIN (\n" +
                "    SELECT fb.ProductID, AVG(CAST(fb.Rating AS FLOAT)) AS AvgRating\n" +
                "    FROM Feedback fb GROUP BY fb.ProductID\n" +
                ") f ON p.ProductID = f.ProductID\n" +
                "LEFT JOIN (\n" +
                "    SELECT od.ProductID, SUM(od.Quantity) AS TotalQuantitySold\n" +
                "    FROM OrderDetails od\n" +
                "    JOIN Orders o2 ON od.OrderID = o2.OrderID\n" +
                "    WHERE o2.OrderStatus = 'Completed'\n" +
                "    GROUP BY od.ProductID\n" +
                ") s ON p.ProductID = s.ProductID\n" +
                "WHERE o.DiscountID = ? AND p.Quantity > 0\n" +
                "ORDER BY NEWID();";

        return jdbcTemplate.query(sql, new Object[]{discountID}, (rs, rowNum) -> {
            ProductsClient product = new ProductsClient();
            product.setProductID(rs.getString("ProductID"));
            product.setProductName(rs.getString("ProductName"));
            product.setUnitPrice(rs.getBigDecimal("UnitPrice"));
            product.setDiscount(rs.getBigDecimal("Discount"));
            product.setQuantity(rs.getInt("Quantity"));

            ProductImage productImage = new ProductImage();
            productImage.setImages(rs.getString("ProductImage"));
            List<ProductImage> images = new ArrayList<>();
            images.add(productImage);
            product.setImages(images);

            BigDecimal averageRating = rs.getBigDecimal("AverageRating");
            if (averageRating != null && averageRating.compareTo(BigDecimal.ZERO) > 0) {
                averageRating = averageRating.setScale(1, RoundingMode.HALF_UP);
            } else {
                averageRating = BigDecimal.ZERO;
            }
            product.setAverageRating(averageRating);

            int totalQuantitySold = rs.getInt("TotalQuantitySold");
            product.setTotalQuantitySold(totalQuantitySold);

            return product;
        });
    }

    // Find products by discount with pagination
    @Override
    public List<ProductsClient> findProductsByDiscount(int discountID, int page, int pageSize) {
        String sql = "WITH RankedProducts AS (\n" +
                "    SELECT p.ProductID, p.ProductName, p.UnitPrice, p.Quantity,\n" +
                "           pi.Image AS ProductImage, p.Discount AS ProductDiscount,\n" +
                "           COALESCE(avgFeedback.AverageRating, 0) AS AverageRating,\n" +
                "           COALESCE(SUM(CASE WHEN o2.OrderStatus = 'Completed' THEN od.Quantity ELSE 0 END), 0) AS TotalQuantitySold\n" +
                "    FROM Products p\n" +
                "    LEFT JOIN Offers o ON p.ProductID = o.ProductID\n" +
                "    LEFT JOIN (\n" +
                "        SELECT ProductID, Image FROM (\n" +
                "            SELECT ProductID, Image,\n" +
                "                   ROW_NUMBER() OVER (PARTITION BY ProductID ORDER BY ProductImageID ASC) AS rn\n" +
                "            FROM ProductImage\n" +
                "        ) t WHERE rn = 1\n" +
                "    ) pi ON p.ProductID = pi.ProductID\n" +
                "    LEFT JOIN (\n" +
                "        SELECT f.ProductID, AVG(CAST(f.Rating AS FLOAT)) AS AverageRating\n" +
                "        FROM Feedback f GROUP BY f.ProductID\n" +
                "    ) avgFeedback ON p.ProductID = avgFeedback.ProductID\n" +
                "    LEFT JOIN OrderDetails od ON p.ProductID = od.ProductID\n" +
                "    LEFT JOIN Orders o2 ON od.OrderID = o2.OrderID\n" +
                "    WHERE o.DiscountID = ?\n" +
                "    GROUP BY p.ProductID, p.ProductName, p.UnitPrice, p.Quantity, pi.Image, p.Discount, avgFeedback.AverageRating\n" +
                ")\n" +
                "SELECT * FROM RankedProducts ORDER BY ProductID OFFSET ? ROWS FETCH NEXT ? ROWS ONLY;";

        int offset = (page - 1) * pageSize;

        return jdbcTemplate.query(sql, new Object[]{discountID, offset, pageSize}, (ResultSet rs) -> {
            List<ProductsClient> productList = new ArrayList<>();

            while (rs.next()) {
                String productID = rs.getString("ProductID");
                if (productID != null) {
                    ProductsClient product = new ProductsClient();
                    product.setProductID(productID);
                    product.setProductName(rs.getString("ProductName"));
                    product.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                    product.setDiscount(rs.getBigDecimal("ProductDiscount"));
                    product.setQuantity(rs.getInt("Quantity"));

                    ProductImage productImage = new ProductImage();
                    productImage.setImages(rs.getString("ProductImage"));
                    List<ProductImage> images = new ArrayList<>();
                    images.add(productImage);
                    product.setImages(images);

                    BigDecimal averageRating = rs.getBigDecimal("AverageRating");
                    if (averageRating != null && averageRating.compareTo(BigDecimal.ZERO) > 0) {
                        averageRating = averageRating.setScale(1, RoundingMode.HALF_UP);
                    } else {
                        averageRating = BigDecimal.ZERO;
                    }

                    int totalQuantitySold = rs.getInt("TotalQuantitySold");

                    product.setAverageRating(averageRating);
                    product.setTotalQuantitySold(totalQuantitySold);

                    productList.add(product);
                }
            }

            return productList;
        });
    }

    // Count products by discount ID
    @Override
    public int countProductsByDiscount(int discountID) {
        String sql = "SELECT COUNT(*) FROM Products p " +
                     "LEFT JOIN Offers o ON p.ProductID = o.ProductID " +
                     "WHERE o.DiscountID = ? AND p.Quantity > 0";

        return jdbcTemplate.queryForObject(sql, new Object[]{discountID}, Integer.class);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}

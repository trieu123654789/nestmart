package com.models;

import com.models.EmployeeResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.multipart.MultipartFile;

public class FeedbackDAOImpl implements FeedbackDAO {

    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    public FeedbackDAOImpl() {
    }

    public FeedbackDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Get average rating and total feedback count for a product
    @Override
    public Map<String, Object> getAverageRatingAndCount(String productId) {
        String sql = "SELECT AVG(CAST(Rating AS FLOAT)) AS averageRating, "
                + "COUNT(*) AS feedbackCount "
                + "FROM Feedback "
                + "WHERE ProductID = ?;";

        return jdbcTemplate.queryForObject(sql, new Object[]{productId}, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> result = new HashMap<>();
                double averageRating = rs.getDouble("averageRating");

                DecimalFormat df = new DecimalFormat("#.#");
                String formattedAverageRating = df.format(averageRating);

                result.put("averageRating", Double.parseDouble(formattedAverageRating));
                result.put("feedbackCount", rs.getInt("feedbackCount"));
                return result;
            }
        });
    }

    // Retrieve all feedback with images, product and customer details
    @Override
    public List<Feedback> findAllFeedback() {
        String query = "SELECT f.FeedbackID, f.CustomerID, f.ProductID, f.FeedbackContent, f.Rating, f.FeedbackDate, "
                + "fi.Image, p.ProductName, a.FullName AS CustomerName "
                + "FROM Feedback f "
                + "LEFT JOIN FeedbackImage fi ON f.FeedbackID = fi.FeedbackID "
                + "INNER JOIN Products p ON f.ProductID = p.ProductID "
                + "INNER JOIN Accounts a ON f.CustomerID = a.AccountID "
                + "ORDER BY f.FeedbackDate DESC";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        Map<Integer, Feedback> fbMap = new HashMap<>();

        for (Map<String, Object> row : rows) {
            int feedbackID = (Integer) row.get("FeedbackID");
            Feedback fb = fbMap.get(feedbackID);

            if (fb == null) {
                fb = new Feedback();
                fb.setFeedbackID(feedbackID);
                fb.setCustomerID((Integer) row.get("CustomerID"));
                fb.setProductID((String) row.get("ProductID"));
                fb.setFeedbackContent((String) row.get("FeedbackContent"));
                fb.setRating(((Number) row.get("Rating")).shortValue());

                Timestamp timestamp = (Timestamp) row.get("FeedbackDate");
                if (timestamp != null) {
                    fb.setFeedbackDate(timestamp.toLocalDateTime());
                }

                fb.setCustomerName((String) row.get("CustomerName"));
                fb.setProductName((String) row.get("ProductName"));

                fb.setImages(new ArrayList<>());
                fbMap.put(feedbackID, fb);
            }

            String img = (String) row.get("Image");
            if (img != null) {
                FeedbackImage feedbackImage = new FeedbackImage();
                feedbackImage.setFeedbackID(feedbackID);
                feedbackImage.setImage(img);
                fb.getImages().add(feedbackImage);
            }
        }

        return new ArrayList<>(fbMap.values());
    }

    // Get all feedback for a specific product with images
    @Override
    public List<Feedback> getFeedbackByProductId(String productID) {

        String query = "SELECT f.FeedbackID, f.FeedbackContent, f.Rating, f.FeedbackDate, f.ProductID, "
                + "p.ProductName, a.FullName AS CustomerName, fi.Image "
                + "FROM Feedback f "
                + "INNER JOIN Products p ON f.ProductID = p.ProductID "
                + "INNER JOIN Accounts a ON f.CustomerID = a.AccountID "
                + "LEFT JOIN FeedbackImage fi ON f.FeedbackID = fi.FeedbackID "
                + "WHERE f.ProductID = ? "
                + "ORDER BY f.FeedbackDate DESC";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, productID);
        Map<Integer, Feedback> fbMap = new HashMap<>();

        for (Map<String, Object> row : rows) {
            int feedbackID = (Integer) row.get("FeedbackID");
            Feedback feedback = fbMap.get(feedbackID);

            if (feedback == null) {
                feedback = new Feedback();
                feedback.setFeedbackID(feedbackID);
                feedback.setFeedbackContent((String) row.get("FeedbackContent"));
                feedback.setRating(((Number) row.get("Rating")).shortValue());

                Timestamp timestamp = (Timestamp) row.get("FeedbackDate");
                if (timestamp != null) {
                    feedback.setFeedbackDate(timestamp.toLocalDateTime());
                }

                feedback.setCustomerName((String) row.get("CustomerName"));

                Products product = new Products();
                product.setProductID((String) row.get("ProductID"));
                product.setProductName((String) row.get("ProductName"));
                feedback.setProduct(product);

                feedback.setImages(new ArrayList<>());
                fbMap.put(feedbackID, feedback);
            }

            String img = (String) row.get("Image");
            if (img != null) {
                FeedbackImage feedbackImage = new FeedbackImage();
                feedbackImage.setFeedbackID(feedbackID);
                feedbackImage.setImage(img);
                feedback.getImages().add(feedbackImage);
            }
        }

        return new ArrayList<>(fbMap.values());
    }

    // Get feedback for specific product and customer combination
    @Override
    public Feedback getFeedbackByProductAndCustomer(String productID, int customerID) {
        String query = "SELECT f.FeedbackID, f.FeedbackContent, f.Rating, f.FeedbackDate, f.ProductID, "
                + "p.ProductName, a.FullName AS CustomerName, fi.Image AS FeedbackImage, "
                + "(SELECT TOP 1 pi.Image FROM ProductImage pi WHERE pi.ProductID = p.ProductID ORDER BY pi.ProductImageID ASC) AS ProductImage "
                + "FROM Feedback f "
                + "INNER JOIN Products p ON f.ProductID = p.ProductID "
                + "INNER JOIN Accounts a ON f.CustomerID = a.AccountID "
                + "LEFT JOIN FeedbackImage fi ON f.FeedbackID = fi.FeedbackID "
                + "WHERE f.ProductID = ? AND f.CustomerID = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, productID, customerID);
        Feedback feedback = null;

        for (Map<String, Object> row : rows) {
            if (feedback == null) {
                feedback = new Feedback();
                feedback.setFeedbackID((Integer) row.get("FeedbackID"));
                feedback.setFeedbackContent((String) row.get("FeedbackContent"));
                feedback.setRating(((Number) row.get("Rating")).shortValue());

                Timestamp timestamp = (Timestamp) row.get("FeedbackDate");
                if (timestamp != null) {
                    feedback.setFeedbackDate(timestamp.toLocalDateTime());
                }

                feedback.setCustomerName((String) row.get("CustomerName"));

                Products product = new Products();
                product.setProductID((String) row.get("ProductID"));
                product.setProductName((String) row.get("ProductName"));
                product.setImages(new ArrayList<>());
                feedback.setProduct(product);

                feedback.setImages(new ArrayList<>());
            }

            String fbImg = (String) row.get("FeedbackImage");
            if (fbImg != null) {
                FeedbackImage feedbackImage = new FeedbackImage();
                feedbackImage.setFeedbackID(feedback.getFeedbackID());
                feedbackImage.setImage(fbImg);
                feedback.getImages().add(feedbackImage);
            }

            String prodImg = (String) row.get("ProductImage");
            if (prodImg != null && feedback.getProduct().getImages().isEmpty()) {
                ProductImage productImage = new ProductImage();
                productImage.setImages(prodImg);
                feedback.getProduct().getImages().add(productImage);
            }
        }

        return feedback;
    }

    // Get feedback by feedback ID with associated data
    @Override
    public List<Feedback> getFeedbackById(int feedbackID) {
        String query = "SELECT f.FeedbackID, f.FeedbackContent, f.Rating, f.FeedbackDate, f.ProductID, "
                + "p.ProductName, a.FullName AS CustomerName, fi.Image "
                + "FROM Feedback f "
                + "INNER JOIN Products p ON f.ProductID = p.ProductID "
                + "INNER JOIN Accounts a ON f.CustomerID = a.AccountID "
                + "LEFT JOIN FeedbackImage fi ON f.FeedbackID = fi.FeedbackID "
                + "WHERE f.FeedbackID = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, feedbackID);
        Map<Integer, Feedback> fbMap = new HashMap<>();

        for (Map<String, Object> row : rows) {
            int id = (Integer) row.get("FeedbackID");
            Feedback feedback = fbMap.get(id);

            if (feedback == null) {
                feedback = new Feedback();
                feedback.setFeedbackID(id);
                feedback.setFeedbackContent((String) row.get("FeedbackContent"));
                feedback.setRating(((Number) row.get("Rating")).shortValue());

                Timestamp timestamp = (Timestamp) row.get("FeedbackDate");
                if (timestamp != null) {
                    feedback.setFeedbackDate(timestamp.toLocalDateTime());
                }

                feedback.setCustomerName((String) row.get("CustomerName"));

                Products product = new Products();
                product.setProductID((String) row.get("ProductID"));
                product.setProductName((String) row.get("ProductName"));
                feedback.setProduct(product);

                feedback.setImages(new ArrayList<>());
                fbMap.put(id, feedback);
            }

            String img = (String) row.get("Image");
            if (img != null) {
                FeedbackImage feedbackImage = new FeedbackImage();
                feedbackImage.setFeedbackID(feedback.getFeedbackID());
                feedbackImage.setImage(img);
                feedback.getImages().add(feedbackImage);
            }
        }

        return new ArrayList<>(fbMap.values());
    }

    // Find product by ID with associated images
    @Override
    public Products findProductByID(String productID) {
        String query = "SELECT p.*, pi.ProductImageID, pi.ProductID , pi.Image "
                + "FROM Products p LEFT JOIN ProductImage pi ON p.ProductID = pi.ProductID "
                + "WHERE p.ProductID = ?";

        return jdbcTemplate.query(query, new Object[]{productID}, rs -> {
            Products product = null;
            List<ProductImage> images = new ArrayList<>();
            while (rs.next()) {
                if (product == null) {
                    product = new Products();
                    product.setProductID(rs.getString("ProductID"));
                    product.setCategoryID(rs.getInt("CategoryID"));
                    product.setBrandID(rs.getInt("BrandID"));
                    product.setProductName(rs.getString("ProductName"));
                    product.setProductDescription(rs.getString("ProductDescription"));
                    product.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                    product.setDateAdded(rs.getTimestamp("DateAdded").toLocalDateTime());
                    product.setDiscount(rs.getBigDecimal("Discount"));
                }

                String imageId = rs.getString("ProductImageID");
                if (imageId != null) {
                    ProductImage image = new ProductImage();
                    image.setProductImageID(imageId);
                    image.setProductID(rs.getString("ProductID"));
                    image.setImages(rs.getString("Image"));
                    images.add(image);
                }
            }
            if (product != null) {
                product.setImages(images);
            }
            return product;
        });
    }

    // Get paginated feedback results without search
    @Override
    public List<Feedback> findPaginated(int page, int pageSize) {
        int offset = (page - 1) * pageSize;

        String query = "SELECT f.FeedbackID, f.CustomerID, f.ProductID, f.FeedbackContent, f.Rating, f.FeedbackDate, "
                + "p.ProductName, a.FullName AS CustomerName "
                + "FROM Feedback f "
                + "INNER JOIN Products p ON f.ProductID = p.ProductID "
                + "INNER JOIN Accounts a ON f.CustomerID = a.AccountID "
                + "ORDER BY f.FeedbackDate DESC "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        return jdbcTemplate.query(query, new Object[]{offset, pageSize}, (rs, rowNum) -> {
            Feedback fb = new Feedback();
            fb.setFeedbackID(rs.getInt("FeedbackID"));
            fb.setCustomerID(rs.getInt("CustomerID"));
            fb.setProductID(rs.getString("ProductID"));
            fb.setFeedbackContent(rs.getString("FeedbackContent"));
            fb.setRating(rs.getShort("Rating"));
            fb.setFeedbackDate(rs.getTimestamp("FeedbackDate").toLocalDateTime());
            fb.setCustomerName(rs.getString("CustomerName"));
            fb.setProductName(rs.getString("ProductName"));
            fb.setImages(getFeedbackImages(rs.getInt("FeedbackID")));
            return fb;
        });
    }

    // Get paginated feedback results with search functionality
    @Override
    public List<Feedback> getPagedFeedback(String keyword, int page, int pageSize) {
        String searchTerm = "%" + keyword + "%";
        int offset = (page - 1) * pageSize;

        String query = "SELECT f.FeedbackID, f.CustomerID, f.ProductID, f.FeedbackContent, f.Rating, f.FeedbackDate, "
                + "p.ProductName, a.FullName AS CustomerName "
                + "FROM Feedback f "
                + "INNER JOIN Products p ON f.ProductID = p.ProductID "
                + "INNER JOIN Accounts a ON f.CustomerID = a.AccountID "
                + "WHERE LOWER(p.ProductName) LIKE LOWER(?) "
                + "   OR LOWER(a.FullName) LIKE LOWER(?) "
                + "   OR CAST(f.FeedbackID AS NVARCHAR) LIKE ? "
                + "   OR CAST(f.ProductID AS NVARCHAR) LIKE ? "
                + "   OR LOWER(f.FeedbackContent) LIKE LOWER(?) "
                + "ORDER BY f.FeedbackDate DESC "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        return jdbcTemplate.query(query,
                new Object[]{searchTerm, searchTerm, searchTerm, searchTerm, searchTerm, offset, pageSize},
                (rs, rowNum) -> {
                    Feedback fb = new Feedback();
                    fb.setFeedbackID(rs.getInt("FeedbackID"));
                    fb.setCustomerID(rs.getInt("CustomerID"));
                    fb.setProductID(rs.getString("ProductID"));
                    fb.setFeedbackContent(rs.getString("FeedbackContent"));
                    fb.setRating(rs.getShort("Rating"));
                    fb.setFeedbackDate(rs.getTimestamp("FeedbackDate").toLocalDateTime());
                    fb.setCustomerName(rs.getString("CustomerName"));
                    fb.setProductName(rs.getString("ProductName"));

                    fb.setImages(getFeedbackImages(fb.getFeedbackID()));
                    return fb;
                });
    }

    // Search feedback by keyword across multiple fields
    @Override
    public List<Feedback> searchByKeyword(String keyword) {
        String searchTerm = "%" + keyword + "%";

        String query = "SELECT f.FeedbackID, f.CustomerID, f.ProductID, f.FeedbackContent, f.Rating, f.FeedbackDate, "
                + "p.ProductName, a.FullName AS CustomerName "
                + "FROM Feedback f "
                + "INNER JOIN Products p ON f.ProductID = p.ProductID "
                + "INNER JOIN Accounts a ON f.CustomerID = a.AccountID "
                + "WHERE LOWER(p.ProductName) LIKE LOWER(?) "
                + "   OR LOWER(a.FullName) LIKE LOWER(?) "
                + "   OR CAST(f.FeedbackID AS NVARCHAR) LIKE ? "
                + "   OR CAST(f.ProductID AS NVARCHAR) LIKE ? "
                + "   OR LOWER(f.FeedbackContent) LIKE LOWER(?) "
                + "ORDER BY f.FeedbackDate DESC";

        return jdbcTemplate.query(query,
                new Object[]{searchTerm, searchTerm, searchTerm, searchTerm, searchTerm},
                (rs, rowNum) -> {
                    Feedback fb = new Feedback();
                    fb.setFeedbackID(rs.getInt("FeedbackID"));
                    fb.setCustomerID(rs.getInt("CustomerID"));
                    fb.setProductID(rs.getString("ProductID"));
                    fb.setFeedbackContent(rs.getString("FeedbackContent"));
                    fb.setRating(rs.getShort("Rating"));
                    fb.setFeedbackDate(rs.getTimestamp("FeedbackDate").toLocalDateTime());
                    fb.setCustomerName(rs.getString("CustomerName"));
                    fb.setProductName(rs.getString("ProductName"));

                    fb.setImages(getFeedbackImages(fb.getFeedbackID()));
                    return fb;
                });
    }

    // Get employee response for a specific feedback (most recent if multiple exist)
    @Override
    public EmployeeResponse getResponseByFeedbackId(int feedbackID) {
        String query = "SELECT TOP 1 ResponseID, EmployeeID, FeedbackID, ResponseContent, ResponseDate FROM EmployeeResponse WHERE FeedbackID = ? ORDER BY ResponseDate DESC";
        try {
            return jdbcTemplate.queryForObject(query, new Object[]{feedbackID}, (rs, rowNum) -> {
                EmployeeResponse er = new EmployeeResponse();
                er.setResponseID(rs.getInt("ResponseID"));
                er.setEmployeeID(rs.getInt("EmployeeID"));
                er.setFeedbackID(rs.getInt("FeedbackID"));
                er.setResponseContent(rs.getString("ResponseContent"));
                Timestamp timestamp = rs.getTimestamp("ResponseDate");
                if (timestamp != null) {
                    LocalDateTime responseDate = timestamp.toLocalDateTime();
                    er.setResponseDate(responseDate);

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedResponseDate = responseDate.format(formatter);
                    er.setFormattedResponseDate(formattedResponseDate);
                }
                return er;
            });
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            // Fallback: use query method to get first result if TOP 1 doesn't work as expected
            List<EmployeeResponse> responses = jdbcTemplate.query(
                "SELECT ResponseID, EmployeeID, FeedbackID, ResponseContent, ResponseDate FROM EmployeeResponse WHERE FeedbackID = ? ORDER BY ResponseDate DESC",
                new Object[]{feedbackID},
                (rs, rowNum) -> {
                    EmployeeResponse er = new EmployeeResponse();
                    er.setResponseID(rs.getInt("ResponseID"));
                    er.setEmployeeID(rs.getInt("EmployeeID"));
                    er.setFeedbackID(rs.getInt("FeedbackID"));
                    er.setResponseContent(rs.getString("ResponseContent"));
                    Timestamp timestamp = rs.getTimestamp("ResponseDate");
                    if (timestamp != null) {
                        LocalDateTime responseDate = timestamp.toLocalDateTime();
                        er.setResponseDate(responseDate);

                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        String formattedResponseDate = responseDate.format(formatter);
                        er.setFormattedResponseDate(formattedResponseDate);
                    }
                    return er;
                }
            );
            return responses.isEmpty() ? null : responses.get(0);
        }
    }

    // Save new feedback with image files
    @Override
    public void save(Feedback feedback, List<MultipartFile> imageFiles, ServletContext servletContext) {
        List<String> savedImages = new ArrayList<>();
        String uploadPath = new File(servletContext.getRealPath("")).getParentFile().getParentFile().getAbsolutePath()
                + "/web/assets/client/images/uploads/feedbacks/";

        try {
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                throw new RuntimeException("Cannot create folder: " + uploadDir.getAbsolutePath());
            }

            String feedbackQuery = "INSERT INTO Feedback (ProductID, CustomerID, Rating, FeedbackContent, FeedbackDate) "
                    + "OUTPUT INSERTED.FeedbackID "
                    + "VALUES (?, ?, ?, ?, ?)";
            Integer feedbackId = jdbcTemplate.queryForObject(
                    feedbackQuery,
                    new Object[]{
                        feedback.getProductID(),
                        feedback.getCustomerID(),
                        feedback.getRating(),
                        feedback.getFeedbackContent(),
                        Timestamp.valueOf(feedback.getFeedbackDate())
                    },
                    Integer.class
            );

            if (imageFiles != null && !imageFiles.isEmpty()) {
                for (MultipartFile imageFile : imageFiles) {
                    if (!imageFile.isEmpty()) {
                        String fileName = Paths.get(imageFile.getOriginalFilename()).getFileName().toString();
                        String filePath = uploadPath + File.separator + fileName;

                        imageFile.transferTo(new File(filePath));
                        savedImages.add(fileName);

                        String imageQuery = "INSERT INTO FeedbackImage (FeedbackID, Image) VALUES (?, ?)";
                        jdbcTemplate.update(imageQuery, feedbackId, fileName);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving feedback images: " + e.getMessage());
        }
    }

    // Get single feedback by ID
    @Override
    public Feedback get(int feedbackID) {
        String query = "SELECT f.FeedbackID, f.FeedbackContent, f.Rating, f.FeedbackDate, f.ProductID, "
                + "p.ProductName, a.FullName AS CustomerName, fi.Image "
                + "FROM Feedback f "
                + "INNER JOIN Products p ON f.ProductID = p.ProductID "
                + "INNER JOIN Accounts a ON f.CustomerID = a.AccountID "
                + "LEFT JOIN FeedbackImage fi ON f.FeedbackID = fi.FeedbackID "
                + "WHERE f.FeedbackID = ?";

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, feedbackID);
        Feedback fb = null;

        for (Map<String, Object> row : rows) {
            if (fb == null) {
                fb = new Feedback();
                fb.setFeedbackID((Integer) row.get("FeedbackID"));
                fb.setFeedbackContent((String) row.get("FeedbackContent"));
                fb.setRating(((Number) row.get("Rating")).shortValue());

                Timestamp timestamp = (Timestamp) row.get("FeedbackDate");
                if (timestamp != null) {
                    fb.setFeedbackDate(timestamp.toLocalDateTime());
                }

                fb.setCustomerName((String) row.get("CustomerName"));

                Products product = new Products();
                product.setProductID((String) row.get("ProductID"));
                product.setProductName((String) row.get("ProductName"));
                fb.setProduct(product);

                fb.setImages(new ArrayList<>());
            }

            String img = (String) row.get("Image");
            if (img != null) {
                FeedbackImage feedbackImage = new FeedbackImage();
                feedbackImage.setFeedbackID(fb.getFeedbackID());
                feedbackImage.setImage(img);
                fb.getImages().add(feedbackImage);
            }
        }

        return fb;
    }

    // Get paginated feedback for specific product with optional star filter
    @Override
    public List<Feedback> getFeedbacksForProductPaged(String productId, Integer starFilter, int page, int pageSize) {
        if (page < 1) {
            page = 1;
        }

        StringBuilder idSql = new StringBuilder("SELECT FeedbackID, ProductID, CustomerID, Rating, FeedbackContent, FeedbackDate FROM Feedback WHERE ProductID = ?");
        List<Object> idParams = new ArrayList<>();
        idParams.add(productId);

        if (starFilter != null && starFilter > 0) {
            idSql.append(" AND Rating = ?");
            idParams.add(starFilter);
        }

        idSql.append(" ORDER BY FeedbackDate DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        idParams.add((page - 1) * pageSize);
        idParams.add(pageSize);

        List<Map<String, Object>> feedbackRows = jdbcTemplate.queryForList(idSql.toString(), idParams.toArray());
        if (feedbackRows.isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> feedbackIds = new ArrayList<>();
        for (Map<String, Object> r : feedbackRows) {
            feedbackIds.add((Integer) r.get("FeedbackID"));
        }

        StringBuilder sqlImg = new StringBuilder("SELECT FeedbackID, Image FROM FeedbackImage WHERE FeedbackID IN (");
        for (int i = 0; i < feedbackIds.size(); i++) {
            sqlImg.append("?");
            if (i < feedbackIds.size() - 1) {
                sqlImg.append(",");
            }
        }
        sqlImg.append(")");

        List<Map<String, Object>> imgRows = jdbcTemplate.queryForList(sqlImg.toString(), feedbackIds.toArray());

        Map<Integer, List<String>> imgMap = new HashMap<>();
        for (Map<String, Object> r : imgRows) {
            int fid = (Integer) r.get("FeedbackID");
            String image = (String) r.get("Image");
            if (!imgMap.containsKey(fid)) {
                imgMap.put(fid, new ArrayList<String>());
            }
            imgMap.get(fid).add(image);
        }

        StringBuilder sqlResponse = new StringBuilder("SELECT FeedbackID, ResponseContent, ResponseDate FROM EmployeeResponse WHERE FeedbackID IN (");
        for (int i = 0; i < feedbackIds.size(); i++) {
            sqlResponse.append("?");
            if (i < feedbackIds.size() - 1) {
                sqlResponse.append(",");
            }
        }
        sqlResponse.append(")");

        List<Map<String, Object>> responseRows = jdbcTemplate.queryForList(sqlResponse.toString(), feedbackIds.toArray());
        Map<Integer, EmployeeResponse> responseMap = new HashMap<>();

        for (Map<String, Object> r : responseRows) {
            int fid = (Integer) r.get("FeedbackID");
            EmployeeResponse response = new EmployeeResponse();
            response.setFeedbackID(fid);
            response.setResponseContent((String) r.get("ResponseContent"));

            Timestamp responseTs = (Timestamp) r.get("ResponseDate");
            if (responseTs != null) {
                response.setResponseDate(responseTs.toLocalDateTime());
            }

            responseMap.put(fid, response);
        }

        List<Feedback> feedbacks = new ArrayList<>();
        for (Map<String, Object> r : feedbackRows) {
            Feedback fb = new Feedback();
            int feedbackID = (Integer) r.get("FeedbackID");
            fb.setFeedbackID(feedbackID);
            fb.setProductID((String) r.get("ProductID"));
            fb.setCustomerID((Integer) r.get("CustomerID"));
            fb.setRating(((Number) r.get("Rating")).shortValue());
            fb.setFeedbackContent((String) r.get("FeedbackContent"));

            Timestamp ts = (Timestamp) r.get("FeedbackDate");
            if (ts != null) {
                fb.setFeedbackDate(ts.toLocalDateTime());
                fb.setFormattedFeedbackDate(ts.toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }

            String customerName = jdbcTemplate.queryForObject(
                    "SELECT FullName FROM Accounts WHERE AccountID = ?",
                    new Object[]{fb.getCustomerID()},
                    String.class
            );
            fb.setCustomerName(customerName);

            List<FeedbackImage> images = new ArrayList<>();
            List<String> imgList = imgMap.get(feedbackID);
            if (imgList != null) {
                for (String imgName : imgList) {
                    FeedbackImage fi = new FeedbackImage();
                    fi.setFeedbackID(feedbackID);
                    fi.setImage(imgName);
                    images.add(fi);
                }
            }
            fb.setImages(images);

            EmployeeResponse empResponse = responseMap.get(feedbackID);
            if (empResponse != null) {
                fb.setEmployeeResponse(empResponse);
            }

            feedbacks.add(fb);
        }

        return feedbacks;
    }

    // Count total feedback for product with optional star filter
    @Override
    public int countFeedbacksForProduct(String productId, Integer starFilter) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM Feedback WHERE ProductID = ?");
        List<Object> params = new ArrayList<>();
        params.add(productId);

        if (starFilter != null && starFilter > 0) {
            sql.append(" AND Rating = ?");
            params.add(starFilter);
        }

        return jdbcTemplate.queryForObject(sql.toString(), params.toArray(), Integer.class);
    }

    // Get feedback that don't have employee responses
    @Override
    public List<Feedback> getFeedbackWithoutResponse() {
        String query = "SELECT f.FeedbackID, f.ProductID, f.FeedbackContent, f.Rating, f.FeedbackDate, f.FeedbackImage, "
                + "p.ProductName, MIN(pi.Image) AS ProductImage, a.FullName AS CustomerName, er.ResponseContent, er.ResponseDate FROM Feedback f "
                + "INNER JOIN Products p ON f.ProductID = p.ProductID "
                + "LEFT JOIN ProductImage pi ON p.ProductID = pi.ProductID "
                + "INNER JOIN Accounts a ON f.CustomerID = a.AccountID "
                + "LEFT JOIN EmployeeResponse er ON f.FeedbackID = er.FeedbackID "
                + "WHERE er.FeedbackID IS NULL "
                + "GROUP BY f.FeedbackID, f.ProductID, p.ProductName, f.Rating, f.FeedbackContent, f.FeedbackDate, f.FeedbackImage, a.FullName, er.ResponseContent, er.ResponseDate";

        return mapFeedbackList(query);
    }

    // Get feedback that have employee responses
    @Override
    public List<Feedback> getFeedbackWithResponse() {
        String query = "SELECT f.FeedbackID, f.ProductID, f.FeedbackContent, f.Rating, f.FeedbackDate, f.FeedbackImage, "
                + "p.ProductName, MIN(pi.Image) AS ProductImage, a.FullName AS CustomerName, er.ResponseContent, er.ResponseDate FROM Feedback f "
                + "INNER JOIN Products p ON f.ProductID = p.ProductID "
                + "LEFT JOIN ProductImage pi ON p.ProductID = pi.ProductID "
                + "INNER JOIN Accounts a ON f.CustomerID = a.AccountID "
                + "INNER JOIN EmployeeResponse er ON f.FeedbackID = er.FeedbackID WHERE er.EmployeeID = 2"
                + "GROUP BY f.FeedbackID, f.ProductID, p.ProductName, f.Rating, f.FeedbackContent, f.FeedbackDate, f.FeedbackImage, a.FullName, er.ResponseContent, er.ResponseDate";

        return mapFeedbackList(query);
    }

    // Map database rows to Feedback list
    private List<Feedback> mapFeedbackList(String query) {
        List<Feedback> feedbackList = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

        Map<Integer, Feedback> fbMap = new HashMap<>();

        for (Map<String, Object> row : rows) {
            int feedbackID = (Integer) row.get("FeedbackID");
            Feedback feedback = fbMap.get(feedbackID);

            if (feedback == null) {
                feedback = new Feedback();
                feedback.setFeedbackID(feedbackID);
                feedback.setFeedbackContent((String) row.get("FeedbackContent"));
                feedback.setRating(((Number) row.get("Rating")).shortValue());

                Timestamp timestampFeedbackDate = (Timestamp) row.get("FeedbackDate");
                LocalDateTime dateTimeFeedbackDate = timestampFeedbackDate != null ? timestampFeedbackDate.toLocalDateTime() : null;
                feedback.setFeedbackDate(dateTimeFeedbackDate);
                if (dateTimeFeedbackDate != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    feedback.setFormattedFeedbackDate(dateTimeFeedbackDate.format(formatter));
                }

                feedback.setProductID((String) row.get("ProductID"));

                Products product = new Products();
                product.setProductName((String) row.get("ProductName"));

                feedback.setProduct(product);
                feedback.setCustomerName((String) row.get("CustomerName"));

                String responseContent = (String) row.get("ResponseContent");
                if (responseContent != null) {
                    feedback.setResponseContent(responseContent);
                }

                Timestamp responseDate = (Timestamp) row.get("ResponseDate");
                if (responseDate != null) {
                    feedback.setResponseDate(responseDate.toLocalDateTime());
                }

                feedback.setImages(new ArrayList<>());
                fbMap.put(feedbackID, feedback);
            }

            String img = (String) row.get("FeedbackImage");
            if (img != null) {
                FeedbackImage feedbackImage = new FeedbackImage();
                feedbackImage.setFeedbackID(feedbackID);
                feedbackImage.setImage(img);
                feedback.getImages().add(feedbackImage);
            }

            String productImagePath = (String) row.get("ProductImage");
            if (productImagePath != null) {
                ProductImage productImage = new ProductImage();
                productImage.setImages(productImagePath);
                feedback.getProduct().setImages(Collections.singletonList(productImage));
            }
        }

        return new ArrayList<>(fbMap.values());
    }

    // Map database rows to Feedback list with images loaded separately
    private List<Feedback> mapFeedbackListWithImages(String query) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        Map<Integer, Feedback> fbMap = new LinkedHashMap<>();

        for (Map<String, Object> row : rows) {
            int feedbackID = (Integer) row.get("FeedbackID");

            if (!fbMap.containsKey(feedbackID)) {
                Feedback feedback = new Feedback();
                feedback.setFeedbackID(feedbackID);
                feedback.setFeedbackContent((String) row.get("FeedbackContent"));
                feedback.setRating(((Number) row.get("Rating")).shortValue());

                Timestamp timestampFeedbackDate = (Timestamp) row.get("FeedbackDate");
                LocalDateTime dateTimeFeedbackDate = timestampFeedbackDate != null
                        ? timestampFeedbackDate.toLocalDateTime() : null;
                feedback.setFeedbackDate(dateTimeFeedbackDate);

                if (dateTimeFeedbackDate != null) {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    feedback.setFormattedFeedbackDate(dateTimeFeedbackDate.format(formatter));
                }

                feedback.setProductID((String) row.get("ProductID"));

                Products product = new Products();
                product.setProductName((String) row.get("ProductName"));

                String productImagePath = (String) row.get("ProductImage");
                if (productImagePath != null) {
                    ProductImage productImage = new ProductImage();
                    productImage.setImages(productImagePath);
                    product.setImages(Collections.singletonList(productImage));
                } else {
                    product.setImages(new ArrayList<>());
                }

                feedback.setProduct(product);
                feedback.setCustomerName((String) row.get("CustomerName"));

                String responseContent = (String) row.get("ResponseContent");
                if (responseContent != null) {
                    feedback.setResponseContent(responseContent);
                }

                Timestamp responseDate = (Timestamp) row.get("ResponseDate");
                if (responseDate != null) {
                    feedback.setResponseDate(responseDate.toLocalDateTime());
                }

                feedback.setImages(new ArrayList<>());
                fbMap.put(feedbackID, feedback);
            }
        }

        if (!fbMap.isEmpty()) {
            List<Integer> feedbackIds = new ArrayList<>(fbMap.keySet());

            StringBuilder inClause = new StringBuilder();
            for (int i = 0; i < feedbackIds.size(); i++) {
                inClause.append("?");
                if (i < feedbackIds.size() - 1) {
                    inClause.append(",");
                }
            }

            String imageQuery = "SELECT FeedbackID, Image FROM FeedbackImage WHERE FeedbackID IN (" + inClause + ")";
            List<Map<String, Object>> imageRows = jdbcTemplate.queryForList(imageQuery, feedbackIds.toArray());

            for (Map<String, Object> imageRow : imageRows) {
                int feedbackID = (Integer) imageRow.get("FeedbackID");
                String imagePath = (String) imageRow.get("Image");

                Feedback feedback = fbMap.get(feedbackID);
                if (feedback != null && imagePath != null) {
                    FeedbackImage feedbackImage = new FeedbackImage();
                    feedbackImage.setFeedbackID(feedbackID);
                    feedbackImage.setImage(imagePath);
                    feedback.getImages().add(feedbackImage);
                }
            }
        }

        return new ArrayList<>(fbMap.values());
    }

    // Find feedback with pagination by offset and page size
    @Override
    public List<Feedback> findFeedbackWithPagination(int offset, int pageSize) {
        String query = "SELECT f.FeedbackID, f.CustomerID, f.ProductID, f.FeedbackContent, f.Rating, f.FeedbackDate, "
                + "p.ProductName, a.FullName AS CustomerName "
                + "FROM Feedback f "
                + "INNER JOIN Products p ON f.ProductID = p.ProductID "
                + "INNER JOIN Accounts a ON f.CustomerID = a.AccountID "
                + "ORDER BY f.FeedbackDate DESC "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, offset, pageSize);
            Map<Integer, Feedback> fbMap = new HashMap<>();

            for (Map<String, Object> row : rows) {
                int feedbackID = (Integer) row.get("FeedbackID");
                Feedback fb = new Feedback();

                fb.setFeedbackID(feedbackID);
                fb.setCustomerID((Integer) row.get("CustomerID"));
                fb.setProductID((String) row.get("ProductID"));
                fb.setFeedbackContent((String) row.get("FeedbackContent"));
                fb.setRating(((Number) row.get("Rating")).shortValue());

                Timestamp timestamp = (Timestamp) row.get("FeedbackDate");
                if (timestamp != null) {
                    fb.setFeedbackDate(timestamp.toLocalDateTime());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    fb.setFormattedFeedbackDate(timestamp.toLocalDateTime().format(formatter));
                }

                fb.setCustomerName((String) row.get("CustomerName"));
                fb.setProductName((String) row.get("ProductName"));

                fb.setImages(getFeedbackImages(feedbackID));
                fbMap.put(feedbackID, fb);
            }

            return new ArrayList<>(fbMap.values());
        } catch (Exception e) {
            System.err.println("Error finding feedback with pagination: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Load feedback images for a specific feedback ID
    private List<FeedbackImage> getFeedbackImages(int feedbackID) {
        String query = "SELECT FeedbackID, Image FROM FeedbackImage WHERE FeedbackID = ?";
        try {
            List<Map<String, Object>> imageRows = jdbcTemplate.queryForList(query, feedbackID);
            List<FeedbackImage> images = new ArrayList<>();

            for (Map<String, Object> row : imageRows) {
                FeedbackImage feedbackImage = new FeedbackImage();
                feedbackImage.setFeedbackID((Integer) row.get("FeedbackID"));
                feedbackImage.setImage((String) row.get("Image"));
                images.add(feedbackImage);
            }

            return images;
        } catch (Exception e) {
            System.err.println("Error loading feedback images for ID " + feedbackID + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Get total count of all feedback
    @Override
    public int getTotalFeedbackCount() {
        String sql = "SELECT COUNT(*) FROM Feedback f "
                + "INNER JOIN Products p ON f.ProductID = p.ProductID "
                + "INNER JOIN Accounts a ON f.CustomerID = a.AccountID";

        try {
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            System.err.println("Error getting total feedback count: " + e.getMessage());
            return 0;
        }
    }

    // Get total feedback count with keyword search
    @Override
    public int getTotalFeedbackCount(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getTotalFeedbackCount();
        }

        String sql = "SELECT COUNT(*) FROM Feedback f "
                + "INNER JOIN Products p ON f.ProductID = p.ProductID "
                + "INNER JOIN Accounts a ON f.CustomerID = a.AccountID "
                + "WHERE LOWER(p.ProductName) LIKE LOWER(?) "
                + "OR LOWER(a.FullName) LIKE LOWER(?) "
                + "OR CAST(f.FeedbackID AS NVARCHAR) LIKE ? "
                + "OR CAST(f.ProductID AS NVARCHAR) LIKE ? "
                + "OR LOWER(f.FeedbackContent) LIKE LOWER(?)";

        String searchTerm = "%" + keyword + "%";

        try {
            return jdbcTemplate.queryForObject(sql, Integer.class,
                    searchTerm, searchTerm, searchTerm, searchTerm, searchTerm);
        } catch (Exception e) {
            System.err.println("Error getting total feedback count with keyword: " + e.getMessage());
            return 0;
        }
    }

    // Find feedback with pagination and optional keyword search
    @Override
    public List<Feedback> findFeedbackWithPagination(int offset, int pageSize, String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return findFeedbackWithPagination(offset, pageSize);
        }

        String query = "SELECT f.FeedbackID, f.CustomerID, f.ProductID, f.FeedbackContent, f.Rating, f.FeedbackDate, "
                + "p.ProductName, a.FullName AS CustomerName "
                + "FROM Feedback f "
                + "INNER JOIN Products p ON f.ProductID = p.ProductID "
                + "INNER JOIN Accounts a ON f.CustomerID = a.AccountID "
                + "WHERE LOWER(p.ProductName) LIKE LOWER(?) "
                + "OR LOWER(a.FullName) LIKE LOWER(?) "
                + "OR CAST(f.FeedbackID AS NVARCHAR) LIKE ? "
                + "OR CAST(f.ProductID AS NVARCHAR) LIKE ? "
                + "OR LOWER(f.FeedbackContent) LIKE LOWER(?) "
                + "ORDER BY f.FeedbackDate DESC "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        String searchTerm = "%" + keyword + "%";

        try {
            List<Map<String, Object>> rows = jdbcTemplate.queryForList(query,
                    searchTerm, searchTerm, searchTerm, searchTerm, searchTerm, offset, pageSize);
            Map<Integer, Feedback> fbMap = new HashMap<>();

            for (Map<String, Object> row : rows) {
                int feedbackID = (Integer) row.get("FeedbackID");
                Feedback fb = new Feedback();

                fb.setFeedbackID(feedbackID);
                fb.setCustomerID((Integer) row.get("CustomerID"));
                fb.setProductID((String) row.get("ProductID"));
                fb.setFeedbackContent((String) row.get("FeedbackContent"));
                fb.setRating(((Number) row.get("Rating")).shortValue());

                Timestamp timestamp = (Timestamp) row.get("FeedbackDate");
                if (timestamp != null) {
                    fb.setFeedbackDate(timestamp.toLocalDateTime());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    fb.setFormattedFeedbackDate(timestamp.toLocalDateTime().format(formatter));
                }

                fb.setCustomerName((String) row.get("CustomerName"));
                fb.setProductName((String) row.get("ProductName"));
                fb.setImages(getFeedbackImages(feedbackID));
                fbMap.put(feedbackID, fb);
            }

            return new ArrayList<>(fbMap.values());
        } catch (Exception e) {
            System.err.println("Error finding feedback with pagination and keyword: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
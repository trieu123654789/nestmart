package com.models;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class OffersDAOImpl implements OffersDAO {

    private JdbcTemplate jdbcTemplate;

    public OffersDAOImpl() {
    }

    public OffersDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Retrieves all offers with related product and discount information
    @Override
    public List<Offers> findAll() {
        String query = "SELECT o.offerID, o.productID, d.discountID, o.offerName, o.description, "
                + " p.productName, p.unitPrice, p.Discount, d.discountName, o.discountType, o.discountValue "
                + "FROM Offers o "
                + "JOIN Products p ON o.productID = p.productID "
                + "JOIN Discounts d ON o.discountID = d.discountID";
        List<Offers> offerList = new ArrayList<>();
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);

        for (Map<String, Object> row : rows) {
            Offers c = new Offers(
                    (int) row.get("OfferID"),
                    (String) row.get("ProductID"),
                    (int) row.get("DiscountID"),
                    (String) row.get("OfferName"),
                    (String) row.get("Description"),
                    (String) row.get("ProductName"),
                    (String) row.get("DiscountName"),
                    (BigDecimal) row.get("UnitPrice"),
                    (BigDecimal) row.get("Discount"),
                    (String) row.get("DiscountType"),
                    ((Number) row.get("DiscountValue")).intValue()
            );
            offerList.add(c);
        }
        return offerList;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Saves a new offer and updates the corresponding product discount
    @Override
    public void save(Offers offers) {
        String checkQuery = "SELECT COUNT(*) FROM Offers WHERE ProductID = ? AND DiscountID = ?";
        Integer count = jdbcTemplate.queryForObject(checkQuery, Integer.class, offers.getProductID(), offers.getDiscountID());

        if (count == null || count == 0) {
            updateProductDiscount(offers.getProductID(), offers.getDiscountType(), offers.getDiscountValue());

            String query = "INSERT INTO Offers (ProductID, DiscountID, OfferName, Description, DiscountType, DiscountValue) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(query,
                    offers.getProductID(),
                    offers.getDiscountID(),
                    offers.getOfferName(),
                    offers.getDescription(),
                    offers.getDiscountType(),
                    offers.getDiscountValue()
            );
        } else {
            throw new RuntimeException("Offer already exists.");
        }
    }

    // Returns paginated list of offers with optional keyword search
    @Override
    public List<Offers> getPagedOffers(String keyword, int page, int pageSize) {
        String query = "SELECT o.offerID, o.productID, d.discountID, o.offerName, o.description, "
                + "       p.productName, d.discountName, p.UnitPrice, p.Discount, "
                + "       o.discountType, o.discountValue "
                + "FROM Offers o "
                + "JOIN Products p ON o.productID = p.productID "
                + "JOIN Discounts d ON o.discountID = d.discountID "
                + "WHERE o.offerName COLLATE Latin1_General_CI_AI LIKE ? "
                + "   OR p.productName COLLATE Latin1_General_CI_AI LIKE ? "
                + "   OR d.discountName COLLATE Latin1_General_CI_AI LIKE ? "
                + "   OR o.discountType COLLATE Latin1_General_CI_AI LIKE ? "
                + "ORDER BY o.offerID "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        String searchKeyword = "%" + keyword + "%";
        int offset = (page - 1) * pageSize;

        return jdbcTemplate.query(query,
                new Object[]{searchKeyword, searchKeyword, searchKeyword, searchKeyword, offset, pageSize},
                new RowMapper<Offers>() {
            @Override
            public Offers mapRow(ResultSet rs, int rowNum) throws SQLException {
                Offers offer = new Offers();
                offer.setOfferID(rs.getInt("offerID"));
                offer.setProductID(rs.getString("productID"));
                offer.setDiscountID(rs.getInt("discountID"));
                offer.setOfferName(rs.getString("offerName"));
                offer.setDescription(rs.getString("description"));
                offer.setProductName(rs.getString("productName"));
                offer.setDiscountName(rs.getString("discountName"));
                offer.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                offer.setPriceAfteroffer(rs.getBigDecimal("Discount"));
                offer.setDiscountType(rs.getString("discountType"));
                offer.setDiscountValue(rs.getInt("discountValue"));
                return offer;
            }
        });
    }

    // Checks if an offer already exists for the same product and discount
    @Override
    public boolean checkIfExists(Offers offers) {
        String query = "SELECT COUNT(*) FROM Offers WHERE ProductID = ? AND DiscountID = ?";
        int count = jdbcTemplate.queryForObject(query, new Object[]{offers.getProductID(), offers.getDiscountID()}, Integer.class);
        return count > 0;
    }

    // Updates product's discount price based on discount type and value
    private void updateProductDiscount(String productId, String discountType, int discountValue) {
        String priceQuery = "SELECT UnitPrice FROM Products WHERE ProductID = ?";
        BigDecimal unitPrice = jdbcTemplate.queryForObject(priceQuery, BigDecimal.class, productId);

        BigDecimal discountedPrice;

        if ("Discount of money".equalsIgnoreCase(discountType)) {
            discountedPrice = unitPrice.subtract(new BigDecimal(discountValue));
            if (discountedPrice.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("The discounted price cannot be negative.");
            }
            if (new BigDecimal(discountValue).compareTo(unitPrice) > 0) {
                throw new RuntimeException("Discount value cannot be greater than the product price.");
            }
        } else if ("% Discount".equalsIgnoreCase(discountType)) {
            BigDecimal discountPercentage = new BigDecimal(discountValue).divide(new BigDecimal(100));
            discountedPrice = unitPrice.subtract(unitPrice.multiply(discountPercentage));
            if (discountedPrice.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("The discounted price cannot be negative.");
            }
        } else {
            throw new RuntimeException("Invalid discount type.");
        }

        String updateQuery = "UPDATE Products SET Discount = ? WHERE ProductID = ?";
        jdbcTemplate.update(updateQuery, discountedPrice, productId);
    }

    // Updates an existing offer and recalculates product discount
    @Override
    public void update(Offers offers) {
        String priceQuery = "SELECT UnitPrice FROM Products WHERE ProductID = ?";
        BigDecimal unitPrice = jdbcTemplate.queryForObject(priceQuery, BigDecimal.class, offers.getProductID());

        BigDecimal discountedPrice;

        if ("Discount of money".equalsIgnoreCase(offers.getDiscountType())) {
            discountedPrice = unitPrice.subtract(new BigDecimal(offers.getDiscountValue()));
            if (discountedPrice.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("The discounted price cannot be negative.");
            }
        } else if ("% Discount".equalsIgnoreCase(offers.getDiscountType())) {
            BigDecimal discountPercentage = new BigDecimal(offers.getDiscountValue()).divide(new BigDecimal(100));
            discountedPrice = unitPrice.subtract(unitPrice.multiply(discountPercentage));
            if (discountedPrice.compareTo(BigDecimal.ZERO) < 0) {
                throw new RuntimeException("The discounted price cannot be negative.");
            }
        } else {
            throw new RuntimeException("Invalid discount type.");
        }

        String query = "UPDATE Offers SET ProductID = ?, DiscountID = ?, OfferName = ?, Description = ?, "
                + " DiscountType = ?, DiscountValue = ? WHERE OfferID = ?";
        jdbcTemplate.update(query,
                offers.getProductID(),
                offers.getDiscountID(),
                offers.getOfferName(),
                offers.getDescription(),
                offers.getDiscountType(),
                offers.getDiscountValue(),
                offers.getOfferID()
        );

        updateProductPrices(offers.getOfferID(), offers.getDiscountType(), offers.getDiscountValue());
    }

    // Updates product discount price when offer is updated
    private void updateProductPrices(int offerID, String discountType, int discountValue) {
        String productQuery = "SELECT p.ProductID, p.UnitPrice FROM Products p JOIN Offers o ON p.ProductID = o.ProductID WHERE o.OfferID = ?";
        List<Map<String, Object>> products = jdbcTemplate.queryForList(productQuery, offerID);

        for (Map<String, Object> product : products) {
            String productId = (String) product.get("ProductID");
            BigDecimal unitPrice = (BigDecimal) product.get("UnitPrice");
            BigDecimal discountedPrice;

            if ("Discount of money".equalsIgnoreCase(discountType)) {
                discountedPrice = unitPrice.subtract(new BigDecimal(discountValue));
            } else if ("% Discount".equalsIgnoreCase(discountType)) {
                BigDecimal discountPercentage = new BigDecimal(discountValue).divide(new BigDecimal(100));
                discountedPrice = unitPrice.multiply(discountPercentage);
            } else {
                continue;
            }

            String updatePriceQuery = "UPDATE Products SET Discount = ? WHERE ProductID = ?";
            jdbcTemplate.update(updatePriceQuery, discountedPrice, productId);
        }
    }

    // Deletes an offer by ID and resets the product discount
    @Override
    public void deleteById(int id) {
        String updateProductsQuery = "UPDATE Products SET Discount = 0 WHERE ProductID IN (SELECT ProductID FROM Offers WHERE OfferID = ?)";
        jdbcTemplate.update(updateProductsQuery, id);
        String query = "DELETE FROM Offers WHERE OfferID = ?";
        jdbcTemplate.update(query, id);
    }

    // Retrieves an offer by its ID
    @Override
    public Offers findById(int id) {
        String query = "SELECT o.offerID, o.productID, d.discountID, o.offerName, o.description, "
                + " p.productName, d.discountName, p.UnitPrice, p.Discount, o.discountType, o.discountValue "
                + "FROM Offers o "
                + "JOIN Products p ON o.productID = p.productID "
                + "JOIN Discounts d ON o.discountID = d.discountID "
                + "WHERE o.OfferID=?";

        return jdbcTemplate.queryForObject(query, new Object[]{id}, new RowMapper<Offers>() {
            @Override
            public Offers mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Offers(
                        rs.getInt("OfferID"),
                        rs.getString("ProductID"),
                        rs.getInt("DiscountID"),
                        rs.getString("OfferName"),
                        rs.getString("Description"),
                        rs.getString("ProductName"),
                        rs.getString("DiscountName"),
                        rs.getBigDecimal("UnitPrice"),
                        rs.getBigDecimal("Discount"),
                        rs.getString("DiscountType"),
                        rs.getInt("DiscountValue")
                );
            }
        });
    }

    // Returns paginated list of offers without filtering
    @Override
    public List<Offers> findPaginated(int page, int pageSize) {
        if (page < 1 || pageSize <= 0) {
            throw new IllegalArgumentException("Invalid page number or page size");
        }

        String query = "SELECT o.offerID, o.productID, d.discountID, o.offerName, o.description, "
                + " p.productName, d.discountName, p.UnitPrice, p.Discount, o.discountType, o.discountValue "
                + "FROM Offers o "
                + "JOIN Products p ON o.productID = p.productID "
                + "JOIN Discounts d ON o.discountID = d.discountID "
                + "ORDER BY o.offerID "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        int offset = (page - 1) * pageSize;

        return jdbcTemplate.query(query, new Object[]{offset, pageSize}, new RowMapper<Offers>() {
            @Override
            public Offers mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Offers(
                        rs.getInt("offerID"),
                        rs.getString("productID"),
                        rs.getInt("discountID"),
                        rs.getString("offerName"),
                        rs.getString("description"),
                        rs.getString("productName"),
                        rs.getString("discountName"),
                        rs.getBigDecimal("UnitPrice"),
                        rs.getBigDecimal("Discount"),
                        rs.getString("DiscountType"),
                        rs.getInt("DiscountValue")
                );
            }
        });
    }

    // Searches offers by keyword in multiple fields
    @Override
    public List<Offers> searchByKeyword(String keyword) {
        String query = "SELECT o.offerID, o.productID, d.discountID, o.offerName, o.description, "
                + "       p.productName, d.discountName, p.UnitPrice, p.Discount, "
                + "       o.discountType, o.discountValue "
                + "FROM Offers o "
                + "JOIN Products p ON o.productID = p.productID "
                + "JOIN Discounts d ON o.discountID = d.discountID "
                + "WHERE o.offerName COLLATE Latin1_General_CI_AI LIKE ? "
                + "   OR o.description COLLATE Latin1_General_CI_AI LIKE ? "
                + "   OR p.productName COLLATE Latin1_General_CI_AI LIKE ? "
                + "   OR d.discountName COLLATE Latin1_General_CI_AI LIKE ? "
                + "   OR o.discountType COLLATE Latin1_General_CI_AI LIKE ? "
                + "ORDER BY o.offerID";

        return jdbcTemplate.query(
                query,
                new Object[]{
                    "%" + keyword + "%",
                    "%" + keyword + "%",
                    "%" + keyword + "%",
                    "%" + keyword + "%",
                    "%" + keyword + "%"
                },
                (rs, rowNum) -> {
                    Offers offer = new Offers();
                    offer.setOfferID(rs.getInt("offerID"));
                    offer.setProductID(rs.getString("productID"));
                    offer.setDiscountID(rs.getInt("discountID"));
                    offer.setOfferName(rs.getString("offerName"));
                    offer.setDescription(rs.getString("description"));
                    offer.setProductName(rs.getString("productName"));
                    offer.setDiscountName(rs.getString("discountName"));
                    offer.setUnitPrice(rs.getBigDecimal("UnitPrice"));
                    offer.setPriceAfteroffer(rs.getBigDecimal("Discount"));
                    offer.setDiscountType(rs.getString("DiscountType"));
                    offer.setDiscountValue(rs.getInt("DiscountValue"));
                    return offer;
                }
        );
    }

    // Counts total number of offers matching a keyword
    @Override
    public int countByKeyword(String keyword) {
        String query = "SELECT COUNT(*) FROM Offers o "
                + "JOIN Products p ON o.productID = p.productID "
                + "JOIN Discounts d ON o.discountID = d.discountID "
                + "WHERE p.productName LIKE ? OR d.discountName LIKE ?";

        String searchKeyword = "%" + keyword + "%";
        return jdbcTemplate.queryForObject(query, new Object[]{searchKeyword, searchKeyword}, Integer.class);
    }

    // Returns total number of offers in the database
    @Override
    public int getTotalOffers() {
        String query = "SELECT COUNT(*) FROM Offers";
        return jdbcTemplate.queryForObject(query, Integer.class);
    }
}

package com.models;

import java.util.List;
import java.util.Map;

public interface ProductsClientDAO {

    List<ProductsClient> findAll();

    ProductsClient findById(String id);

    int getTotalProducts();

    List<ProductsClient> findPaginated(int page, int pageSize);

    List<ProductsClient> findByCategoryId(int categoryId);

    Map<String, String> getProductNames(List<String> productIds);

    List<ProductsClient> searchByKeyword(String keyword);

    String findClosestMatch(String keyword);

    public List<ProductsClient> select5random();

    int levenshteinDistance(String a, String b);

    public List<ProductsClient> searchByProductName(String productName, int page, int pageSize);

    public int countByKeyword(String keyword);

    public List<ProductsClient> findByCategory(int categoryID, int page, int pageSize);

    public int countByCategory(int categoryID);

    public List<ProductsClient> select10random();

    public DiscountClient getNewestDiscountSingle();

    public List<ProductsClient> findRandom10ProductsByDiscount(int discountID);
}

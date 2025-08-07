package com.models;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import org.springframework.web.multipart.MultipartFile;

public interface ProductsDAO {

    List<Products> findAll();

    void save(Products product, List<MultipartFile> imageFiles, ServletContext servletContext);

    public String getProductNameById(String productId);

    Products findById(String id);

    public int getProductQuantity(String productID);

    void update(Products product, List<MultipartFile> imageFiles, List<String> imagesToDelete, ServletContext servletContext);

    void deleteById(String id);

    int getTotalProducts();

    public String formatPrice(BigDecimal price);

    List<Products> findPaginated(int page, int pageSize);

    List<Products> findByCategoryId(int categoryId);

    public List<Products> findAllProductsForOffers();

    public void updateProductQuantity(String productId, int quantitySold);

    Map<String, String> getProductNames(List<String> productIds);

    boolean isProductSold(String productId);

    public List<Products> searchByKeyword(String keyword);

    public List<Products> getPagedProducts(String keyword, int page, int pageSize);

    public void restoreProductQuantity(String productId, int quantityToAdd);

}

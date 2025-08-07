/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.models;

import java.util.List;

public interface CategoryDetailsDAO {

    List<CategoryDetails> findAll();

    CategoryDetails findById(int categoryDetailID);

    void save(CategoryDetails categoryDetails);

    void deleteById(int categoryDetailID);

    List<CategoryDetails> findByCategoryId(int categoryId);

    List<Products> findProductsByCategoryId(int categoryId);

    void update(CategoryDetails categoryDetails);

    int getTotalCategoryDetails();

    List<CategoryDetails> findPaginated(int page, int pageSize);

    List<CategoryDetails> searchByKeyword(String keyword);

    List<AttributeDetail> findByProductId(String productId);

}

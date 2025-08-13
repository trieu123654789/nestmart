package com.controllers;

import com.models.Categories;
import com.models.CategoryDetails;
import com.models.CategoryDetailsDAO;
import com.models.CategoriesDAO;
import com.models.Products;
import com.models.ProductsDAO;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utils.RoleUtils;

@Controller
@RequestMapping("/admin")
public class CategoryDetailsController {

    @Autowired
    private CategoryDetailsDAO categoryDetailsDAO;
    @Autowired
    private CategoriesDAO categoriesDAO;
    @Autowired
    private ProductsDAO productsDAO;

    // Get list of products by category in JSON format
    @ResponseBody
    @GetMapping(value = "getProductsByCategory", produces = "application/json")
    public List<Products> getProductsByCategory(@RequestParam("categoryID") Integer categoryID) {
        return categoryDetailsDAO.findProductsByCategoryId(categoryID);
    }

    // Display paginated category details with optional keyword search
    @GetMapping("/categoryDetail")
    public String showCategoryDetails(@RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "8") int pageSize,
                                      @RequestParam(value = "keyword", required = false) String keyword,
                                      ModelMap model,
                                      HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.ADMIN_ROLE);
        if (redirect != null) return redirect;

        List<CategoryDetails> listCategoryDetails;
        int totalCategoryDetails;
        String finalKeyword = (keyword != null) ? keyword.trim() : null;

        if (finalKeyword != null && !finalKeyword.isEmpty()) {
            List<CategoryDetails> allResults = categoryDetailsDAO.searchByKeyword(finalKeyword);
            totalCategoryDetails = allResults.size();
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalCategoryDetails);
            listCategoryDetails = (fromIndex < toIndex) ? allResults.subList(fromIndex, toIndex) : new ArrayList<>();
        } else {
            totalCategoryDetails = categoryDetailsDAO.getTotalCategoryDetails();
            listCategoryDetails = categoryDetailsDAO.findPaginated(page, pageSize);
        }

        model.addAttribute("listCategoryDetails", listCategoryDetails);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) totalCategoryDetails / pageSize));
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keyword", finalKeyword);
        model.addAttribute("totalCategoryDetails", totalCategoryDetails);

        return "/admin/categoryDetail";
    }

    // Show form to create category details
    @GetMapping("/categoryDetailCreate")
    public String showCategoryDetailsCreateForm(@RequestParam(value = "categoryID", required = false) Integer categoryID,
                                                ModelMap model,
                                                HttpServletRequest request,
                                                RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.ADMIN_ROLE);
        if (redirect != null) return redirect;

        List<Categories> listCategories = categoriesDAO.findCategoriesWithProducts();
        model.addAttribute("listCategories", listCategories);

        if (categoryID == null && !listCategories.isEmpty()) {
            categoryID = listCategories.get(0).getCategoryID();
        }

        List<Products> listProducts = (categoryID != null) ? categoryDetailsDAO.findProductsByCategoryId(categoryID) : new ArrayList<>();
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("categoryID", categoryID);
        model.addAttribute("attributeCount", 1);

        return "/admin/categoryDetailCreate";
    }

    // Handle creation of category details
    @PostMapping("/categoryDetailCreate")
    public String createCategoryDetails(@RequestParam("categoryID") int categoryID,
                                        @RequestParam(value = "productID", required = false) String productID,
                                        @RequestParam("attributeCount") int attributeCount,
                                        @RequestParam(value = "attributeName", required = false) List<String> attributeNames,
                                        @RequestParam(value = "attributeValue", required = false) List<String> attributeValues,
                                        Model model,
                                        HttpServletRequest request,
                                        RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.ADMIN_ROLE);
        if (redirect != null) return redirect;

        if (attributeNames == null || attributeValues == null
                || attributeNames.isEmpty() || attributeValues.isEmpty()
                || attributeNames.size() != attributeCount || attributeValues.size() != attributeCount) {

            model.addAttribute("errorMessage", "Please fill out all attribute names and values.");
            model.addAttribute("listCategories", categoriesDAO.findAll());
            model.addAttribute("listProducts", categoryDetailsDAO.findProductsByCategoryId(categoryID));
            model.addAttribute("attributeCount", attributeCount);
            return "/admin/categoryDetailCreate";
        }

        for (int i = 0; i < attributeCount; i++) {
            CategoryDetails categoryDetails = new CategoryDetails();
            categoryDetails.setCategoryID(categoryID);
            categoryDetails.setProductID(productID);
            categoryDetails.setAttributeName(attributeNames.get(i));
            categoryDetails.setAttributeValue(attributeValues.get(i));
            categoryDetailsDAO.save(categoryDetails);
        }

        return "redirect:/admin/categoryDetail.htm";
    }

    // Show form to update category detail
    @GetMapping("/categoryDetailUpdate")
    public String showCategoryDetailsUpdateForm(@RequestParam("categoryDetailID") int categoryDetailID,
                                                ModelMap model,
                                                HttpServletRequest request,
                                                RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.ADMIN_ROLE);
        if (redirect != null) return redirect;

        CategoryDetails categoryDetails = categoryDetailsDAO.findById(categoryDetailID);
        model.addAttribute("categoryDetails", categoryDetails);

        List<Categories> listCategories = categoriesDAO.findAll();
        model.addAttribute("listCategories", listCategories);

        List<Products> listProducts = productsDAO.findByCategoryId(categoryDetails.getCategoryID());
        model.addAttribute("listProducts", listProducts);
        model.addAttribute("attributeCount", 1);

        return "/admin/categoryDetailUpdate";
    }

    // Handle update of category detail
    @PostMapping("/categoryDetailUpdate")
    public String updateCategoryDetails(@RequestParam("categoryDetailID") int categoryDetailID,
                                        @RequestParam("attributeName") String attributeName,
                                        @RequestParam("attributeValue") String attributeValue,
                                        Model model,
                                        HttpServletRequest request,
                                        RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.ADMIN_ROLE);
        if (redirect != null) return redirect;

        try {
            CategoryDetails categoryDetails = categoryDetailsDAO.findById(categoryDetailID);
            categoryDetails.setAttributeName(attributeName);
            categoryDetails.setAttributeValue(attributeValue);
            categoryDetailsDAO.update(categoryDetails);
            return "redirect:/admin/categoryDetail.htm";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while updating the category details.");
            return "/admin/categoryDetailUpdate";
        }
    }

    // Delete a category detail by ID
    @GetMapping("/categoryDetailDelete")
    public String deleteCategoryDetail(@RequestParam("categoryDetailID") int id,
                                       HttpServletRequest request,
                                       RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.ADMIN_ROLE);
        if (redirect != null) return redirect;

        categoryDetailsDAO.deleteById(id);
        return "redirect:/admin/categoryDetail.htm";
    }
}

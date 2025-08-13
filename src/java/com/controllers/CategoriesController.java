package com.controllers;

import com.models.Categories;
import com.models.CategoriesDAO;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utils.RoleUtils;

@Controller
@RequestMapping("/admin")
public class CategoriesController {

    @Autowired
    private CategoriesDAO categoriesDAO;

    // Display paginated list of categories with optional search
    @GetMapping("/categories")
    public String showCategories(ModelMap model,
                                 @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "12") int pageSize,
                                 @RequestParam(value = "keyword", required = false) String keyword,
                                 HttpServletRequest request,
                                 RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.ADMIN_ROLE);
        if (redirect != null) return redirect;

        List<Categories> listCategories;
        int totalCategories;
        String finalKeyword = (keyword != null) ? keyword.trim() : null;

        if (finalKeyword != null && !finalKeyword.isEmpty()) {
            List<Categories> allResults = categoriesDAO.searchByKeyword(finalKeyword);
            totalCategories = allResults.size();
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalCategories);
            listCategories = (fromIndex < toIndex) ? allResults.subList(fromIndex, toIndex) : new ArrayList<>();
        } else {
            totalCategories = categoriesDAO.getTotalCategories();
            listCategories = categoriesDAO.findPaginated(page, pageSize);
        }

        model.addAttribute("listCategories", listCategories);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) totalCategories / pageSize));
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keyword", finalKeyword);
        model.addAttribute("totalCategories", totalCategories);

        return "/admin/category";
    }

    // Show form to create a new category
    @GetMapping("/categoryCreate")
    public String showCreateCategoryForm(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.ADMIN_ROLE);
        if (redirect != null) return redirect;

        model.addAttribute("category", new Categories());
        return "/admin/categoryCreate";
    }

    // Handle creation of a new category
    @PostMapping("/categoryCreate")
    public String createCategory(@ModelAttribute("category") Categories category,
                                 HttpServletRequest request,
                                 RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.ADMIN_ROLE);
        if (redirect != null) return redirect;

        categoriesDAO.save(category);
        return "redirect:/admin/categories.htm";
    }

    // Show form to edit an existing category
    @GetMapping("/categoryUpdate")
    public String showEditCategoryForm(@RequestParam("categoryID") int id,
                                       Model model,
                                       HttpServletRequest request,
                                       RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.ADMIN_ROLE);
        if (redirect != null) return redirect;

        Categories category = categoriesDAO.findById(id);
        model.addAttribute("category", category);
        return "/admin/categoryUpdate";
    }

    // Handle update of an existing category
    @PostMapping("/categoryUpdate")
    public String updateCategory(@ModelAttribute("category") Categories category,
                                 HttpServletRequest request,
                                 RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.ADMIN_ROLE);
        if (redirect != null) return redirect;

        categoriesDAO.update(category);
        return "redirect:/admin/categories.htm";
    }

    // Handle deletion of a category
    @GetMapping("/categoryDelete")
    public String deleteCategory(@RequestParam("categoryID") int id,
                                 HttpServletRequest request,
                                 RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.ADMIN_ROLE);
        if (redirect != null) return redirect;

        try {
            categoriesDAO.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Category deleted successfully");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/categories.htm";
    }
}

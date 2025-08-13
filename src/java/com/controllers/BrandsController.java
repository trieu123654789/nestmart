package com.controllers;

import com.models.Brands;
import com.models.BrandsDAO;
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
public class BrandsController {

    @Autowired
    private BrandsDAO brandsDAO;

    // Display paginated list of brands with optional search
    @GetMapping("/brand")
    public String showBrands(ModelMap model,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "12") int pageSize,
                             @RequestParam(value = "keyword", required = false) String keyword,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.ADMIN_ROLE);
        if (redirect != null) return redirect;

        List<Brands> listBrands = new ArrayList<>();
        int totalBrands = 0;
        String finalKeyword = (keyword != null) ? keyword.trim() : null;

        if (finalKeyword != null && !finalKeyword.isEmpty()) {
            List<Brands> allResults = brandsDAO.searchByKeyword(finalKeyword);
            totalBrands = allResults.size();
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalBrands);
            listBrands = (fromIndex < toIndex) ? allResults.subList(fromIndex, toIndex) : new ArrayList<>();
        } else {
            totalBrands = brandsDAO.getTotalBrands();
            listBrands = brandsDAO.findPaginated(page, pageSize);
        }

        model.addAttribute("listBrands", listBrands);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) totalBrands / pageSize));
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keyword", finalKeyword);
        model.addAttribute("totalBrands", totalBrands);

        return "/admin/brand";
    }

    // Show form to create a new brand
    @GetMapping("/brandCreate")
    public String showCreateBrandForm(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.ADMIN_ROLE);
        if (redirect != null) return redirect;

        model.addAttribute("brand", new Brands());
        return "/admin/brandCreate";
    }

    // Handle creation of a new brand
    @PostMapping("/brandCreate")
    public String createBrand(@ModelAttribute("brand") Brands brand,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.ADMIN_ROLE);
        if (redirect != null) return redirect;

        brandsDAO.save(brand);
        return "redirect:/admin/brand.htm";
    }

    // Show form to edit an existing brand
    @GetMapping("/brandUpdate")
    public String showEditBrandForm(@RequestParam("brandID") int id,
                                    Model model,
                                    HttpServletRequest request,
                                    RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.ADMIN_ROLE);
        if (redirect != null) return redirect;

        Brands brand = brandsDAO.findById(id);
        model.addAttribute("brand", brand);
        return "/admin/brandUpdate";
    }

    // Handle update of an existing brand
    @PostMapping("/brandUpdate")
    public String updateBrand(@ModelAttribute("brand") Brands brand,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.ADMIN_ROLE);
        if (redirect != null) return redirect;

        brandsDAO.update(brand);
        return "redirect:/admin/brand.htm";
    }

    // Handle deletion of a brand
    @GetMapping("/brandDelete")
    public String deleteBrand(@RequestParam("brandID") int id,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.ADMIN_ROLE);
        if (redirect != null) return redirect;

        try {
            brandsDAO.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Brand deleted successfully");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/brand.htm";
    }
}

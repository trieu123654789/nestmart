package com.controllers;

import com.models.Discount;
import com.models.DiscountDAO;
import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utils.RoleUtils;

@Controller
@RequestMapping(value = "/admin")
public class DiscountController {

    @Autowired
    private ServletContext servletContext;
    @Autowired
    private DiscountDAO discountDAO;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.isEmpty()) {
                    setValue(null);
                } else {
                    setValue(LocalDateTime.parse(text, formatter));
                }
            }

            @Override
            public String getAsText() {
                LocalDateTime value = (LocalDateTime) getValue();
                return (value != null) ? value.format(formatter) : "";
            }
        });
    }

    // Display paginated discounts with optional search
    @RequestMapping(value = "discount", method = RequestMethod.GET)
    public String showDiscounts(ModelMap model,
                                @RequestParam(defaultValue = "1") int page,
                                @RequestParam(defaultValue = "10") int pageSize,
                                @RequestParam(value = "keyword", required = false) String keyword,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        String finalKeyword = (keyword != null) ? keyword.trim() : "";
        List<Discount> listDiscount;
        int totalDiscounts;

        if (!finalKeyword.isEmpty()) {
            List<Discount> allResults = discountDAO.searchByKeyword(finalKeyword, 1, Integer.MAX_VALUE);
            totalDiscounts = allResults.size();
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalDiscounts);
            listDiscount = (fromIndex < toIndex) ? allResults.subList(fromIndex, toIndex) : new ArrayList<>();
        } else {
            totalDiscounts = discountDAO.getTotalDiscounts();
            listDiscount = discountDAO.findPaginated(page, pageSize);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Discount d : listDiscount) {
            if (d.getStartDate() != null) {
                d.setFormattedStartDate(d.getStartDate().format(formatter));
            }
            if (d.getEndDate() != null) {
                d.setFormattedEndDate(d.getEndDate().format(formatter));
            }
        }

        model.addAttribute("listDiscount", listDiscount);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) totalDiscounts / pageSize));
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keyword", finalKeyword);
        model.addAttribute("totalDiscounts", totalDiscounts);

        return "/admin/discount";
    }

    // Show form to create a new discount
    @RequestMapping(value = "discountCreate", method = RequestMethod.GET)
    public String showCreateDiscountForm(HttpSession session, RedirectAttributes redirectAttributes, Model model) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        model.addAttribute("discount", new Discount());
        return "/admin/discountCreate";
    }

    // Handle creation of a discount
    @RequestMapping(value = "/discountCreate", method = RequestMethod.POST)
    public String createDiscount(@ModelAttribute Discount discount,
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        if (discount.getStartDate() != null && discount.getEndDate() != null
                && discount.getStartDate().isAfter(discount.getEndDate())) {
            model.addAttribute("error", "The end date cannot be before the start date.");
            model.addAttribute("discount", discount);
            return "admin/discountCreate";
        }

        try {
            discountDAO.save(discount, imageFile, servletContext);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error creating discount: " + e.getMessage());
            model.addAttribute("discount", discount);
            return "admin/discountCreate";
        }

        redirectAttributes.addFlashAttribute("message", "Discount created successfully.");
        return "redirect:/admin/discount.htm";
    }

    // Show form to edit an existing discount
    @RequestMapping(value = "discountUpdate", method = RequestMethod.GET)
    public String showEditDiscountForm(@RequestParam("discountID") int id,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        Discount discount = discountDAO.findById(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (discount.getStartDate() != null) {
            discount.setFormattedStartDate(discount.getStartDate().format(formatter));
        }
        if (discount.getEndDate() != null) {
            discount.setFormattedEndDate(discount.getEndDate().format(formatter));
        }
        model.addAttribute("discount", discount);
        return "/admin/discountUpdate";
    }

    // Handle update of a discount
    @RequestMapping(value = "/discountUpdate", method = RequestMethod.POST)
    public String updateDiscount(@ModelAttribute Discount discount,
                                 @RequestParam("imageFile") MultipartFile imageFile,
                                 HttpServletRequest request,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        if (discount.getStartDate() != null && discount.getEndDate() != null
                && discount.getStartDate().isAfter(discount.getEndDate())) {
            model.addAttribute("error", "The end date cannot be before the start date.");
            model.addAttribute("discount", discount);
            return "admin/discountUpdate";
        }

        try {
            discountDAO.update(discount, imageFile, request.getServletContext());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating discount: " + e.getMessage());
            model.addAttribute("discount", discount);
            return "admin/discountUpdate";
        }

        redirectAttributes.addFlashAttribute("message", "Discount updated successfully.");
        return "redirect:/admin/discount.htm";
    }

    // Delete a discount by ID
    @RequestMapping(value = "discountDelete", method = RequestMethod.GET)
    public String deleteDiscount(@RequestParam("discountID") int id,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        discountDAO.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Discount deleted successfully.");
        return "redirect:/admin/discount.htm";
    }
}

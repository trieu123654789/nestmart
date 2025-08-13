package com.controllers;

import com.models.Voucher;
import com.models.VoucherDAO;
import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utils.RoleUtils;

@Controller
@RequestMapping("/employee")
public class VoucherEmpController {

    @Autowired
    private VoucherDAO voucherDAO;

    // Configure date-time binder for form inputs
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue((text == null || text.isEmpty()) ? null : LocalDateTime.parse(text, formatter));
            }

            @Override
            public String getAsText() {
                LocalDateTime value = (LocalDateTime) getValue();
                return (value != null) ? value.format(formatter) : "";
            }
        });
    }

    // Display paginated voucher list
    @RequestMapping(value = "vouchers", method = RequestMethod.GET)
    public String showVouchers(ModelMap model,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int pageSize,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        String finalKeyword = (keyword != null) ? keyword.trim() : "";
        List<Voucher> listVoucher;
        int totalVouchers;

        if (!finalKeyword.isEmpty()) {
            List<Voucher> allResults = voucherDAO.searchByKeyword(finalKeyword, 1, Integer.MAX_VALUE);
            totalVouchers = allResults.size();
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalVouchers);
            listVoucher = (fromIndex < toIndex) ? allResults.subList(fromIndex, toIndex) : new ArrayList<>();
        } else {
            totalVouchers = voucherDAO.getTotalVouchers();
            listVoucher = voucherDAO.findPaginated(page, pageSize);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Voucher v : listVoucher) {
            if (v.getStartDate() != null) v.setFormattedStartDate(v.getStartDate().format(formatter));
            if (v.getEndDate() != null) v.setFormattedEndDate(v.getEndDate().format(formatter));
        }

        model.addAttribute("listVoucher", listVoucher);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) totalVouchers / pageSize));
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keyword", finalKeyword);
        model.addAttribute("totalVouchers", totalVouchers);

        return "/employee/voucher";
    }

    // Display create voucher form
    @RequestMapping(value = "voucherCreate", method = RequestMethod.GET)
    public String showCreateVoucherForm(Model model, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        model.addAttribute("voucher", new Voucher());
        return "/employee/vouchercreate";
    }

    // Add new voucher
    @RequestMapping(value = "/voucherCreate", method = RequestMethod.POST)
    public String addVoucher(@ModelAttribute Voucher voucher, RedirectAttributes redirectAttributes) {
        if (voucherDAO.existsByCode(voucher.getCode())) {
            redirectAttributes.addFlashAttribute("error", "Voucher code already exists.");
            return "redirect:/employee/voucherCreate.htm";
        }

        if (voucher.getStartDate() != null && voucher.getEndDate() != null
                && voucher.getStartDate().isAfter(voucher.getEndDate())) {
            redirectAttributes.addFlashAttribute("error", "Start Date cannot be after End Date.");
            return "redirect:/employee/voucherCreate.htm";
        }

        if ("FixedAmount".equalsIgnoreCase(voucher.getDiscountType())
                && voucher.getDiscountValue() != null && voucher.getMaxDiscount() != null
                && voucher.getDiscountValue().compareTo(voucher.getMaxDiscount()) <= 0) {
            redirectAttributes.addFlashAttribute("error", "Discount Value must be greater than Max Discount for FixedAmount type.");
            return "redirect:/employee/voucherCreate.htm";
        }

        if ((voucher.getDiscountValue() != null && voucher.getDiscountValue().compareTo(BigDecimal.ZERO) < 0)
                || (voucher.getMinOrderValue() != null && voucher.getMinOrderValue().compareTo(BigDecimal.ZERO) < 0)
                || (voucher.getMaxDiscount() != null && voucher.getMaxDiscount().compareTo(BigDecimal.ZERO) < 0)
                || voucher.getUsageLimit() < 0) {
            redirectAttributes.addFlashAttribute("error", "Numeric values cannot be negative.");
            return "redirect:/employee/voucherCreate.htm";
        }

        voucher.setUsedCount(0);
        voucherDAO.addVoucher(voucher);
        redirectAttributes.addFlashAttribute("success", "Voucher created successfully.");
        return "redirect:/employee/vouchers.htm";
    }

    // Display edit voucher form
    @GetMapping("/voucherUpdate")
    public String showEditVoucherForm(@RequestParam("voucherID") int id, Model model,
                                      HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        Voucher voucher = voucherDAO.findById(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (voucher.getStartDate() != null) voucher.setFormattedStartDate(voucher.getStartDate().format(formatter));
        if (voucher.getEndDate() != null) voucher.setFormattedEndDate(voucher.getEndDate().format(formatter));

        model.addAttribute("voucher", voucher);
        return "employee/voucherupdate";
    }

    // Update existing voucher
    @PostMapping("/voucherUpdate")
    public String updateVoucher(@ModelAttribute Voucher voucher, RedirectAttributes redirectAttributes) {
        try {
            if (voucher.getStartDate() != null && voucher.getEndDate() != null
                    && voucher.getStartDate().isAfter(voucher.getEndDate())) {
                redirectAttributes.addFlashAttribute("error", "Start Date cannot be after End Date.");
                return "redirect:/employee/voucherUpdate.htm?voucherID=" + voucher.getVoucherID();
            }

            if (voucherDAO.existsByCodeExceptId(voucher.getCode(), voucher.getVoucherID())) {
                redirectAttributes.addFlashAttribute("error", "Voucher code already exists.");
                return "redirect:/employee/voucherUpdate.htm?voucherID=" + voucher.getVoucherID();
            }

            if ("FixedAmount".equalsIgnoreCase(voucher.getDiscountType())
                    && voucher.getDiscountValue() != null && voucher.getMaxDiscount() != null
                    && voucher.getDiscountValue().compareTo(voucher.getMaxDiscount()) <= 0) {
                redirectAttributes.addFlashAttribute("error", "Discount Value must be greater than Max Discount for FixedAmount type.");
                return "redirect:/employee/voucherUpdate.htm?voucherID=" + voucher.getVoucherID();
            }

            if ((voucher.getDiscountValue() != null && voucher.getDiscountValue().compareTo(BigDecimal.ZERO) < 0)
                    || (voucher.getMinOrderValue() != null && voucher.getMinOrderValue().compareTo(BigDecimal.ZERO) < 0)
                    || (voucher.getMaxDiscount() != null && voucher.getMaxDiscount().compareTo(BigDecimal.ZERO) < 0)
                    || voucher.getUsageLimit() < 0) {
                redirectAttributes.addFlashAttribute("error", "Numeric values cannot be negative.");
                return "redirect:/employee/voucherUpdate.htm?voucherID=" + voucher.getVoucherID();
            }

            voucherDAO.update(voucher);
            redirectAttributes.addFlashAttribute("success", "Voucher updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating voucher: " + e.getMessage());
            return "redirect:/employee/voucherUpdate.htm?voucherID=" + voucher.getVoucherID();
        }

        return "redirect:/employee/vouchers.htm";
    }

    // Delete voucher
    @RequestMapping(value = "/voucherDelete", method = RequestMethod.GET)
    public String deleteVoucher(@RequestParam("voucherID") int id, RedirectAttributes redirectAttributes) {
        try {
            voucherDAO.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Voucher deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting voucher: " + e.getMessage());
        }
        return "redirect:/employee/vouchers.htm";
    }
}

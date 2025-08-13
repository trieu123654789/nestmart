package com.controllers;

import com.models.AccountVoucher;
import com.models.AccountVoucherDAO;
import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utils.RoleUtils;

@Controller
@RequestMapping("/employee")
public class AccountVoucherEmpController {

    @Autowired
    private AccountVoucherDAO accountVoucherDAO;

    // Bind LocalDateTime from form input
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

    // Display paginated list of account vouchers
    @GetMapping("/accountVouchers")
    public String showAccountVouchers(ModelMap model,
                                      @RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int pageSize,
                                      @RequestParam(value = "keyword", required = false) String keyword,
                                      HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.EMPLOYEE_ROLE);
        if (redirect != null) return redirect;

        String finalKeyword = (keyword != null) ? keyword.trim() : "";
        List<AccountVoucher> listAccountVoucher;
        int totalAccountVouchers;

        if (!finalKeyword.isEmpty()) {
            List<AccountVoucher> allResults = accountVoucherDAO.searchByKeyword(finalKeyword, 1, Integer.MAX_VALUE);
            totalAccountVouchers = allResults.size();
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalAccountVouchers);
            listAccountVoucher = (fromIndex < toIndex) ? allResults.subList(fromIndex, toIndex) : new ArrayList<>();
        } else {
            totalAccountVouchers = accountVoucherDAO.getTotalAccountVouchers();
            listAccountVoucher = accountVoucherDAO.findPaginated(page, pageSize);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (AccountVoucher av : listAccountVoucher) {
            if (av.getExpiryDate() != null) av.setFormattedExpiryDate(av.getExpiryDate().format(formatter));
            if (av.getAssignedDate() != null) av.setFormattedAssignedDate(av.getAssignedDate().format(formatter));
        }

        model.addAttribute("listAccountVoucher", listAccountVoucher);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) totalAccountVouchers / pageSize));
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keyword", finalKeyword);
        model.addAttribute("totalAccountVouchers", totalAccountVouchers);

        return "/employee/accountvoucher";
    }

    // Show form to create a new account voucher
    @GetMapping("/accountVoucherCreate")
    public String showCreateForm(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.EMPLOYEE_ROLE);
        if (redirect != null) return redirect;

        List<Map<String, Object>> accounts = accountVoucherDAO.findAccountsForDropdown();
        List<Map<String, Object>> vouchers = accountVoucherDAO.findVouchersForDropdown();

        model.addAttribute("accountVoucher", new AccountVoucher());
        model.addAttribute("accounts", accounts);
        model.addAttribute("vouchers", vouchers);

        return "/employee/accountvouchercreate";
    }

    // Handle creation of a new account voucher
    @PostMapping("/accountVoucherCreate")
    public String createAccountVoucher(@ModelAttribute AccountVoucher accountVoucher,
                                       RedirectAttributes redirectAttributes) {
        if (accountVoucher.getAssignedDate() != null && accountVoucher.getExpiryDate() != null
                && accountVoucher.getAssignedDate().isAfter(accountVoucher.getExpiryDate())) {
            redirectAttributes.addFlashAttribute("error", "Assigned Date cannot be after Expiry Date.");
            return "redirect:/employee/accountVoucherCreate.htm";
        }

        try {
            accountVoucherDAO.create(accountVoucher);
            redirectAttributes.addFlashAttribute("success", "Account Voucher created successfully.");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Error creating Account Voucher: " + ex.getMessage());
            return "redirect:/employee/accountVoucherCreate.htm";
        }

        return "redirect:/employee/accountVouchers.htm";
    }

    // Show form to update an existing account voucher
    @GetMapping("/accountVoucherUpdate")
    public String showUpdateForm(@RequestParam("accountVoucherID") int id, Model model,
                                 HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, RoleUtils.EMPLOYEE_ROLE);
        if (redirect != null) return redirect;

        AccountVoucher av = accountVoucherDAO.findById(id);
        List<Map<String, Object>> accounts = accountVoucherDAO.findAccountsForDropdown();
        List<Map<String, Object>> vouchers = accountVoucherDAO.findVouchersForDropdown();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (av.getExpiryDate() != null) av.setFormattedExpiryDate(av.getExpiryDate().format(formatter));
        if (av.getAssignedDate() != null) av.setFormattedAssignedDate(av.getAssignedDate().format(formatter));

        model.addAttribute("accountVoucher", av);
        model.addAttribute("accounts", accounts);
        model.addAttribute("vouchers", vouchers);

        return "/employee/accountvoucherupdate";
    }

    // Handle update of an existing account voucher
    @PostMapping("/accountVoucherUpdate")
    public String updateAccountVoucher(@ModelAttribute AccountVoucher accountVoucher,
                                       RedirectAttributes redirectAttributes) {
        if (accountVoucher.getAssignedDate() != null && accountVoucher.getExpiryDate() != null
                && accountVoucher.getAssignedDate().isAfter(accountVoucher.getExpiryDate())) {
            redirectAttributes.addFlashAttribute("error", "Assigned Date cannot be after Expiry Date.");
            return "redirect:/employee/accountVoucherUpdate.htm?accountVoucherID=" + accountVoucher.getAccountVoucherID();
        }

        try {
            accountVoucherDAO.update(accountVoucher);
            redirectAttributes.addFlashAttribute("success", "Account Voucher updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating Account Voucher: " + e.getMessage());
            return "redirect:/employee/accountVoucherUpdate.htm?accountVoucherID=" + accountVoucher.getAccountVoucherID();
        }

        return "redirect:/employee/accountVouchers.htm";
    }

    // Delete an account voucher by ID
    @GetMapping("/accountVoucherDelete")
    public String deleteAccountVoucher(@RequestParam("accountVoucherID") int id, RedirectAttributes redirectAttributes) {
        try {
            accountVoucherDAO.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Account Voucher deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting Account Voucher: " + e.getMessage());
        }
        return "redirect:/employee/accountVouchers.htm";
    }
}

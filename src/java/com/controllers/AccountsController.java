package com.controllers;

import com.models.Accounts;
import com.models.AccountsDAO;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utils.RoleUtils;

@Controller
@RequestMapping("/admin")
public class AccountsController {

    @Autowired
    private AccountsDAO accountsDAO;

    // Display paginated list of accounts with optional search
    @GetMapping("/account")
    public String showAccounts(ModelMap model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword,
            HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) {
            return redirect;
        }

        List<Accounts> listAccount = new ArrayList<>();
        int totalAccounts = 0;
        String finalKeyword = (keyword != null) ? keyword.trim() : null;

        if (finalKeyword != null && !finalKeyword.isEmpty()) {
            List<Accounts> allResults = accountsDAO.searchByKeyword(finalKeyword);
            totalAccounts = allResults.size();
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalAccounts);
            listAccount = (fromIndex < toIndex) ? allResults.subList(fromIndex, toIndex) : new ArrayList<>();
        } else {
            totalAccounts = accountsDAO.getTotalAccounts("");
            listAccount = accountsDAO.getPagedAccounts("", page, pageSize);
        }

        listAccount.forEach(account -> account.setRoleName(getRoleName(account.getRole())));

        model.addAttribute("listAccount", listAccount);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) totalAccounts / pageSize));
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keyword", finalKeyword);
        model.addAttribute("totalAccounts", totalAccounts);

        return "/admin/account";
    }

    // Show form to create a new account
    @GetMapping("/accountCreate")
    public String showAccountCreateForm(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) {
            return redirect;
        }

        model.addAttribute("account", new Accounts());
        return "/admin/accountCreate";
    }

    // Handle creation of a new account
    @PostMapping("/accountCreate.htm")
    public String createAccount(@RequestParam("fullName") String fullName,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("address") String address,
            @RequestParam("gender") String gender,
            @RequestParam("birthday") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthday,
            @RequestParam("role") int role,
            Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) {
            return redirect; // Giờ sẽ redirect về dashboard của user
        }
        Map<String, String> errors = new HashMap<>();

        if (fullName.isEmpty()) {
            errors.put("fullName", "Full name is required.");
        }
        if (email.isEmpty()) {
            errors.put("email", "Email is required.");
        } else if (accountsDAO.existsByEmail(email)) {
            errors.put("email", "Email already exists.");
        }
        if (password.isEmpty() || password.length() < 6 || !password.matches(".*[0-9].*") || !password.matches(".*[a-zA-Z].*")) {
            errors.put("password", "Password must be at least 6 characters long and contain both letters and numbers.");
        }
        if (confirmPassword.isEmpty() || !confirmPassword.equals(password)) {
            errors.put("confirmPassword", "Confirm password does not match the password.");
        }
        if (phoneNumber.isEmpty()) {
            errors.put("phoneNumber", "Phone number is required.");
        } else if (accountsDAO.existsByPhoneNumber(phoneNumber)) {
            errors.put("phoneNumber", "Phone number already exists.");
        }
        if (address.isEmpty()) {
            errors.put("address", "Address is required.");
        }
        if (gender.isEmpty()) {
            errors.put("gender", "Gender is required.");
        }
        if (birthday == null) {
            errors.put("birthday", "Birthday is required.");
        } else {
            int age = Period.between(birthday, LocalDate.now()).getYears();
            if (age < 14) {
                errors.put("birthday", "You must be at least 14 years old to register.");
            }
        }

        if (!errors.isEmpty()) {
            model.addAttribute("errors", errors);
            model.addAttribute("account", new Accounts(0, phoneNumber, password, email, role, gender, fullName, java.sql.Date.valueOf(birthday), address, BigDecimal.ZERO));
            return "/admin/accountCreate";
        }

        Accounts newAccount = new Accounts(0, phoneNumber, password, email, role, gender, fullName, java.sql.Date.valueOf(birthday), address, BigDecimal.ZERO);
        try {
            accountsDAO.save(newAccount);
        } catch (Exception e) {
            model.addAttribute("error", "Account creation failed. Please try again.");
            return "/admin/accountCreate";
        }

        return "redirect:/admin/account.htm";
    }

    // Show form to edit an existing account
    @GetMapping("/accountUpdate.htm")
    public String showEditAccountForm(@RequestParam("accountID") int id, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) {
            return redirect;
        }

        Accounts account = accountsDAO.findById(id);
        if (account == null) {
            throw new IllegalArgumentException("Invalid account Id: " + id);
        }
        model.addAttribute("account", account);
        return "/admin/accountUpdate";
    }

    // Handle update of an existing account
    @PostMapping("/editAccount.htm")
    public String updateAccount(@RequestParam("accountID") int id,
            @ModelAttribute("account") Accounts account,
            BindingResult result, Model model,
            HttpServletRequest request, RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) {
            return redirect;
        }

        Accounts existingAccount = accountsDAO.findById(id);
        if (existingAccount == null) {
            result.rejectValue("accountID", "error.account", "Account not found.");
            return "/admin/accountUpdate";
        }

        if (accountsDAO.existsByEmail(account.getEmail()) && !existingAccount.getEmail().equals(account.getEmail())) {
            result.rejectValue("email", "error.account", "Email already exists.");
        }

        if (!account.getEmail().matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            result.rejectValue("email", "error.account", "Email is not in correct format.");
        }

        if (accountsDAO.existsByPhoneNumber(account.getPhoneNumber()) && !existingAccount.getPhoneNumber().equals(account.getPhoneNumber())) {
            result.rejectValue("phoneNumber", "error.account", "Phone Number already exists.");
        }

        if (result.hasErrors()) {
            model.addAttribute("account", account);
            return "/admin/accountUpdate";
        }

        existingAccount.setEmail(account.getEmail());
        existingAccount.setGender(account.getGender());
        existingAccount.setRole(account.getRole());
        existingAccount.setPhoneNumber(account.getPhoneNumber());
        existingAccount.setHourlyRate(account.getHourlyRate());

        accountsDAO.update(existingAccount);
        return "redirect:/admin/account.htm";
    }

    // Convert role ID to role name
    private String getRoleName(int role) {
        switch (role) {
            case 1:
                return "Admin";
            case 2:
                return "Employee";
            case 3:
                return "Shipper";
            case 4:
                return "Customer";
            default:
                return "Unknown";
        }
    }
}

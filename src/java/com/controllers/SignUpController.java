package com.controllers;

import com.models.Accounts;
import com.models.AccountsDAO;
import com.services.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/")
public class SignUpController {

    @Autowired
    private AccountsDAO accountsDAO;
    
    @Autowired
    private PasswordService passwordService;

    // Display the registration form
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String showRegisterForm(Model model) {
        model.addAttribute("account", new Accounts());
        return "signup";
    }

    // Handle account creation with validation
    @RequestMapping(value = "/signup.htm", method = RequestMethod.POST)
    public String createAccount(@RequestParam("fullName") String fullName,
                                @RequestParam("email") String email,
                                @RequestParam("password") String password,
                                @RequestParam(value = "confirmPassword", required = false) String confirmPassword,
                                @RequestParam("phoneNumber") String phoneNumber,
                                @RequestParam("address") String address,
                                @RequestParam("gender") String gender,
                                @RequestParam("birthday") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate birthday,
                                Model model) {

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

        if (confirmPassword == null || confirmPassword.isEmpty() || !confirmPassword.equals(password)) {
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
            model.addAttribute("fullName", fullName);
            model.addAttribute("email", email);
            model.addAttribute("phoneNumber", phoneNumber);
            model.addAttribute("address", address);
            model.addAttribute("gender", gender);
            model.addAttribute("birthday", birthday);
            return "/signup";
        }

        // Hash the password using PasswordService
        String hashedPassword = passwordService.hashPassword(password);
        
        Date sqlBirthday = Date.valueOf(birthday);
        Accounts newAccount = new Accounts(0, phoneNumber, hashedPassword, email, 4, gender, fullName, sqlBirthday, address, BigDecimal.ZERO);

        try {
            accountsDAO.save(newAccount);
            return "redirect:/login.htm";
        } catch (Exception e) {
            model.addAttribute("error", "Unexpected error occurred: " + e.getMessage());
            return "/signup";
        }
    }

    // Initialize date binder for parsing dates
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
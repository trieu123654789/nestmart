package com.controllers;

import com.models.Accounts;
import com.models.AccountsDAO;
import com.services.PasswordService;
import com.services.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class LoginController {

    @Autowired
    private PasswordService passwordService;
    
    @Autowired
    private AccountsDAO accountsDAO;

    @Autowired
    private SessionService sessionService;

    // Show login page
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String showLoginForm() {
        return "login";
    }

    // Handle login form submission
    @RequestMapping(value = "/login.htm", method = RequestMethod.POST)
    public String loginUser(@RequestParam("email") String email,
                            @RequestParam("password") String password,
                            HttpServletRequest request,
                            HttpSession session,
                            Model model) {

        boolean hasError = false;

        if (email == null || email.isEmpty()) {
            model.addAttribute("error", "Email is required.");
            hasError = true;
        }

        if (password == null || password.isEmpty()) {
            model.addAttribute("error", "Password is required.");
            hasError = true;
        }

        if (hasError) {
            return "login";
        }

        Accounts account = accountsDAO.findByEmail(email);

        // Use PasswordService to verify password
        if (account != null && passwordService.verifyPassword(password, account.getPassword())) {
            session.setAttribute("email", email);
            session.setAttribute("phoneNumber", account.getPhoneNumber());
            session.setAttribute("fullName", account.getFullName());
            session.setAttribute("birthday", account.getBirthday());
            session.setAttribute("address", account.getAddress());
            session.setAttribute("role", account.getRole());
            session.setAttribute("accountId", account.getAccountID());

            sessionService.createSession(session, account.getEmail(), account.getRole());

            String redirectUrl;
            switch (account.getRole()) {
                case 1:
                    redirectUrl = "/admin/account.htm";
                    break;
                case 2:
                    redirectUrl = "/employee/index.htm";
                    break;
                case 3:
                    redirectUrl = "/shipper/shippers.htm";
                    break;
                case 4:
                    redirectUrl = "/client/clientboard.htm";
                    break;
                default:
                    redirectUrl = "/index.htm";
            }

            return "redirect:" + redirectUrl;
        } else {
            model.addAttribute("error", "Invalid email or password.");
            return "login";
        }
    }

    // Handle user logout
    @RequestMapping(value = "/logout.htm", method = RequestMethod.GET)
    public String logout(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            sessionService.invalidateSession(session);
        }
        redirectAttributes.addFlashAttribute("message", "You have been logged out successfully.");
        return "redirect:/client/clientboard.htm";
    }
}
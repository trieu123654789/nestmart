package com.controllers;

import com.models.AccountsDAO;
import com.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class PasswordResetController {

    @Autowired
    private AccountsDAO accountsDAO;

    @Autowired
    private EmailService emailService;

    // Temporary storage for reset tokens mapped to email
    private final Map<String, String> tokenStore = new HashMap<>();

    // Show form to input email for password reset
    @RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
    public String showForgotPasswordForm() {
        return "forgotPassword";
    }

    // Handle password reset request and send email with token
    @RequestMapping(value = "/forgotPassword.htm", method = RequestMethod.POST)
    public String requestResetPassword(@RequestParam("email") String email, Model model) {
        boolean emailExists = accountsDAO.existsByEmail(email);

        if (!emailExists) {
            model.addAttribute("message", "The email address is not registered. Please sign up first.");
            return "signup";
        }

        String token = UUID.randomUUID().toString();
        tokenStore.put(token, email);

        String resetLink = "http://localhost:8080/nestmartFinal/resetPassword.htm?token=" + token;
        emailService.sendResetPasswordEmail(email, resetLink);

        model.addAttribute("message", "A password reset link has been sent to your email.");
        return "forgotPassword";
    }

    // Show form to input new password after clicking reset link
    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        if (!tokenStore.containsKey(token)) {
            model.addAttribute("message", "Invalid token.");
            return "forgotPassword";
        }

        model.addAttribute("token", token);
        return "resetPassword";
    }

    // Reset password using the token
    @RequestMapping(value = "/resetPassword.htm", method = RequestMethod.POST)
    public String resetPassword(@RequestParam("token") String token, 
                                @RequestParam("password") String newPassword, Model model) {
        if (!tokenStore.containsKey(token)) {
            model.addAttribute("message", "Invalid token.");
            return "forgotPassword";
        }

        String email = tokenStore.get(token);
        accountsDAO.updatePassword(email, newPassword);
        tokenStore.remove(token);

        model.addAttribute("message", "Your password has been reset successfully.");
        return "login";
    }
}

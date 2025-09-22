package com.controllers;

import com.models.AccountsDAO;
import com.services.EmailService;
import com.services.PasswordService;
import com.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import utils.RoleUtils;

@Controller
public class PasswordResetController {

    @Autowired
    private AccountsDAO accountsDAO;

    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordService passwordService;

    // Temporary storage for reset tokens mapped to email
    private final Map<String, TokenService.TokenInfo> tokenStore = new HashMap<>();

    // Show form to input email for password reset
    @RequestMapping(value = "/forgotPassword", method = RequestMethod.GET)
public String showForgotPasswordForm(HttpServletRequest request,
                                     RedirectAttributes redirectAttributes) {

    HttpSession session = request.getSession(false);

    if (session == null || session.getAttribute("role") == null) {
        return "forgotPassword";
    }

    Integer rolecheck = (Integer) session.getAttribute("role");

    if (rolecheck == 1) {
        return "redirect:/admin/changePassword.htm";
    } else if (rolecheck == 2) {
        return "redirect:/employee/changePassword.htm";
    } else if (rolecheck == 3) {
        return "redirect:/shipper/changePassword.htm";
    } else if (rolecheck == 4) {
        return "redirect:/client/changePassword.htm";
    } else {
        return "forgotPassword.htm";
    }
}


    // Handle password reset request and send email with token
    @RequestMapping(value = "/forgotPassword.htm", method = RequestMethod.POST)
    public String requestResetPassword(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        boolean emailExists = accountsDAO.existsByEmail(email);

        if (!emailExists) {
            redirectAttributes.addFlashAttribute("message", "The email address is not registered. Please sign up first.");
            return "redirect:/signup.htm";
        }

        String token = UUID.randomUUID().toString();
        long expiryTime = System.currentTimeMillis() + 30 * 60 * 1000;
        TokenService.TokenInfo tokenInfo = new TokenService.TokenInfo(email, expiryTime);

        tokenStore.put(token, tokenInfo);

        String resetLink = "http://localhost:8080/nestmart/resetPassword.htm?token=" + token;
        emailService.sendResetPasswordEmail(email, resetLink);

        redirectAttributes.addFlashAttribute("message", "A password reset link has been sent to your email.");
        return "redirect:/forgotPassword.htm";
    }

    // Show form to input new password after clicking reset link
    @RequestMapping(value = "/resetPassword.htm", method = RequestMethod.GET)
    public String showResetPasswordForm(@RequestParam(value = "token", required = false) String token,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (token == null || token.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid link. Please request password reset from Forgot Password page.");
            return "redirect:/forgotPassword.htm";
        }

        TokenService.TokenInfo info = tokenStore.get(token);
        if (info == null || info.isExpired()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid or expired password reset link. Please request a new link.");
            return "redirect:/forgotPassword.htm";
        }

        model.addAttribute("token", token);
        return "resetPassword";
    }

    // Reset password using the token
    @RequestMapping(value = "/resetPassword.htm", method = RequestMethod.POST)
    public String resetPassword(@RequestParam(value = "token", required = false) String token,
            @RequestParam("password") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            RedirectAttributes redirectAttributes,
            Model model) {

        TokenService.TokenInfo info = tokenStore.get(token);
        if (token == null || token.trim().isEmpty() || info == null || info.isExpired()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid password reset session. Please request a new link.");
            return "redirect:/forgotPassword.htm";
        }

        if (newPassword == null || newPassword.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password cannot be empty.");
            return "redirect:/resetPassword.htm?token=" + token;
        }

        if (newPassword.length() < 6) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password must be at least 6 characters long.");
            return "redirect:/resetPassword.htm?token=" + token;
        }

        boolean hasLetters = newPassword.matches(".*[a-zA-Z].*");
        boolean hasNumbers = newPassword.matches(".*[0-9].*");

        if (!hasLetters || !hasNumbers) {
            redirectAttributes.addFlashAttribute("errorMessage", "Password must contain both letters and numbers.");
            return "redirect:/resetPassword.htm?token=" + token;
        }

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Confirm password does not match.");
            return "redirect:/resetPassword.htm?token=" + token;
        }

        try {
            String email = info.getEmail();
            String hashedNewPassword = passwordService.hashPassword(newPassword);
            accountsDAO.updatePassword(email, hashedNewPassword);
            tokenStore.remove(token);

            redirectAttributes.addFlashAttribute("message", "Password reset successfully. You can now login with your new password.");
            return "redirect:/login.htm";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "An error occurred while resetting password. Please try again.");
            return "redirect:/resetPassword.htm?token=" + token;
        }
    }

}

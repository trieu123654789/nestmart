package com.controllers;

import com.models.Accounts;
import com.models.AccountsDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utils.RoleUtils;

@Controller
@RequestMapping("/")
public class ChangeInforController {

    @Autowired
    private AccountsDAO accountDAO;

    // Display admin account information page
    @RequestMapping(value = "admin/accountInformation", method = RequestMethod.GET)
    public String showAdminAccountInformation(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        String email = (String) session.getAttribute("email");
        Accounts account = accountDAO.findByEmail(email);
        if (account == null) return "redirect:/login.htm";

        model.addAttribute("account", account);
        return "/admin/accountInformation";
    }

    // Update admin account information
    @RequestMapping(value = "admin/updateInformation", method = RequestMethod.POST)
    public String updateAdminAccountInformation(@ModelAttribute("account") Accounts account,
            RedirectAttributes redirectAttributes, HttpSession session) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        Accounts existingAccount = accountDAO.findById(account.getAccountID());
        if (existingAccount != null) {
            if (!existingAccount.getPhoneNumber().equals(account.getPhoneNumber())
                    && accountDAO.isPhoneNumberRegistered(account.getPhoneNumber())) {
                redirectAttributes.addFlashAttribute("error", "Phone number is already registered.");
                return "redirect:/admin/accountInformation.htm";
            }

            existingAccount.setEmail(account.getEmail());
            existingAccount.setGender(account.getGender());
            existingAccount.setPhoneNumber(account.getPhoneNumber());
            existingAccount.setAddress(account.getAddress());
            existingAccount.setFullName(account.getFullName());
            existingAccount.setBirthday(account.getBirthday());

            accountDAO.update(existingAccount);
            redirectAttributes.addFlashAttribute("message", "Account information updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Account not found.");
        }
        return "redirect:/admin/account.htm";
    }

    // Display admin change password page
    @RequestMapping(value = "admin/changePassword", method = RequestMethod.GET)
    public String showAdminChangePasswordPage(HttpSession session, Model model) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, null, 1);
        if (redirect != null) return redirect;

        String email = (String) session.getAttribute("email");
        Accounts account = accountDAO.findByEmail(email);
        if (account == null) return "redirect:/login.htm";

        model.addAttribute("account", account);
        return "/admin/ChangePassword";
    }

    // Update admin password
    @RequestMapping(value = "admin/changePassword", method = RequestMethod.POST)
    public String updateAdminPassword(@RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmNewPassword") String confirmNewPassword,
            HttpSession session, RedirectAttributes redirectAttributes) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        String email = (String) session.getAttribute("email");
        Accounts existingAccount = accountDAO.findByEmail(email);
        if (existingAccount == null) {
            redirectAttributes.addFlashAttribute("error", "Account not found.");
            return "redirect:/login.htm";
        }

        if (!oldPassword.equals(existingAccount.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Incorrect old password.");
            return "redirect:/admin/changePassword.htm";
        }

        if (newPassword.length() < 6 || !newPassword.matches("^(?=.*[a-zA-Z])(?=.*\\d).+$")) {
            redirectAttributes.addFlashAttribute("error", "Password must be at least 6 characters long and contain both letters and numbers.");
            return "redirect:/admin/changePassword.htm";
        }

        if (!newPassword.equals(confirmNewPassword)) {
            redirectAttributes.addFlashAttribute("error", "New password and confirmation do not match.");
            return "redirect:/admin/changePassword.htm";
        }

        existingAccount.setPassword(newPassword);
        accountDAO.update(existingAccount);
        redirectAttributes.addFlashAttribute("message", "Password updated successfully.");
        return "redirect:/admin/account.htm";
    }

    // Display employee account information page
    @RequestMapping(value = "employee/accountInformation", method = RequestMethod.GET)
    public String showEmployeeAccountInformation(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        String email = (String) session.getAttribute("email");
        Accounts account = accountDAO.findByEmail(email);
        if (account == null) return "redirect:/login.htm";

        model.addAttribute("account", account);
        return "/employee/accountInformation";
    }

    // Update employee account information
    @RequestMapping(value = "employee/updateInformation", method = RequestMethod.POST)
    public String updateEmployeeAccountInformation(@ModelAttribute("account") Accounts account,
            RedirectAttributes redirectAttributes, HttpSession session) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        Accounts existingAccount = accountDAO.findById(account.getAccountID());
        if (existingAccount != null) {
            if (!existingAccount.getPhoneNumber().equals(account.getPhoneNumber())
                    && accountDAO.isPhoneNumberRegistered(account.getPhoneNumber())) {
                redirectAttributes.addFlashAttribute("error", "Phone number is already registered.");
                return "redirect:/employee/accountInformation.htm";
            }

            existingAccount.setEmail(account.getEmail());
            existingAccount.setGender(account.getGender());
            existingAccount.setPhoneNumber(account.getPhoneNumber());
            existingAccount.setAddress(account.getAddress());
            existingAccount.setFullName(account.getFullName());
            existingAccount.setBirthday(account.getBirthday());

            accountDAO.update(existingAccount);
            redirectAttributes.addFlashAttribute("message", "Account information updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Account not found.");
        }

        return "redirect:/employee/index.htm";
    }

    // Display employee change password page
    @RequestMapping(value = "employee/changePassword", method = RequestMethod.GET)
    public String showEmployeeChangePasswordPage(HttpSession session, Model model) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, null, 2);
        if (redirect != null) return redirect;

        String email = (String) session.getAttribute("email");
        Accounts account = accountDAO.findByEmail(email);
        if (account == null) return "redirect:/login.htm";

        model.addAttribute("account", account);
        return "/employee/ChangePassword";
    }

    // Update employee password
    @RequestMapping(value = "employee/changePassword", method = RequestMethod.POST)
    public String updateEmployeePassword(@RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmNewPassword") String confirmNewPassword,
            HttpSession session, RedirectAttributes redirectAttributes) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        String email = (String) session.getAttribute("email");
        Accounts existingAccount = accountDAO.findByEmail(email);
        if (existingAccount == null) {
            redirectAttributes.addFlashAttribute("error", "Account not found.");
            return "redirect:/login.htm";
        }

        if (!oldPassword.equals(existingAccount.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Incorrect old password.");
            return "redirect:/employee/changePassword.htm";
        }

        if (newPassword.length() < 6 || !newPassword.matches("^(?=.*[a-zA-Z])(?=.*\\d).+$")) {
            redirectAttributes.addFlashAttribute("error", "Password must be at least 6 characters long and contain both letters and numbers.");
            return "redirect:/employee/changePassword.htm";
        }

        if (!newPassword.equals(confirmNewPassword)) {
            redirectAttributes.addFlashAttribute("error", "New password and confirmation do not match.");
            return "redirect:/employee/changePassword.htm";
        }

        existingAccount.setPassword(newPassword);
        accountDAO.update(existingAccount);
        redirectAttributes.addFlashAttribute("message", "Password updated successfully.");
        return "redirect:/employee/index.htm";
    }

    // Display shipper account information page
    @RequestMapping(value = "shipper/accountInformation", method = RequestMethod.GET)
    public String showShipperAccountInformation(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 3);
        if (redirect != null) return redirect;

        String email = (String) session.getAttribute("email");
        Accounts account = accountDAO.findByEmail(email);
        if (account == null) return "redirect:/login.htm";

        model.addAttribute("account", account);
        return "/shipper/accountInformation";
    }

    // Update shipper account information
    @RequestMapping(value = "shipper/updateInformation", method = RequestMethod.POST)
    public String updateShipperAccountInformation(@ModelAttribute("account") Accounts account,
            RedirectAttributes redirectAttributes, HttpSession session) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 3);
        if (redirect != null) return redirect;

        Accounts existingAccount = accountDAO.findById(account.getAccountID());
        if (existingAccount != null) {
            if (!existingAccount.getPhoneNumber().equals(account.getPhoneNumber())
                    && accountDAO.isPhoneNumberRegistered(account.getPhoneNumber())) {
                redirectAttributes.addFlashAttribute("error", "Phone number is already registered.");
                return "redirect:/shipper/accountInformation.htm";
            }

            existingAccount.setEmail(account.getEmail());
            existingAccount.setGender(account.getGender());
            existingAccount.setPhoneNumber(account.getPhoneNumber());
            existingAccount.setAddress(account.getAddress());
            existingAccount.setFullName(account.getFullName());
            existingAccount.setBirthday(account.getBirthday());

            accountDAO.update(existingAccount);
            redirectAttributes.addFlashAttribute("message", "Account information updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Account not found.");
        }

        return "redirect:/shipper/index.htm";
    }

    // Display shipper change password page
    @RequestMapping(value = "shipper/changePassword", method = RequestMethod.GET)
    public String showShipperChangePasswordPage(HttpSession session, Model model) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, null, 3);
        if (redirect != null) return redirect;

        String email = (String) session.getAttribute("email");
        Accounts account = accountDAO.findByEmail(email);
        if (account == null) return "redirect:/login.htm";

        model.addAttribute("account", account);
        return "/shipper/ChangePassword";
    }

    // Update shipper password
    @RequestMapping(value = "shipper/changePassword", method = RequestMethod.POST)
    public String updateShipperPassword(@RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmNewPassword") String confirmNewPassword,
            HttpSession session, RedirectAttributes redirectAttributes) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 3);
        if (redirect != null) return redirect;

        String email = (String) session.getAttribute("email");
        Accounts existingAccount = accountDAO.findByEmail(email);
        if (existingAccount == null) {
            redirectAttributes.addFlashAttribute("error", "Account not found.");
            return "redirect:/login.htm";
        }

        if (!oldPassword.equals(existingAccount.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Incorrect old password.");
            return "redirect:/shipper/changePassword.htm";
        }

        if (newPassword.length() < 6 || !newPassword.matches("^(?=.*[a-zA-Z])(?=.*\\d).+$")) {
            redirectAttributes.addFlashAttribute("error", "Password must be at least 6 characters long and contain both letters and numbers.");
            return "redirect:/shipper/changePassword.htm";
        }

        if (!newPassword.equals(confirmNewPassword)) {
            redirectAttributes.addFlashAttribute("error", "New password and confirmation do not match.");
            return "redirect:/shipper/changePassword.htm";
        }

        existingAccount.setPassword(newPassword);
        accountDAO.update(existingAccount);
        redirectAttributes.addFlashAttribute("message", "Password updated successfully.");
        return "redirect:/shipper/index.htm";
    }

    // Display client account information page
    @RequestMapping(value = "client/accountInformation", method = RequestMethod.GET)
    public String showClientAccountInformation(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 4);
        if (redirect != null) return redirect;

        String email = (String) session.getAttribute("email");
        Accounts account = accountDAO.findByEmail(email);
        if (account == null) return "redirect:/login.htm";

        model.addAttribute("account", account);
        return "/client/accountInformation";
    }

    // Update client account information
    @RequestMapping(value = "client/updateInformation", method = RequestMethod.POST)
    public String updateClientAccountInformation(@ModelAttribute("account") Accounts account,
            RedirectAttributes redirectAttributes, HttpSession session) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 4);
        if (redirect != null) return redirect;

        Accounts existingAccount = accountDAO.findById(account.getAccountID());
        if (existingAccount != null) {
            if (!existingAccount.getPhoneNumber().equals(account.getPhoneNumber())
                    && accountDAO.isPhoneNumberRegistered(account.getPhoneNumber())) {
                redirectAttributes.addFlashAttribute("error", "Phone number is already registered.");
                return "redirect:/client/accountInformation.htm";
            }

            existingAccount.setEmail(account.getEmail());
            existingAccount.setGender(account.getGender());
            existingAccount.setPhoneNumber(account.getPhoneNumber());
            existingAccount.setAddress(account.getAddress());
            existingAccount.setFullName(account.getFullName());
            existingAccount.setBirthday(account.getBirthday());

            accountDAO.update(existingAccount);
            redirectAttributes.addFlashAttribute("successMessage", "Account information updated successfully.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Account not found.");
        }

        return "redirect:/client/clientboard.htm";
    }

    // Display client change password page
    @RequestMapping(value = "client/changePassword", method = RequestMethod.GET)
    public String showClientChangePasswordPage(HttpSession session, Model model) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, null, 4);
        if (redirect != null) return redirect;

        String email = (String) session.getAttribute("email");
        Accounts account = accountDAO.findByEmail(email);
        if (account == null) return "redirect:/login.htm";

        model.addAttribute("account", account);
        return "/client/ChangePassword";
    }

    // Update client password
    @RequestMapping(value = "client/changePassword", method = RequestMethod.POST)
    public String updateClientPassword(@RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmNewPassword") String confirmNewPassword,
            HttpSession session, RedirectAttributes redirectAttributes) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 4);
        if (redirect != null) return redirect;

        String email = (String) session.getAttribute("email");
        Accounts existingAccount = accountDAO.findByEmail(email);
        if (existingAccount == null) {
            redirectAttributes.addFlashAttribute("error", "Account not found.");
            return "redirect:/login.htm";
        }

        if (!oldPassword.equals(existingAccount.getPassword())) {
            redirectAttributes.addFlashAttribute("error", "Incorrect old password.");
            return "redirect:/client/changePassword.htm";
        }

        if (newPassword.length() < 6 || !newPassword.matches("^(?=.*[a-zA-Z])(?=.*\\d).+$")) {
            redirectAttributes.addFlashAttribute("error", "Password must be at least 6 characters long and contain both letters and numbers.");
            return "redirect:/client/changePassword.htm";
        }

        if (!newPassword.equals(confirmNewPassword)) {
            redirectAttributes.addFlashAttribute("error", "New password and confirmation do not match.");
            return "redirect:/client/changePassword.htm";
        }

        existingAccount.setPassword(newPassword);
        accountDAO.update(existingAccount);
        redirectAttributes.addFlashAttribute("successMessage", "Password updated successfully.");
        return "redirect:/client/clientboard.htm";
    }
}

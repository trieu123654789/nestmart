package com.controllers;

import com.models.Accounts;
import com.models.AccountsDAO;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utils.RoleUtils;

@Controller
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private AccountsDAO accountsDAO;

    // Display list of user accounts for employee with search and pagination
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String showEmployee(ModelMap model,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int pageSize,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               HttpServletRequest request,
                               RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

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

        return "/employee/index";
    }

    // Return role name by role ID
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

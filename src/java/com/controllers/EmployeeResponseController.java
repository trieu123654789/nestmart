package com.controllers;

import com.models.EmployeeResponseDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utils.RoleUtils;

@Controller
@RequestMapping("/employee")
public class EmployeeResponseController {

    @Autowired
    private EmployeeResponseDAO employeeResponseDAO;

    // Display responses for a specific feedback
    @GetMapping("/feedbackResponses")
    public String viewResponses(@RequestParam("feedbackID") int feedbackID,
                                Model model,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        model.addAttribute("responses", employeeResponseDAO.getResponsesByFeedbackID(feedbackID));
        return "/employee/feedbackResponses";
    }
}

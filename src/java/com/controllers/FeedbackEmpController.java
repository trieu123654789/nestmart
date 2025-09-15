package com.controllers;

import com.models.EmployeeResponse;
import com.models.EmployeeResponseDAO;
import com.models.Feedback;
import com.models.FeedbackDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import utils.RoleUtils;

@Controller
@RequestMapping("/employee")
public class FeedbackEmpController {

    @Autowired
    private FeedbackDAO feedbackDAO;
    @Autowired
    private EmployeeResponseDAO employeeResponseDAO;

    // Display paginated feedback list for employee
    @RequestMapping(value = "viewFeedbackEmp", method = RequestMethod.GET)
    public String showAllFeedback(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword,
            ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null && !redirect.isEmpty()) return redirect;

        if (page < 1) page = 1;
        if (pageSize < 5) pageSize = 5;
        if (pageSize > 100) pageSize = 100;

        List<Feedback> feedbacks;
        int totalFeedbacks;
        String trimmedKeyword = (keyword != null) ? keyword.trim() : null;

        if (trimmedKeyword != null && !trimmedKeyword.isEmpty()) {
            List<Feedback> allResults = feedbackDAO.searchByKeyword(trimmedKeyword);
            totalFeedbacks = allResults.size();
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalFeedbacks);
            feedbacks = (fromIndex < toIndex) ? allResults.subList(fromIndex, toIndex) : new ArrayList<>();
        } else {
            totalFeedbacks = feedbackDAO.getTotalFeedbackCount();
            feedbacks = feedbackDAO.findPaginated(page, pageSize);
        }

        int totalPages = (int) Math.ceil((double) totalFeedbacks / pageSize);
        int startItem = totalFeedbacks > 0 ? (page - 1) * pageSize + 1 : 0;
        int endItem = Math.min(page * pageSize, totalFeedbacks);

        Map<Integer, EmployeeResponse> feedbackResponses = new HashMap<>();
        for (Feedback feedback : feedbacks) {
            EmployeeResponse response = feedbackDAO.getResponseByFeedbackId(feedback.getFeedbackID());
            feedbackResponses.put(feedback.getFeedbackID(), response);
        }

        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("feedbackResponses", feedbackResponses);
        model.addAttribute("newResponse", new EmployeeResponse());
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalFeedbacks", totalFeedbacks);
        model.addAttribute("startItem", startItem);
        model.addAttribute("endItem", endItem);
        model.addAttribute("keyword", trimmedKeyword);

        return "/employee/feedbackEmp";
    }

    // Initialize a new Feedback object for model attribute
    @ModelAttribute("responseForm")
    public Feedback responseForm() {
        return new Feedback();
    }

    // Display response form for a specific feedback
    @RequestMapping(value = "responseFeedback", method = RequestMethod.GET)
    public String showResponseForm(@RequestParam("feedbackID") int feedbackID, ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null && !redirect.isEmpty()) return redirect;

        Feedback fb = feedbackDAO.get(feedbackID);
        model.addAttribute("feedbackdetails", fb);
        return "/employee/responseFeedback";
    }

    // Save employee response to feedback
    @RequestMapping(value = "saveFeedbackResponse", method = RequestMethod.POST)
    public String saveFeedbackResponse(
            @ModelAttribute("newResponse") @Valid EmployeeResponse employeeResponse,
            BindingResult br,
            @RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword,
            ModelMap model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null && !redirect.isEmpty()) return redirect;

        if (currentPage < 1) currentPage = 1;
        if (pageSize < 5) pageSize = 5;
        if (pageSize > 100) pageSize = 100;

        if (br.hasErrors()) {
            return "redirect:viewFeedbackEmp.htm?page=" + currentPage + "&pageSize=" + pageSize
                    + (keyword != null && !keyword.trim().isEmpty() ? "&keyword=" + keyword.trim() : "")
                    + "&error=1";
        }

        if (employeeResponse.getResponseDate() == null) {
            employeeResponse.setResponseDate(LocalDateTime.now());
        }

        employeeResponseDAO.addEmployeeResponse(employeeResponse);

        String redirectUrl = "redirect:viewFeedbackEmp.htm?page=" + currentPage + "&pageSize=" + pageSize;
        if (keyword != null && !keyword.trim().isEmpty()) {
            redirectUrl += "&keyword=" + keyword.trim();
        }
        redirectUrl += "&success=1";

        return redirectUrl;
    }
}

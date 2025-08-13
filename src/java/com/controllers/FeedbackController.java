package com.controllers;

import com.models.Feedback;
import com.models.FeedbackDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import utils.RoleUtils;

@Controller
@RequestMapping("/admin")
public class FeedbackController {

    @Autowired
    private FeedbackDAO feedbackDAO;

    // Display paginated feedback list for admin
    @RequestMapping(value = "viewFeedbackAd", method = RequestMethod.GET)
    public String showAllFeedbackAdmin(ModelMap model,
                                       @RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "8") int pageSize,
                                       @RequestParam(value = "keyword", required = false) String keyword,
                                       HttpServletRequest request,
                                       RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        List<Feedback> feedbacks = new ArrayList<>();
        int totalFeedback = 0;
        String finalKeyword = (keyword != null) ? keyword.trim() : null;

        if (finalKeyword != null && !finalKeyword.isEmpty()) {
            List<Feedback> allResults = feedbackDAO.searchByKeyword(finalKeyword);
            totalFeedback = allResults.size();
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalFeedback);
            feedbacks = (fromIndex < toIndex) ? allResults.subList(fromIndex, toIndex) : new ArrayList<>();
        } else {
            totalFeedback = feedbackDAO.getTotalFeedbackCount("");
            feedbacks = feedbackDAO.getPagedFeedback("", page, pageSize);
        }

        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) totalFeedback / pageSize));
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keyword", finalKeyword);
        model.addAttribute("totalFeedback", totalFeedback);

        return "/admin/feedback";
    }
}

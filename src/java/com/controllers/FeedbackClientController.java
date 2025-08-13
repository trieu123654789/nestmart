package com.controllers;

import com.models.EmployeeResponse;
import com.models.Feedback;
import com.models.FeedbackDAO;
import com.models.Products;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utils.RoleUtils;

@Controller
@RequestMapping("/client")
public class FeedbackClientController {

    @Autowired
    private FeedbackDAO feedbackDAO;

    @Autowired
    private ServletContext servletContext;

    // Display the feedback form for a specific product
    @RequestMapping(value = "feedback", method = RequestMethod.GET)
    public String showFeedbackForm(@RequestParam("productID") String productID,
                                   ModelMap model,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {

        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 4);
        if (redirect != null) return redirect;

        Products product = feedbackDAO.findProductByID(productID);
        model.addAttribute("product", product);
        return "/client/feedback";
    }

    // Handle feedback submission from customer
    @RequestMapping(value = "feedbacks", method = RequestMethod.POST)
    public String submitFeedback(@RequestParam("productID") String productID,
                                 @RequestParam("rating") int rating,
                                 @RequestParam("feedbackContent") String feedbackContent,
                                 @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes) {

        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 4);
        if (redirect != null) return redirect;

        Integer customerID = (Integer) session.getAttribute("accountId");
        try {
            Feedback feedback = new Feedback();
            feedback.setProductID(productID);
            feedback.setCustomerID(customerID);
            feedback.setRating((short) rating);
            feedback.setFeedbackContent(feedbackContent);
            feedback.setFeedbackDate(LocalDateTime.now());

            feedbackDAO.save(feedback, imageFiles, servletContext);
            redirectAttributes.addFlashAttribute("successMessage", "Thank you for your feedback!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error submitting feedback: " + e.getMessage());
        }

        return "redirect:/client/orderHistory.htm?status=Completed";
    }

    // Display customer feedback and employee response for a product
    @RequestMapping(value = "viewFeedback", method = RequestMethod.GET)
    public String showFeedback(ModelMap model,
                               @RequestParam("productID") String productID,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {

        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 4);
        if (redirect != null) return redirect;

        Integer customerID = (Integer) session.getAttribute("accountId");
        Feedback feedback = feedbackDAO.getFeedbackByProductAndCustomer(productID, customerID);

        if (feedback == null) {
            model.addAttribute("message", "You have not submitted feedback for this product yet.");
            return "/client/feedbackView";
        }

        EmployeeResponse empResponse = feedbackDAO.getResponseByFeedbackId(feedback.getFeedbackID());
        model.addAttribute("feedback", feedback);
        model.addAttribute("employeeResponse", empResponse);

        return "/client/feedbackView";
    }
}

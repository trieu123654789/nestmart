package com.controllers;

import com.models.Orders;
import com.models.ReturnRequestResponse;
import com.models.ReturnRequestResponseDAO;
import com.models.OrdersDAO;
import java.time.LocalDateTime;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employee")
public class EmployeeReturnController {

    @Autowired
    private OrdersDAO ordersDAO;

    @Autowired
    private ReturnRequestResponseDAO returnRequestResponseDAO;

    // List all return requests and completed returns
    @GetMapping("/returnRequests")
    public String listReturnRequests(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("email") == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to access this page.");
            return "redirect:/login.htm";
        }

        Integer role = (Integer) session.getAttribute("role");
        if (role == null || role != 2) { // assuming role 2 is employee
            String currentUrl = request.getHeader("referer");
            return (currentUrl != null && !currentUrl.isEmpty()) ? "redirect:" + currentUrl : "redirect:/";
        }

        List<Orders> returnRequests = returnRequestResponseDAO.getOrdersWithReturnRequests();
        List<Orders> completedReturns = returnRequestResponseDAO.getCompletedReturnOrders();

        model.addAttribute("returnRequests", returnRequests);
        model.addAttribute("completedReturns", completedReturns);

        return "/employee/returnRequests";
    }

    // Process a return request
    @PostMapping("/process")
    public String processReturnRequest(
            @RequestParam("orderID") String orderID,
            @RequestParam("message") String message,
            @RequestParam("newOrderStatus") String newOrderStatus,
            @RequestParam(value = "newReturnRequestStatus", required = false) String newReturnRequestStatus,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Integer employeeId = (Integer) session.getAttribute("accountId");
        if (employeeId == null) {
            redirectAttributes.addFlashAttribute("error", "No employee logged in");
            return "redirect:/employee/returnRequests.htm";
        }

        if (newReturnRequestStatus == null || newReturnRequestStatus.isEmpty()) {
            newReturnRequestStatus = newOrderStatus;
        }

        ReturnRequestResponse response = new ReturnRequestResponse();
        response.setOrderID(orderID);
        response.setEmployeeID(employeeId);
        response.setReturnRequestResponseDate(LocalDateTime.now());
        response.setMessage(message);

        returnRequestResponseDAO.addReturnRequestResponse(response);
        returnRequestResponseDAO.updateOrderStatus(orderID, newOrderStatus, newReturnRequestStatus);

        redirectAttributes.addFlashAttribute("success", "Return request processed successfully");
        return "redirect:/employee/returnRequests.htm";
    }

    // Delete a completed return order
    @PostMapping("/deleteCompletedReturn")
    public String deleteCompletedReturnOrder(
            @RequestParam("orderID") String orderID,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Integer employeeId = (Integer) session.getAttribute("accountId");
        if (employeeId == null) {
            redirectAttributes.addFlashAttribute("error", "No employee logged in");
            return "redirect:/employee/returnRequests.htm";
        }

        if (orderID == null || orderID.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Invalid order ID");
            return "redirect:/employee/returnRequests.htm";
        }

        try {
            returnRequestResponseDAO.deleteCompletedReturnOrder(orderID);
            redirectAttributes.addFlashAttribute("success", "Completed return order deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete completed return order: " + e.getMessage());
        }

        return "redirect:/employee/returnRequests.htm";
    }
}

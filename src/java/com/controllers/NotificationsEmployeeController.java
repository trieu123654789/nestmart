package com.controllers;

import com.models.Accounts;
import com.models.Notifications;
import com.models.NotificationsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import utils.RoleUtils;

@Controller
@RequestMapping("/employee")
public class NotificationsEmployeeController {

    @Autowired
    private NotificationsDAO notificationsDAO;

    // Display notifications list for employee
    @RequestMapping(value = "viewNotifications", method = RequestMethod.GET)
    public String showNotifications(
            @RequestParam(value = "keyword", required = false) String keyword,
            ModelMap model,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        int employeeID = (Integer) session.getAttribute("accountId");
        String finalKeyword = (keyword != null) ? keyword.trim() : "";

        List<Notifications> notifications = finalKeyword.isEmpty() ?
                notificationsDAO.findNotificationsByEmployeeId(employeeID) :
                notificationsDAO.searchByKeyword(employeeID, finalKeyword);

        model.addAttribute("notifications", notifications);
        model.addAttribute("totalNotifications", notifications.size());
        model.addAttribute("keyword", finalKeyword);

        return "/employee/notificationsView";
    }

    // Delete a single notification by ID
    @RequestMapping(value = "empDeleteNotification", method = RequestMethod.GET)
    public String empDeleteNotification(@RequestParam("notificationID") int notificationID,
                                        HttpServletRequest request,
                                        RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        notificationsDAO.delete(notificationID);
        return "redirect:viewNotifications.htm";
    }

    // Search notifications by keyword
    @RequestMapping(value = "findNotifications", method = RequestMethod.GET)
    public String findNotification(@RequestParam("keyword") String keyword, ModelMap model) {
        List<Notifications> notificationsList = notificationsDAO.findNotifications(keyword);
        model.addAttribute("notificationsList", notificationsList);
        return "/employee/notificationsView";
    }

    // Return empty notification form for creating new notification
    @ModelAttribute("addNotificationForm")
    public Notifications notificationForm() {
        return new Notifications();
    }

    // Show form for creating a new notification
    @RequestMapping(value = "addNotificationForm", method = RequestMethod.GET)
    public String showNotificationForm() {
        return "/employee/notificationsCreate";
    }

    // Create and send notification to all customers
    @RequestMapping(value = "addNotification", method = RequestMethod.POST)
    public String createNotification(@ModelAttribute("addNotificationForm") @Valid Notifications noti,
                                     BindingResult br,
                                     HttpSession session,
                                     ModelMap model) {

        if (br.hasErrors()) return "/employee/notificationsCreate";

        if (noti.getSendDate() == null) noti.setSendDate(Date.valueOf(LocalDate.now()));

        int employeeID = (Integer) session.getAttribute("accountId");
        noti.setEmployeeID(employeeID);

        int notificationsSent = notificationsDAO.addNotificationToAllCustomers(noti);
        model.addAttribute("successMessage", "Notification sent successfully to " + notificationsSent + " customers!");

        return "redirect:viewNotifications.htm";
    }

    // Get total number of customers (AJAX)
    @RequestMapping(value = "getCustomerCount", method = RequestMethod.GET)
    @ResponseBody
    public int getCustomerCount() {
        List<Accounts> customers = notificationsDAO.getAllCustomer();
        return customers != null ? customers.size() : 0;
    }

    // Return empty notification form for editing
    @ModelAttribute("editNotificationForm")
    public Notifications editNotificationForm() {
        return new Notifications();
    }

    // Show form to edit an existing notification
    @RequestMapping(value = "updateNotification", method = RequestMethod.GET)
    public String updateNotificationForm(@RequestParam("notificationID") int notificationID,
                                         ModelMap model,
                                         HttpServletRequest request,
                                         RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        Notifications noti = notificationsDAO.get(notificationID);
        model.addAttribute("notification", noti);
        return "/employee/notificationsUpdate";
    }

    // Update an existing notification
    @RequestMapping(value = "editNotification", method = RequestMethod.POST)
    public String updateNotification(@ModelAttribute("editNotificationForm") @Valid Notifications noti,
                                     BindingResult br,
                                     HttpSession session) {

        if (br.hasErrors()) return "/employee/notificationsUpdate";

        if (noti.getSendDate() == null) noti.setSendDate(Date.valueOf(LocalDate.now()));

        int employeeID = (Integer) session.getAttribute("accountId");
        noti.setEmployeeID(employeeID);

        notificationsDAO.update(noti);
        return "redirect:viewNotifications.htm";
    }

    // Delete multiple notifications in bulk
    @RequestMapping(value = "bulkDeleteNotifications", method = RequestMethod.POST)
    public String bulkDeleteNotifications(@RequestParam("selectedNotifications") List<Integer> notificationIds,
                                          HttpSession session,
                                          RedirectAttributes redirectAttributes) {

        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        if (notificationIds == null || notificationIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "No notifications selected for deletion.");
            return "redirect:viewNotifications.htm";
        }

        int deletedCount = notificationsDAO.deleteBulkNotifications(notificationIds);

        if (deletedCount > 0) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Successfully deleted " + deletedCount + " notification(s).");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "No notifications were deleted. Please try again.");
        }

        return "redirect:viewNotifications.htm";
    }

    // Delete all notifications of current employee
    @RequestMapping(value = "deleteAllNotifications", method = RequestMethod.POST)
    public String deleteAllNotifications(HttpSession session,
                                         RedirectAttributes redirectAttributes) {

        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        int employeeID = (Integer) session.getAttribute("accountId");
        int deletedCount = notificationsDAO.deleteAllNotificationsByEmployee(employeeID);

        if (deletedCount > 0) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "Successfully deleted all " + deletedCount + " notification(s).");
        } else {
            redirectAttributes.addFlashAttribute("infoMessage",
                    "No notifications found to delete.");
        }

        return "redirect:viewNotifications.htm";
    }
}

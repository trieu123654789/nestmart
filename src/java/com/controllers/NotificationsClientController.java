package com.controllers;

import com.models.Notifications;
import com.models.NotificationsDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import utils.RoleUtils;

@Controller
@RequestMapping("/client")
public class NotificationsClientController {

    @Autowired
    private NotificationsDAO notificationsDAO;

    // Display list of notifications for client
    @RequestMapping(value = "viewNotifications", method = RequestMethod.GET)
    public String showNotifications(@RequestParam(value = "status", required = false) String status,
                                    ModelMap model,
                                    HttpServletRequest request,
                                    RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 4);
        if (redirect != null) return redirect;

        int customerID = (int) session.getAttribute("accountId");

        List<Notifications> notifications;
        if (status != null && !status.isEmpty()) {
            notifications = notificationsDAO.findNotificationsByCustomerIdAndStatus(customerID, status);
        } else {
            notifications = notificationsDAO.findNotificationsByCustomerId(customerID);
        }

        model.addAttribute("notifications", notifications);
        return "/client/notifications";
    }

    // Display notification details and update status if unread
    @RequestMapping(value = "viewNotificationDetails", method = RequestMethod.GET)
    public String viewNotificationDetail(@RequestParam("notificationID") int notificationID,
                                         ModelMap model,
                                         HttpServletRequest request,
                                         RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 4);
        if (redirect != null) return redirect;

        Notifications notification = notificationsDAO.get(notificationID);
        model.addAttribute("notification", notification);

        if ("Unread".equals(notification.getStatus())) {
            notification.setStatus("Read");
            notificationsDAO.update(notification);
        }

        return "/client/notificationDetails";
    }
}

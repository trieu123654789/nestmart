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
import java.util.ArrayList;
import java.util.List;
import utils.RoleUtils;

@Controller
@RequestMapping("/admin")
public class NotificationsController {

    @Autowired
    private NotificationsDAO notificationsDAO;

    // Display paginated notifications list for admin
    @RequestMapping(value = "notifications", method = RequestMethod.GET)
    public String showNotifications(
            ModelMap model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        String finalKeyword = (keyword != null) ? keyword.trim() : "";
        List<Notifications> notificationsList;
        int totalNotifications;

        if (!finalKeyword.isEmpty()) {
            List<Notifications> allResults = notificationsDAO.searchByKeyword(finalKeyword, 1, Integer.MAX_VALUE);
            totalNotifications = allResults.size();
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalNotifications);
            notificationsList = (fromIndex < toIndex) ? allResults.subList(fromIndex, toIndex) : new ArrayList<>();
        } else {
            totalNotifications = notificationsDAO.getTotalNotifications();
            notificationsList = notificationsDAO.findPaginated(page, pageSize);
        }

        model.addAttribute("notificationsList", notificationsList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) totalNotifications / pageSize));
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keyword", finalKeyword);
        model.addAttribute("totalNotifications", totalNotifications);

        return "/admin/notifications";
    }

    // Delete a notification by ID
    @RequestMapping(value = "deleteNotification", method = RequestMethod.GET)
    public String deleteNotification(
            @RequestParam("notificationID") int notificationID,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        notificationsDAO.delete(notificationID);
        return "redirect:/admin/notifications.htm";
    }
}

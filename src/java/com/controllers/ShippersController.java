package com.controllers;

import com.models.AccountsDAO;
import com.models.OrdersDAO;
import com.models.ShipperScheduleAndSalaryDAO;
import com.models.Orders;
import com.models.WeekSchedule;
import com.models.WeekDetails;
import com.models.WeekSalaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utils.RoleUtils;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shipper")
public class ShippersController {

    @Autowired
    private AccountsDAO accountsDAO;
    @Autowired
    private OrdersDAO orderDAO;
    @Autowired
    private ShipperScheduleAndSalaryDAO scheduleAndSalaryDAO;

    // Display shipper orders page
    @RequestMapping("/shippers")
    public String showShipperPage(@RequestParam(value = "orderID", required = false) String orderIDQuery,
                                  @RequestParam(value = "search", required = false) String searchQuery,
                                  @RequestParam(value = "status", required = false) String status,
                                  Model model,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 3);
        if (redirect != null) return redirect;

        Integer accountId = (Integer) session.getAttribute("accountId");
        List<Orders> orders = orderDAO.searchAndFilterOrders(accountId, orderIDQuery, searchQuery, status);

        model.addAttribute("orders", orders);
        model.addAttribute("orderID", orderIDQuery);
        model.addAttribute("search", searchQuery);
        model.addAttribute("status", status);
        model.addAttribute("accountId", accountId);

        return "/shipper/shippers";
    }

    // Display shipper schedule and salary page
    @RequestMapping("/scheduleAndSalary")
    public String showScheduleAndSalaryPage(@RequestParam(value = "startMonth", required = false) Integer startMonth,
                                            @RequestParam(value = "startYear", required = false) Integer startYear,
                                            @RequestParam(value = "endMonth", required = false) Integer endMonth,
                                            @RequestParam(value = "endYear", required = false) Integer endYear,
                                            @RequestParam(value = "currentTab", defaultValue = "schedule") String currentTab,
                                            Model model,
                                            HttpSession session,
                                            RedirectAttributes redirectAttributes) {

        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 3);
        if (redirect != null) return redirect;

        Integer shipperID = (Integer) session.getAttribute("accountId");

        try {
            java.sql.Date sqlStartDate = null;
            java.sql.Date sqlEndDate = null;

            if (startMonth != null && startYear != null) {
                Calendar startCal = Calendar.getInstance();
                startCal.set(startYear, startMonth - 1, 1, 0, 0, 0);
                startCal.set(Calendar.MILLISECOND, 0);
                sqlStartDate = new java.sql.Date(startCal.getTimeInMillis());
            }

            if (endMonth != null && endYear != null) {
                Calendar endCal = Calendar.getInstance();
                endCal.set(endYear, endMonth - 1, 1, 0, 0, 0);
                endCal.set(Calendar.MILLISECOND, 0);
                endCal.set(Calendar.DAY_OF_MONTH, endCal.getActualMaximum(Calendar.DAY_OF_MONTH));
                endCal.set(Calendar.HOUR_OF_DAY, 23);
                endCal.set(Calendar.MINUTE, 59);
                endCal.set(Calendar.SECOND, 59);
                sqlEndDate = new java.sql.Date(endCal.getTimeInMillis());
            }

            List<WeekSchedule> schedules;
            if ("schedule".equals(currentTab) && sqlStartDate != null && sqlEndDate != null) {
                schedules = scheduleAndSalaryDAO.getShipperWeekSchedulesByDateRange(shipperID, sqlStartDate, sqlEndDate);
            } else {
                schedules = scheduleAndSalaryDAO.getShipperWeekSchedules(shipperID);
            }

            List<WeekSalaryDTO> salaryHistory;
            if ("salary".equals(currentTab) && sqlStartDate != null && sqlEndDate != null) {
                salaryHistory = scheduleAndSalaryDAO.getShipperSalaryByDateRange(shipperID, sqlStartDate, sqlEndDate);
            } else {
                salaryHistory = scheduleAndSalaryDAO.getShipperSalaryHistory(shipperID);
            }

            WeekSchedule currentSchedule = scheduleAndSalaryDAO.getCurrentWeekSchedule(shipperID);
            List<WeekSchedule> upcomingSchedules = scheduleAndSalaryDAO.getUpcomingWeekSchedules(shipperID, 4);

            model.addAttribute("schedules", schedules);
            model.addAttribute("salaryHistory", salaryHistory);
            model.addAttribute("currentSchedule", currentSchedule);
            model.addAttribute("upcomingSchedules", upcomingSchedules);
            model.addAttribute("pastSchedules", schedules);
            model.addAttribute("startMonth", startMonth);
            model.addAttribute("startYear", startYear);
            model.addAttribute("endMonth", endMonth);
            model.addAttribute("endYear", endYear);
            model.addAttribute("currentTab", currentTab);
            model.addAttribute("shipperID", shipperID);

        } catch (Exception e) {
            model.addAttribute("error", "Error loading schedule and salary data: " + e.getMessage());
        }

        return "/shipper/scheduleAndSalary";
    }

    // Test endpoint
    @RequestMapping(value = "/test.htm", method = RequestMethod.GET)
    @ResponseBody
    public String testEndpoint(HttpSession session, RedirectAttributes redirectAttributes) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 3);
        if (redirect != null) return "Not authorized";
        return "Test endpoint working!";
    }

    // Get performance XML
    @RequestMapping(value = "/performance/{weekScheduleID}.htm", method = RequestMethod.GET)
    @ResponseBody
    public String getWeekPerformance(@PathVariable int weekScheduleID, HttpSession session) {
        if (!RoleUtils.hasRequiredRole(session, 3)) return "<performance><error>Not authorized</error></performance>";

        Integer shipperID = (Integer) session.getAttribute("accountId");

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version='1.0' encoding='UTF-8'?><performance>");

        try {
            int totalWorkingHours = scheduleAndSalaryDAO.getTotalWorkingHours(shipperID, weekScheduleID);
            int totalOrdersDelivered = scheduleAndSalaryDAO.getTotalOrdersDelivered(shipperID, weekScheduleID);
            WeekSalaryDTO weekSalary = scheduleAndSalaryDAO.getShipperWeekSalary(shipperID, weekScheduleID);

            xml.append("<totalWorkingHours>").append(totalWorkingHours).append("</totalWorkingHours>");
            xml.append("<totalOrdersDelivered>").append(totalOrdersDelivered).append("</totalOrdersDelivered>");
            xml.append("<totalSalary>").append(weekSalary != null ? weekSalary.getTotalSalary() : 0).append("</totalSalary>");
            xml.append("<totalOvertimeHours>").append(weekSalary != null ? weekSalary.getTotalOvertimeHours() : 0).append("</totalOvertimeHours>");
            xml.append("<totalOvertimeSalary>").append(weekSalary != null ? weekSalary.getTotalOvertimeSalary() : 0).append("</totalOvertimeSalary>");
        } catch (Exception e) {
            xml.append("<error>Database error</error>");
        }

        xml.append("</performance>");
        return xml.toString();
    }

    // Get week details XML
    @RequestMapping(value = "/week-details/{weekScheduleID}.htm", method = RequestMethod.GET)
    @ResponseBody
    public String getWeekDetails(@PathVariable int weekScheduleID, HttpSession session) {
        if (!RoleUtils.hasRequiredRole(session, 3)) return "<weekDetails><error>Not authorized</error></weekDetails>";

        Integer shipperID = (Integer) session.getAttribute("accountId");

        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version='1.0' encoding='UTF-8'?><weekDetails>");

        try {
            List<WeekDetails> details = scheduleAndSalaryDAO.getShipperWeekDetails(shipperID, weekScheduleID);
            if (details != null && !details.isEmpty()) {
                for (WeekDetails detail : details) {
                    xml.append("<detail>");
                    xml.append("<dayID>").append(detail.getDayID()).append("</dayID>");
                    xml.append("<shiftID>").append(detail.getShiftID()).append("</shiftID>");
                    xml.append("<overtimeHours>").append(detail.getOvertimeHours()).append("</overtimeHours>");
                    xml.append("<status>").append(detail.getStatus() != null ? detail.getStatus() : "Scheduled").append("</status>");
                    xml.append("</detail>");
                }
            } else {
                xml.append("<error>No details found</error>");
            }
        } catch (Exception e) {
            xml.append("<error>Database error</error>");
        }

        xml.append("</weekDetails>");
        return xml.toString();
    }

    // Get specific week salary
    @RequestMapping(value = "/week-salary/{weekScheduleID}", method = RequestMethod.GET)
    @ResponseBody
    public WeekSalaryDTO getWeekSalaryDetails(@PathVariable int weekScheduleID, HttpSession session) {
        if (!RoleUtils.hasRequiredRole(session, 3)) return null;
        Integer shipperID = (Integer) session.getAttribute("accountId");
        return scheduleAndSalaryDAO.getShipperWeekSalary(shipperID, weekScheduleID);
    }

    // Check schedule on specific date
    @RequestMapping(value = "/schedule-check", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Boolean> checkScheduleOnDate(@RequestParam("date") String dateStr, HttpSession session) {
        Map<String, Boolean> result = new HashMap<>();
        if (!RoleUtils.hasRequiredRole(session, 3)) {
            result.put("hasSchedule", false);
            return result;
        }

        Integer shipperID = (Integer) session.getAttribute("accountId");

        try {
            Date date = Date.valueOf(dateStr);
            boolean hasSchedule = scheduleAndSalaryDAO.hasScheduleOnDate(shipperID, date);
            result.put("hasSchedule", hasSchedule);
        } catch (Exception e) {
            result.put("hasSchedule", false);
        }
        return result;
    }

    // Approve return
    @PostMapping("/approveReturn")
    @ResponseBody
    public String approveReturn(@RequestParam("orderID") String orderID, HttpSession session) {
        if (!RoleUtils.hasRequiredRole(session, 3)) return "error";

        try {
            orderDAO.updateOrder(orderID, "Approved", null);
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

    // Confirm return pickup
    @PostMapping("/confirmReturnPickup")
    @ResponseBody
    public String confirmReturnPickup(@RequestParam("orderID") String orderID, HttpSession session) {
        if (!RoleUtils.hasRequiredRole(session, 3)) return "error";

        try {
            orderDAO.updateOrder(orderID, "Return Completed", null);
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

    // Update order status
    @PostMapping("/updateOrderStatus")
    @ResponseBody
    public String updateOrderStatus(@RequestParam("orderID") String orderID,
                                    @RequestParam("status") String status,
                                    @RequestParam(value = "paymentMethod", required = false) String paymentMethod,
                                    HttpSession session) {
        if (!RoleUtils.hasRequiredRole(session, 3)) return "error";

        try {
            orderDAO.updateOrder(orderID, status, paymentMethod);
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

    // Accept order
    @PostMapping("/acceptOrder")
    @ResponseBody
    public String acceptOrder(@RequestParam("orderID") String orderID, HttpSession session) {
        if (!RoleUtils.hasRequiredRole(session, 3)) return "error";

        try {
            orderDAO.updateOrder(orderID, "Delivering", "Cash on Delivery");
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }

    // Complete order
    @PostMapping("/completeOrder")
    @ResponseBody
    public String completeOrder(@RequestParam("orderID") String orderID, HttpSession session) {
        if (!RoleUtils.hasRequiredRole(session, 3)) return "error";

        try {
            orderDAO.updateOrder(orderID, "Completed", "Cash on Delivery");
            return "success";
        } catch (Exception e) {
            return "error";
        }
    }
}

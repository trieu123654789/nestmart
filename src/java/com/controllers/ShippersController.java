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
import java.time.LocalDate;
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
//
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

    // Export week schedule to CSV
    @RequestMapping(value = "/exportWeekCSV.htm", method = RequestMethod.GET)
    public void exportWeekCSV(@RequestParam("weekScheduleID") int weekScheduleID,
                              HttpServletResponse response,
                              HttpSession session) throws IOException {
        String redirect = RoleUtils.checkRoleAndRedirect(session, null, 3);
        if (redirect != null) {
            response.sendRedirect("/nestmart/login.htm");
            return;
        }

        Integer shipperID = (Integer) session.getAttribute("accountId");
        
        try {
            // Get week schedule details
            List<WeekDetails> weekDetails = scheduleAndSalaryDAO.getShipperWeekDetails(shipperID, weekScheduleID);
            WeekSalaryDTO weekSalary = scheduleAndSalaryDAO.getShipperWeekSalary(shipperID, weekScheduleID);
            
            // Set response headers for CSV download
            response.setContentType("text/csv; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            
            String filename = String.format("Shipper_Week_Schedule_%d_%s.csv", 
                    weekScheduleID, LocalDate.now().toString());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            
            PrintWriter writer = response.getWriter();
            writer.write('\uFEFF'); // BOM for UTF-8
            
            // Write CSV header
            writer.println("===============================================");
            writer.println("           SHIPPER WEEKLY SCHEDULE");
            writer.println("===============================================");
            writer.println("Shipper ID: " + shipperID);
            writer.println("Week Schedule ID: " + weekScheduleID);
            writer.println("Generated: " + LocalDate.now());
            writer.println("===============================================");
            writer.println();
            
            if (weekDetails != null && !weekDetails.isEmpty()) {
                writer.println("Day ID,Shift ID,Overtime Hours,Status");
                writer.println("─────────────────────────────────────────");
                
                for (WeekDetails detail : weekDetails) {
                    writer.printf("%d,%d,%.1f,%s%n",
                            detail.getDayID(),
                            detail.getShiftID(),
                            detail.getOvertimeHours() != null ? detail.getOvertimeHours().doubleValue() : 0.0,
                            detail.getStatus() != null ? detail.getStatus() : "Scheduled");
                }
                
                writer.println();
                writer.println("===============================================");
                writer.println("                SALARY SUMMARY");
                writer.println("===============================================");
                
                if (weekSalary != null) {
                    writer.println("Total Salary: $" + (weekSalary.getTotalSalary() != null ? weekSalary.getTotalSalary() : "0.00"));
                    writer.println("Total Overtime Hours: " + (weekSalary.getTotalOvertimeHours() != null ? weekSalary.getTotalOvertimeHours() : "0.0"));
                    writer.println("Total Overtime Salary: $" + (weekSalary.getTotalOvertimeSalary() != null ? weekSalary.getTotalOvertimeSalary() : "0.00"));
                } else {
                    writer.println("No salary data available for this week.");
                }
            } else {
                writer.println("No schedule details found for this week.");
            }
            
            writer.println();
            writer.println("===============================================");
            writer.println("              END OF REPORT");
            writer.println("===============================================");
            
            writer.flush();
            writer.close();
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                    "Error generating CSV: " + e.getMessage());
        }
    }

    // Print week payroll
    @RequestMapping(value = "/printWeekPayroll.htm", method = RequestMethod.GET)
    public String printWeekPayroll(@RequestParam("weekScheduleID") int weekScheduleID,
                                   Model model,
                                   HttpSession session,
                                   RedirectAttributes redirectAttributes) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 3);
        if (redirect != null) return redirect;

        Integer shipperID = (Integer) session.getAttribute("accountId");
        String shipperEmail = (String) session.getAttribute("email");
        
        try {
            // Get week schedule information
            WeekSchedule schedule = scheduleAndSalaryDAO.getWeekScheduleById(weekScheduleID);
            
            // Get week schedule details
            List<WeekDetails> weekDetails = scheduleAndSalaryDAO.getShipperWeekDetails(shipperID, weekScheduleID);
            WeekSalaryDTO weekSalary = scheduleAndSalaryDAO.getShipperWeekSalary(shipperID, weekScheduleID);
            
            // Get performance metrics
            int totalWorkingHours = scheduleAndSalaryDAO.getTotalWorkingHours(shipperID, weekScheduleID);
            int totalOrdersDelivered = scheduleAndSalaryDAO.getTotalOrdersDelivered(shipperID, weekScheduleID);
            
            // Calculate salary components
            BigDecimal basicSalary = weekSalary != null ? weekSalary.getTotalSalary() : BigDecimal.ZERO;
            BigDecimal overtimeHours = BigDecimal.ZERO;
            BigDecimal overtimePay = BigDecimal.ZERO;
            BigDecimal totalSalary = basicSalary;
            
            if (weekSalary != null) {
                overtimeHours = weekSalary.getTotalOvertimeHours() != null ? weekSalary.getTotalOvertimeHours() : BigDecimal.ZERO;
                overtimePay = weekSalary.getTotalOvertimeSalary() != null ? weekSalary.getTotalOvertimeSalary() : BigDecimal.ZERO;
                totalSalary = basicSalary.add(overtimePay);
            }
            
            // Set model attributes to match JSP expectations
            model.addAttribute("schedule", schedule);
            model.addAttribute("shipperID", shipperID);
            model.addAttribute("shipperEmail", shipperEmail != null ? shipperEmail : "N/A");
            model.addAttribute("reportDate", LocalDate.now());
            model.addAttribute("paymentDate", LocalDate.now().plusDays(7)); // Payment 1 week after report
            
            // Performance metrics
            model.addAttribute("totalWorkingHours", totalWorkingHours);
            model.addAttribute("ordersDelivered", totalOrdersDelivered);
            model.addAttribute("overtimeHours", overtimeHours);
            
            // Salary breakdown
            model.addAttribute("basicSalary", basicSalary);
            model.addAttribute("overtimePay", overtimePay);
            model.addAttribute("totalSalary", totalSalary);
            
        } catch (Exception e) {
            model.addAttribute("error", "Error loading payroll data: " + e.getMessage());
        }
        
        return "/shipper/printWeekPayroll";
    }
    // Add this method to your ShippersController.java
@RequestMapping(value = "/checkNewOrders.htm", method = RequestMethod.GET)
@ResponseBody
public void checkNewOrders(@RequestParam("accountId") Integer accountId,
                          @RequestParam("lastCount") Integer lastCount,
                          HttpSession session,
                          HttpServletResponse response) throws IOException {
    
    // Set response content type explicitly
    response.setContentType("application/xml; charset=UTF-8");
    response.setCharacterEncoding("UTF-8");
    
    StringBuilder xml = new StringBuilder();
    xml.append("<?xml version='1.0' encoding='UTF-8'?>");
    xml.append("<response>");
    
    try {
        // Check role authorization
        if (!RoleUtils.hasRequiredRole(session, 3)) {
            xml.append("<hasNewOrders>false</hasNewOrders>");
            xml.append("<totalOrders>0</totalOrders>");
            xml.append("<error>Not authorized</error>");
            xml.append("</response>");
            response.getWriter().write(xml.toString());
            response.getWriter().flush();
            return;
        }

        // Get current orders for this shipper
        List<Orders> currentOrders = orderDAO.searchAndFilterOrders(accountId, null, null, null);
        int currentCount = currentOrders != null ? currentOrders.size() : 0;
        
        // Check if there are new orders
        boolean hasNewOrders = currentCount > lastCount;
        int newOrdersCount = hasNewOrders ? (currentCount - lastCount) : 0;
        
        xml.append("<hasNewOrders>").append(hasNewOrders).append("</hasNewOrders>");
        xml.append("<totalOrders>").append(currentCount).append("</totalOrders>");
        xml.append("<newOrdersCount>").append(newOrdersCount).append("</newOrdersCount>");
        
        // If there are new orders, include details of the latest one
        if (hasNewOrders && currentOrders != null && !currentOrders.isEmpty()) {
            Orders latestOrder = currentOrders.get(0);
            xml.append("<latestOrder>");
            xml.append("<orderID>").append(latestOrder.getOrderID()).append("</orderID>");
            xml.append("<customerName>").append(escapeXml(latestOrder.getCustomerName())).append("</customerName>");
            xml.append("<totalAmount>").append(latestOrder.getTotalAmount()).append("</totalAmount>");
            xml.append("<orderStatus>").append(escapeXml(latestOrder.getOrderStatus())).append("</orderStatus>");
            xml.append("</latestOrder>");
        }
        
        System.out.println("XML Response: " + xml.toString()); // Debug log
        
    } catch (Exception e) {
        System.err.println("Error in checkNewOrders: " + e.getMessage());
        e.printStackTrace();
        xml.append("<hasNewOrders>false</hasNewOrders>");
        xml.append("<totalOrders>0</totalOrders>");
        xml.append("<error>").append(escapeXml(e.getMessage())).append("</error>");
    }
    
    xml.append("</response>");
    
    // Write response
    PrintWriter writer = response.getWriter();
    writer.write(xml.toString());
    writer.flush();
    writer.close();
}

// Helper method to escape XML special characters
private String escapeXml(String input) {
    if (input == null) return "";
    return input.replace("&", "&amp;")
               .replace("<", "&lt;")
               .replace(">", "&gt;")
               .replace("\"", "&quot;")
               .replace("'", "&apos;");
}
}

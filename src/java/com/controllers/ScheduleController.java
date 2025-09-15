package com.controllers;

import com.models.Accounts;
import com.models.AccountsDAO;
import com.models.DayOfWeek;
import com.models.DayOfWeekDAO;
import com.models.SalaryDAO;
import com.models.Shift;
import com.models.ShiftDAO;
import com.models.WeekDetails;
import com.models.WeekDetailsDAO;
import com.models.WeekSchedule;
import com.models.WeekScheduleDAO;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utils.RoleUtils;

@Controller
@RequestMapping("/admin")
public class ScheduleController {

    @Autowired
    private WeekScheduleDAO weekScheduleDAO;

    @Autowired
    private WeekDetailsDAO weekDetailsDAO;

    @Autowired
    private DayOfWeekDAO dayOfWeekDAO;

    @Autowired
    private ShiftDAO shiftDAO;

    @Autowired
    private AccountsDAO accountsDAO;

    @Autowired
    private SalaryDAO salaryDAO;

    // Helper method to load week schedules data
    private void loadWeekSchedulesData(Model model, HttpServletRequest request) {
        int page = (request.getParameter("page") != null) ? Integer.parseInt(request.getParameter("page")) : 1;
        if (page < 1) page = 1;
        int pageSize = 10;

        int totalRecords = weekScheduleDAO.getTotalWeekSchedules();
        int totalPages = (int) Math.ceil((double) totalRecords / pageSize);
        if (totalRecords == 0) {
            page = 1;
            totalPages = 1;
        }

        List<WeekSchedule> weekSchedules = weekScheduleDAO.getPagedWeekSchedules(page, pageSize);

        model.addAttribute("weekSchedules", weekSchedules);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
    }

    // Display list of week schedules with pagination
    @RequestMapping(value = "/schedule", method = RequestMethod.GET)
    public String getWeekScheduleList(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        loadWeekSchedulesData(model, request);
        return "admin/schedule";
    }

    // Display details of a specific week schedule
    @RequestMapping(value = "/scheduleDetails.htm", method = RequestMethod.GET)
    public String getWeekScheduleDetails(@RequestParam("weekScheduleID") int weekScheduleID,
                                         Model model,
                                         HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, null, 1);
        if (redirect != null) return redirect;

        WeekSchedule weekSchedule = weekScheduleDAO.findByID(weekScheduleID);
        List<WeekDetails> weekDetailsList = weekDetailsDAO.findByWeekScheduleID(weekScheduleID);
        List<DayOfWeek> dayOfWeekList = dayOfWeekDAO.findAll();
        List<Shift> shiftList = shiftDAO.findAll();
        List<Accounts> accountsList = accountsDAO.findAll();

        List<Accounts> filteredAccountsList = accountsList.stream()
                .filter(account -> {
                    Integer role = account.getRole();
                    return role != null && (role == 2 || role == 3);
                })
                .collect(Collectors.toList());

        Map<Integer, String> employeeMap = filteredAccountsList.stream()
                .collect(Collectors.toMap(Accounts::getAccountID, Accounts::getFullName));

        Map<Integer, String> roleMap = filteredAccountsList.stream()
                .collect(Collectors.toMap(
                        Accounts::getAccountID,
                        account -> {
                            Integer role = account.getRole();
                            if (role != null) {
                                switch (role) {
                                    case 2:
                                        return "Employee";
                                    case 3:
                                        return "Shipper";
                                    default:
                                        return "Unknown Role";
                                }
                            }
                            return "Unknown Role";
                        }
                ));

        Map<Integer, Shift> shiftMap = shiftList.stream()
                .collect(Collectors.toMap(Shift::getShiftID, shift -> shift));

        Map<String, Map<String, List<WeekDetails>>> scheduleMap = new LinkedHashMap<>();
        if (weekDetailsList != null && !weekDetailsList.isEmpty()) {
            Map<Integer, String> dayNameMap = dayOfWeekList.stream()
                    .collect(Collectors.toMap(DayOfWeek::getDayID, DayOfWeek::getDayName));
            Map<Integer, String> shiftNameMap = shiftList.stream()
                    .collect(Collectors.toMap(Shift::getShiftID, Shift::getShiftName));

            scheduleMap = weekDetailsList.stream()
                    .collect(Collectors.groupingBy(
                            wd -> dayNameMap.getOrDefault(wd.getDayID(), "Unknown Day"),
                            Collectors.groupingBy(wd -> shiftNameMap.getOrDefault(wd.getShiftID(), "Unknown Shift"))
                    ));
        }

        List<String> daysOfWeek = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        Map<String, Map<String, List<WeekDetails>>> sortedScheduleMap = new LinkedHashMap<>();
        for (String day : daysOfWeek) {
            if (scheduleMap.containsKey(day)) {
                sortedScheduleMap.put(day, scheduleMap.get(day));
            }
        }

        model.addAttribute("weekSchedule", weekSchedule);
        model.addAttribute("weekDetailsGroupedByDayAndShift", sortedScheduleMap);
        model.addAttribute("employeeMap", employeeMap);
        model.addAttribute("roleMap", roleMap);
        model.addAttribute("shiftMap", shiftMap);
        model.addAttribute("dayOfWeekList", dayOfWeekList);
        model.addAttribute("shiftList", shiftList);
        model.addAttribute("accountsList", filteredAccountsList);
        model.addAttribute("hasData", !weekDetailsList.isEmpty());

        return "admin/scheduleDetails";
    }

    // Create a new week schedule
    @RequestMapping(value = "/weekScheduleCreate", method = RequestMethod.POST)
    public String createWeekSchedule(@RequestParam("weekStartDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate weekStartDate,
                                     @RequestParam("weekEndDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate weekEndDate,
                                     HttpServletRequest request,
                                     Model model) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, null, 1);
        if (redirect != null) return redirect;

        if (weekStartDate.isAfter(weekEndDate)) {
            model.addAttribute("error", "The start date cannot be after the end date.");
            loadWeekSchedulesData(model, request);
            return "admin/schedule";
        }

        long daysBetween = ChronoUnit.DAYS.between(weekStartDate, weekEndDate) + 1;
        if (daysBetween != 7) {
            model.addAttribute("error", "A work week must have 7 days.");
            loadWeekSchedulesData(model, request);
            return "admin/schedule";
        }

        if (weekScheduleDAO.isWeekScheduleExists(Date.valueOf(weekStartDate), Date.valueOf(weekEndDate))) {
            model.addAttribute("error", "The work schedule has existed for this period of time.");
            loadWeekSchedulesData(model, request);
            return "admin/schedule";
        }

        WeekSchedule weekSchedule = new WeekSchedule();
        weekSchedule.setWeekStartDate(Date.valueOf(weekStartDate));
        weekSchedule.setWeekEndDate(Date.valueOf(weekEndDate));
        weekScheduleDAO.save(weekSchedule);

        return "redirect:/admin/schedule.htm";
    }

    // Delete a week schedule
    @RequestMapping(value = "/weekScheduleDelete", method = RequestMethod.GET)
    public String deleteWeekSchedule(@RequestParam("weekScheduleID") int weekScheduleID,
                                     RedirectAttributes redirectAttributes) {
        WeekSchedule weekSchedule = weekScheduleDAO.findByID(weekScheduleID);
        if (weekSchedule == null) {
            redirectAttributes.addFlashAttribute("error", "Week schedule not found.");
            return "redirect:/admin/schedule.htm";
        }

        List<WeekDetails> assignments = weekDetailsDAO.findByWeekScheduleID(weekScheduleID);
        if (assignments != null && !assignments.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Cannot delete this schedule as it has assignments.");
            return "redirect:/admin/schedule.htm";
        }

        weekScheduleDAO.delete(weekScheduleID);
        redirectAttributes.addFlashAttribute("success", "Week schedule deleted successfully.");
        return "redirect:/admin/schedule.htm";
    }

    // Assign a shift to an employee for a specific day
    @RequestMapping(value = "/assignShift", method = RequestMethod.POST)
    public String assignShift(@RequestParam("weekScheduleID") int weekScheduleID,
                              @RequestParam("employeeID") int employeeID,
                              @RequestParam("dayID") int dayID,
                              @RequestParam("shiftID") int shiftID,
                              RedirectAttributes redirectAttributes) {
        WeekSchedule weekSchedule = weekScheduleDAO.findByID(weekScheduleID);
        if (weekSchedule == null) {
            redirectAttributes.addFlashAttribute("error", "Week schedule not found.");
            return "redirect:/admin/scheduleDetails.htm?weekScheduleID=" + weekScheduleID;
        }

        Accounts account = accountsDAO.findById(employeeID);
        if (account == null) {
            redirectAttributes.addFlashAttribute("error", "Employee not found.");
            return "redirect:/admin/scheduleDetails.htm?weekScheduleID=" + weekScheduleID;
        }

        DayOfWeek day = dayOfWeekDAO.findById(dayID);
        Shift shift = shiftDAO.findById(shiftID);
        if (day == null || shift == null) {
            redirectAttributes.addFlashAttribute("error", "Invalid day or shift.");
            return "redirect:/admin/scheduleDetails.htm?weekScheduleID=" + weekScheduleID;
        }

        WeekDetails existingWeekDetails = weekDetailsDAO.findByScheduleAndDayAndShift(weekScheduleID, dayID, shiftID, employeeID);
        WeekDetails weekDetails = (existingWeekDetails == null) ? new WeekDetails() : existingWeekDetails;
        weekDetails.setWeekScheduleID(weekScheduleID);
        weekDetails.setEmployeeID(employeeID);
        weekDetails.setDayID(dayID);
        weekDetails.setShiftID(shiftID);

        weekDetailsDAO.save(weekDetails);

        redirectAttributes.addFlashAttribute("success", "Shift assigned and salary updated successfully.");
        return "redirect:/admin/scheduleDetails.htm?weekScheduleID=" + weekScheduleID;
    }

    // Show form to edit a week detail
    @RequestMapping(value = "/editWeekDetail.htm", method = RequestMethod.GET)
    public String showEditWeekDetailForm(@RequestParam("weekDetailID") int weekDetailID,
                                         Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, null, 1);
        if (redirect != null) return redirect;

        WeekDetails weekDetail = weekDetailsDAO.findByID(weekDetailID);
        if (weekDetail == null) {
            model.addAttribute("error", "Week detail not found.");
            return "redirect:/admin/schedule.htm";
        }

        List<Accounts> accountsList = accountsDAO.findAll();
        List<DayOfWeek> dayOfWeekList = dayOfWeekDAO.findAll();
        List<Shift> shiftList = shiftDAO.findAll();

        model.addAttribute("weekDetail", weekDetail);
        model.addAttribute("accountsList", accountsList);
        model.addAttribute("dayOfWeekList", dayOfWeekList);
        model.addAttribute("shiftList", shiftList);

        return "admin/scheduleDetailsUpdate";
    }

    // Update a week detail
    @RequestMapping(value = "/editWeekDetail", method = RequestMethod.POST)
    public String editWeekDetail(@ModelAttribute("weekDetail") WeekDetails weekDetail,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (weekDetail.getEmployeeID() == 0) {
            redirectAttributes.addFlashAttribute("error", "Please select an employee.");
            return "redirect:/admin/editWeekDetail.htm?weekDetailID=" + weekDetail.getWeekDetailID();
        }
        if (weekDetail.getDayID() == 0) {
            redirectAttributes.addFlashAttribute("error", "Please select a day.");
            return "redirect:/admin/editWeekDetail.htm?weekDetailID=" + weekDetail.getWeekDetailID();
        }
        if (weekDetail.getShiftID() == 0) {
            redirectAttributes.addFlashAttribute("error", "Please select a shift.");
            return "redirect:/admin/editWeekDetail.htm?weekDetailID=" + weekDetail.getWeekDetailID();
        }

        WeekDetails existingDetail = weekDetailsDAO.findByID(weekDetail.getWeekDetailID());
        if (existingDetail == null) {
            redirectAttributes.addFlashAttribute("error", "Week detail not found.");
            return "redirect:/admin/scheduleDetails.htm?weekScheduleID=" + weekDetail.getWeekScheduleID();
        }

        List<WeekDetails> overlappingDetails = weekDetailsDAO.findByShiftAndDay(weekDetail.getShiftID(), weekDetail.getDayID());
        for (WeekDetails detail : overlappingDetails) {
            if (detail.getEmployeeID() != existingDetail.getEmployeeID() && detail.getEmployeeID() == weekDetail.getEmployeeID()) {
                redirectAttributes.addFlashAttribute("error", "This shift is already taken by another employee.");
                return "redirect:/admin/editWeekDetail.htm?weekDetailID=" + weekDetail.getWeekDetailID();
            }
        }

        existingDetail.setEmployeeID(weekDetail.getEmployeeID());
        existingDetail.setDayID(weekDetail.getDayID());
        existingDetail.setShiftID(weekDetail.getShiftID());
        existingDetail.setOvertimeHours(weekDetail.getOvertimeHours());

        weekDetailsDAO.update(existingDetail);

        redirectAttributes.addFlashAttribute("message", "Week detail and salary updated successfully.");
        return "redirect:/admin/scheduleDetails.htm?weekScheduleID=" + weekDetail.getWeekScheduleID();
    }

    // Delete a shift and associated week detail
    @RequestMapping(value = "/deleteShift", method = RequestMethod.POST)
    public String deleteShift(@RequestParam(value = "shiftID") int shiftID,
                              @RequestParam(value = "weekScheduleID") int weekScheduleID,
                              @RequestParam(value = "weekDetailID") int weekDetailID,
                              RedirectAttributes redirectAttributes) {
        Shift existingShift = shiftDAO.findById(shiftID);
        if (existingShift == null) {
            redirectAttributes.addFlashAttribute("error", "Shift not found.");
        } else {
            try {
                weekDetailsDAO.delete(weekDetailID);
                shiftDAO.delete(shiftID);
                redirectAttributes.addFlashAttribute("message", "Shift and associated details deleted successfully.");
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Failed to delete shift: " + e.getMessage());
            }
        }
        return "redirect:/admin/scheduleDetails.htm?weekScheduleID=" + weekScheduleID;
    }

    // Export a week schedule to CSV
    @RequestMapping(value = "/exportScheduleCSV", method = RequestMethod.GET)
    public void exportScheduleToCSV(@RequestParam("weekScheduleID") int weekScheduleID,
                                    HttpServletResponse response,
                                    HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, null, 1);
        if (redirect != null) {
            response.sendRedirect("/login.htm");
            return;
        }

        WeekSchedule weekSchedule = weekScheduleDAO.findByID(weekScheduleID);
        if (weekSchedule == null) {
            response.sendError(HttpStatus.NOT_FOUND.value(), "Week schedule not found");
            return;
        }

        List<WeekDetails> weekDetailsList = weekDetailsDAO.findByWeekScheduleID(weekScheduleID);
        List<DayOfWeek> dayOfWeekList = dayOfWeekDAO.findAll();
        List<Shift> shiftList = shiftDAO.findAll();
        List<Accounts> accountsList = accountsDAO.findAll();

        List<Accounts> filteredAccountsList = accountsList.stream()
                .filter(account -> {
                    Integer role = account.getRole();
                    return role != null && (role == 2 || role == 3);
                })
                .collect(Collectors.toList());

        Map<Integer, String> employeeMap = filteredAccountsList.stream()
                .collect(Collectors.toMap(Accounts::getAccountID, Accounts::getFullName));

        Map<Integer, String> roleMap = filteredAccountsList.stream()
                .collect(Collectors.toMap(
                        Accounts::getAccountID,
                        account -> {
                            Integer role = account.getRole();
                            if (role != null) {
                                switch (role) {
                                    case 2: return "Employee";
                                    case 3: return "Shipper";
                                    default: return "Unknown Role";
                                }
                            }
                            return "Unknown Role";
                        }
                ));

        Map<Integer, String> dayNameMap = dayOfWeekList.stream()
                .collect(Collectors.toMap(DayOfWeek::getDayID, DayOfWeek::getDayName));

        Map<Integer, Shift> shiftMap = shiftList.stream()
                .collect(Collectors.toMap(Shift::getShiftID, shift -> shift));

        response.setContentType("text/csv; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String filename = String.format("Weekly_Schedule_%s_to_%s.csv",
                weekSchedule.getWeekStartDate().toString(),
                weekSchedule.getWeekEndDate().toString());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        PrintWriter writer = response.getWriter();
        writer.write('\uFEFF');

        writer.println("===============================================");
        writer.println("        WEEKLY WORK SCHEDULE REPORT");
        writer.println("===============================================");
        writer.println("Period: " + weekSchedule.getWeekStartDate() + " to " + weekSchedule.getWeekEndDate());
        writer.println("Generated: " + LocalDate.now());
        writer.println("===============================================");
        writer.println();
        writer.println();

        if (weekDetailsList == null || weekDetailsList.isEmpty()) {
            writer.println("*** NO SCHEDULE DATA AVAILABLE ***");
            writer.println();
            writer.println("No assignments have been made for this period.");
            writer.flush();
            writer.close();
            return;
        }

        Map<String, Map<String, List<WeekDetails>>> scheduleMap = weekDetailsList.stream()
                .collect(Collectors.groupingBy(
                        wd -> dayNameMap.getOrDefault(wd.getDayID(), "Unknown Day"),
                        LinkedHashMap::new,
                        Collectors.groupingBy(
                                wd -> {
                                    Shift shift = shiftMap.get(wd.getShiftID());
                                    return shift != null ? shift.getShiftName() : "Unknown Shift";
                                },
                                LinkedHashMap::new,
                                Collectors.toList()
                        )
                ));

        List<String> daysOfWeek = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");

        writer.println("┌─────────────────────────────────────────────────────────┐");
        writer.println("│                    DAILY SCHEDULE                      │");
        writer.println("└─────────────────────────────────────────────────────────┘");
        writer.println();

        for (String day : daysOfWeek) {
            writer.println("▌ " + day.toUpperCase());
            writer.println("├──────────────────────────────────────────────────────");

            if (scheduleMap.containsKey(day)) {
                Map<String, List<WeekDetails>> dayShifts = scheduleMap.get(day);

                for (Map.Entry<String, List<WeekDetails>> shiftEntry : dayShifts.entrySet()) {
                    String shiftName = shiftEntry.getKey();
                    List<WeekDetails> shiftDetails = shiftEntry.getValue();

                    String shiftTime = "";
                    if (!shiftDetails.isEmpty()) {
                        Shift shift = shiftMap.get(shiftDetails.get(0).getShiftID());
                        if (shift != null) {
                            shiftTime = String.format("(%s - %s)",
                                    shift.getStartTime() != null ? shift.getStartTime().toString() : "N/A",
                                    shift.getEndTime() != null ? shift.getEndTime().toString() : "N/A");
                        }
                    }

                    writer.println("│  ◆ " + shiftName + " " + shiftTime);

                    for (WeekDetails detail : shiftDetails) {
                        String employeeName = employeeMap.getOrDefault(detail.getEmployeeID(), "Unknown Employee");
                        String role = roleMap.getOrDefault(detail.getEmployeeID(), "Unknown Role");
                        String status = detail.getStatus() != null ? detail.getStatus() : "Scheduled";
                        double overtime = detail.getOvertimeHours() != null ? detail.getOvertimeHours().doubleValue() : 0.0;

                        writer.printf("│     • %-20s [%-8s] OT: %4.1fh Status: %s%n",
                                employeeName, role, overtime, status);
                    }
                    writer.println("│");
                }
            } else {
                writer.println("│     *** No assignments ***");
                writer.println("│");
            }
            writer.println();
        }

        writer.println();
        writer.println();

        writer.println("┌─────────────────────────────────────────────────────────┐");
        writer.println("│                 EMPLOYEE SUMMARY                       │");
        writer.println("└─────────────────────────────────────────────────────────┘");
        writer.println();

        writer.println("Employee Name,Role,Total Shifts,Overtime Hours,Working Days");
        writer.println("─────────────────────────────────────────────────────────────");

        Map<Integer, List<WeekDetails>> employeeSchedule = weekDetailsList.stream()
                .collect(Collectors.groupingBy(WeekDetails::getEmployeeID));

        for (Map.Entry<Integer, List<WeekDetails>> entry : employeeSchedule.entrySet()) {
            int employeeID = entry.getKey();
            List<WeekDetails> employeeShifts = entry.getValue();

            String employeeName = employeeMap.getOrDefault(employeeID, "Unknown Employee");
            String role = roleMap.getOrDefault(employeeID, "Unknown Role");
            int totalShifts = employeeShifts.size();
            double totalOvertime = employeeShifts.stream()
                    .mapToDouble(wd -> wd.getOvertimeHours() != null ? wd.getOvertimeHours().doubleValue() : 0.0)
                    .sum();

            String workingDays = employeeShifts.stream()
                    .map(wd -> dayNameMap.getOrDefault(wd.getDayID(), "Unknown"))
                    .distinct()
                    .collect(Collectors.joining(" | "));

            writer.printf("%-25s,%-10s,%6d shifts,%8.1f hours,\"%s\"%n",
                    employeeName, role, totalShifts, totalOvertime, workingDays);
        }

        writer.println();
        writer.println();

        writer.println("┌─────────────────────────────────────────────────────────┐");
        writer.println("│                 COVERAGE ANALYSIS                      │");
        writer.println("└─────────────────────────────────────────────────────────┘");
        writer.println();

        writer.println("Day,Total Staff,Shifts Covered,Coverage Status");
        writer.println("─────────────────────────────────────────────────────");

        for (String day : daysOfWeek) {
            if (scheduleMap.containsKey(day)) {
                Map<String, List<WeekDetails>> dayShifts = scheduleMap.get(day);

                Set<Integer> dayEmployees = dayShifts.values().stream()
                        .flatMap(List::stream)
                        .map(WeekDetails::getEmployeeID)
                        .collect(Collectors.toSet());

                int totalStaff = dayEmployees.size();
                int shiftsCount = dayShifts.size();
                String coverageStatus = totalStaff >= 3 ? "Good Coverage" : (totalStaff >= 2 ? "Adequate" : "Low Coverage");

                writer.printf("%-12s,%6d staff,%8d shifts,  %s%n",
                        day, totalStaff, shiftsCount, coverageStatus);
            } else {
                writer.printf("%-12s,%6d staff,%8d shifts,  %s%n",
                        day, 0, 0, "No Coverage");
            }
        }

        writer.println();
        writer.println();

        writer.println("┌─────────────────────────────────────────────────────────┐");
        writer.println("│                   SHIFT DETAILS                        │");
        writer.println("└─────────────────────────────────────────────────────────┘");
        writer.println();

        writer.println("Shift Name,Time Period,Total Assignments,Coverage Days");
        writer.println("─────────────────────────────────────────────────────────");

        Map<String, List<WeekDetails>> shiftSummary = weekDetailsList.stream()
                .collect(Collectors.groupingBy(
                        wd -> {
                            Shift shift = shiftMap.get(wd.getShiftID());
                            return shift != null ? shift.getShiftName() : "Unknown Shift";
                        }
                ));

        for (Map.Entry<String, List<WeekDetails>> entry : shiftSummary.entrySet()) {
            String shiftName = entry.getKey();
            List<WeekDetails> shiftAssignments = entry.getValue();

            String timePeriod = "";
            if (!shiftAssignments.isEmpty()) {
                Shift shift = shiftMap.get(shiftAssignments.get(0).getShiftID());
                if (shift != null) {
                    timePeriod = String.format("%s - %s",
                            shift.getStartTime() != null ? shift.getStartTime().toString() : "N/A",
                            shift.getEndTime() != null ? shift.getEndTime().toString() : "N/A");
                }
            }

            String daysCovered = shiftAssignments.stream()
                    .map(wd -> dayNameMap.getOrDefault(wd.getDayID(), "Unknown"))
                    .distinct()
                    .collect(Collectors.joining(" | "));

            writer.printf("%-15s,%-15s,%10d times,\"%s\"%n",
                    shiftName, timePeriod, shiftAssignments.size(), daysCovered);
        }

        writer.println();
        writer.println();
        writer.println("===============================================");
        writer.println("            END OF SCHEDULE REPORT");
        writer.println("===============================================");

        writer.flush();
        writer.close();
    }

    private String escapeCSV(String value) {
        if (value == null) return "";
        value = value.replaceAll("[\\r\\n]+", " ").trim();
        if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
            value = value.replace("\"", "\"\"");
            return "\"" + value + "\"";
        }
        return value;
    }
}

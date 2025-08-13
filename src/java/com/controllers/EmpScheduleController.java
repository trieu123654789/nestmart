package com.controllers;

import com.models.Accounts;
import com.models.AccountsDAO;
import com.models.DayOfWeek;
import com.models.DayOfWeekDAO;
import com.models.EmpScheduleAndSalaryDAO;
import com.models.Shift;
import com.models.ShiftDAO;
import com.models.WeekDetails;
import com.models.WeekSalaryDTO;
import com.models.WeekSchedule;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utils.RoleUtils;

@Controller
@RequestMapping("/employee")
public class EmpScheduleController {

    @Autowired
    private EmpScheduleAndSalaryDAO empScheduleAndSalaryDAO;

    @Autowired
    private DayOfWeekDAO dayOfWeekDAO;

    @Autowired
    private ShiftDAO shiftDAO;

    @Autowired
    private AccountsDAO accountsDAO;

    // Display employee schedule overview
    @RequestMapping(value = "/scheduleEmp", method = RequestMethod.GET)
    public String getEmployeeSchedule(Model model, HttpServletRequest request, 
                                     RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        Integer employeeID = (Integer) session.getAttribute("accountId");
        try {
            WeekSchedule currentWeek = empScheduleAndSalaryDAO.getCurrentWeekSchedule(employeeID);
            List<WeekSchedule> upcomingSchedules = empScheduleAndSalaryDAO.getUpcomingWeekSchedules(employeeID, 4);
            List<WeekSchedule> recentSchedules = empScheduleAndSalaryDAO.getEmployeeWeekSchedules(employeeID);
            if (recentSchedules.size() > 8) recentSchedules = recentSchedules.subList(0, 8);

            Accounts employee = accountsDAO.findById(employeeID);

            model.addAttribute("employeeID", employeeID);
            model.addAttribute("currentWeek", currentWeek);
            model.addAttribute("upcomingSchedules", upcomingSchedules);
            model.addAttribute("recentSchedules", recentSchedules);
            model.addAttribute("hasCurrentWeek", currentWeek != null);
            model.addAttribute("employeeName", employee.getFullName());
            model.addAttribute("roleName", employee.getRoleName());

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error loading schedule data: " + e.getMessage());
            return "redirect:/login.htm";
        }
        return "/employee/scheduleEmp";
    }

    // Display detailed schedule for a specific week
    @RequestMapping(value = "/schedule", method = RequestMethod.GET)
    public String getWeekScheduleDetails(@RequestParam(value = "weekScheduleID", required = false) Integer weekScheduleID,
                                        Model model, HttpServletRequest request,
                                        RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        Integer employeeID = (Integer) session.getAttribute("accountId");
        if (weekScheduleID == null) {
            redirectAttributes.addFlashAttribute("info", "Please select a week to view schedule details.");
            return "redirect:/employee/scheduleEmp.htm";
        }

        try {
            WeekSchedule weekSchedule = empScheduleAndSalaryDAO.getWeekScheduleById(weekScheduleID);
            if (weekSchedule == null) return "redirect:/employee/scheduleEmp.htm";

            List<WeekDetails> weekDetailsList = empScheduleAndSalaryDAO.getEmployeeWeekDetails(employeeID, weekScheduleID);
            if (weekDetailsList.isEmpty()) return "redirect:/employee/scheduleEmp.htm";

            List<DayOfWeek> dayOfWeekList = dayOfWeekDAO.findAll();
            List<Shift> shiftList = shiftDAO.findAll();
            Accounts employee = accountsDAO.findById(employeeID);

            Map<Integer, String> dayNameMap = dayOfWeekList.stream()
                    .collect(Collectors.toMap(DayOfWeek::getDayID, DayOfWeek::getDayName));
            Map<Integer, Shift> shiftMap = shiftList.stream()
                    .collect(Collectors.toMap(Shift::getShiftID, shift -> shift));

            Map<String, List<WeekDetails>> scheduleByDay = weekDetailsList.stream()
                    .collect(Collectors.groupingBy(
                            wd -> dayNameMap.getOrDefault(wd.getDayID(), "Unknown Day"),
                            LinkedHashMap::new,
                            Collectors.toList()
                    ));

            List<String> daysOfWeek = Arrays.asList("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
            Map<String, List<WeekDetails>> sortedScheduleByDay = new LinkedHashMap<>();
            for (String day : daysOfWeek) {
                if (scheduleByDay.containsKey(day)) {
                    List<WeekDetails> dayShifts = scheduleByDay.get(day);
                    dayShifts.sort((wd1, wd2) -> {
                        Shift shift1 = shiftMap.get(wd1.getShiftID());
                        Shift shift2 = shiftMap.get(wd2.getShiftID());
                        return (shift1 != null && shift2 != null) ? shift1.getStartTime().compareTo(shift2.getStartTime()) : 0;
                    });
                    sortedScheduleByDay.put(day, dayShifts);
                }
            }

            int totalHours = empScheduleAndSalaryDAO.getTotalWorkingHours(employeeID, weekScheduleID);
            BigDecimal totalOvertime = weekDetailsList.stream()
                    .map(wd -> wd.getOvertimeHours() != null ? wd.getOvertimeHours() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal regularHours = BigDecimal.valueOf(totalHours).subtract(totalOvertime);
            int totalShifts = weekDetailsList.size();

            model.addAttribute("employee", employee);
            model.addAttribute("weekSchedule", weekSchedule);
            model.addAttribute("scheduleByDay", sortedScheduleByDay);
            model.addAttribute("shiftMap", shiftMap);
            model.addAttribute("dayNameMap", dayNameMap);
            model.addAttribute("totalHours", totalHours);
            model.addAttribute("regularHours", regularHours);
            model.addAttribute("totalOvertime", totalOvertime);
            model.addAttribute("totalShifts", totalShifts);
            model.addAttribute("weekDetailsList", weekDetailsList);
            model.addAttribute("employeeID", employeeID);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error loading schedule details.");
            return "redirect:/employee/scheduleEmp.htm";
        }

        return "/employee/scheduleEmpDetails";
    }

    // Display employee salary history and current month statistics
    @RequestMapping(value = "/salary", method = RequestMethod.GET)
    public String getEmployeeSalary(Model model, HttpServletRequest request,
                                   RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        Integer employeeID = (Integer) session.getAttribute("accountId");

        try {
            List<WeekSalaryDTO> salaryHistory = empScheduleAndSalaryDAO.getEmployeeSalaryHistory(employeeID);
            if (salaryHistory.size() > 12) salaryHistory = salaryHistory.subList(0, 12);

            LocalDate now = LocalDate.now();
            LocalDate monthStart = now.withDayOfMonth(1);
            LocalDate monthEnd = now.withDayOfMonth(now.lengthOfMonth());

            List<WeekSalaryDTO> currentMonthSalary = empScheduleAndSalaryDAO.getEmployeeSalaryByDateRange(
                    employeeID, Date.valueOf(monthStart), Date.valueOf(monthEnd));

            BigDecimal currentMonthTotal = currentMonthSalary.stream()
                    .map(WeekSalaryDTO::getTotalSalary)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            int currentMonthHours = currentMonthSalary.stream()
                    .mapToInt(WeekSalaryDTO::getTotalHoursWorked)
                    .sum();

            BigDecimal currentMonthOvertime = currentMonthSalary.stream()
                    .map(WeekSalaryDTO::getTotalOvertimeHours)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalEarnings = salaryHistory.stream()
                    .map(WeekSalaryDTO::getTotalSalary)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Accounts employee = accountsDAO.findById(employeeID);

            model.addAttribute("employee", employee);
            model.addAttribute("salaryHistory", salaryHistory);
            model.addAttribute("currentMonthSalary", currentMonthSalary);
            model.addAttribute("currentMonthTotal", currentMonthTotal);
            model.addAttribute("currentMonthHours", currentMonthHours);
            model.addAttribute("currentMonthOvertime", currentMonthOvertime);
            model.addAttribute("totalEarnings", totalEarnings);
            model.addAttribute("currentMonth", now.getMonth().name());
            model.addAttribute("currentYear", now.getYear());
            model.addAttribute("employeeID", employeeID);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error loading salary data.");
            return "redirect:/login.htm";
        }

        return "/employee/salaryEmp";
    }

    // Display detailed salary information for a specific week
    @RequestMapping(value = "/salaryDetailsEmp", method = RequestMethod.GET)
    public String getWeekSalaryDetails(@RequestParam("weekScheduleID") int weekScheduleID,
                                      Model model, HttpServletRequest request,
                                      RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        Integer employeeID = (Integer) session.getAttribute("accountId");

        try {
            WeekSalaryDTO weekSalary = empScheduleAndSalaryDAO.getEmployeeWeekSalary(employeeID, weekScheduleID);
            if (weekSalary == null) return "redirect:/employee/salary.htm";

            List<WeekDetails> weekDetailsList = empScheduleAndSalaryDAO.getEmployeeWeekDetails(employeeID, weekScheduleID);
            List<DayOfWeek> dayOfWeekList = dayOfWeekDAO.findAll();
            List<Shift> shiftList = shiftDAO.findAll();
            Accounts employee = accountsDAO.findById(employeeID);

            Map<Integer, String> dayNameMap = dayOfWeekList.stream()
                    .collect(Collectors.toMap(DayOfWeek::getDayID, DayOfWeek::getDayName));
            Map<Integer, Shift> shiftMap = shiftList.stream()
                    .collect(Collectors.toMap(Shift::getShiftID, shift -> shift));

            int totalHoursWorked = weekSalary.getTotalHoursWorked();
            if (totalHoursWorked <= 0) {
                totalHoursWorked = empScheduleAndSalaryDAO.getTotalWorkingHours(employeeID, weekScheduleID);
            }

            model.addAttribute("employee", employee);
            model.addAttribute("weekSalary", weekSalary);
            model.addAttribute("weekDetailsList", weekDetailsList);
            model.addAttribute("dayNameMap", dayNameMap);
            model.addAttribute("shiftMap", shiftMap);
            model.addAttribute("totalHoursWorked", totalHoursWorked);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error loading salary details.");
            return "redirect:/employee/salary.htm";
        }

        return "/employee/salaryDetailsEmp";
    }

    // Filter salary data by date range
    @RequestMapping(value = "/salaryFilter", method = RequestMethod.GET)
    public String filterSalaryByDate(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                    @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                    Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        Integer employeeID = (Integer) session.getAttribute("accountId");
        if (startDate.isAfter(endDate)) {
            redirectAttributes.addFlashAttribute("error", "Start date cannot be after end date.");
            return "redirect:/employee/salary.htm";
        }

        try {
            List<WeekSalaryDTO> filteredSalary = empScheduleAndSalaryDAO.getEmployeeSalaryByDateRange(
                    employeeID, Date.valueOf(startDate), Date.valueOf(endDate));

            BigDecimal periodTotal = filteredSalary.stream()
                    .map(WeekSalaryDTO::getTotalSalary)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            int periodHours = filteredSalary.stream()
                    .mapToInt(WeekSalaryDTO::getTotalHoursWorked)
                    .sum();

            BigDecimal periodOvertime = filteredSalary.stream()
                    .map(WeekSalaryDTO::getTotalOvertimeHours)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Accounts employee = accountsDAO.findById(employeeID);

            model.addAttribute("employee", employee);
            model.addAttribute("salaryHistory", filteredSalary);
            model.addAttribute("periodTotal", periodTotal);
            model.addAttribute("periodHours", periodHours);
            model.addAttribute("periodOvertime", periodOvertime);
            model.addAttribute("startDate", startDate);
            model.addAttribute("endDate", endDate);
            model.addAttribute("isFiltered", true);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error filtering salary data.");
            return "redirect:/employee/salary.htm";
        }

        return "/employee/salaryEmp";
    }

    // Export salary data to CSV
    @RequestMapping(value = "/exportSalaryCSV", method = RequestMethod.GET)
    public void exportSalaryToCSV(@RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                 @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                 @RequestParam(value = "month", required = false) Integer month,
                                 @RequestParam(value = "year", required = false) Integer year,
                                 HttpServletResponse response,
                                 HttpServletRequest request) throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("email") == null) {
            response.sendRedirect("/login.htm");
            return;
        }

        Integer employeeID = (Integer) session.getAttribute("accountId");
        Integer userRole = (Integer) session.getAttribute("role");
        if (employeeID == null || userRole == null || userRole != 2) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Access denied");
            return;
        }

        try {
            List<WeekSalaryDTO> salaryData;
            String dateRange;

            if (month != null && year != null) {
                YearMonth yearMonth = YearMonth.of(year, month);
                LocalDate monthStart = yearMonth.atDay(1);
                LocalDate monthEnd = yearMonth.atEndOfMonth();

                salaryData = empScheduleAndSalaryDAO.getEmployeeSalaryByDateRange(
                        employeeID, Date.valueOf(monthStart), Date.valueOf(monthEnd));
                dateRange = Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + year;

            } else if (startDate != null && endDate != null) {
                salaryData = empScheduleAndSalaryDAO.getEmployeeSalaryByDateRange(
                        employeeID, Date.valueOf(startDate), Date.valueOf(endDate));
                dateRange = startDate.toString() + " to " + endDate.toString();

            } else {
                salaryData = empScheduleAndSalaryDAO.getEmployeeSalaryHistory(employeeID);
                dateRange = "All Time";
            }

            Accounts employee = accountsDAO.findById(employeeID);
            String employeeName = employee != null ? employee.getFullName() : "Unknown Employee";

            response.setContentType("text/csv; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");

            String filename = String.format("Salary_Report_%s_%s.csv",
                                           employeeName.replaceAll("[^a-zA-Z0-9]", "_"),
                                           LocalDate.now().toString());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

            PrintWriter writer = response.getWriter();
            writer.write('\uFEFF');

            writer.println("===============================================");
            writer.println("             SALARY REPORT");
            writer.println("===============================================");
            writer.println("Employee: " + employeeName);
            writer.println("Period: " + dateRange);
            writer.println("Generated: " + LocalDate.now());
            writer.println("===============================================");
            writer.println();

            if (salaryData.isEmpty()) {
                writer.println("*** NO SALARY DATA AVAILABLE ***");
                writer.flush();
                writer.close();
                return;
            }

            writer.println("Week Period,Total Salary,Hours Worked,Overtime Hours,Overtime Pay,Payment Date");
            writer.println("─────────────────────────────────────────────────────────────────────────────");

            BigDecimal grandTotal = BigDecimal.ZERO;
            int totalHours = 0;
            BigDecimal totalOvertimeHours = BigDecimal.ZERO;
            BigDecimal totalOvertimePay = BigDecimal.ZERO;

            for (WeekSalaryDTO salary : salaryData) {
                String weekPeriod = String.format("%s to %s",
                                                 salary.getWeekStartDate().toString(),
                                                 salary.getWeekEndDate().toString());

                writer.printf("%-25s,$%8.2f,%6d hours,$%6.2f,$%8.2f,%s%n",
                            weekPeriod,
                            salary.getTotalSalary().doubleValue(),
                            salary.getTotalHoursWorked(),
                            salary.getTotalOvertimeHours().doubleValue(),
                            salary.getTotalOvertimeSalary().doubleValue(),
                            salary.getSalaryPaymentDate() != null ? salary.getSalaryPaymentDate().toString() : "Pending");

                grandTotal = grandTotal.add(salary.getTotalSalary());
                totalHours += salary.getTotalHoursWorked();
                totalOvertimeHours = totalOvertimeHours.add(salary.getTotalOvertimeHours());
                totalOvertimePay = totalOvertimePay.add(salary.getTotalOvertimeSalary());
            }

            writer.println("─────────────────────────────────────────────────────────────────────────────");
            writer.printf("TOTALS:                  ,$%8.2f,%6d hours,$%6.2f,$%8.2f%n",
                         grandTotal.doubleValue(),
                         totalHours,
                         totalOvertimeHours.doubleValue(),
                         totalOvertimePay.doubleValue());

            writer.println();
            writer.println("===============================================");
            writer.println("            END OF SALARY REPORT");
            writer.println("===============================================");

            writer.flush();
            writer.close();

        } catch (Exception e) {
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error generating salary report");
        }
    }
}

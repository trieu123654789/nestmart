package com.controllers;

import com.models.EmployeeSalaryDTO;
import com.models.SalaryDAO;
import com.models.WeekDetailsDAO;
import com.models.WeekSalaryDTO;
import com.models.WeekSchedule;
import com.models.WeekScheduleDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utils.RoleUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class SalaryController {

    @Autowired
    private SalaryDAO salaryDAO;

    @Autowired
    private WeekScheduleDAO weekScheduleDAO;

    @Autowired
    private WeekDetailsDAO weekDetailsDAO;

    // Display weekly salary list
    @RequestMapping(value = "/salary", method = RequestMethod.GET)
    public String salaryList(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        try {
            List<WeekSalaryDTO> weekSalaryList = salaryDAO.calculateWeeklySalaries();
            if (weekSalaryList == null) weekSalaryList = new ArrayList<>();
            model.addAttribute("weekSalaryList", weekSalaryList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error occurred while fetching weekly salaries.");
        }

        return "/admin/salary";
    }

    // Display detailed salary info for a specific week
    @RequestMapping(value = "/salaryDetails", method = RequestMethod.GET)
    public String salaryDetails(@RequestParam int weekScheduleID, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        try {
            List<EmployeeSalaryDTO> salaryDetails = salaryDAO.findSalariesWithHoursByWeek(weekScheduleID);
            model.addAttribute("salaryDetails", salaryDetails);

            BigDecimal totalSalary = salaryDetails.stream()
                    .map(EmployeeSalaryDTO::getTotalSalary)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            model.addAttribute("totalSalary", totalSalary);

            int totalHoursWorked = salaryDetails.stream()
                    .mapToInt(EmployeeSalaryDTO::getTotalHours)
                    .sum();
            model.addAttribute("totalHours", totalHoursWorked);

            WeekSchedule weekSchedule = weekScheduleDAO.findByID(weekScheduleID);
            model.addAttribute("weekSchedule", weekSchedule);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error occurred while fetching salary details.");
        }

        return "/admin/salaryDetails";
    }

    // Export detailed salary report as CSV
    @RequestMapping(value = "/exportSalaryCSV", method = RequestMethod.GET)
    public void exportSalaryCSV(@RequestParam int weekScheduleID,
                                HttpServletResponse response,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) throws IOException {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) {
            response.sendRedirect(request.getContextPath() + redirect);
            return;
        }

        try {
            List<EmployeeSalaryDTO> salaryDetails = salaryDAO.findSalariesWithHoursByWeek(weekScheduleID);
            WeekSchedule weekSchedule = weekScheduleDAO.findByID(weekScheduleID);

            BigDecimal totalSalary = salaryDetails.stream()
                    .map(EmployeeSalaryDTO::getTotalSalary)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            int totalHours = salaryDetails.stream()
                    .mapToInt(EmployeeSalaryDTO::getTotalHours)
                    .sum();

            response.setContentType("text/csv; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String fileName = "NestMart_Salary_Report_" + sdf.format(weekSchedule.getWeekStartDate()) +
                    "_to_" + sdf.format(weekSchedule.getWeekEndDate()) + ".csv";
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            PrintWriter writer = response.getWriter();
            writer.print('\ufeff');

            writer.println("Employee ID,Full Name,Hourly Rate ($),Total Hours,Total Salary ($)");
            for (EmployeeSalaryDTO salary : salaryDetails) {
                writer.printf("%d,\"%s\",%.2f,%d,%.2f%n",
                        salary.getAccountID(),
                        salary.getFullName(),
                        salary.getHourlyRate(),
                        salary.getTotalHours(),
                        salary.getTotalSalary()
                );
            }

            writer.flush();
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error occurred while exporting data");
        }
    }

    // Helper to truncate strings
    private String truncateString(String str, int maxLength) {
        if (str == null) return "";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
}

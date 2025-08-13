package com.controllers;

import com.models.OrderDetailsDAO;
import com.models.OrdersDTODAO;
import com.models.OrdersDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import utils.RoleUtils;

@Controller
@RequestMapping("/employee")
public class OrderEMPController {

    @Autowired
    private OrdersDTODAO ordersDTODAO;

    @Autowired
    private OrderDetailsDAO orderDetailsDAO;

    // Display list of orders for employee with pagination and search
    @RequestMapping(value = "order", method = RequestMethod.GET)
    public String showOrders(ModelMap model,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "10") int pageSize,
                             @RequestParam(value = "keyword", required = false) String keyword,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        List<OrdersDTO> listOrder;
        int totalOrders = 0;
        String finalKeyword = (keyword != null) ? keyword.trim() : "";

        if (!finalKeyword.isEmpty()) {
            List<OrdersDTO> allResults = ordersDTODAO.searchByKeyword(finalKeyword);
            totalOrders = allResults.size();
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalOrders);
            listOrder = (fromIndex < toIndex) ? allResults.subList(fromIndex, toIndex) : new ArrayList<>();
        } else {
            totalOrders = ordersDTODAO.getTotalOrders();
            listOrder = ordersDTODAO.findPaginated(page, pageSize);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (OrdersDTO order : listOrder) {
            if (order.getOrderDate() != null) {
                order.setFormattedDate(order.getOrderDate().format(formatter));
            }
        }

        model.addAttribute("listOrder", listOrder);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) totalOrders / pageSize));
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keyword", finalKeyword);
        model.addAttribute("totalOrders", totalOrders);

        return "/employee/order";
    }
}

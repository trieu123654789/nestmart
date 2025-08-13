package com.controllers;

import com.models.Accounts;
import com.models.AccountsDAO;
import com.models.AssignShipper;
import com.models.OrderDetailsDTO;
import com.models.OrderDetailsDTODAO;
import com.models.OrdersDAO;
import com.models.OrdersDTO;
import com.models.OrdersDTODAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import utils.RoleUtils;

@Controller
@RequestMapping(value = "/employee")
public class OrderDetailsController {

    @Autowired
    private OrderDetailsDTODAO orderDetailsDTODAO;

    @Autowired
    private OrdersDTODAO orderDTODAO;

    @Autowired
    private OrdersDAO ordersDAO;

    @Autowired
    private AccountsDAO accountsDAO;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    // Display form to assign a shipper to an order
    @RequestMapping(value = "assignShipper", method = RequestMethod.GET)
    public String showAssignShipperForm(@RequestParam("orderID") String id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        OrdersDTO order = orderDTODAO.findByOrderId(id);
        List<Accounts> shippers = accountsDAO.findShippers();
        List<OrderDetailsDTO> orderDetails = orderDetailsDTODAO.findByOrderId(id);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (order.getOrderDate() != null) {
            order.setFormattedDate(order.getOrderDate().format(formatter));
        }
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("order", order);
        model.addAttribute("shippers", shippers);
        return "employee/assignShipper";
    }

    // Assign a shipper to an order
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public String assignNewShipper(@ModelAttribute("assignShipper") AssignShipper assignShipper,
                                   RedirectAttributes redirectAttributes,
                                   HttpSession session) {

        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        if (assignShipper.getShipperID() == 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "No shippers found to assign");
            return "redirect:/employee/assignShipper.htm?orderID=" + assignShipper.getOrderID();
        }

        ordersDAO.updateShipperAndStatus(assignShipper.getOrderID(), assignShipper.getShipperID());
        redirectAttributes.addFlashAttribute("successMessage", "Shipper assigned successfully!");
        return "redirect:/employee/order.htm";
    }

    // View order details and assigned shipper
    @RequestMapping(value = "viewOrder", method = RequestMethod.GET)
    public String viewAssignShipperForm(@RequestParam("orderID") String id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        List<OrderDetailsDTO> orderDetails = orderDetailsDTODAO.findByOrderId(id);
        OrdersDTO order = orderDTODAO.findByOrderId(id);
        Accounts shipper = accountsDAO.findShipperById(order.getOrderID());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (order.getOrderDate() != null) {
            order.setFormattedDate(order.getOrderDate().format(formatter));
        }
        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("order", order);
        model.addAttribute("shipper", shipper);
        return "employee/viewOrder";
    }
}

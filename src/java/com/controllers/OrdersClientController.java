package com.controllers;

import com.models.OrderDetails;
import com.models.OrderDetailsDAO;
import com.models.Orders;
import com.models.OrdersDAO;
import com.models.ProductsDAO;
import com.models.ReturnRequestResponse;
import com.models.ReturnRequestResponseDAO;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/client")
public class OrdersClientController {

    @Autowired
    OrdersDAO ordersDAO;
    @Autowired
    OrderDetailsDAO orderDetailsDAO;
    @Autowired
    private ProductsDAO productsDAO;
    @Autowired
    ReturnRequestResponseDAO returnRequestResponseDAO;

    // Display the logged-in client's order history with search and filter
    @RequestMapping(value = "/orderHistory")
    public String viewOrders(ModelMap model,
                             @RequestParam(required = false) String status,
                             @RequestParam(required = false) String search,
                             HttpSession session, RedirectAttributes redirectAttributes) {

        Integer customerID = (Integer) session.getAttribute("accountId");
        if (customerID == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to go to this page.");
            return "redirect:/login.htm";
        }

        List<Orders> ordersList;

        if (search != null && !search.trim().isEmpty()) {
            ordersList = ordersDAO.searchOrders(customerID, search.trim(), status);
        } else {
            if (status == null || status.isEmpty()) {
                ordersList = ordersDAO.findAllOrders(customerID);
            } else {
                ordersList = ordersDAO.getOrdersByStatus(status, customerID);
            }
        }

        ordersList.sort((o1, o2) -> {
            if (o1.getOrderDate() == null && o2.getOrderDate() == null) return 0;
            if (o1.getOrderDate() == null) return 1;
            if (o2.getOrderDate() == null) return -1;
            return o2.getOrderDate().compareTo(o1.getOrderDate());
        });

        for (Orders order : ordersList) {
            if ("Return Requested".equals(order.getOrderStatus())
                    || "Approved".equals(order.getOrderStatus())
                    || "Denied".equals(order.getOrderStatus())
                    || "Returning".equals(order.getOrderStatus())
                    || "Returned".equals(order.getOrderStatus())) {
                Orders fullOrder = ordersDAO.get(String.valueOf(order.getOrderID()));
                order.setReturnRequestStatus(fullOrder.getReturnRequestStatus());
                order.setReturnRequestReason(fullOrder.getReturnRequestReason());
                order.setReturnRequestDate(fullOrder.getReturnRequestDate());
            }

            for (OrderDetails orderDetail : order.getOrderDetails()) {
                Integer feedbackID = ordersDAO.getFeedback(order.getOrderID(), orderDetail.getProduct().getProductID(), customerID);
                boolean hasFeedback = feedbackID != null;
                orderDetail.setHasFeedback(hasFeedback);
                orderDetail.setFeedbackID(feedbackID);
            }
        }

        model.addAttribute("ordersList", ordersList);
        model.addAttribute("searchQuery", search);
        model.addAttribute("currentStatus", status);
        model.addAttribute("noOrdersFound", ordersList == null || ordersList.isEmpty());

        return "/client/orderHistory";
    }

    // Display return order form for a specific order
    @RequestMapping(value = "returnorder", method = RequestMethod.GET)
    public String showReturnOrderForm(@RequestParam("orderId") String orderId,
                                      HttpSession session,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {

        Integer customerID = (Integer) session.getAttribute("accountId");
        if (customerID == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to access this page.");
            return "redirect:/login.htm";
        }
        Orders order = ordersDAO.viewOrderInfo(orderId);
        if (order == null) {
            model.addAttribute("error", "Order not found!");
            return "client/error";
        }
        model.addAttribute("order", order);
        return "client/returnorder";
    }

    // Submit a return request for a completed order
    @RequestMapping(value = "returnorder", method = RequestMethod.POST)
    public String returnorder(@RequestParam("orderId") String orderId,
                              @RequestParam("reason") String reason,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {

        Integer customerID = (Integer) session.getAttribute("accountId");
        if (customerID == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to go to this page.");
            return "redirect:/login.htm";
        }

        Orders order = ordersDAO.get(String.valueOf(orderId));
        if (order != null && "Completed".equals(order.getOrderStatus())) {
            order.setOrderStatus("Return Requested");
            order.setReturnRequestDate(LocalDateTime.now());
            order.setReturnRequestStatus("Pending");
            order.setReturnRequestReason(reason);

            ordersDAO.update(order);
            redirectAttributes.addFlashAttribute("successMessage", "Return request submitted successfully! We will get back to you as soon as possible!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to submit return request. Order not found or not eligible for return.");
        }
        return "redirect:/client/orderHistory.htm?status=Return Requested";
    }

    // Update return request status by employee response
    @RequestMapping(value = "updateReturnStatus", method = RequestMethod.POST)
    public String updateReturnStatus(@RequestParam("orderId") String orderId,
                                     @RequestParam("status") String status,
                                     @RequestParam("employeeId") int employeeId,
                                     @RequestParam("message") String message,
                                     RedirectAttributes redirectAttributes) {

        Orders order = ordersDAO.get(String.valueOf(orderId));
        if (order != null && "Return Requested".equals(order.getOrderStatus())) {
            order.setReturnRequestStatus(status);
            if ("On Delivery".equals(status)) {
                order.setOrderStatus("Returning");
            } else if ("Completed".equals(status)) {
                order.setOrderStatus("Returned");
            }
            ordersDAO.update(order);
            redirectAttributes.addFlashAttribute("message", "Return request status updated successfully and response recorded");
        } else {
            redirectAttributes.addFlashAttribute("error", "Failed to update return request status. Order not found or not eligible for update.");
        }
        return "redirect:/client/orderHistory.htm";
    }

    // Cancel an order and restore product quantities
    @PostMapping("/cancelOrder")
    public String cancelOrder(@RequestParam("orderID") String orderID,
                              RedirectAttributes redirectAttributes,
                              HttpSession session) {

        Integer customerID = (Integer) session.getAttribute("accountId");
        if (customerID == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to go to this page.");
            return "redirect:/login.htm";
        }

        Orders order = ordersDAO.get(orderID);
        if (order != null && !"Cancelled".equals(order.getOrderStatus())
                && !"Completed".equals(order.getOrderStatus())) {

            List<OrderDetails> details = orderDetailsDAO.getOrderDetailsByOrderId(orderID);
            if (details != null) {
                for (OrderDetails detail : details) {
                    if (detail.getProduct() != null) {
                        String productId = detail.getProduct().getProductID();
                        int quantityToRestore = detail.getQuantity();
                        if (productId != null && !productId.trim().isEmpty()) {
                            productsDAO.restoreProductQuantity(productId.trim(), quantityToRestore);
                        } else {
                            String alternativeProductId = orderDetailsDAO.getProductIdFromDetail(detail);
                            if (alternativeProductId != null && !alternativeProductId.trim().isEmpty()) {
                                productsDAO.restoreProductQuantity(alternativeProductId.trim(), quantityToRestore);
                            }
                        }
                    }
                }
            }

            order.setOrderStatus("Cancelled");
            ordersDAO.update(order);
            redirectAttributes.addFlashAttribute("cancelMessage", "Order has been cancelled");

        } else {
            redirectAttributes.addFlashAttribute("error", "Order not found or cannot be cancelled.");
        }

        return "redirect:/client/orderHistory.htm?status=Cancelled";
    }

    // View detailed information of a specific order
    @RequestMapping("/orderinfo")
    public String viewOrderInfo(@RequestParam("order") String orderId, Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        Integer customerID = (Integer) session.getAttribute("accountId");
        if (customerID == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to go to this page.");
            return "redirect:/login.htm";
        }

        Orders order = ordersDAO.viewOrderInfo(orderId);
        if (order == null) {
            model.addAttribute("error", "Order not found!");
            return "client/error";
        }

        if ("Approved".equals(order.getOrderStatus()) || "Denied".equals(order.getOrderStatus())) {
            List<ReturnRequestResponse> responses = returnRequestResponseDAO.getReturnResponses(orderId);
            model.addAttribute("responses", responses);
        }

        Map<String, Object> voucherInfo = ordersDAO.getUsedVoucherInfo(orderId);
        model.addAttribute("usedVoucher", voucherInfo);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (order.getOrderDate() != null) {
            order.setFormattedOrderDate(order.getOrderDate().format(formatter));
        }

        model.addAttribute("order", order);
        return "client/orderinfo";
    }
}

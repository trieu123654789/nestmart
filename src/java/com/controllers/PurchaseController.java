package com.controllers;

import com.models.CartItem;
import com.models.ProductsDAO;
import com.models.Voucher;
import com.models.VoucherDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utils.RoleUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/client")
public class PurchaseController {

    @Autowired
    private ProductsDAO productsDAO;

    @Autowired
    private VoucherDAO voucherDAO;

    // Display purchase page and validate cart items
    @GetMapping("/purchase")
    public String showPurchasePage(HttpSession session, 
                                   Model model, 
                                   RedirectAttributes redirectAttributes) {

        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 2);
        if (redirect != null) return redirect;

        String fullName = (String) session.getAttribute("fullName");
        Integer accountId = (Integer) session.getAttribute("accountId");

        Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty.");
            return "redirect:/client/cart.htm";
        }

        Map<String, CartItem> availableCart = new HashMap<>();
        BigDecimal grandTotal = BigDecimal.ZERO;
        StringBuilder warning = new StringBuilder();

        for (CartItem item : cart.values()) {
            int currentStock = productsDAO.getProductQuantity(item.getProductId());
            if (currentStock <= 0) {
                continue;
            }
            if (item.getQuantity() > currentStock) {
                item.setQuantity(currentStock);
            }
            item.setAvailableQuantity(currentStock);
            item.setOutOfStock(false);
            availableCart.put(item.getProductId(), item);
            grandTotal = grandTotal.add(item.getProductPrice()
                               .multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        if (availableCart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", 
                    "No items are currently available for checkout. Please review your cart.");
            return "redirect:/client/cart.htm";
        }

        BigDecimal shippingFee = new BigDecimal("5");
        BigDecimal orderTotal = grandTotal.add(shippingFee);

        List<Voucher> availableVouchers = voucherDAO.getAvailableVouchersForAccount(accountId, grandTotal);
        List<Voucher> unavailableVouchers = voucherDAO.getUnavailableVouchersForAccount(accountId, grandTotal);

        session.setAttribute("grandTotal", grandTotal);
        session.setAttribute("orderTotal", orderTotal);

        model.addAttribute("cart", availableCart);
        model.addAttribute("grandTotal", grandTotal);
        model.addAttribute("orderTotal", orderTotal);
        model.addAttribute("shippingFee", shippingFee);
        model.addAttribute("fullName", fullName);
        model.addAttribute("availableVouchers", availableVouchers);
        model.addAttribute("unavailableVouchers", unavailableVouchers);

        if (warning.length() > 0) {
            model.addAttribute("warningMessage", "Some items in your cart were adjusted:\n" + warning);
        }

        model.addAttribute("phone", session.getAttribute("phoneNumber"));
        model.addAttribute("address", session.getAttribute("address"));

        return "client/purchase";
    }

    // Apply voucher to current order
    @PostMapping("/applyVoucher")
    public void applyVoucher(@RequestParam("voucherCode") String voucherCode,
                             HttpSession session,
                             HttpServletResponse response) throws IOException {

        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        Integer accountId = (Integer) session.getAttribute("accountId");
        BigDecimal grandTotal = (BigDecimal) session.getAttribute("grandTotal");

        out.println("<?xml version='1.0' encoding='UTF-8'?>");
        out.println("<response>");

        if (accountId == null || grandTotal == null) {
            out.println("<success>false</success>");
            out.println("<message>Session expired. Please refresh the page.</message>");
            out.println("</response>");
            return;
        }

        Voucher voucher = voucherDAO.validateAndGetVoucher(accountId, voucherCode, grandTotal);

        if (voucher == null) {
            out.println("<success>false</success>");
            out.println("<message>Invalid voucher code or voucher cannot be used for this order.</message>");
            out.println("</response>");
            return;
        }

        BigDecimal discountAmount = voucher.calculateDiscount(grandTotal);
        BigDecimal shippingFee = new BigDecimal("5");
        BigDecimal newTotal = grandTotal.subtract(discountAmount).add(shippingFee);

        session.setAttribute("appliedVoucherCode", voucher.getCode());
        session.setAttribute("appliedDiscountType", voucher.getDiscountType());
        session.setAttribute("appliedDiscountValue", voucher.getDiscountValue());
        session.setAttribute("appliedMaxDiscount", voucher.getMaxDiscount());
        session.setAttribute("appliedMinOrderValue", voucher.getMinOrderValue());
        session.setAttribute("discountAmount", discountAmount);
        session.setAttribute("finalTotal", newTotal);

        out.println("<success>true</success>");
        out.println("<voucherCode>" + voucher.getCode() + "</voucherCode>");
        out.println("<discountAmount>" + discountAmount.toString() + "</discountAmount>");
        out.println("<newTotal>" + newTotal.toString() + "</newTotal>");
        out.println("<discountType>" + voucher.getDiscountType() + "</discountType>");
        out.println("<discountValue>" + voucher.getDiscountValue().toString() + "</discountValue>");
        out.println("</response>");
    }

    // Remove applied voucher from current order
    @PostMapping("/removeVoucher")
    public void removeVoucher(HttpSession session, HttpServletResponse response) throws IOException {

        response.setContentType("application/xml");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        BigDecimal grandTotal = (BigDecimal) session.getAttribute("grandTotal");
        BigDecimal shippingFee = new BigDecimal("5");
        BigDecimal originalTotal = grandTotal.add(shippingFee);

        session.removeAttribute("appliedVoucherCode");
        session.removeAttribute("appliedDiscountType");
        session.removeAttribute("appliedDiscountValue");
        session.removeAttribute("appliedMaxDiscount");
        session.removeAttribute("appliedMinOrderValue");
        session.removeAttribute("discountAmount");
        session.removeAttribute("finalTotal");

        out.println("<?xml version='1.0' encoding='UTF-8'?>");
        out.println("<response>");
        out.println("<success>true</success>");
        out.println("<originalTotal>" + originalTotal.toString() + "</originalTotal>");
        out.println("</response>");
    }
}

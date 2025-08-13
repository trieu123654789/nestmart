package com.controllers;

import com.models.CartItem;
import com.models.OrderDetailsDAO;
import com.models.Orders;
import com.models.OrdersDAO;
import com.models.ProductsDAO;
import com.models.Voucher;
import com.models.VoucherDAO;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/client")
@SessionAttributes("cart")
public class CartController {

    @Autowired
    private OrdersDAO ordersDAO;
    @Autowired
    private OrderDetailsDAO orderDetailsDAO;
    @Autowired
    private ProductsDAO productsDAO;
    @Autowired
    private VoucherDAO voucherDAO;

    // Initialize or get cart from session
    @ModelAttribute("cart")
    public Map<String, CartItem> getCart(HttpSession session) {
        Map<String, CartItem> cart = (Map<String, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    // Get total number of items in cart
    @ModelAttribute("cartTotal")
    public int getCartTotal(@ModelAttribute("cart") Map<String, CartItem> cart) {
        return cart.size();
    }

    // Display the shopping cart page with updated stock info
    @GetMapping("/cart")
    public String viewCart(@ModelAttribute("cart") Map<String, CartItem> cart, Model model) {
        BigDecimal grandTotal = BigDecimal.ZERO;

        for (CartItem item : cart.values()) {
            int currentQuantity = productsDAO.getProductQuantity(item.getProductId());
            item.setAvailableQuantity(currentQuantity);

            if (currentQuantity <= 0) {
                item.setOutOfStock(true);
            } else {
                item.setOutOfStock(false);
                if (item.getQuantity() > currentQuantity) {
                    item.setQuantity(currentQuantity);
                }
                BigDecimal itemTotal = item.getProductPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity()));
                grandTotal = grandTotal.add(itemTotal);
            }
        }

        model.addAttribute("cart", cart);
        model.addAttribute("grandTotal", grandTotal);
        return "/client/cart";
    }

    // Update cart quantities and proceed to checkout
    @PostMapping("/updateCartAndProceedToCheckout")
    public String updateCartAndProceedToCheckout(
            @RequestParam(value = "productIds", required = false) List<String> productIds,
            @RequestParam(value = "quantities", required = false) List<Integer> quantities,
            @ModelAttribute("cart") Map<String, CartItem> cart,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (productIds == null || quantities == null || productIds.size() != quantities.size()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid cart data");
            return "redirect:/client/cart.htm";
        }

        Map<String, CartItem> updatedCart = new HashMap<>();
        BigDecimal grandTotal = BigDecimal.ZERO;
        boolean hasStockIssue = false;
        StringBuilder errorMessage = new StringBuilder();

        for (int i = 0; i < productIds.size(); i++) {
            String productId = productIds.get(i);
            int requestedQuantity = quantities.get(i);

            CartItem existingItem = cart.get(productId);
            if (existingItem != null) {
                int currentStock = productsDAO.getProductQuantity(productId);

                if (currentStock <= 0) {
                    continue;
                }

                if (requestedQuantity > currentStock) {
                    hasStockIssue = true;
                    errorMessage.append("• ")
                            .append(existingItem.getProductName())
                            .append(": Only ")
                            .append(currentStock)
                            .append(" available, requested ")
                            .append(requestedQuantity)
                            .append("\n");

                    requestedQuantity = currentStock;
                }

                CartItem updatedItem = new CartItem(
                        existingItem.getProductId(),
                        existingItem.getProductName(),
                        existingItem.getProductPrice(),
                        existingItem.getProductImage(),
                        requestedQuantity,
                        currentStock,
                        currentStock <= 0
                );

                updatedCart.put(productId, updatedItem);

                BigDecimal itemTotal = existingItem.getProductPrice().multiply(BigDecimal.valueOf(requestedQuantity));
                grandTotal = grandTotal.add(itemTotal);
            }
        }

        if (updatedCart.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "No items available for checkout");
            return "redirect:/client/cart.htm";
        }

        session.setAttribute("cart", updatedCart);
        session.setAttribute("grandTotal", grandTotal);

        if (hasStockIssue) {
            errorMessage.insert(0, "Some quantities were adjusted due to stock availability:\n");
            errorMessage.append("\nProceeding with available quantities.");
            redirectAttributes.addFlashAttribute("warningMessage", errorMessage.toString());
        }

        return "redirect:/client/purchase.htm";
    }

    // Check stock availability before checkout
    @PostMapping("/checkStockBeforeCheckout")
    public String checkStockBeforeCheckout(
            @RequestParam(value = "productIds", required = false) List<String> productIds,
            @RequestParam(value = "quantities", required = false) List<Integer> quantities,
            @SessionAttribute(value = "cart", required = false) Map<String, CartItem> cart,
            RedirectAttributes redirectAttributes) {

        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your cart is empty.");
            return "redirect:/client/cart.htm";
        }

        if (productIds != null && quantities != null) {
            for (int i = 0; i < productIds.size(); i++) {
                String productId = productIds.get(i);
                int newQuantity = quantities.get(i);

                if (cart.containsKey(productId)) {
                    CartItem item = cart.get(productId);
                    item.setQuantity(newQuantity);
                }
            }
        }

        boolean allSoldOut = cart.values().stream()
                .allMatch(item -> item.isOutOfStock() || item.getQuantity() <= 0);

        if (allSoldOut) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Your cart only contains sold-out products. Please add available products to checkout.");
            return "redirect:/client/cart.htm";
        }

        boolean hasStockIssue = false;
        StringBuilder errorMessage = new StringBuilder();
        errorMessage.append("Stock availability has changed:\n");

        for (int i = 0; i < productIds.size(); i++) {
            String productId = productIds.get(i);
            int requestedQty = quantities.get(i);
            int currentStock = productsDAO.getProductQuantity(productId);

            if (requestedQty > currentStock) {
                hasStockIssue = true;
                CartItem cartItem = cart.get(productId);

                if (cartItem != null) {
                    cartItem.setAvailableQuantity(currentStock);
                    if (currentStock <= 0) {
                        cartItem.setOutOfStock(true);
                    } else {
                        cartItem.setQuantity(Math.min(requestedQty, currentStock));
                    }
                }
            }
        }

        if (hasStockIssue) {
            errorMessage.append("\nPlease review your cart and try again.");
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage.toString());
            return "redirect:/client/cart.htm";
        }

        return "redirect:/client/purchase.htm";
    }

    // Update quantity of a single cart item
    @PostMapping("/updateCart")
    public String updateCart(
            @RequestParam("productId") String productId,
            @RequestParam("quantity") int quantity,
            @ModelAttribute("cart") Map<String, CartItem> cart) {

        CartItem item = cart.get(productId);
        if (item != null) {
            if (quantity <= 0) {
                cart.remove(productId);
            } else {
                item.setQuantity(quantity);
            }
        }
        return "redirect:/client/cart.htm";
    }

    // Remove a product from the cart
    @PostMapping("/removeFromCart")
    public String removeFromCart(
            @RequestParam("productId") String productId,
            @ModelAttribute("cart") Map<String, CartItem> cart) {

        cart.remove(productId);
        return "redirect:/client/cart.htm";
    }

    // Add a product to the cart
    @PostMapping("/addToCart")
    public String addToCart(
            @RequestParam("productId") String productId,
            @RequestParam("productName") String productName,
            @RequestParam("productPrice") BigDecimal productPrice,
            @RequestParam("productImage") String productImage,
            @RequestParam("quantity") int quantity,
            @ModelAttribute("cart") Map<String, CartItem> cart,
            RedirectAttributes redirectAttributes) {

        int availableQuantity = productsDAO.getProductQuantity(productId);

        CartItem item = cart.get(productId);
        if (item == null) {
            boolean outOfStock = availableQuantity <= 0;
            item = new CartItem(productId, productName, productPrice, productImage,
                    quantity, availableQuantity, outOfStock);
            cart.put(productId, item);
        } else {
            int newQuantity = item.getQuantity() + quantity;

            if (newQuantity > availableQuantity) {
                redirectAttributes.addFlashAttribute("errorMessage", "Not enough stock available!");
                return "redirect:/client/productDetails.htm?productID=" + productId;
            }

            item.setQuantity(newQuantity);
        }

        redirectAttributes.addFlashAttribute("cart", cart);
        redirectAttributes.addFlashAttribute("successMessage", "Product added to cart successfully!");
        return "redirect:/client/productDetails.htm?productID=" + productId;
    }

    // Create order and process payment
    @PostMapping("createOrder")
    public String createOrder(
            @ModelAttribute("order") Orders order,
            @RequestParam("productId") List<String> productIds,
            @RequestParam("quantity") List<Integer> quantities,
            @RequestParam("unitPrice") List<BigDecimal> unitPrices,
            @RequestParam("totalAmount") BigDecimal totalAmount,
            @RequestParam("note") String note,
            @RequestParam("orderId") String orderId,
            @RequestParam(value = "appliedVoucherCode", required = false) String appliedVoucherCode,
            @RequestParam(value = "discountAmount", defaultValue = "0") BigDecimal discountAmount,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            SessionStatus sessionStatus) {

        for (int i = 0; i < productIds.size(); i++) {
            int availableStock = productsDAO.getProductQuantity(productIds.get(i));
            String productName = productsDAO.getProductNameById(productIds.get(i));
            if (availableStock <= 0) {
                redirectAttributes.addFlashAttribute("errorMessage", "Product " + productName + " is sold out.");
                return "redirect:/client/purchase.htm";
            }
            if (quantities.get(i) > availableStock) {
                redirectAttributes.addFlashAttribute("errorMessage", "Product " + productName + " only has " + availableStock + " left.");
                return "redirect:/client/purchase.htm";
            }
        }

        Integer accountId = (Integer) session.getAttribute("accountId");
        Voucher appliedVoucher = null;

        if (appliedVoucherCode != null && !appliedVoucherCode.trim().isEmpty() && accountId != null) {
            appliedVoucher = voucherDAO.validateAndGetVoucher(accountId, appliedVoucherCode, totalAmount);

            if (appliedVoucher == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "The voucher is invalid, expired, or does not meet the requirements.");
                return "redirect:/client/purchase.htm";
            }
        }

        order.setOrderID(orderId);
        order.setNotes(note);
        ordersDAO.saveOrder(
                order.getCustomerID(),
                order.getShippingAddress(),
                order.getNotes(),
                order.getPaymentMethod(),
                order.getName(),
                order.getPhone(),
                totalAmount,
                order.getOrderID());

        for (int i = 0; i < productIds.size(); i++) {
            BigDecimal totalPrice = unitPrices.get(i).multiply(BigDecimal.valueOf(quantities.get(i)));
            orderDetailsDAO.saveOrderDetail(
                    orderId,
                    productIds.get(i),
                    quantities.get(i),
                    unitPrices.get(i),
                    totalPrice);
            productsDAO.updateProductQuantity(productIds.get(i), quantities.get(i));
        }

        if (appliedVoucher != null) {
            voucherDAO.markVoucherAsUsed(accountId, appliedVoucher.getVoucherID(), orderId);
            voucherDAO.incrementVoucherUsedCount(appliedVoucherCode);

            session.removeAttribute("appliedVoucherCode");
            session.removeAttribute("appliedDiscountType");
            session.removeAttribute("appliedDiscountValue");
            session.removeAttribute("appliedMaxDiscount");
            session.removeAttribute("appliedMinOrderValue");
            session.removeAttribute("discountAmount");
            session.removeAttribute("finalTotal");
        }

        session.removeAttribute("cart");
        sessionStatus.setComplete();

        redirectAttributes.addFlashAttribute("successMessage", "Order successfully!");
        return "redirect:/client/product.htm";
    }
}

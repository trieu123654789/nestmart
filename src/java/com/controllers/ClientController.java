package com.controllers;

import com.models.DiscountClient;
import com.models.DiscountClientDAO;
import com.models.ProductsClient;
import com.models.ProductsClientDAO;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ProductsClientDAO productsClientDAO;

    @Autowired
    private DiscountClientDAO discountClientDAO;

    // Display client homepage with products and discounts
    @GetMapping("/clientboard")
    public String showCustomerIndex(HttpSession session, Model model) {
        List<ProductsClient> listProducts = productsClientDAO.select10random();
        model.addAttribute("listProducts", listProducts);

        DiscountClient newestDiscount = discountClientDAO.getNewestDiscountSingle();

        if (newestDiscount != null) {
            List<ProductsClient> listProducts1 = discountClientDAO.findRandom10ProductsByDiscount(newestDiscount.getDiscountID());
            model.addAttribute("discount", newestDiscount);
            model.addAttribute("listProducts1", listProducts1);
        } else {
            model.addAttribute("discount", null);
            model.addAttribute("listProducts1", new ArrayList<ProductsClient>());
        }

        return "/client/clientboard";
    }

    // Display refund policy page
    @GetMapping("/refundpolicy")
    public String refundpolicy() {
        return "client/refundpolicy";
    }

    // Display shipping policy page
    @GetMapping("/shippingpolicy")
    public String shippingpolicy() {
        return "client/shippingpolicy";
    }

}

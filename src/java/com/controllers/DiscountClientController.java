package com.controllers;

import com.models.DiscountClient;
import com.models.DiscountClientDAO;
import com.models.ProductsClient;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/client")
public class DiscountClientController {

    @Autowired
    private DiscountClientDAO discountClientDAO;

    // Display all discounts with their associated products
    @RequestMapping("/discount")
    public String listDiscounts(Model model) {
        List<DiscountClient> listDiscounts = discountClientDAO.getAllDiscountsWithProducts();
        model.addAttribute("listDiscounts", listDiscounts);
        return "/client/discount";
    }

    // Display paginated products under a specific discount
    @RequestMapping(value = "/discountinfo", method = RequestMethod.GET)
    public String showDiscountProducts(
            @RequestParam("discountID") int discountID,
            @RequestParam(value = "page", defaultValue = "1") int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            ModelMap model) {

        int totalProducts = discountClientDAO.countProductsByDiscount(discountID);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        List<ProductsClient> listProducts = discountClientDAO.findProductsByDiscount(discountID, currentPage, pageSize);

        model.addAttribute("listProducts", listProducts);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("discountID", discountID);

        return "client/discountinfo";
    }
}

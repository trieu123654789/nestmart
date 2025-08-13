package com.controllers;

import com.models.DiscountDAO;
import com.models.Offers;
import com.models.OffersDAO;
import com.models.ProductsDAO;
import com.models.ProductsClientDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import utils.RoleUtils;

@Controller
@RequestMapping(value = "/admin")
public class OffersController {

    @Autowired
    private OffersDAO offersDAO;

    @Autowired
    private ProductsClientDAO productsClientDAO;

    @Autowired
    private ProductsDAO productsDAO;

    @Autowired
    private DiscountDAO discountDAO;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    // Display all offers with pagination
    @RequestMapping(value = "offers", method = RequestMethod.GET)
    public String showOffers(ModelMap model,
                             @RequestParam(defaultValue = "1") int page,
                             @RequestParam(defaultValue = "8") int pageSize,
                             @RequestParam(value = "keyword", required = false) String keyword,
                             HttpServletRequest request,
                             RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        List<Offers> listOffers = new ArrayList<>();
        int totalOffers;
        String finalKeyword = (keyword != null) ? keyword.trim() : null;

        if (finalKeyword != null && !finalKeyword.isEmpty()) {
            List<Offers> allResults = offersDAO.searchByKeyword(finalKeyword);
            totalOffers = allResults.size();
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalOffers);
            listOffers = (fromIndex < toIndex) ? allResults.subList(fromIndex, toIndex) : new ArrayList<>();
        } else {
            totalOffers = offersDAO.getTotalOffers();
            listOffers = offersDAO.getPagedOffers("", page, pageSize);
        }

        model.addAttribute("listOffers", listOffers);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) totalOffers / pageSize));
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keyword", finalKeyword);
        model.addAttribute("totalOffers", totalOffers);
        return "/admin/offers";
    }

    // Show create offer form
    @RequestMapping(value = "offersCreate", method = RequestMethod.GET)
    public String showCreateOffersForm(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        model.addAttribute("offers", new Offers());
        model.addAttribute("listProducts", productsDAO.findAllProductsForOffers());
        model.addAttribute("listDiscount", discountDAO.findAll());
        return "/admin/offersCreate";
    }

    // Create a new offer
    @RequestMapping(value = "/offersCreate", method = RequestMethod.POST)
    public String createOffers(@ModelAttribute("offers") Offers offers,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {

        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        if (offersDAO.checkIfExists(offers)) {
            redirectAttributes.addFlashAttribute("error", "This product with this discount already exists.");
            return "redirect:/admin/offersCreate.htm";
        }

        try {
            offersDAO.save(offers);
            redirectAttributes.addFlashAttribute("success", "Offer created successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/offersCreate.htm";
        }

        return "redirect:/admin/offers.htm";
    }

    // Show edit offer form
    @RequestMapping(value = "offersUpdate", method = RequestMethod.GET)
    public String showEditOffersForm(@RequestParam("offersID") int id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {

        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        model.addAttribute("listProducts", productsClientDAO.findAll());
        model.addAttribute("listDiscount", discountDAO.findAll());
        model.addAttribute("offers", offersDAO.findById(id));
        return "/admin/offersUpdate";
    }

    // Update an existing offer
    @RequestMapping(value = "/offersUpdate", method = RequestMethod.POST)
    public String updateOffers(@ModelAttribute("offers") Offers offers,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {

        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        try {
            offersDAO.update(offers);
            redirectAttributes.addFlashAttribute("success", "Offer updated successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/offersUpdate.htm?offersID=" + offers.getOfferID();
        }
        return "redirect:/admin/offers.htm";
    }

    // Delete an offer
    @RequestMapping(value = "offersDelete", method = RequestMethod.GET)
    public String deleteOffers(@RequestParam("offersID") int id,
                               RedirectAttributes redirectAttributes,
                               HttpSession session) {

        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        offersDAO.deleteById(id);
        redirectAttributes.addFlashAttribute("success", "Offer deleted successfully!");
        return "redirect:/admin/offers.htm";
    }
}

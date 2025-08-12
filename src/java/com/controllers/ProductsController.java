package com.controllers;

import com.models.Brands;
import com.models.BrandsDAO;
import com.models.Categories;
import com.models.CategoriesDAO;
import com.models.Products;
import com.models.ProductsDAO;
import java.beans.PropertyEditorSupport;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import utils.RoleUtils;

@Controller
@RequestMapping("/admin")
public class ProductsController {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private ProductsDAO productsDAO;

    @Autowired
    private CategoriesDAO categoriesDAO;

    @Autowired
    private BrandsDAO brandsDAO;

    // Initialize LocalDateTime binder
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        binder.registerCustomEditor(LocalDateTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.isEmpty()) {
                    setValue(null);
                } else {
                    setValue(LocalDateTime.parse(text, formatter));
                }
            }

            @Override
            public String getAsText() {
                LocalDateTime value = (LocalDateTime) getValue();
                return (value != null) ? value.format(formatter) : "";
            }
        });
    }

    // Display list of products with pagination and search
    @RequestMapping(value = "products", method = RequestMethod.GET)
    public String showProducts(ModelMap model,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "8") int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        List<Products> listProducts = new ArrayList<>();
        int totalProducts = 0;
        String finalKeyword = (keyword != null) ? keyword.trim() : null;

        if (finalKeyword != null && !finalKeyword.isEmpty()) {
            List<Products> allResults = productsDAO.searchByKeyword(finalKeyword);
            totalProducts = allResults.size();
            int fromIndex = (page - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalProducts);
            listProducts = (fromIndex < toIndex) ? allResults.subList(fromIndex, toIndex) : new ArrayList<>();
        } else {
            totalProducts = productsDAO.getTotalProducts();
            listProducts = productsDAO.getPagedProducts("", page, pageSize);
        }

        Map<Integer, String> categoryNames = new HashMap<>();
        Map<Integer, String> brandNames = new HashMap<>();
        Map<String, Boolean> canDeleteMap = new HashMap<>();
        for (Products product : listProducts) {
            int categoryId = product.getCategoryID();
            if (!categoryNames.containsKey(categoryId)) {
                categoryNames.put(categoryId, categoriesDAO.getCategoryNameById(categoryId));
            }

            int brandId = product.getBrandID();
            if (!brandNames.containsKey(brandId)) {
                brandNames.put(brandId, brandsDAO.getBrandNameById(brandId));
            }

            canDeleteMap.put(product.getProductID(), !productsDAO.isProductSold(product.getProductID()));
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        for (Products p : listProducts) {
            if (p.getDateAdded() != null) {
                p.setFormattedDateAdded(p.getDateAdded().format(formatter));
            }
        }

        model.addAttribute("listProducts", listProducts);
        model.addAttribute("categoryNames", categoryNames);
        model.addAttribute("brandNames", brandNames);
        model.addAttribute("canDeleteMap", canDeleteMap);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", (int) Math.ceil((double) totalProducts / pageSize));
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keyword", finalKeyword);
        model.addAttribute("totalProducts", totalProducts);

        return "/admin/product";
    }

    // Show form to create new product
    @RequestMapping(value = "productCreate", method = RequestMethod.GET)
    public String showCreateProductForm(Model model,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        model.addAttribute("product", new Products());
        model.addAttribute("listCategories", categoriesDAO.findAll());
        model.addAttribute("listBrands", brandsDAO.findAll());

        return "/admin/productCreate";
    }

    // Handle creation of new product
    @RequestMapping(value = "/productCreate", method = RequestMethod.POST)
    public String createProduct(@ModelAttribute Products product,
            @RequestParam("imageFiles") List<MultipartFile> imageFiles,
            Model model) {

        try {
            productsDAO.save(product, imageFiles, servletContext);
            return "redirect:/admin/products.htm";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error when creating product: " + e.getMessage());
            model.addAttribute("product", product);
            model.addAttribute("listCategories", categoriesDAO.findAll());
            model.addAttribute("listBrands", brandsDAO.findAll());

            NumberFormat formatter = NumberFormat.getNumberInstance(Locale.US);
            formatter.setGroupingUsed(true);
            formatter.setMaximumFractionDigits(2);
            model.addAttribute("formattedUnitPrice", formatter.format(product.getUnitPrice()));

            return "/admin/productCreate";
        }
    }

    // Show form to edit existing product
    @RequestMapping(value = "/productUpdate", method = RequestMethod.GET)
    public String showEditProductForm(@RequestParam("productID") String id,
            Model model,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        String redirect = RoleUtils.checkRoleAndRedirect(session, redirectAttributes, 1);
        if (redirect != null) return redirect;

        Products product = productsDAO.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("listCategories", categoriesDAO.findAll());
        model.addAttribute("listBrands", brandsDAO.findAll());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        if (product.getDateAdded() != null) {
            product.setFormattedDateAdded(product.getDateAdded().format(formatter));
        }

        return "/admin/productUpdate";
    }

    // Handle updating product
    @PostMapping("/productUpdate")
    public String updateProduct(@ModelAttribute Products product,
            @RequestParam(value = "imageFiles", required = false) List<MultipartFile> imageFiles,
            @RequestParam(value = "imagesToDelete", required = false) List<String> imagesToDelete,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            if (product.getUnitPrice() != null) {
                BigDecimal unitPrice = new BigDecimal(product.getUnitPrice().toString().replace(",", ""));
                product.setUnitPrice(unitPrice);
            }

            productsDAO.update(product, imageFiles, imagesToDelete, request.getServletContext());
            redirectAttributes.addFlashAttribute("successMessage", "Product updated successfully!");
            return "redirect:/admin/products.htm";

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error updating product: " + e.getMessage());
            model.addAttribute("listCategories", categoriesDAO.findAll());
            model.addAttribute("listBrands", brandsDAO.findAll());
            model.addAttribute("product", productsDAO.findById(product.getProductID()));
            return "/admin/productUpdate";
        }
    }

    // Delete product by ID
    @RequestMapping(value = "/productDelete", method = RequestMethod.GET)
    public String deleteProduct(@RequestParam("productID") String id) {
        productsDAO.deleteById(id);
        return "redirect:/admin/products.htm";
    }
}

package com.controllers;

import com.models.AttributeDetail;
import com.models.Brands;
import com.models.BrandsDAO;
import com.models.Categories;
import com.models.CategoriesDAO;
import com.models.Feedback;
import com.models.FeedbackDAO;
import com.models.FeedbackImage;
import com.models.ProductsClient;
import com.models.ProductsClientDAO;
import com.models.CategoryDetailsDAO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/client")
public class ProductClientController {

    @Autowired
    private ServletContext servletContext;
    @Autowired
    private ProductsClientDAO productsDAO;
    @Autowired
    private CategoryDetailsDAO categoryDetailsDAO;
    @Autowired
    private CategoriesDAO categoriesDAO;
    @Autowired
    private FeedbackDAO feedbackDAO;
    @Autowired
    private BrandsDAO brandsDAO;

    // Display products with optional keyword or category filtering
    @RequestMapping(value = "product", method = RequestMethod.GET)
    public String showProducts(ModelMap model,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "10") int pageSize,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "categoryID", required = false) Integer categoryID) {

        keyword = (keyword != null) ? keyword.trim() : "";

        List<ProductsClient> listProducts;
        String closestMatch = null;
        List<Categories> listCategories = categoriesDAO.findAll();
        model.addAttribute("listCategories", listCategories);

        int totalProducts;
        if (categoryID != null) {
            listProducts = productsDAO.findByCategory(categoryID, page, pageSize);
            totalProducts = productsDAO.countByCategory(categoryID);
        } else if (!keyword.isEmpty()) {
            listProducts = productsDAO.searchByProductName(keyword, page, pageSize);
            totalProducts = productsDAO.countByKeyword(keyword);
            if (listProducts.isEmpty()) {
                closestMatch = productsDAO.findClosestMatch(keyword);
            }
        } else {
            listProducts = productsDAO.findPaginated(page, pageSize);
            totalProducts = productsDAO.getTotalProducts();
        }

        int totalPages = (totalProducts + pageSize - 1) / pageSize;

        Map<Integer, String> categoryNames = new HashMap<>();
        Map<Integer, String> brandNames = new HashMap<>();
        for (ProductsClient product : listProducts) {
            int categoryId = product.getCategoryID();
            if (!categoryNames.containsKey(categoryId)) {
                categoryNames.put(categoryId, categoriesDAO.getCategoryNameById(categoryId));
            }
            int brandId = product.getBrandID();
            if (!brandNames.containsKey(brandId)) {
                brandNames.put(brandId, brandsDAO.getBrandNameById(brandId));
            }
        }

        model.addAttribute("listProducts", listProducts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("keyword", keyword);
        model.addAttribute("closestMatch", closestMatch);
        model.addAttribute("categoryID", categoryID);

        return "/client/product";
    }

    // Display product details along with feedback and related data
    @RequestMapping("/productDetails")
    public String showProductDetails(@RequestParam("productID") String id,
                                     @RequestParam(value = "starFilter", required = false) Integer starFilter,
                                     @RequestParam(value = "page", defaultValue = "1") int page,
                                     Model model) {

        if (id == null || id.isEmpty()) return "errorPage";
        if (page < 1) page = 1;

        int pageSize = 10;
        int totalFeedbacks = feedbackDAO.countFeedbacksForProduct(id, starFilter);
        int totalPages = (int) Math.ceil((double) totalFeedbacks / pageSize);
        if (page > totalPages && totalPages > 0) page = totalPages;

        List<Feedback> feedbacks = feedbackDAO.getFeedbacksForProductPaged(id, starFilter, page, pageSize);
        ProductsClient product = productsDAO.findById(id);
        List<Categories> listCategories = categoriesDAO.findAll();
        List<Brands> listBrands = brandsDAO.findAll();
        Map<String, Object> ratingData = feedbackDAO.getAverageRatingAndCount(id);
        double averageRating = (double) ratingData.getOrDefault("averageRating", 0.0);
        int feedbackCount = (int) ratingData.getOrDefault("feedbackCount", 0);
        List<AttributeDetail> categoryDetailsList = categoryDetailsDAO.findByProductId(id);
        List<ProductsClient> listProducts1 = productsDAO.select5random();

        model.addAttribute("listProducts1", listProducts1);
        model.addAttribute("categoryDetailsList", categoryDetailsList);
        model.addAttribute("listCategories", listCategories);
        model.addAttribute("listBrands", listBrands);
        model.addAttribute("product", product);
        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("feedbackCount", feedbackCount);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("starFilter", starFilter);

        return "/client/productDetails";
    }

    // Return JSON of feedbacks for a product with optional star filter and pagination
    @RequestMapping(value = "/getFeedbacksFiltered", method = RequestMethod.GET)
    @ResponseBody
    public String getFeedbacksFiltered(@RequestParam("productID") String productID,
                                       @RequestParam(value = "starFilter", required = false) Integer starFilter,
                                       @RequestParam(value = "page", defaultValue = "1") int page,
                                       HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        try {
            int pageSize = 10;
            if (page < 1) page = 1;
            Integer filterValue = (starFilter != null && starFilter > 0) ? starFilter : null;
            List<Feedback> feedbacks = feedbackDAO.getFeedbacksForProductPaged(productID, filterValue, page, pageSize);
            int totalFeedbacks = feedbackDAO.countFeedbacksForProduct(productID, filterValue);
            int totalPages = (int) Math.ceil((double) totalFeedbacks / pageSize);
            if (page > totalPages && totalPages > 0) {
                page = totalPages;
                feedbacks = feedbackDAO.getFeedbacksForProductPaged(productID, filterValue, page, pageSize);
            }

            StringBuilder json = new StringBuilder("{\"success\":true,");
            json.append("\"currentPage\":").append(page).append(",");
            json.append("\"totalPages\":").append(totalPages).append(",");
            json.append("\"starFilter\":").append(starFilter != null ? starFilter : "null").append(",");
            json.append("\"totalFeedbacks\":").append(totalFeedbacks).append(",");
            json.append("\"feedbacks\":[");

            if (feedbacks != null && !feedbacks.isEmpty()) {
                for (int i = 0; i < feedbacks.size(); i++) {
                    Feedback fb = feedbacks.get(i);
                    if (i > 0) json.append(",");
                    json.append("{");
                    json.append("\"customerName\":\"").append(escapeJson(fb.getCustomerName())).append("\",");
                    json.append("\"rating\":").append(fb.getRating()).append(",");
                    json.append("\"feedbackContent\":\"").append(escapeJson(fb.getFeedbackContent())).append("\",");

                    json.append("\"feedbackImages\":[");
                    if (fb.getImages() != null && !fb.getImages().isEmpty()) {
                        for (int j = 0; j < fb.getImages().size(); j++) {
                            if (j > 0) json.append(",");
                            json.append("\"").append(escapeJson(fb.getImages().get(j).getImage())).append("\"");
                        }
                    }
                    json.append("],");

                    String firstImage = (fb.getImages() != null && !fb.getImages().isEmpty()) ? fb.getImages().get(0).getImage() : "";
                    json.append("\"feedbackImage\":\"").append(escapeJson(firstImage)).append("\",");

                    json.append("\"formattedFeedbackDate\":\"").append(fb.getFormattedFeedbackDate() != null ? escapeJson(fb.getFormattedFeedbackDate()) : "").append("\",");

                    json.append("\"employeeResponse\":");
                    if (fb.getEmployeeResponse() != null) {
                        json.append("{\"responseContent\":\"").append(escapeJson(fb.getEmployeeResponse().getResponseContent())).append("\",");
                        json.append("\"formattedResponseDate\":\"").append(fb.getEmployeeResponse().getFormattedResponseDate() != null ? escapeJson(fb.getEmployeeResponse().getFormattedResponseDate()) : "").append("\"}");
                    } else {
                        json.append("null");
                    }

                    json.append("}");
                }
            }
            json.append("]}");

            return json.toString();

        } catch (Exception e) {
            String errorJson = "{\"success\":false,\"error\":\"" + escapeJson(e.getMessage()) + "\",\"feedbacks\":[],\"currentPage\":1,\"totalPages\":0}";
            return errorJson;
        }
    }

    private String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    // Alternative endpoint to get feedbacks
    @RequestMapping(value = "/getFeedbacks", method = RequestMethod.GET)
    @ResponseBody
    public String getFeedbacksAlternative(@RequestParam("productID") String productID,
                                          @RequestParam(value = "starFilter", required = false) Integer starFilter,
                                          @RequestParam(value = "page", defaultValue = "1") int page,
                                          HttpServletResponse response) {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        return getFeedbacksFiltered(productID, starFilter, page, response);
    }

    // Get feedback by feedback ID
    @RequestMapping(value = "/getFeedbackById", method = RequestMethod.GET)
    @ResponseBody
    public String getFeedbackById(@RequestParam("feedbackID") int feedbackID, HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        try {
            Feedback feedback = feedbackDAO.get(feedbackID);
            StringBuilder json = new StringBuilder("{\"success\":true,");
            if (feedback != null) {
                json.append("\"feedback\":{");
                json.append("\"feedbackID\":").append(feedback.getFeedbackID()).append(",");
                json.append("\"customerName\":\"").append(escapeJson(feedback.getCustomerName())).append("\",");
                json.append("\"rating\":").append(feedback.getRating()).append(",");
                json.append("\"feedbackContent\":\"").append(escapeJson(feedback.getFeedbackContent())).append("\",");

                json.append("\"feedbackImages\":[");
                if (feedback.getImages() != null && !feedback.getImages().isEmpty()) {
                    for (int i = 0; i < feedback.getImages().size(); i++) {
                        if (i > 0) json.append(",");
                        json.append("{\"image\":\"").append(escapeJson(feedback.getImages().get(i).getImage())).append("\"}");
                    }
                }
                json.append("],");
                json.append("\"formattedFeedbackDate\":\"").append(feedback.getFormattedFeedbackDate() != null ? escapeJson(feedback.getFormattedFeedbackDate()) : "").append("\"}");
            } else {
                json.append("\"feedback\":null");
            }
            json.append("}");
            return json.toString();

        } catch (Exception e) {
            String errorJson = "{\"success\":false,\"error\":\"" + escapeJson(e.getMessage()) + "\"}";
            return errorJson;
        }
    }

    // Get feedback by product and customer
    @RequestMapping(value = "/getFeedbackByProductAndCustomer", method = RequestMethod.GET)
    @ResponseBody
    public String getFeedbackByProductAndCustomer(@RequestParam("productID") String productID,
                                                  @RequestParam("customerID") int customerID,
                                                  HttpServletResponse response) {
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");

        try {
            Feedback feedback = feedbackDAO.getFeedbackByProductAndCustomer(productID, customerID);
            StringBuilder json = new StringBuilder("{\"success\":true,");
            if (feedback != null) {
                json.append("\"feedback\":{");
                json.append("\"feedbackID\":").append(feedback.getFeedbackID()).append(",");
                json.append("\"customerName\":\"").append(escapeJson(feedback.getCustomerName())).append("\",");
                json.append("\"rating\":").append(feedback.getRating()).append(",");
                json.append("\"feedbackContent\":\"").append(escapeJson(feedback.getFeedbackContent())).append("\",");

                json.append("\"images\":[");
                if (feedback.getImages() != null && !feedback.getImages().isEmpty()) {
                    for (int j = 0; j < feedback.getImages().size(); j++) {
                        if (j > 0) json.append(",");
                        json.append("{\"image\":\"").append(escapeJson(feedback.getImages().get(j).getImage())).append("\"}");
                    }
                }
                json.append("],");

                json.append("\"product\":{");
                if (feedback.getProduct() != null) {
                    json.append("\"productID\":\"").append(escapeJson(feedback.getProduct().getProductID())).append("\",");
                    json.append("\"productName\":\"").append(escapeJson(feedback.getProduct().getProductName())).append("\",");
                    json.append("\"productImage\":\"");
                    if (feedback.getProduct().getImages() != null && !feedback.getProduct().getImages().isEmpty()) {
                        json.append(escapeJson(feedback.getProduct().getImages().get(0).getImages()));
                    }
                    json.append("\"");
                }
                json.append("},");
                json.append("\"formattedFeedbackDate\":\"").append(feedback.getFormattedFeedbackDate() != null ? escapeJson(feedback.getFormattedFeedbackDate()) : "").append("\"}");
            } else {
                json.append("\"feedback\":null");
            }
            json.append("}");
            return json.toString();

        } catch (Exception e) {
            String errorJson = "{\"success\":false,\"error\":\"" + escapeJson(e.getMessage()) + "\"}";
            return errorJson;
        }
    }
}

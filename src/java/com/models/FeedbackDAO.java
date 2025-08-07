package com.models;

import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import org.springframework.web.multipart.MultipartFile;

public interface FeedbackDAO {

    Map<String, Object> getAverageRatingAndCount(String productId);

    List<Feedback> findAllFeedback();

    Products findProductByID(String productID);

    EmployeeResponse getResponseByFeedbackId(int feedbackID);

    List<Feedback> getFeedbackWithoutResponse();

    public List<Feedback> getFeedbackByProductId(String productID);

    List<Feedback> getFeedbackWithResponse();

    void save(Feedback feedback, List<MultipartFile> imageFiles, ServletContext servletContext);

    Feedback get(int feedbackID);

    List<Feedback> getFeedbacksForProductPaged(String productId, Integer starFilter, int page, int pageSize);

    int countFeedbacksForProduct(String productId, Integer starFilter);

    public Feedback getFeedbackByProductAndCustomer(String productID, int customerID);

    List<Feedback> getFeedbackById(int feedbackID);

    public int getTotalFeedbackCount();

    public List<Feedback> findFeedbackWithPagination(int offset, int pageSize);

    public int getTotalFeedbackCount(String keyword);

    public List<Feedback> findFeedbackWithPagination(int offset, int pageSize, String keyword);

    public List<Feedback> getPagedFeedback(String keyword, int page, int pageSize);

    public List<Feedback> findPaginated(int page, int pageSize);

    public List<Feedback> searchByKeyword(String keyword);
}

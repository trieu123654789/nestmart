package com.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Feedback {

    private int feedbackID;
    private int customerID;
    private String productID;
    private String feedbackContent;
    private short rating;
    private LocalDateTime feedbackDate;
    private String formattedFeedbackDate;
    private EmployeeResponse employeeResponse;

    private List<FeedbackImage> images = new ArrayList<>();

    private String productName;
    private String productImage;
    private Products product;
    private String customerName;
    private String responseContent;
    private LocalDateTime responseDate;
    private String formattedResponseDate;

    public Feedback() {
        this.feedbackID = 0;
        this.customerID = 0;
        this.productID = "";
        this.feedbackContent = "";
        this.rating = 0;
        this.feedbackDate = null;
    }

    public Feedback(int feedbackID, int customerID, String productID, String feedbackContent, short rating, LocalDateTime feedbackDate) {
        this.feedbackID = feedbackID;
        this.customerID = customerID;
        this.productID = productID;
        this.feedbackContent = feedbackContent;
        this.rating = rating;
        this.feedbackDate = feedbackDate;
    }

    public List<FeedbackImage> getImages() {
        return images;
    }

    public void setImages(List<FeedbackImage> images) {
        this.images = images;
    }

    public int getFeedbackID() {
        return feedbackID;
    }

    public void setFeedbackID(int feedbackID) {
        this.feedbackID = feedbackID;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public short getRating() {
        return rating;
    }

    public void setRating(short rating) {
        this.rating = rating;
    }

    public LocalDateTime getFeedbackDate() {
        return feedbackDate;
    }

    public void setFeedbackDate(LocalDateTime feedbackDate) {
        this.feedbackDate = feedbackDate;
    }

    public String getFormattedFeedbackDate() {
        if (feedbackDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return feedbackDate.format(formatter);
        }
        return "";
    }

    public void setFormattedFeedbackDate(String formattedFeedbackDate) {
        this.formattedFeedbackDate = formattedFeedbackDate;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public LocalDateTime getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(LocalDateTime responseDate) {
        this.responseDate = responseDate;
    }

    public String getFormattedResponseDate() {
        if (responseDate != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return responseDate.format(formatter);
        }
        return "";
    }

    public void setFormattedResponseDate(String formattedResponseDate) {
        this.formattedResponseDate = formattedResponseDate;
    }

    public EmployeeResponse getEmployeeResponse() {
        return employeeResponse;
    }

    public void setEmployeeResponse(EmployeeResponse employeeResponse) {
        this.employeeResponse = employeeResponse;
    }
}

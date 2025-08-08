/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 *
 * @author Acer
 */
public class Discount {

    private int discountID;
    private String discountName;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String image;
    private String formattedStartDate;
    private String formattedEndDate;

    public Discount() {
        this.discountID = 0;
        this.discountName = "";
        this.description = "";
        this.startDate = null;
        this.endDate = null;
        this.image = "";

    }

    public Discount(int discountID, String discountName, String description, LocalDateTime startDate, LocalDateTime endDate, String image) {
        this.discountID = discountID;
        this.discountName = discountName;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.image = image;
    }

    public void setDiscountID(int discountID) {
        this.discountID = discountID;
    }

    public void setDiscountName(String discountName) {
        this.discountName = discountName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getDiscountID() {
        return discountID;
    }

    public String getDiscountName() {
        return discountName;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getFormattedStartDate() {
        return formattedStartDate;
    }

    public void setFormattedStartDate(String formattedStartDate) {
        this.formattedStartDate = formattedStartDate;
    }

    public String getFormattedEndDate() {
        return formattedEndDate;
    }

    public void setFormattedEndDate(String formattedEndDate) {
        this.formattedEndDate = formattedEndDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

}

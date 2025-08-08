package com.models;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;

public class Voucher {

    private int voucherID;
    private String code;
    private String discountType;
    private BigDecimal discountValue;
    private BigDecimal minOrderValue;
    private BigDecimal maxDiscount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer usageLimit;
    private int usedCount;
    private boolean status;
    private String formattedStartDate;
    private String formattedEndDate;
    private boolean isUsed;
    private Date assignedDate;
    private Date expiryDate;
    private boolean canUse;
    private String reasonCannotUse;

    public Voucher() {
    }

    public Voucher(int voucherID, String code, String discountType, BigDecimal discountValue,
            BigDecimal minOrderValue, BigDecimal maxDiscount, LocalDateTime startDate, LocalDateTime endDate,
            Integer usageLimit, int usedCount, boolean status) {
        this.voucherID = voucherID;
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.minOrderValue = minOrderValue;
        this.maxDiscount = maxDiscount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.usageLimit = usageLimit;
        this.usedCount = usedCount;
        this.status = status;
    }

    public BigDecimal calculateDiscount(BigDecimal orderValue) {
        if (!canUse || orderValue.compareTo(minOrderValue) < 0) {
            return BigDecimal.ZERO;
        }

        if ("Percentage".equals(discountType)) {
            BigDecimal discount = orderValue.multiply(discountValue).divide(new BigDecimal(100));
            if (maxDiscount != null && discount.compareTo(maxDiscount) > 0) {
                return maxDiscount;
            }
            return discount;
        } else if ("FixedAmount".equals(discountType)) {
            return discountValue;
        }

        return BigDecimal.ZERO;
    }

    public boolean isExpired() {
        LocalDateTime currentDate = LocalDateTime.now();
        if (expiryDate != null) {
            return expiryDate.toLocalDate().atStartOfDay().isBefore(currentDate);
        }
        if (endDate != null) {
            return endDate.isBefore(currentDate);
        }
        return false;
    }

    public boolean isUsageLimitReached() {
        return usageLimit != null && usedCount >= usageLimit;
    }

    public boolean meetsMinimumOrder(BigDecimal orderValue) {
        return minOrderValue == null || orderValue.compareTo(minOrderValue) >= 0;
    }

    public int getVoucherID() {
        return voucherID;
    }

    public void setVoucherID(int voucherID) {
        this.voucherID = voucherID;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(BigDecimal minOrderValue) {
        this.minOrderValue = minOrderValue;
    }

    public BigDecimal getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(BigDecimal maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setIsUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }

    public Date getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Date assignedDate) {
        this.assignedDate = assignedDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public boolean isCanUse() {
        return canUse;
    }

    public void setCanUse(boolean canUse) {
        this.canUse = canUse;
    }

    public String getReasonCannotUse() {
        return reasonCannotUse;
    }

    public void setReasonCannotUse(String reasonCannotUse) {
        this.reasonCannotUse = reasonCannotUse;
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
}

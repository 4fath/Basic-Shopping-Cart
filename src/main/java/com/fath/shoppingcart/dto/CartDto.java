package com.fath.shoppingcart.dto;

import com.fath.shoppingcart.util.DiscountType;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Data
public class CartDto implements Serializable {

    private String id;
    private final Set<ProductDto> products = new HashSet<>(); // store distinct products
    private final Map<ProductDto, Integer> productToCount = new HashMap<>();
    private int totalProductCount;

    private boolean isDiscountApplied;
    private DiscountType discountType;
    private double discountAmount;

    private double totalAmount; // newer change this
    private double appliedDiscountAmount;
    private double totalAmountWithDiscount;

    private final Set<CampaignDto> appliedCampaigns = new HashSet<>();
    private final Set<CouponDto> appliedCoupons = new HashSet<>();

    public void applyDiscount(CampaignDto campaignDto) {
        if (this.totalProductCount < campaignDto.getMinItemCount() || campaignDto.isApplied()) {
            return;
        }

        double canApply = this.calculateApplicableDiscount(campaignDto);
        if (canApply > this.appliedDiscountAmount) {
            this.appliedDiscountAmount = canApply;
            this.totalAmountWithDiscount = totalAmount - appliedDiscountAmount;
            this.isDiscountApplied = true;
            this.discountType = campaignDto.getDiscountType();
            this.discountAmount = campaignDto.getDiscount();
        }
    }

    private double calculateApplicableDiscount(CampaignDto campaignDto) {
        final double[] totalDiscount = {0}; // really awful way
        switch (campaignDto.getDiscountType()) {
            case RATE:
                products.stream()
                        .filter(productDto -> productDto.getCategoryId().equalsIgnoreCase(campaignDto.getCategoryId()))
                        .map(ProductDto::getPrice)
                        .forEach(appliedDiscountAmount -> {
                            double discount = (appliedDiscountAmount * campaignDto.getDiscount()) / 100;
                            totalDiscount[0] = totalDiscount[0] + discount;
                        });
                break;
            case AMOUNT:
                products.stream()
                        .filter(productDto -> productDto.getCategoryId().equalsIgnoreCase(campaignDto.getCategoryId()))
                        .forEach(appliedDiscountAmount -> totalDiscount[0] = totalDiscount[0] + campaignDto.getDiscount());
                break;
            default:
        }
        return totalDiscount[0];
    }

    public boolean canApplyCampaign(CampaignDto campaignDto) {
        if (appliedCampaigns.contains(campaignDto)) {
            return false;
        }

        String categoryId = campaignDto.getCategoryId();
        for (ProductDto product : products) {
            if (product.getCategoryId().equalsIgnoreCase(categoryId)) {
                return true;
            }
        }
        return false;
    }

    // TODO : handle count issue
    // same product can add cart multiple times
    public void addProduct(ProductDto productDto, Integer count) {
        products.add(productDto);
        totalProductCount = totalProductCount + count;
        totalAmount = totalAmount + productDto.getPrice() * count;

        productToCount.compute(productDto, (ProductDto productDto1, Integer currentCount) -> {
            if (currentCount == null) {
                return productToCount.put(productDto, count);
            } else {
                return productToCount.put(productDto1, currentCount + count);
            }
        });

        if (isDiscountApplied) {
            if (DiscountType.RATE.equals(discountType)) {
                double discount = (productDto.getPrice() * discountAmount) / 100;
                discount = discount * count;
                appliedDiscountAmount = appliedDiscountAmount + discount;
            } else {
                appliedDiscountAmount = appliedDiscountAmount + discountAmount;

            }
            totalAmountWithDiscount = totalAmount - appliedDiscountAmount;
        }
    }

    public boolean canApplyCoupon(CouponDto couponDto) {
        if (appliedCoupons.contains(couponDto)) {
            return false;
        }
        return totalAmountWithDiscount >= couponDto.getMinApplicablePrice();
    }

    public void applyCoupon(CouponDto couponDto) {
        switch (couponDto.getDiscountType()) {
            case RATE:
                double discount = (totalAmountWithDiscount * couponDto.getDiscount()) / 100;
                appliedDiscountAmount = appliedDiscountAmount + discount;
                break;
            case AMOUNT:
                appliedDiscountAmount = appliedDiscountAmount + couponDto.getDiscount();
                break;

        }
        totalAmountWithDiscount = totalAmount  - appliedDiscountAmount;
    }
}

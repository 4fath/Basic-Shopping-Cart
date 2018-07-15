package com.fath.shoppingcart.dto;

import com.fath.shoppingcart.util.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;

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

    private double couponDiscount;
    private double campaignDiscount;
    private double deliveryDiscount;

    public void applyDiscount(CampaignDto campaignDto) {
        if (this.totalProductCount < campaignDto.getMinItemCount() || campaignDto.isApplied()) {
            return;
        }

        double applicableAmount = this.calculateApplicableDiscount(campaignDto);

        if (applicableAmount > this.appliedDiscountAmount) {
            this.appliedDiscountAmount = applicableAmount;
            this.totalAmountWithDiscount = totalAmount - appliedDiscountAmount;
            this.isDiscountApplied = true;
            this.discountType = campaignDto.getDiscountType();
            this.discountAmount = campaignDto.getDiscount();
        }
    }

    private double calculateApplicableDiscount(CampaignDto campaignDto) {
        appliedCampaigns.add(campaignDto);

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
        appliedCoupons.add(couponDto);
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

    public Cart print() {
        Cart cart = Cart.builder().build();
        Map<String, List<ProductDto>> collect = products.stream().collect(groupingBy(ProductDto::getCategoryId));



        return cart;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static final class Cart implements Serializable {
        private List<ListItem> listItems = new ArrayList<>();
        private double totalAmount;
        private double deliveryCost;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static final class ListItem implements Serializable {
        private String categoryName;
        private String productName;
        private Integer quantity;
        private double unitPrice;
        private double totalPrice;
        //private double totalDiscount;
    }
}

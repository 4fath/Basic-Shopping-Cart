package com.fath.shoppingcart.dto;

import com.fath.shoppingcart.util.DiscountSource;
import com.fath.shoppingcart.util.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// coupons can apply cart => has minimum amount criteria
// campaign can apply to category =>

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public final class CartDto implements Serializable {

    private static final long serialVersionUID = -4963278247934968467L;

    private String id;
    private final Set<CartItem> cartItems = new HashSet<>();

    private final Map<String, CampaignDto> appliedCampaigns = new HashMap<>();
    private final Set<String> appliedCoupons = new HashSet<>();

    private final Set<ProductDto> products = new HashSet<>(); // store distinct products
    private final Map<ProductDto, CartItem> productToCartItem = new HashMap<>();

    private double totalCartAmount; // newer change this

    //==========
    private double deliveryDiscount;

    public boolean canApplyCampaign(CampaignDto campaignDto) {
        if (appliedCampaigns.values().contains(campaignDto)) {
            return false; // already applied, can throw exception
        }
        String categoryId = campaignDto.getCategoryId();
        Stream<CartItem> filteredItem = cartItems.stream() // TODO : be curious try parallelStream
                .filter(cartItem -> cartItem.getProduct().getCategoryId().equalsIgnoreCase(categoryId));

        Optional<CartItem> canApply = filteredItem.findAny();
        int sum = filteredItem.mapToInt(CartItem::getQuantity).sum();

        return canApply.isPresent() && (sum >= campaignDto.getMinItemCount());
    }

    public void applyCampaignToAllCarts(CampaignDto campaignDto) {
        String categoryId = campaignDto.getCategoryId();
        List<CartItem> toApply = cartItems.stream()
                .filter(cartItem -> cartItem.getProduct().getCategoryId().equalsIgnoreCase(categoryId))
                .collect(Collectors.toList());

        toApply.forEach(cartItem -> applyCampaign(campaignDto, cartItem));

        appliedCampaigns.put(campaignDto.getId(), campaignDto);
    }

    private void applyCampaign(CampaignDto campaignDto, CartItem cartItem) {
        double applyAmount = calculateApplicableAmount(campaignDto, cartItem.getProduct().getPrice());

        if (applyAmount > cartItem.getDiscountAmount()) {
            cartItem.setDiscountSource(DiscountSource.CAMPAIGN);
            cartItem.setAppliedDiscountType(campaignDto.getDiscountType());
            cartItem.setDiscountAmount(applyAmount);
            cartItem.setTotalPriceWithDiscount(cartItem.getTotalPrice() - (cartItem.getDiscountAmount() * cartItem.getQuantity()));
        }
    }

    private double calculateApplicableAmount(Discountable campaignDto, Double price) {
        switch (campaignDto.getDiscountType()) {
            case AMOUNT:
                return campaignDto.getDiscount();
            case RATE:
                return price * campaignDto.getDiscount() / 100;
            default:
                return 0;
        }
    }

    // same product can add cart multiple times
    public void addProduct(ProductDto productDto, Integer count) {
        if (products.contains(productDto)) { // add existed cartItem
            CartItem cartItem = productToCartItem.get(productDto);
            cartItem.addNewProduct(count);

        } else { // new cartItem
            products.add(productDto);
            CartItem newItem = CartItem.builder()
                    .product(productDto)
                    .unitPrice(productDto.getPrice())
                    .quantity(count)
                    .totalPrice(productDto.getPrice() * count)
                    .build();

            cartItems.add(newItem);
            productToCartItem.put(productDto, newItem);

            Set<String> appliedCampaignIds = appliedCampaigns.keySet();
            if (appliedCampaignIds.contains(productDto.getCategoryId())) {
                applyCampaign(appliedCampaigns.get(productDto.getCategoryId()), newItem);
            }
        }
        totalCartAmount = totalCartAmount + (productDto.getPrice() * count);
    }

    public boolean canApplyCoupon(CouponDto couponDto) {
        if (appliedCoupons.contains(couponDto.getId())) { // already applied
            return false;
        }
        double sum = cartItems.stream().mapToDouble(CartItem::getItemTotalDiscount).sum();
        if (sum > couponDto.getDiscount()) { // applied discount higher than processing one
            return false;
        }
        return totalCartAmount >= couponDto.getMinApplicablePrice();
    }

    public void applyCoupon(Discountable couponDto) {
        appliedCoupons.add(couponDto.getId());
        double totalDiscount = calculateApplicableAmount(couponDto, totalCartAmount);

        double ratio = totalDiscount / totalCartAmount;

        cartItems.forEach(cartItem -> {
            cartItem.setDiscountSource(DiscountSource.COUPON);
            cartItem.setAppliedDiscountType(couponDto.getDiscountType());
            cartItem.setDiscountAmount(cartItem.getTotalPrice() * ratio);
            cartItem.setTotalPriceWithDiscount(cartItem.getTotalPrice() - cartItem.getDiscountAmount());
        });
    }

    public String print() {
        final Map<String, List<CartItem>> categoryToItems = new HashMap<>();

        cartItems.forEach(cartItem -> {
            String currentCategory = cartItem.getProduct().getCategoryId();
            if (categoryToItems.keySet().contains(currentCategory)) {
                List<CartItem> cartItems = categoryToItems.get(currentCategory);
                cartItems.add(cartItem);
            } else {
                categoryToItems.put(currentCategory, Collections.singletonList(cartItem));
            }
        });


        StringBuilder strB = new StringBuilder();
        String delimiter = " | ";
        categoryToItems.values().forEach(cartItems1 -> {
            cartItems1.forEach(cartItem ->
                    strB
                            .append(cartItem.getProduct().getCategoryId())
                            .append(delimiter)
                            .append(cartItem.getProduct().getTitle())
                            .append(delimiter)
                            .append(cartItem.getQuantity())
                            .append(delimiter)
                            .append(cartItem.getUnitPrice())
                            .append(delimiter)
                            .append(cartItem.getTotalPrice())
                            .append(delimiter)
                            .append(cartItem.getDiscountAmount())
            );
            strB.append("\n");
        });

        strB.append(getTotalCartAmount()).append("\n").append(getDeliveryDiscount());
        return strB.toString();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    private static final class CartItem implements Serializable {

        private static final long serialVersionUID = -7471836720814687017L;

        private ProductDto product;
        private Integer quantity;
        private DiscountType appliedDiscountType;
        private DiscountSource discountSource;
        private double discountAmount; // discount per product

        private double unitPrice; // product price
        private double totalPrice; // (unit price * quantity)
        private double totalPriceWithDiscount; // (unitPrice - discountAmount) * quantity

        void addNewProduct(Integer count) {
            quantity = quantity + count;
            totalPrice = unitPrice * quantity;
            totalPriceWithDiscount = (unitPrice - discountAmount) * quantity;
        }

        double getItemTotalDiscount() {
            return discountAmount * quantity;
        }

    }
}

package com.fath.shoppingcart.respository;

import com.fath.shoppingcart.dto.*;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// savingInformation

@Repository
public class ApiRepository {

    private final Map<String, CategoryDto> categoryMap = new ConcurrentHashMap<>();
    private final Map<String, ProductDto> productMap = new ConcurrentHashMap<>();
    private final Map<String, CampaignDto> campaignMap = new ConcurrentHashMap<>();
    private final Map<String, CouponDto> couponMap = new ConcurrentHashMap<>();
    private final Map<String, CartDto> cartMap = new ConcurrentHashMap<>();

    public void saveCategory(CategoryDto categoryDto) {
        categoryMap.put(categoryDto.getId(), categoryDto);
    }

    public void saveProduct(ProductDto productDto) {
        productMap.put(productDto.getId(), productDto);
    }

    public void saveCampaign(CampaignDto campaignDto) {
        campaignMap.put(campaignDto.getId(), campaignDto);
    }

    public void saveCoupon(CouponDto couponDto) {
        couponMap.put(couponDto.getId(), couponDto);
    }

    public void saveCart(CartDto cartDto) {
        cartMap.put(cartDto.getId(), cartDto);
    }

    public Map<String, CategoryDto> _categoryMap() {
        return categoryMap;
    }

    public Map<String, ProductDto> _productMap() {
        return productMap;
    }

    public Map<String, CampaignDto> _campaignMap() {
        return campaignMap;
    }

    public Map<String, CouponDto> _couponMap() {
        return couponMap;
    }

    public Map<String, CartDto> _cartMap() {
        return cartMap;
    }
}

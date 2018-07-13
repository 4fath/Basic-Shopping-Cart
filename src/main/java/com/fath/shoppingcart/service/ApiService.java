package com.fath.shoppingcart.service;

import com.fath.shoppingcart.dto.*;
import com.fath.shoppingcart.respository.ApiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ApiService {

    @Autowired
    private ApiRepository repository;

    public String createCategory(CategoryDto categoryDto) {
        String uuid = UUID.randomUUID().toString();
        categoryDto.setId(uuid);
        repository.saveCategory(categoryDto);
        return uuid;
    }

    public String createProduct(ProductDto productDto) {
        String uuid = UUID.randomUUID().toString();
        productDto.setId(uuid);
        repository.saveProduct(productDto);
        return uuid;
    }

    public String createCampaign(CampaignDto campaignDto) {
        String uuid = UUID.randomUUID().toString();
        campaignDto.setId(uuid);
        repository.saveCampaign(campaignDto);

        applyCampaignToCarts(campaignDto);

        return uuid;
    }

    private void applyCampaignToCarts(CampaignDto campaignDto) {
        repository._cartMap().values().forEach(cartDto -> {
            boolean canApplyCampaign = cartDto.canApplyCampaign(campaignDto);
            if (canApplyCampaign) {
                cartDto.applyDiscount(campaignDto);
            }
        });
    }

    public String createCoupon(CouponDto couponDto) {
        String uuid = UUID.randomUUID().toString();
        couponDto.setId(uuid);
        repository.saveCoupon(couponDto);
        return uuid;
    }

    public String createCart(CartDto cartDto) {
        String uuid = UUID.randomUUID().toString();
        cartDto.setId(uuid);
        repository.saveCart(cartDto);
        return uuid;
    }

    public void putProductToCart(String productId, String cartId, Integer count) {
        ProductDto productDto = repository._productMap().get(productId);
        CartDto cartDto = repository._cartMap().get(cartId);
        cartDto.addProduct(productDto, count);
    }

    public void applyCoupon(String cartId, String couponId) {
        CouponDto couponDto = repository._couponMap().get(couponId);
        CartDto cartDto = repository._cartMap().get(cartId);

        boolean canApply = cartDto.canApplyCoupon(couponDto);
        if (canApply) {
            cartDto.applyCoupon(couponDto);
        }
    }

    public CartDto printCart(String cartId) {
        String uuid = UUID.randomUUID().toString();
        return null;
    }
}

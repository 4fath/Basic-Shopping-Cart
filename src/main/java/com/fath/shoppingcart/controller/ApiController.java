package com.fath.shoppingcart.controller;

import com.fath.shoppingcart.dto.*;
import com.fath.shoppingcart.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ApiController {

    @Autowired
    private ApiService service;

    @PostMapping("/category")
    public ResponseEntity createCategory(@RequestBody CategoryDto categoryDto) {
        String categoryId = service.createCategory(categoryDto);
        return new ResponseEntity<>(categoryId, HttpStatus.CREATED);
    }

    @PostMapping("/product")
    public ResponseEntity createProduct(@RequestBody ProductDto productDto) {
        String productId = service.createProduct(productDto);
        return new ResponseEntity<>(productId, HttpStatus.CREATED);
    }

    @PostMapping("/campaign")
    public ResponseEntity createCampaign(@RequestBody CampaignDto campaignDto) {
        String campaignId = service.createCampaign(campaignDto);
        return new ResponseEntity<>(campaignId, HttpStatus.CREATED);
    }

    @PostMapping("/coupon")
    public ResponseEntity createCoupon(@RequestBody CouponDto couponDto) {
        String couponId = service.createCoupon(couponDto);
        return new ResponseEntity<>(couponId, HttpStatus.CREATED);
    }

    @PostMapping("/cart")
    public ResponseEntity createCart(@RequestBody CartDto cartDto) {
        String cartId = service.createCart(cartDto);
        return new ResponseEntity<>(cartId, HttpStatus.CREATED);
    }

    @PutMapping("/addProduct/{cartId}/{productId}/{count}")
    public ResponseEntity addToCart(@PathVariable("productId") String productId, @PathVariable("cartId") String cartId,
                                    @PathVariable("count") Integer count) {
        service.putProductToCart(productId, cartId, count);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/applyCoupon/{cartId}/{couponId}")
    public ResponseEntity applyCoupon(@PathVariable("cartId") String cartId, @PathVariable("cartId") String couponId) {
        service.applyCoupon(cartId, couponId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/print/{cartId}")
    public ResponseEntity printCart(@PathVariable("cartId") String cartId) {
        CartDto cartDto = service.printCart(cartId);
        return ResponseEntity.ok(cartDto);
    }

}

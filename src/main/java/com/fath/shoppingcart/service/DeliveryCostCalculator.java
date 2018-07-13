package com.fath.shoppingcart.service;

import com.fath.shoppingcart.dto.CartDto;

import java.util.HashSet;
import java.util.Set;

public final class DeliveryCostCalculator {

    public static final double DEFAULT_COST_PER_DELIVERY = 4.00;
    public static final double DEFAULT_COST_PER_PRODUCT = 4.50;
    public static final double DEFAULT_FIXED_COST = 2.99;

    private Double costPerDelivery;
    private Double costPerProduct;
    private Double fixedCost = 2.99;

    public DeliveryCostCalculator(Double costPerDelivery, Double costPerProduct, Double fixedCost) {
        this.costPerDelivery = costPerDelivery;
        this.costPerProduct = costPerProduct;
        this.fixedCost = fixedCost;
    }

    public double calculateFor(CartDto cart) {
        int numberOfDeliveries = this.getNumberOfDeliveries(cart);
        int numberOfProduct = this.getNumberOfProducts(cart);
        return (costPerDelivery * numberOfDeliveries) + (costPerProduct * numberOfProduct) + fixedCost;
    }

    private int getNumberOfProducts(CartDto cart) {
        return cart.getProducts().size();
    }

    private int getNumberOfDeliveries(CartDto cart) {
        Set<String> categoryIds = new HashSet<>();
        cart.getProducts().forEach(productDto -> categoryIds.add(productDto.getCategoryId()));
        return categoryIds.size();
    }

}

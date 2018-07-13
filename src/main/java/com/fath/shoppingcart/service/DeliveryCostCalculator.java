package com.fath.shoppingcart.service;

import com.fath.shoppingcart.dto.CartDto;

public final class DeliveryCostCalculator {

    private Double costPerDelivery;
    private Integer numberOfDeliveries;
    private Double costPerProduct;
    private Integer numberOfProducts;
    private Double fixedCost = 2.99;

    public DeliveryCostCalculator(Double costPerDelivery, Double costPerProduct, Double fixedCost) {
        this.costPerDelivery = costPerDelivery;
        this.costPerProduct = costPerProduct;
        this.fixedCost = fixedCost;
    }

    public double calculateFor(CartDto cart) {
        return 0;
    }

}

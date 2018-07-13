package com.fath.shoppingcart.service;


import org.junit.Before;
import org.junit.Test;

import static com.fath.shoppingcart.service.DeliveryCostCalculator.*;


public class DeliveryCalculatorTest {

    private DeliveryCostCalculator calculator;

    @Before
    public void init() {
        calculator = new DeliveryCostCalculator(DEFAULT_COST_PER_DELIVERY, DEFAULT_COST_PER_PRODUCT, DEFAULT_FIXED_COST);
    }

    @Test
    public void should_calculate_cart_discount() {

    }

}

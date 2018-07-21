package com.fath.shoppingcart.dto;

import com.fath.shoppingcart.util.DiscountType;

public interface Discountable {

    String getId();

    DiscountType getDiscountType();

    Double getDiscount();

}

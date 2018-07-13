package com.fath.shoppingcart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fath.shoppingcart.util.DiscountType;
import lombok.Data;

import java.io.Serializable;

@Data
public class CouponDto implements Serializable {

    @JsonIgnore
    private String id;
    private Double minApplicablePrice;
    private Double discount;
    private DiscountType discountType;

}

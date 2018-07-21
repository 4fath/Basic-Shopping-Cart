package com.fath.shoppingcart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fath.shoppingcart.util.DiscountType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(of = {"id"})
public final class CouponDto implements Discountable, Serializable {

    private static final long serialVersionUID = -1147663597251765938L;

    @JsonIgnore
    private String id;
    private Double minApplicablePrice;
    private Double discount;
    private DiscountType discountType;

}

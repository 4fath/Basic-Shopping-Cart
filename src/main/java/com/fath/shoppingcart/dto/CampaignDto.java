package com.fath.shoppingcart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fath.shoppingcart.util.DiscountType;
import lombok.Data;

import java.io.Serializable;

@Data
public final class CampaignDto implements Discountable, Serializable {

    private static final long serialVersionUID = 2084718320808609393L;

    @JsonIgnore
    private String id;
    private String categoryId;

    private Double discount;
    private Integer minItemCount;
    private DiscountType discountType;

    private boolean isApplied;
}

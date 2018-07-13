package com.fath.shoppingcart.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id", "title"})
public class ProductDto {

    private String id;
    private String title;
    private Double price;
    private String categoryId;

}

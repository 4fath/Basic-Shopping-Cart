package com.fath.shoppingcart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = {"id", "title"})
public class ProductDto {

    @JsonIgnore
    private String id;
    private String title;
    private Double price;
    private String categoryId;

}

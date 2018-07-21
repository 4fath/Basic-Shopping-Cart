package com.fath.shoppingcart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(of = {"id", "title"})
public final class ProductDto implements Serializable {

    private static final long serialVersionUID = 7428851082887789397L;

    @JsonIgnore
    private String id;
    private String title;
    private Double price;
    private String categoryId;

}

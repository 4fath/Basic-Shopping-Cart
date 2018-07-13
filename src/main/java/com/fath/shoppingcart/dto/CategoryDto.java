package com.fath.shoppingcart.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryDto implements Serializable {

    private String id;
    private String name;
    private String parentId;
}

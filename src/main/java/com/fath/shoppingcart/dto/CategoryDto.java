package com.fath.shoppingcart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryDto implements Serializable {

    @JsonIgnore
    private String id;
    private String name;
    private String parentId;
}

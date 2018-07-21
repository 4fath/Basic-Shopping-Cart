package com.fath.shoppingcart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

@Data
public final class CategoryDto implements Serializable {

    private static final long serialVersionUID = -206513582256155033L;

    @JsonIgnore
    private String id;
    private String name;
    private String parentId;
}

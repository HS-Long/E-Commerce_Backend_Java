package com.ms.crud_api.model.request.product;

import java.io.Serializable;

public class ProductRestore implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

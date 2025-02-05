package com.ms.crud_api.model.request.category;

import java.io.Serializable;

public class CategoryRestore implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

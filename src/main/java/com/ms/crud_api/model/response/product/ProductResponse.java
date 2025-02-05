package com.ms.crud_api.model.response.product;

import com.ms.crud_api.infrastructure.model.response.BaseResponse;
import com.ms.crud_api.model.entity.ProductEntity;

import java.util.List;

public class ProductResponse extends BaseResponse {
    private final String name;
    private final String description;
    private final Double price;
    private final List<String> colors;
    private final List<String> sizes;
    private final int stockQuantity;

    public ProductResponse(ProductEntity entity) {
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.colors = entity.getColors();
        this.sizes = entity.getSizes();
        this.stockQuantity = entity.getStockQuantity();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public List<String> getColors() {
        return colors;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }
}
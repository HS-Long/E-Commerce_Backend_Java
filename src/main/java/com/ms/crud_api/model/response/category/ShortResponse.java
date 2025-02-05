package com.ms.crud_api.model.response.category;

import com.ms.crud_api.infrastructure.model.response.BaseResponse;
import com.ms.crud_api.model.entity.CategoryEntity;
import com.ms.crud_api.model.response.product.ProductResponse;

public class ShortResponse extends BaseResponse {

    private final Long id;
    private String name;
    private String description;
    private ProductResponse product;

    public ShortResponse(Long id, String name, String description, ProductResponse product) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.product = product;
    }

    public static ShortResponse fromEntity(CategoryEntity entity) {
        ProductResponse product = null;
        if (entity.getProductEntities() != null && !entity.getProductEntities().isEmpty()) {
            product = new ProductResponse(entity.getProductEntities().get(0));
        }
        return new ShortResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                product
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public void setProduct(ProductResponse product) {
        this.product = product;
    }
}
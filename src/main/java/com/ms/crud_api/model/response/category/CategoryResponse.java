package com.ms.crud_api.model.response.category;

import com.ms.crud_api.infrastructure.model.response.BaseResponse;
import com.ms.crud_api.model.entity.CategoryEntity;
import com.ms.crud_api.model.response.orderItem.OrderItemResponse;
import com.ms.crud_api.model.response.product.ProductResponse;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CategoryResponse extends BaseResponse{

    private final Long id;
    private String name;
    private String description;
    private List<ProductResponse> product;

    public CategoryResponse(Long id, String name, String description, List<ProductResponse> product) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.product = product;
    }

    public static CategoryResponse fromEntity(CategoryEntity entity) {
        List<ProductResponse> product = entity.getProductEntities().stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());

        return new CategoryResponse(
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

    public List<ProductResponse> getProduct() {
        return product;
    }

    public void setProduct(List<ProductResponse> product) {
        this.product = product;
    }
}
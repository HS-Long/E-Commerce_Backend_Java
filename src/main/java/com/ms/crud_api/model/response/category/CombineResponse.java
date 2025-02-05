package com.ms.crud_api.model.response.category;

import com.ms.crud_api.infrastructure.model.response.BaseResponse;
import com.ms.crud_api.model.entity.CategoryEntity;
import com.ms.crud_api.model.entity.ProductEntity;
import com.ms.crud_api.model.response.product.ProductResponse;

import java.util.List;
import java.util.stream.Collectors;

public class CombineResponse extends BaseResponse {

    private CategoryResponse category;
    private List<ProductResponse> products;

    public CombineResponse(CategoryResponse category, List<ProductResponse> products) {
        this.category = category;
        this.products = products;
    }

    public static CombineResponse fromEntities(CategoryEntity categoryEntity, List<ProductEntity> productEntities) {
        CategoryResponse categoryResponse = CategoryResponse.fromEntity(categoryEntity);
        List<ProductResponse> productResponses = productEntities.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
        return new CombineResponse(categoryResponse, productResponses);
    }

    public CategoryResponse getCategory() {
        return category;
    }

    public void setCategory(CategoryResponse category) {
        this.category = category;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponse> products) {
        this.products = products;
    }

}

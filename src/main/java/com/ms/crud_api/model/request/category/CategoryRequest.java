package com.ms.crud_api.model.request.category;

import com.ms.crud_api.model.entity.CategoryEntity;
import com.ms.crud_api.model.entity.ProductEntity;
import com.ms.crud_api.model.request.product.ProductRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

public class CategoryRequest implements Serializable {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "Women", maxLength = 50)
    @NotNull(message = "street is required!")
    @NotEmpty(message = "street cannot be empty!")
    @Size(max = 50, message = "street cannot be bigger than 50 characters!")
    private String name;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "Best Women Collection", maxLength = 50)
    @NotNull(message = "street is required!")
    @NotEmpty(message = "street cannot be empty!")
    @Size(max = 50, message = "street cannot be bigger than 50 characters!")
    private String description;

    private List<ProductRequest> products;



    public CategoryRequest(){
    }

    public CategoryEntity toEntity(List<ProductEntity> products) {
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setName(this.name);
        categoryEntity.setDescription(this.description);
        categoryEntity.setProductEntities(products);
        return categoryEntity;
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


    public List<ProductRequest> getProducts() {
        return products;
    }

    public void setProducts(List<ProductRequest> products) {
        this.products = products;
    }
}

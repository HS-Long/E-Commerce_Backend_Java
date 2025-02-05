// ProductRequest.java
package com.ms.crud_api.model.request.product;

import com.ms.crud_api.model.entity.CategoryEntity;
import com.ms.crud_api.model.entity.ProductEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.List;

public class ProductRequest implements Serializable {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "T-Shirt")
    @NotNull(message = "name is required!")
    @Size(max = 50, message = "name cannot be bigger than 50 characters!")
    private String name;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "John Doe", maxLength = 50)
    @NotNull(message = "description is required!")
    @Size(max = 50, message = "description cannot be bigger than 50 characters!")
    private String description;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "232")
    @NotNull(message = "price is required!")
    private double price;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"M\", \"L\"]")
    @NotNull(message = "sizes are required!")
    private List<String> sizes;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "[\"Blue\", \"Red\"]")
    @NotNull(message = "colors are required!")
    private List<String> colors;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    @NotNull(message = "stockQuantity is required!")
    private int stockQuantity;

    public ProductRequest() {
    }

    public ProductEntity toEntity(CategoryEntity category) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(this.name);
        productEntity.setDescription(this.description);
        productEntity.setPrice(this.price);
        productEntity.setSizes(this.sizes);
        productEntity.setColors(this.colors);
        productEntity.setStockQuantity(this.stockQuantity);
        productEntity.setCategoryEntity(category);
        return productEntity;
    }

    // Getters and setters for all fields


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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<String> getSizes() {
        return sizes;
    }

    public void setSizes(List<String> sizes) {
        this.sizes = sizes;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }
}
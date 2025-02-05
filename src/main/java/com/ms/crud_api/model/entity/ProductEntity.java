package com.ms.crud_api.model.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.crud_api.infrastructure.model.entity.BaseSoftDeleteEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class ProductEntity extends BaseSoftDeleteEntity<Long> {
    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 50)
    private String description;

    @Column(nullable = false)
    private double price;


    @Column(nullable = false, columnDefinition = "TEXT")
    private String sizes;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String colors;

    @Column(nullable = false)
    private int stockQuantity;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity categoryEntity;

    public ProductEntity(String name, String description, double price, List<String> sizes, List<String> colors, int stockQuantity, CategoryEntity categoryEntity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.sizes = convertListToJson(sizes);
        this.colors = convertListToJson(colors);
        this.stockQuantity = stockQuantity;
        this.categoryEntity = categoryEntity;
    }

    public ProductEntity() {
    }

    public List<String> getSizes() {
        return convertJsonToList(sizes);
    }

    public void setSizes(List<String> sizes) {
        this.sizes = convertListToJson(sizes);
    }

    public List<String> getColors() {
        return convertJsonToList(colors);
    }

    public void setColors(List<String> colors) {
        this.colors = convertListToJson(colors);
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public CategoryEntity getCategoryEntity() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    private String convertListToJson(List<String> list) {
        try {
            return new ObjectMapper().writeValueAsString(list);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert list to JSON", e);
        }
    }

    private List<String> convertJsonToList(String json) {
        try {
            return new ObjectMapper().readValue(json, List.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to list", e);
        }
    }
}
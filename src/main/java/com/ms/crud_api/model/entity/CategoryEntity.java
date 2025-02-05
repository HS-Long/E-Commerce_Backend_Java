package com.ms.crud_api.model.entity;

import com.ms.crud_api.infrastructure.model.entity.BaseSoftDeleteEntity;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "categories")
public class CategoryEntity extends BaseSoftDeleteEntity<Long> {
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 50)
    private  String description;


    @OneToMany(mappedBy = "categoryEntity" ,  fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> productEntities;

    public CategoryEntity(String name, String description, List<ProductEntity> productEntities) {
        this.name = name;
        this.description = description;
        this.productEntities = productEntities;
    }

    public CategoryEntity() {

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

    public List<ProductEntity> getProductEntities() {
        return productEntities;
    }

    public void setProductEntities(List<ProductEntity> productEntities) {
        this.productEntities = productEntities;
    }
}

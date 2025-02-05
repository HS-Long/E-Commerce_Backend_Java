package com.ms.crud_api.model.entity;

import com.ms.crud_api.infrastructure.model.entity.BaseSoftDeleteEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "reviews")
public class ReviewEntity extends BaseSoftDeleteEntity<Long> {
    @Column(nullable = false, length = 10)
    private int rating;
    @Column(nullable = false, length = 50)
    private String comment;


    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private ProductEntity productEntity;


    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity userEntity;

    public ReviewEntity(int rating, String comment, ProductEntity productEntity, UserEntity userEntity) {
        this.rating = rating;
        this.comment = comment;
        this.productEntity = productEntity;
        this.userEntity = userEntity;
    }

    public ReviewEntity() {

    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }
}

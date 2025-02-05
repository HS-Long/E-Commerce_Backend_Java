package com.ms.crud_api.model.request.review;

import com.ms.crud_api.model.entity.PaymentMethodEntity;
import com.ms.crud_api.model.entity.ProductEntity;
import com.ms.crud_api.model.entity.ReviewEntity;
import com.ms.crud_api.model.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class ReviewRequest implements Serializable {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    @NotNull(message = "name is required!")
    private int rating;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "The Best", maxLength = 100)
    @NotNull(message = "comment is required!")
    @Size(max = 100, message = "comment cannot be bigger than 50 characters!")
    private String comment;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    @NotNull(message = "name is required!")
    private Integer productId;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    @NotNull(message = "name is required!")
    private Integer userId;

    public ReviewRequest() {
    }

    public ReviewEntity toEntity(ProductEntity productId , UserEntity userId){
        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setRating(this.rating);
        reviewEntity.setComment(this.comment);
        reviewEntity.setProductEntity(productId);
        reviewEntity.setUserEntity(userId);
        return reviewEntity;
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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

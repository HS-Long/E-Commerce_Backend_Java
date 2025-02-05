package com.ms.crud_api.model.response.review;

import com.ms.crud_api.infrastructure.model.response.BaseResponse;
import com.ms.crud_api.model.entity.ProductEntity;
import com.ms.crud_api.model.entity.ReviewEntity;
import com.ms.crud_api.model.entity.UserEntity;

public class ReviewResponse extends BaseResponse {

    private Long id;

    private int rating;

    private String comment;

    private Integer productId;

    private Integer useId;

    public ReviewResponse(Long id, int rating, String comment, Integer productId, Integer useId) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.productId = productId;
        this.useId = useId;
    }


    public static ReviewResponse fromEntity(ReviewEntity entity){
        return new ReviewResponse(
                entity.getId(),
                entity.getRating(),
                entity.getComment(),
                Math.toIntExact(entity.getProductEntity().getId()),
                Math.toIntExact(entity.getUserEntity().getId())
        );
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getUseId() {
        return useId;
    }

    public void setUseId(Integer useId) {
        this.useId = useId;
    }
}

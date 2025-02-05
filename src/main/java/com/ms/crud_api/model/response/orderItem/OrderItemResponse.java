package com.ms.crud_api.model.response.orderItem;

import com.ms.crud_api.model.entity.OrderItemEntity;

import java.io.Serializable;

public class OrderItemResponse implements Serializable {

    private int quantity;
    private Long productId;

    public OrderItemResponse(OrderItemEntity entity) {
        this.quantity = entity.getQuantity();
        this.productId = entity.getProductEntity().getId();
    }

    // Getters and setters
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
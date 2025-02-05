package com.ms.crud_api.model.request.orderItem;

import com.ms.crud_api.model.entity.OrderEntity;
import com.ms.crud_api.model.entity.OrderItemEntity;
import com.ms.crud_api.model.entity.ProductEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class OrderItemRequest implements Serializable {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Quantity is required!")
    private int quantity;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Product ID is required!")
    private Integer productId;

    public OrderItemRequest() {
    }

    public OrderItemEntity toEntity(OrderEntity order, ProductEntity product) {
        OrderItemEntity orderItem = new OrderItemEntity();
        orderItem.setQuantity(this.quantity);
        orderItem.setOrder(order);
        orderItem.setProductEntity(product);
        return orderItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

}
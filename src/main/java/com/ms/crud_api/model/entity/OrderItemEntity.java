package com.ms.crud_api.model.entity;

import com.ms.crud_api.infrastructure.model.entity.BaseSoftDeleteEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItemEntity extends BaseSoftDeleteEntity<Long> {

    @Column(nullable = false)
    private int quantity;
    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private OrderEntity order;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private ProductEntity productEntity;



    public OrderItemEntity(int quantity, OrderEntity order, ProductEntity productEntity) {
        this.quantity = quantity;
        this.order = order;
        this.productEntity = productEntity;
    }

    public OrderItemEntity() {

    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public ProductEntity getProductEntity() {
        return productEntity;
    }

    public void setProductEntity(ProductEntity productEntity) {
        this.productEntity = productEntity;
    }


}

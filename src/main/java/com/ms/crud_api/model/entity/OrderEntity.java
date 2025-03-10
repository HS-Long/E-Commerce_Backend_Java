
package com.ms.crud_api.model.entity;

import com.ms.crud_api.infrastructure.model.entity.BaseSoftDeleteEntity;
import com.ms.crud_api.model.entityHistory.OrderHistoryEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class OrderEntity extends BaseSoftDeleteEntity<Long> {

    @Column(nullable = false)
    private double totalAmount;
    private String status;
    @Column(nullable = false, length = 50)
    private String paymentMethod;
    @Column(nullable = false, length = 50)
    private String shippingAddress;

    @Column(nullable = false, length = 50)
    private String customerName;

    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItemEntity> orderItemEntity = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity userId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderHistoryEntity> orderHistory = new HashSet<>();

    public OrderEntity(double totalAmount, String status, String paymentMethod, String shippingAddress, String customerName, Set<OrderItemEntity> orderItemEntity, UserEntity userId, Set<OrderHistoryEntity> orderHistory) {
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.customerName = customerName;
        this.orderItemEntity = orderItemEntity;
        this.userId = userId;
        this.orderHistory = orderHistory;
    }

    public OrderEntity() {
    }

    public Set<OrderItemEntity> getOrderItemEntity() {
        return orderItemEntity;
    }

    public void setOrderItemEntity(Set<OrderItemEntity> orderItemEntity) {
        this.orderItemEntity = orderItemEntity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public UserEntity getUserId() {
        return userId;
    }

    public void setUserId(UserEntity userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Set<OrderHistoryEntity> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(Set<OrderHistoryEntity> orderHistory) {
        this.orderHistory = orderHistory;
    }
}
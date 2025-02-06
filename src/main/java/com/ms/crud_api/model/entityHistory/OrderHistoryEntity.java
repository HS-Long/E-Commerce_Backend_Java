package com.ms.crud_api.model.entityHistory;

import com.ms.crud_api.constant.enums.CrudTypeEnum;
import com.ms.crud_api.infrastructure.model.entity.BaseSoftDeleteEntity;
import com.ms.crud_api.model.entity.OrderEntity;
import com.ms.crud_api.model.entity.OrderItemEntity;
import com.ms.crud_api.model.entity.UserEntity;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "order_history")
public class OrderHistoryEntity extends BaseSoftDeleteEntity<Long> {

    @Column(nullable = false)
    private double totalAmount;
    private String status;
    @Column(nullable = false, length = 50)
    private String paymentMethod;
    @Column(nullable = false, length = 50)
    private String shippingAddress;

    @Column(nullable = false, length = 50)
    private String customerName;

//    @ManyToMany
//    @JoinTable(
//            name = "order_history_order_items",
//            joinColumns = @JoinColumn(name = "order_history_id"),
//            inverseJoinColumns = @JoinColumn(name = "order_item_id")
//    )
//    private Set<OrderItemEntity> orderItemEntity = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity userId;

    @ManyToOne
    @JoinColumn(name = "order_Id", nullable = false)
    private OrderEntity order;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CrudTypeEnum type;

    public OrderHistoryEntity(double totalAmount, String status, String paymentMethod, String shippingAddress, String customerName, UserEntity userId, OrderEntity order, CrudTypeEnum type) {
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.customerName = customerName;
        this.userId = userId;
        this.order = order;
        this.type = type;
    }

    public OrderHistoryEntity() {
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

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public CrudTypeEnum getType() {
        return type;
    }

    public void setType(CrudTypeEnum type) {
        this.type = type;
    }
}
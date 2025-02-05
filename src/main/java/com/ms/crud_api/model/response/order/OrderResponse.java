package com.ms.crud_api.model.response.order;

import com.ms.crud_api.infrastructure.model.response.BaseResponse;
import com.ms.crud_api.model.entity.OrderEntity;
import com.ms.crud_api.model.response.orderItem.OrderItemResponse;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderResponse extends BaseResponse implements Serializable {

    private Long id;
    private String status;
    private String paymentMethod;
    private String shippingAddress;
    private String customerName;
    private Integer userId;

    private double totalAmount;
    private Set<OrderItemResponse> orderItems;

    public OrderResponse(Long id, String status, String paymentMethod, String shippingAddress, String customerName, Integer userId, double totalAmount, Set<OrderItemResponse> orderItems) {
        this.id = id;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.shippingAddress = shippingAddress;
        this.customerName = customerName;
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.orderItems = orderItems;
    }

    public static OrderResponse fromEntity(OrderEntity entity) {
        Set<OrderItemResponse> orderItems = entity.getOrderItemEntity().stream()
                .map(OrderItemResponse::new)
                .collect(Collectors.toSet());

        return new OrderResponse(
                entity.getId(),
                entity.getStatus(),
                entity.getPaymentMethod(),
                entity.getShippingAddress(),
                entity.getCustomerName(),
                Math.toIntExact(entity.getUserId().getId()),
                entity.getTotalAmount(),
                orderItems
        );
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Set<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
}
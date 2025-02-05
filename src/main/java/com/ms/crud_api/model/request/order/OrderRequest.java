package com.ms.crud_api.model.request.order;

import com.ms.crud_api.model.entity.OrderEntity;
import com.ms.crud_api.model.entity.OrderItemEntity;
import com.ms.crud_api.model.entity.UserEntity;
import com.ms.crud_api.model.entity.ProductEntity;
import com.ms.crud_api.model.request.orderItem.OrderItemRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class OrderRequest implements Serializable {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "PENDING", maxLength = 50)
    @NotNull(message = "status is required!")
    @Size(max = 50, message = "status cannot be bigger than 50 characters!")
    private String status;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "CASH", maxLength = 50)
    @NotNull(message = "paymentMethod is required!")
    @Size(max = 50, message = "paymentMethod cannot be bigger than 50 characters!")
    private String paymentMethod;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "Jl. Jendral Sudirman No. 1", maxLength = 50)
    @NotNull(message = "shippingAddress is required!")
    @Size(max = 50, message = "shippingAddress cannot be bigger than 50 characters!")
    private String shippingAddress;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "John Doe", maxLength = 50)
    @NotNull(message = "customerName is required!")
    @Size(max = 50, message = "customerName cannot be bigger than 50 characters!")
    private String customerName;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "userId is required!")
    private Integer userId;

    private List<OrderItemRequest> orderItem;




    public OrderRequest() {
    }
    public OrderEntity toEntity(UserEntity user, List<OrderItemEntity> orderItems) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setStatus(this.status);
        orderEntity.setPaymentMethod(this.paymentMethod);
        orderEntity.setShippingAddress(this.shippingAddress);
        orderEntity.setCustomerName(this.customerName);
        orderEntity.setUserId(user);
        orderEntity.setOrderItemEntity(new HashSet<>(orderItems));
        return orderEntity;
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

    public List<OrderItemRequest> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(List<OrderItemRequest> orderItem) {
        this.orderItem = orderItem;
    }
}
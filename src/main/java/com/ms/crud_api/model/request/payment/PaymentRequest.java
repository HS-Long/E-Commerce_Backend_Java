package com.ms.crud_api.model.request.payment;

import com.ms.crud_api.model.entity.PaymentMethodEntity;
import com.ms.crud_api.model.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class PaymentRequest implements Serializable {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "243565768756845")
    @NotNull(message = "cardNumber is required!")
    private Long cardNumber;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "John Doe", maxLength = 50)
    @NotNull(message = "cardHolder is required!")
    @Size(max = 50, message = "cardHolder cannot be bigger than 50 characters!")
    private String cardHolder;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "24/12", maxLength = 50)
    @NotNull(message = "expirationDate is required!")
    @Size(max = 50, message = "expirationDate cannot be bigger than 50 characters!")
    private String expirationDate;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "234")
    @NotNull(message = "cvv is required!")
    private Long cvv;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "userId is required!")
    private Integer user;

    public PaymentRequest() {
    }

    public PaymentMethodEntity toEntity(UserEntity user){
        PaymentMethodEntity paymentMethodEntity = new PaymentMethodEntity();
        paymentMethodEntity.setCardNumber(this.cardNumber);
        paymentMethodEntity.setCardHolder(this.cardHolder);
        paymentMethodEntity.setExpirationDate(this.expirationDate);
        paymentMethodEntity.setCvv(this.cvv);
        paymentMethodEntity.setUser(user);

        return paymentMethodEntity;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(Integer user) {
        this.user = user;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Long getCvv() {
        return cvv;
    }

    public void setCvv(Long cvv) {
        this.cvv = cvv;
    }
}
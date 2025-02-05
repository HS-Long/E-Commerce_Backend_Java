package com.ms.crud_api.model.response.Payment;

import com.ms.crud_api.infrastructure.model.response.BaseResponse;
import com.ms.crud_api.model.entity.PaymentMethodEntity;
import com.ms.crud_api.model.response.user.UserResponse;


public class PaymentResponse extends BaseResponse {
    private Long id;

    private Long cardNumber;

    private String cardHolder;

    private String expirationDate;

    private Long cvv;


    private Integer userId;


    public PaymentResponse(Long id, Long cardNumber, String cardHolder, String expirationDate, Long cvv, Integer userId) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.userId = userId;

    }

    public static PaymentResponse fromEntity(PaymentMethodEntity entity) {
        return new PaymentResponse(
                entity.getId(),
                entity.getCardNumber(),
                entity.getCardHolder(),
                entity.getExpirationDate(),
                entity.getCvv(),
                Math.toIntExact(entity.getUser().getId())
        );
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

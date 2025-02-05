package com.ms.crud_api.model.entity;

import com.ms.crud_api.infrastructure.model.entity.BaseSoftDeleteEntity;
import jakarta.persistence.*;
import org.apache.catalina.User;

@Entity
@Table(name = "payment_methods")
public class PaymentMethodEntity extends BaseSoftDeleteEntity<Long>{

    @Column(nullable = false)
    private Long cardNumber;
    @Column(nullable = false)
    private String cardHolder;
    @Column(nullable = false)
    private String expirationDate;
    @Column(nullable = false)
    private Long cvv;



    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    public PaymentMethodEntity(Long cardNumber, String cardHolder, String expirationDate, Long cvv, UserEntity user) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.expirationDate = expirationDate;
        this.cvv = cvv;
        this.user = user;
    }

    //Default constructor
    public PaymentMethodEntity() {
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


    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}

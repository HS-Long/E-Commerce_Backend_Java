package com.ms.crud_api.model.request.payment;

import java.io.Serializable;

public class PaymentRestore implements Serializable {

    private int cardNumber;

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }
}

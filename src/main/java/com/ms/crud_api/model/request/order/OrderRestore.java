package com.ms.crud_api.model.request.order;

import java.io.Serializable;

public class OrderRestore implements Serializable {

    private String customerName;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}

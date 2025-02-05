package com.ms.crud_api.infrastructure.model.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;
@MappedSuperclass
public abstract class BaseEntity <ID extends Serializable>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;


    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}

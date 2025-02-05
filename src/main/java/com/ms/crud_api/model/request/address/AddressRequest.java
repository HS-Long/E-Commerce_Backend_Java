package com.ms.crud_api.model.request.address;

import com.ms.crud_api.model.entity.AddressEntity;
import com.ms.crud_api.model.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class AddressRequest implements Serializable {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "152", maxLength = 50)
    @NotNull(message = "street is required!")
    @NotEmpty(message = "street cannot be empty!")
    @Size(max = 50, message = "street cannot be bigger than 50 characters!")
    private String street;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "Phnom Penh", maxLength = 50)
    @NotNull(message = "city is required!")
    @NotEmpty(message = "city cannot be empty!")
    @Size(max = 50, message = "city cannot be bigger than 50 characters!")
    private String city;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "245", maxLength = 50)
    @NotNull(message = "state is required!")
    @NotEmpty(message = "state cannot be empty!")
    @Size(max = 50, message = "state cannot be bigger than 50 characters!")
    private String state;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "Cambodia", maxLength = 50)
    @NotNull(message = "country is required!")
    @NotEmpty(message = "country cannot be empty!")
    @Size(max = 50, message = "country cannot be bigger than 50 characters!")
    private String country;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "12435", maxLength = 50)
    @NotNull(message = "zipCode is required!")
    @NotEmpty(message = "zipCode cannot be empty!")
    @Size(max = 50, message = "zipCode cannot be bigger than 50 characters!")
    private String zipCode;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "+85570908375", maxLength = 50)
    @NotNull(message = "phone is required!")
    @NotEmpty(message = "phone cannot be empty!")
    @Size(max = 50, message = "phone cannot be bigger than 50 characters!")
    private String phone;


    public AddressEntity toEntity(UserEntity user) {
        AddressEntity address = new AddressEntity();
        address.setStreet(this.street);
        address.setCity(this.city);
        address.setState(this.state);
        address.setCountry(this.country);
        address.setZipCode(this.zipCode);
        address.setPhone(this.phone);
        address.setUserEntity(user);

        return address;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

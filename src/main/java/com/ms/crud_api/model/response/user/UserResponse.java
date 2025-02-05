package com.ms.crud_api.model.response.user;

import com.ms.crud_api.infrastructure.model.response.BaseResponse;
import com.ms.crud_api.model.entity.UserEntity;
import com.ms.crud_api.model.response.address.AddressResponse;

public class UserResponse extends BaseResponse {

    private final Long id;

    private final String username;

    private final String email;

    private final String role;

    private AddressResponse address;

    public UserResponse(Long id, String username, String email, String role, AddressResponse address) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.address = address;
    }



    public static UserResponse fromEntity(UserEntity entity) {
        AddressResponse addr;
        if (entity.getAddress() == null)
            addr = null;
        else addr = new AddressResponse(entity.getAddress().getId(), entity.getAddress().getStreet() , entity.getAddress().getCity(),entity.getAddress().getCountry(),entity.getAddress().getZipCode() ,entity.getAddress().getPhone(), entity.getAddress().getState());
        return new UserResponse(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getRole(),
                addr
        );
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public AddressResponse getAddress() {
        return address;
    }

    public void setAddress(AddressResponse address) {
        this.address = address;
    }
}

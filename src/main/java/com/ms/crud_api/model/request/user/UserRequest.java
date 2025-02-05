package com.ms.crud_api.model.request.user;

import com.ms.crud_api.model.entity.UserEntity;
import com.ms.crud_api.model.request.address.AddressRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class UserRequest implements Serializable {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "John Doe", maxLength = 50)
    @NotNull(message = "Name is required!")
    @NotEmpty(message = "Name cannot be empty!")
    @Size(max = 50, message = "Name cannot be bigger than 50 characters!")
    private String username;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "JohnDoe@gmail.com", maxLength = 50)
    @NotNull(message = "email is required!")
    @NotEmpty(message = "email cannot be empty!")
    @Size(max = 50, message = "Name cannot be bigger than 50 characters!")
    private String email;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "admin", maxLength = 10)
    @NotNull(message = "Role is required!")
    @NotEmpty(message = "Role cannot be empty!")
    @Size(max = 10, message = "Role cannot be bigger than 10 characters!")
    private String role;

    private AddressRequest addrees;


    public UserEntity toEntity() {
        UserEntity user = new UserEntity();
        user.setUsername(this.username);
        user.setEmail(this.email);
        user.setRole(this.role);
        user.setAddress(this.addrees.toEntity(user));
        return user;
    }

    public AddressRequest getAddrees() {
        return addrees;
    }

    public void setAddrees(AddressRequest addrees) {
        this.addrees = addrees;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

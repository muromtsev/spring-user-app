package org.pozvezd.springuserapi.model.dto.request;


import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class CreateUserRequest {

    @NotBlank(message = "FIO is mandatory")
    private String fio;

    private String phoneNumber;
    private String avatar;

    @NotBlank(message = "Role is mandatory")
    private String roleName;

    public CreateUserRequest() {
    }

    public CreateUserRequest(String fio, String phoneNumber, String avatar, String roleName) {
        this.fio = fio;
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
        this.roleName = roleName;
    }

    public @NotBlank(message = "FIO is mandatory") String getFio() {
        return fio;
    }

    public void setFio(@NotBlank(message = "FIO is mandatory") String fio) {
        this.fio = fio;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public @NotBlank(message = "Role is mandatory") String getRoleName() {
        return roleName;
    }

    public void setRoleName(@NotBlank(message = "Role is mandatory") String roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateUserRequest that = (CreateUserRequest) o;
        return Objects.equals(fio, that.fio) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(avatar, that.avatar) && Objects.equals(roleName, that.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fio, phoneNumber, avatar, roleName);
    }

    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "fio='" + fio + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", avatar='" + avatar + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}

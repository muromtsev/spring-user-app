package org.pozvezd.springuserapi.model.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;
import java.util.UUID;

public class UpdateUserRequest {

    @NotNull(message = "User UUID is mandatory")
    private UUID userUuid;

    private String fio;
    private String phoneNumber;
    private String avatar;
    private String roleName;

    public UpdateUserRequest() {
    }

    public UpdateUserRequest(UUID userUuid, String fio, String phoneNumber, String avatar, String roleName) {
        this.userUuid = userUuid;
        this.fio = fio;
        this.phoneNumber = phoneNumber;
        this.avatar = avatar;
        this.roleName = roleName;
    }

    public @NotNull(message = "User UUID is mandatory") UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(@NotNull(message = "User UUID is mandatory") UUID userUuid) {
        this.userUuid = userUuid;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UpdateUserRequest that = (UpdateUserRequest) o;
        return Objects.equals(userUuid, that.userUuid) && Objects.equals(fio, that.fio) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(avatar, that.avatar) && Objects.equals(roleName, that.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userUuid, fio, phoneNumber, avatar, roleName);
    }

    @Override
    public String toString() {
        return "UpdateUserRequest{" +
                "userUuid=" + userUuid +
                ", fio='" + fio + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", avatar='" + avatar + '\'' +
                ", roleName='" + roleName + '\'' +
                '}';
    }
}

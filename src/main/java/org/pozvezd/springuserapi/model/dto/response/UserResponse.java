package org.pozvezd.springuserapi.model.dto.response;

import java.util.Objects;
import java.util.UUID;

public class UserResponse {

    private UUID uuid;
    private String fio;
    private String phoneNumber;
    private String avatar;
    private RoleResponse role;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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

    public RoleResponse getRole() {
        return role;
    }

    public void setRole(RoleResponse role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponse that = (UserResponse) o;
        return Objects.equals(uuid, that.uuid) && Objects.equals(fio, that.fio) && Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(avatar, that.avatar) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, fio, phoneNumber, avatar, role);
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "uuid=" + uuid +
                ", fio='" + fio + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", avatar='" + avatar + '\'' +
                ", role=" + role +
                '}';
    }

    public static class RoleResponse {
        private UUID uuid;
        private String roleName;

        public UUID getUuid() {
            return uuid;
        }

        public void setUuid(UUID uuid) {
            this.uuid = uuid;
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
            RoleResponse that = (RoleResponse) o;
            return Objects.equals(uuid, that.uuid) && Objects.equals(roleName, that.roleName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(uuid, roleName);
        }

        @Override
        public String toString() {
            return "RoleResponse{" +
                    "uuid=" + uuid +
                    ", roleName='" + roleName + '\'' +
                    '}';
        }
    }

}

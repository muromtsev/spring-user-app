package org.pozvezd.springuserapi.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(columnDefinition = "UUID DEFAULT get_random_uuid()", updatable = false, nullable = false)
    private UUID uuid;

    @NotBlank
    @Column(nullable = false)
    private String fio;
    private String phoneNumber;
    private String avatarUrl;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_uuid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RoleEntity role;

    public UUID getUuid() {
        return uuid;
    }

    public @NotBlank String getFio() {
        return fio;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public RoleEntity getRole() {
        return role;
    }

    public void setFio(@NotBlank String fio) {
        this.fio = fio;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

}

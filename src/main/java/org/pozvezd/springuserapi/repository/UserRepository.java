package org.pozvezd.springuserapi.repository;

import org.pozvezd.springuserapi.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);

    @Modifying
    @Query("DELETE FROM UserEntity u WHERE u.uuid = :uuid")
    void deleteByUuid(@Param("uuid") UUID uuid);

}

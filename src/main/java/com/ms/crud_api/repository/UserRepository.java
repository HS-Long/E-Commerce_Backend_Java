package com.ms.crud_api.repository;

import com.ms.crud_api.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
    boolean existsByUsernameAndDeletedAtIsNull(String firstName);

    Optional<UserEntity> findByIdAndDeletedAtIsNull(Long id);

    Optional<UserEntity> findByIdAndDeletedAtIsNotNull(Long id);

    // find by id
    Optional<UserEntity> findById(Long id);
}

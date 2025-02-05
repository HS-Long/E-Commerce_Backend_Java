package com.ms.crud_api.repository;

import com.ms.crud_api.model.entity.OrderEntity;
import com.ms.crud_api.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long>, JpaSpecificationExecutor<OrderEntity> {

    boolean existsByCustomerNameAndDeletedAtIsNull(String CustomerName);

    Optional<OrderEntity> findByIdAndDeletedAtIsNull(Long id);

    Optional<OrderEntity> findByIdAndDeletedAtIsNotNull(Long id);

}

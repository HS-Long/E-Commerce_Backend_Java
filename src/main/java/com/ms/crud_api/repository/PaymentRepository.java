package com.ms.crud_api.repository;


import com.ms.crud_api.model.entity.PaymentMethodEntity;
import com.ms.crud_api.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentMethodEntity, Long>, JpaSpecificationExecutor<PaymentMethodEntity> {

    boolean existsBycardNumberAndDeletedAtIsNull(Long cardNumber);

    Optional<PaymentMethodEntity> findByIdAndDeletedAtIsNull(Long id);

    Optional<PaymentMethodEntity> findByIdAndDeletedAtIsNotNull(Long id);

    // find by id
    Optional<PaymentMethodEntity> findById(Long id);

}

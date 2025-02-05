package com.ms.crud_api.repository;


import com.ms.crud_api.model.entity.OrderEntity;
import com.ms.crud_api.model.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long>, JpaSpecificationExecutor<ReviewEntity> {


    Optional<ReviewEntity> findByIdAndDeletedAtIsNull(Long id);

    Optional<ReviewEntity> findByIdAndDeletedAtIsNotNull(Long id);


}

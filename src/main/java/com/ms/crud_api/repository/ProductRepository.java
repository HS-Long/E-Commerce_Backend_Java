package com.ms.crud_api.repository;

import com.ms.crud_api.model.entity.CategoryEntity;
import com.ms.crud_api.model.entity.PaymentMethodEntity;
import com.ms.crud_api.model.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>, JpaSpecificationExecutor<ProductEntity> {
    boolean existsBynameAndDeletedAtIsNull(String name);

    Optional<ProductEntity> findByIdAndDeletedAtIsNull(Long id);


    List<ProductEntity> findByCategoryEntity(CategoryEntity foundData);
}

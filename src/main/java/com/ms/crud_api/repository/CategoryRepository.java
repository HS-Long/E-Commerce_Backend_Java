package com.ms.crud_api.repository;

import com.ms.crud_api.model.entity.CategoryEntity;
import com.ms.crud_api.model.entity.PaymentMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long>, JpaSpecificationExecutor<CategoryEntity> {

    boolean existsBynameAndDeletedAtIsNull(String name);

    Optional<CategoryEntity> findByIdAndDeletedAtIsNull(Long id);
    Optional<CategoryEntity> findByIdAndDeletedAtIsNotNull(Long id);

}

package com.ms.crud_api.repository;


import com.ms.crud_api.model.entity.FileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    Optional<FileEntity> findByIdAndDeletedAtIsNull(Long id);

    Optional<FileEntity> findByIdAndDeletedAtIsNotNull(Long id);

    Page<FileEntity> findAllByOriginalNameContainsAndDeletedAtIsNull(String q, Pageable pageable);

    Page<FileEntity> findAllByOriginalNameContainsAndDeletedAtIsNotNull(String q, Pageable pageable);

}

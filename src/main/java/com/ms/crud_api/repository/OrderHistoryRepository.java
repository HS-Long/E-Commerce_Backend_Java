package com.ms.crud_api.repository;

import com.ms.crud_api.model.entityHistory.OrderHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHistoryRepository extends JpaRepository<OrderHistoryEntity, Long> {
}

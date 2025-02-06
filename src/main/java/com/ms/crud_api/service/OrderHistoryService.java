package com.ms.crud_api.service;

import com.ms.crud_api.model.entityHistory.OrderHistoryEntity;
import com.ms.crud_api.repository.OrderHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderHistoryService {
    
    private final OrderHistoryRepository orderHistoryRepository;

    public OrderHistoryService(OrderHistoryRepository orderHistoryRepository) {
        this.orderHistoryRepository = orderHistoryRepository;
    }

    // Create the OrderHistoryEntity
    @Transactional
    public OrderHistoryEntity create(OrderHistoryEntity entity) throws Exception {
        try {
            return this.orderHistoryRepository.save(entity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    // Update the OrderHistoryEntity
    @Transactional
    public OrderHistoryEntity update(OrderHistoryEntity entity) throws Exception {
        try {
            return this.orderHistoryRepository.save(entity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    // Delete the OrderHistoryEntity
    @Transactional
    public void delete(Long id) throws Exception {
        try {
            this.orderHistoryRepository.deleteById(id);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }
    
    // restore the OrderHistoryEntity
    @Transactional
    public void restore(Long id) throws Exception {
        try {
            this.orderHistoryRepository.getById(id);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }



}

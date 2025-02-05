package com.ms.crud_api.service;

import com.ms.crud_api.exception.AlreadyExistException;
import com.ms.crud_api.exception.BadRequestException;
import com.ms.crud_api.exception.NotFoundException;
import com.ms.crud_api.model.entity.PaymentMethodEntity;
import com.ms.crud_api.model.entity.UserEntity;
import com.ms.crud_api.model.request.payment.PaymentRequest;
import com.ms.crud_api.model.request.payment.PaymentRestore;
import com.ms.crud_api.repository.PaymentRepository;
import com.ms.crud_api.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final UserRepository userRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    public PaymentMethodEntity create(PaymentRequest request) throws Exception {

        UserEntity user = userRepository.findById(Long.valueOf(request.getUser())).orElseThrow(() -> new NotFoundException("User Not found"));
        // prepare request to entity
        PaymentMethodEntity data = request.toEntity(user);

        // check name from request exists or not in db
        if (this.paymentRepository.existsBycardNumberAndDeletedAtIsNull(data.getCardNumber()))
            throw new AlreadyExistException("Cart name already exists!");

        try {
            // save entity
            return this.paymentRepository.save(data);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    public PaymentMethodEntity findOne(Long id) throws NotFoundException {
        return this.paymentRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public PaymentMethodEntity update(Long id, PaymentRequest request) throws Exception {
        // check from data from database and if it isn't exist then throw error
        PaymentMethodEntity foundData = this.findOne(id);

        // check name from request exists or not in db
//        if (!Objects.equals(foundData.getFirstName(), request.getFirstName()))
        if (this.paymentRepository.existsBycardNumberAndDeletedAtIsNull(request.getCardNumber()))
            throw new AlreadyExistException("User name already exists!");

        foundData.setCardNumber(request.getCardNumber());
        foundData.setCardHolder(request.getCardHolder());
        foundData.setExpirationDate(request.getExpirationDate());
        foundData.setCvv(request.getCvv());

        try {
            // update entity
            return this.paymentRepository.save(foundData);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    public Page<PaymentMethodEntity> findAll(int page, int limit, boolean isPage, String sort, boolean isTrash, Map<String, String> reqParam) throws BadRequestException {
        if (page <= 0 || limit <= 0) throw new BadRequestException("Invalid pagination!");

        List<Sort.Order> sortByList = new ArrayList<>();
        for (String item : sort.split(",")) {
            String[] srt = item.split(":");
            if (srt.length != 2) continue;

            String direction = srt[1].toLowerCase();
            String field = srt[0];

            sortByList.add(new Sort.Order(direction.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, field));
        }

        Pageable pageable;
        if (isPage) pageable = PageRequest.of(page - 1, limit, Sort.by(sortByList));
        else pageable = Pageable.unpaged();

        return this.paymentRepository.findAll((Specification<PaymentMethodEntity>) (root, query, criteriaBuilder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, String> entry : reqParam.entrySet()) {
                if (entry.getKey().startsWith("q_")) {
                    String qKey = entry.getKey().split("q_", 2)[1];
                    String qValue = entry.getValue() == null ? "" : entry.getValue();
                    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get(qKey).as(String.class)), "%" + qValue.toUpperCase() + "%"));
                }
            }

            if (predicates.size() == 0) predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("cardNumber").as(String.class)), "%" + "" + "%"));

            return criteriaBuilder.and(
                    isTrash ? criteriaBuilder.isNotNull(root.get("deletedAt")) : criteriaBuilder.isNull(root.get("deletedAt")), criteriaBuilder.or(predicates.toArray(new Predicate[0]))
            );
        }, pageable);
    }

    public PaymentMethodEntity delete(Long id) throws Exception {
        PaymentMethodEntity paymentMethodEntity = this.findOne(id);

        paymentMethodEntity.setDeletedAt(new Date());

        try {
            return this.paymentRepository.save(paymentMethodEntity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }


    public PaymentMethodEntity findOneWithSoftDeleted(Long id) throws NotFoundException {
        return this.paymentRepository.findByIdAndDeletedAtIsNotNull(id).orElseThrow(() -> new NotFoundException("Cart not found"));
    }

    public PaymentMethodEntity restore(Long id, PaymentRestore req) throws Exception {
        // get category data from db by id
        PaymentMethodEntity paymentMethodEntity = this.findOneWithSoftDeleted(id);

        // check name from request exists or not in db
        if (this.paymentRepository.existsBycardNumberAndDeletedAtIsNull((long)req.getCardNumber()))
            throw new AlreadyExistException("Username has already exits!");

        // move deleted_at field to null value
        paymentMethodEntity.setDeletedAt(null);
        paymentMethodEntity.setCardNumber((long)req.getCardNumber());                      // I use long because i want to case int to long value

        try {
            return this.paymentRepository.save(paymentMethodEntity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }

    }

    public PaymentMethodEntity deleteFromTrash(Long id) throws Exception {
        PaymentMethodEntity paymentMethodEntity = this.findOneWithSoftDeleted(id);

        try {
            this.paymentRepository.delete(paymentMethodEntity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return paymentMethodEntity;
    }

    public PaymentMethodEntity ForceDelete(Long id) throws Exception {
        PaymentMethodEntity paymentMethodEntity = this.paymentRepository.findById(id).orElseThrow(() -> new NotFoundException("Cart not found"));

        try {
            this.paymentRepository.delete(paymentMethodEntity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return paymentMethodEntity;
    }






}

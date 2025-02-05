package com.ms.crud_api.service;

import com.ms.crud_api.exception.AlreadyExistException;
import com.ms.crud_api.exception.BadRequestException;
import com.ms.crud_api.exception.NotFoundException;
import com.ms.crud_api.model.entity.PaymentMethodEntity;
import com.ms.crud_api.model.entity.ProductEntity;
import com.ms.crud_api.model.entity.ReviewEntity;
import com.ms.crud_api.model.entity.UserEntity;
import com.ms.crud_api.model.request.payment.PaymentRequest;
import com.ms.crud_api.model.request.payment.PaymentRestore;
import com.ms.crud_api.model.request.review.ReviewRequest;
import com.ms.crud_api.model.request.user.UserRequest;
import com.ms.crud_api.repository.ProductRepository;
import com.ms.crud_api.repository.ReviewRepository;
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
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }




    public ReviewEntity create(ReviewRequest request) throws Exception {

        UserEntity user = userRepository.findById(Long.valueOf(request.getUserId())).orElseThrow(() -> new NotFoundException("User Not found"));
        ProductEntity product = productRepository.findById(Long.valueOf(request.getProductId())).orElseThrow(() -> new NotFoundException("Product Not found"));
        // prepare request to entity
        ReviewEntity data = request.toEntity(product , user);

        // check name from request exists or not in db

        try {
            // save entity
            return this.reviewRepository.save(data);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }


    public ReviewEntity findOne(Long id) throws NotFoundException {
        return this.reviewRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new NotFoundException("Review not found"));
    }

    public ReviewEntity update(Long id, ReviewRequest request) throws Exception {
        // check from data from database and if it isn't exist then throw error
        ReviewEntity foundData = this.findOne(id);

        foundData.setRating(request.getRating());
        foundData.setComment(request.getComment());

        try {
            // update entity
            return this.reviewRepository.save(foundData);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    public Page<ReviewEntity> findAll(int page, int limit, boolean isPage, String sort, boolean isTrash, Map<String, String> reqParam) throws BadRequestException {
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

        return this.reviewRepository.findAll((Specification<ReviewEntity>) (root, query, criteriaBuilder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, String> entry : reqParam.entrySet()) {
                if (entry.getKey().startsWith("q_")) {
                    String qKey = entry.getKey().split("q_", 2)[1];
                    String qValue = entry.getValue() == null ? "" : entry.getValue();
                    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get(qKey).as(String.class)), "%" + qValue.toUpperCase() + "%"));
                }
            }

            if (predicates.size() == 0) predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("comment").as(String.class)), "%" + "" + "%"));

            return criteriaBuilder.and(
                    isTrash ? criteriaBuilder.isNotNull(root.get("deletedAt")) : criteriaBuilder.isNull(root.get("deletedAt")), criteriaBuilder.or(predicates.toArray(new Predicate[0]))
            );
        }, pageable);
    }

//    public ReviewEntity delete(Long id) throws Exception {
//        ReviewEntity reviewEntity = this.findOne(id);
//
//        reviewEntity.setDeletedAt(new Date());
//
//        try {
//            return this.reviewRepository.save(reviewEntity);
//        } catch (Exception ex) {
//            throw new Exception(ex);
//        }
//    }


    public ReviewEntity findOneWithSoftDeleted(Long id) throws NotFoundException {
        return this.reviewRepository.findByIdAndDeletedAtIsNotNull(id).orElseThrow(() -> new NotFoundException("Cart not found"));
    }
//
//    public PaymentMethodEntity restore(Long id, PaymentRestore req) throws Exception {
//        // get category data from db by id
//        PaymentMethodEntity paymentMethodEntity = this.findOneWithSoftDeleted(id);
//
//        // check name from request exists or not in db
//        if (this.paymentRepository.existsBycardNumberAndDeletedAtIsNull((long)req.getCardNumber()))
//            throw new AlreadyExistException("Username has already exits!");
//
//        // move deleted_at field to null value
//        paymentMethodEntity.setDeletedAt(null);
//        paymentMethodEntity.setCardNumber((long)req.getCardNumber());                      // I use long because i want to case int to long value
//
//        try {
//            return this.paymentRepository.save(paymentMethodEntity);
//        } catch (Exception ex) {
//            throw new Exception(ex);
//        }
//
//    }

//    public PaymentMethodEntity deleteFromTrash(Long id) throws Exception {
//        PaymentMethodEntity paymentMethodEntity = this.findOneWithSoftDeleted(id);
//
//        try {
//            this.paymentRepository.delete(paymentMethodEntity);
//        } catch (Exception ex) {
//            throw new Exception(ex);
//        }
//        return paymentMethodEntity;
//    }

    public ReviewEntity ForceDelete(Long id) throws Exception {
        ReviewEntity reviewEntity = this.reviewRepository.findById(id).orElseThrow(() -> new NotFoundException("Cart not found"));

        try {
            this.reviewRepository.delete(reviewEntity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return reviewEntity;
    }


}

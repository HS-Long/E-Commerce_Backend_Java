package com.ms.crud_api.service;

import com.ms.crud_api.exception.AlreadyExistException;
import com.ms.crud_api.exception.BadRequestException;
import com.ms.crud_api.exception.NotFoundException;
import com.ms.crud_api.model.entity.OrderEntity;
import com.ms.crud_api.model.entity.OrderItemEntity;
import com.ms.crud_api.model.entity.ProductEntity;
import com.ms.crud_api.model.entity.UserEntity;
import com.ms.crud_api.model.request.order.OrderRequest;
import com.ms.crud_api.model.request.order.OrderRestore;
import com.ms.crud_api.model.request.orderItem.OrderItemRequest;
import com.ms.crud_api.repository.OrderItemRepository;
import com.ms.crud_api.repository.OrderRepository;
import com.ms.crud_api.repository.ProductRepository;
import com.ms.crud_api.repository.UserRepository;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public OrderEntity create(OrderRequest request) throws Exception {
        UserEntity user = userRepository.findById(Long.valueOf(request.getUserId()))
                .orElseThrow(() -> new NotFoundException("User Not found"));

        List<OrderItemRequest> orderItems = request.getOrderItem();
        List<OrderItemEntity> orderItemEntities = new ArrayList<>();
        double totalAmount = 0;

        for (OrderItemRequest orderItemRequest : orderItems) {
            ProductEntity product = productRepository.findById(Long.valueOf(orderItemRequest.getProductId()))
                    .orElseThrow(() -> new NotFoundException("Product Not found"));

            if (product.getStockQuantity() < orderItemRequest.getQuantity()) {
                throw new BadRequestException("Not enough stock for product: " + product.getName());
            }

            product.setStockQuantity(product.getStockQuantity() - orderItemRequest.getQuantity());
            productRepository.save(product);

            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setProductEntity(product);
            orderItemEntity.setQuantity(orderItemRequest.getQuantity());
            orderItemEntities.add(orderItemEntity);

            totalAmount += product.getPrice() * orderItemRequest.getQuantity();
        }

        OrderEntity data = request.toEntity(user, orderItemEntities);
        data.setTotalAmount(totalAmount);

        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            orderItemEntity.setOrder(data);
        }

//        if (this.orderRepository.existsByCustomerNameAndDeletedAtIsNull(data.getCustomerName()))
//            throw new AlreadyExistException("Order name already exists!");

        try {
            return this.orderRepository.save(data);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    public OrderEntity findOne(Long id) throws NotFoundException {
        return this.orderRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new NotFoundException("Order not found"));
    }



    public OrderEntity update(Long id, OrderRequest request) throws Exception {
        OrderEntity foundData = this.findOne(id);

        for (OrderItemEntity existingOrderItem : foundData.getOrderItemEntity()) {
            ProductEntity product = existingOrderItem.getProductEntity();
            product.setStockQuantity(product.getStockQuantity() + existingOrderItem.getQuantity());
            productRepository.save(product);
        }

        UserEntity user = userRepository.findById(Long.valueOf(request.getUserId()))
                .orElseThrow(() -> new NotFoundException("User Not found"));

        List<OrderItemRequest> orderItems = request.getOrderItem();
        List<OrderItemEntity> orderItemEntities = new ArrayList<>();
        double totalAmount = 0;

        for (OrderItemRequest orderItemRequest : orderItems) {
            ProductEntity product = productRepository.findById(Long.valueOf(orderItemRequest.getProductId()))
                    .orElseThrow(() -> new NotFoundException("Product Not found"));

            if (product.getStockQuantity() < orderItemRequest.getQuantity()) {
                throw new BadRequestException("Not enough stock for product: " + product.getName());
            }

            product.setStockQuantity(product.getStockQuantity() - orderItemRequest.getQuantity());
            productRepository.save(product);

            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setProductEntity(product);
            orderItemEntity.setQuantity(orderItemRequest.getQuantity());
            orderItemEntities.add(orderItemEntity);

            totalAmount += product.getPrice() * orderItemRequest.getQuantity();
        }

        foundData.setStatus(request.getStatus());
        foundData.setPaymentMethod(request.getPaymentMethod());
        foundData.setShippingAddress(request.getShippingAddress());
        foundData.setCustomerName(request.getCustomerName());
        foundData.setUserId(user);
        foundData.setOrderItemEntity(new HashSet<>(orderItemEntities));
        foundData.setTotalAmount(totalAmount);

        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            orderItemEntity.setOrder(foundData);
        }

        try {
            return this.orderRepository.save(foundData);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    public Page<OrderEntity> findAll(int page, int limit, boolean isPage, String sort, boolean isTrash, Map<String, String> reqParam) throws BadRequestException {
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

        return this.orderRepository.findAll((Specification<OrderEntity>) (root, query, criteriaBuilder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, String> entry : reqParam.entrySet()) {
                if (entry.getKey().startsWith("q_")) {
                    String qKey = entry.getKey().split("q_", 2)[1];
                    String qValue = entry.getValue() == null ? "" : entry.getValue();
                    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get(qKey).as(String.class)), "%" + qValue.toUpperCase() + "%"));
                }
            }

            if (predicates.size() == 0) predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("customerName").as(String.class)), "%" + "" + "%"));

            return criteriaBuilder.and(
                    isTrash ? criteriaBuilder.isNotNull(root.get("deletedAt")) : criteriaBuilder.isNull(root.get("deletedAt")), criteriaBuilder.or(predicates.toArray(new Predicate[0]))
            );
        }, pageable);
    }

    public OrderEntity forceDelete(Long id) throws Exception {
        OrderEntity orderEntity = this.orderRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new NotFoundException("Order not found"));

        try {
            this.orderRepository.delete(orderEntity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return orderEntity;
    }
    public OrderEntity findOneWithSoftDeleted(Long id) throws NotFoundException {
        return this.orderRepository.findByIdAndDeletedAtIsNotNull(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public OrderEntity restore(Long id, OrderRestore req) throws Exception {
        // get category data from db by id
        OrderEntity orderEntity = this.findOneWithSoftDeleted(id);

        // check name from request exists or not in db
        if (this.orderRepository.existsByCustomerNameAndDeletedAtIsNull(req.getCustomerName()))
            throw new AlreadyExistException("Username has already exits!");

        // move deleted_at field to null value
        orderEntity.setDeletedAt(null);
        orderEntity.setCustomerName(req.getCustomerName());

        try {
            return this.orderRepository.save(orderEntity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }

    }

    public OrderEntity delete(Long id) throws Exception {
        OrderEntity orderEntity = this.findOne(id);

        orderEntity.setDeletedAt(new Date());

        try {
            return this.orderRepository.save(orderEntity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }
    public OrderEntity deleteFromTrash(Long id) throws Exception {
        OrderEntity orderEntity = this.findOneWithSoftDeleted(id);

        try {
            this.orderRepository.delete(orderEntity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return orderEntity;
    }


}
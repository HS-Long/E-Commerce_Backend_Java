package com.ms.crud_api.service;

import com.ms.crud_api.constant.enums.CrudTypeEnum;
import com.ms.crud_api.exception.AlreadyExistException;
import com.ms.crud_api.exception.BadRequestException;
import com.ms.crud_api.exception.NotFoundException;
import com.ms.crud_api.model.entity.OrderEntity;
import com.ms.crud_api.model.entity.OrderItemEntity;
import com.ms.crud_api.model.entity.ProductEntity;
import com.ms.crud_api.model.entity.UserEntity;
import com.ms.crud_api.model.entityHistory.OrderHistoryEntity;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;

    private final OrderHistoryService orderHistoryService;

    @Autowired
    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, UserRepository userRepository, ProductRepository productRepository, OrderHistoryService orderHistoryService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderHistoryService = orderHistoryService;
    }

    @Transactional(timeout = 10, rollbackFor = {Exception.class}, noRollbackFor = {AlreadyExistException.class})
    public OrderEntity create(OrderRequest request) throws Exception {
        // Find the user by ID
        UserEntity user = userRepository.findById(Long.valueOf(request.getUserId()))
                .orElseThrow(() -> new NotFoundException("User Not found"));

        // Prepare order items and calculate total amount
        List<OrderItemRequest> orderItems = request.getOrderItem();
        List<OrderItemEntity> orderItemEntities = new ArrayList<>();
        double totalAmount = 0;

        for (OrderItemRequest orderItemRequest : orderItems) {
            // Find the product by ID
            ProductEntity product = productRepository.findById(Long.valueOf(orderItemRequest.getProductId()))
                    .orElseThrow(() -> new NotFoundException("Product Not found"));
            // Check if there is enough stock for the product
            if (product.getStockQuantity() < orderItemRequest.getQuantity()) {
                throw new BadRequestException("Not enough stock for product: " + product.getName());
            }
            // Update the product stock quantity
            product.setStockQuantity(product.getStockQuantity() - orderItemRequest.getQuantity());
            productRepository.save(product);
            // Create order item entity
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setProductEntity(product);
            orderItemEntity.setQuantity(orderItemRequest.getQuantity());
            orderItemEntities.add(orderItemEntity);
            // Calculate total amount
            totalAmount += product.getPrice() * orderItemRequest.getQuantity();
        }

        // Create order entity
        OrderEntity data = request.toEntity(user, orderItemEntities);
        data.setTotalAmount(totalAmount);
        OrderHistoryEntity orderHistoryEntity = new OrderHistoryEntity();
        orderHistoryEntity.setTotalAmount(data.getTotalAmount());
        orderHistoryEntity.setStatus(data.getStatus());
        orderHistoryEntity.setPaymentMethod(data.getPaymentMethod());
        orderHistoryEntity.setShippingAddress(data.getShippingAddress());
        orderHistoryEntity.setCustomerName(data.getCustomerName());
        orderHistoryEntity.setUserId(data.getUserId());
        orderHistoryEntity.setType(CrudTypeEnum.CREATE);

        orderHistoryEntity.setOrder(data);
        // Set order reference in order items
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            orderItemEntity.setOrder(data);
        }
        // Check if order with the same customer name already exists
//        if (this.orderRepository.existsByCustomerNameAndDeletedAtIsNull(data.getCustomerName()))
//            throw new AlreadyExistException("Order name already exists!");

        try {
            // Save the order entity
            OrderEntity saveData =  this.orderRepository.save(data);
            // Create order history
            orderHistoryService.create(orderHistoryEntity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return data;
    }



    public OrderEntity findOne(Long id) throws NotFoundException {
        return this.orderRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new NotFoundException("Order not found"));
    }


    @Transactional(timeout = 10, rollbackFor = {Exception.class}, noRollbackFor = {AlreadyExistException.class})
    public OrderEntity update(Long id, OrderRequest request) throws Exception {

        // Find the order by ID
        OrderEntity foundData = this.findOne(id);

        // Restore stock quantities for existing order items
        for (OrderItemEntity existingOrderItem : foundData.getOrderItemEntity()) {
            ProductEntity product = existingOrderItem.getProductEntity();
            product.setStockQuantity(product.getStockQuantity() + existingOrderItem.getQuantity());
            productRepository.save(product);
        }

        // Find the user by ID
        UserEntity user = userRepository.findById(Long.valueOf(request.getUserId()))
                .orElseThrow(() -> new NotFoundException("User Not found"));

        // Prepare new order items and calculate total amount
        List<OrderItemRequest> orderItems = request.getOrderItem();
        List<OrderItemEntity> orderItemEntities = new ArrayList<>();
        double totalAmount = 0;

        for (OrderItemRequest orderItemRequest : orderItems) {
            // Find the product by ID
            ProductEntity product = productRepository.findById(Long.valueOf(orderItemRequest.getProductId()))
                    .orElseThrow(() -> new NotFoundException("Product Not found"));

            // Check if there is enough stock for the product
            if (product.getStockQuantity() < orderItemRequest.getQuantity()) {
                throw new BadRequestException("Not enough stock for product: " + product.getName());
            }

            // Update the product stock quantity
            product.setStockQuantity(product.getStockQuantity() - orderItemRequest.getQuantity());
            productRepository.save(product);

            // Create order item entity
            OrderItemEntity orderItemEntity = new OrderItemEntity();
            orderItemEntity.setProductEntity(product);
            orderItemEntity.setQuantity(orderItemRequest.getQuantity());
            orderItemEntities.add(orderItemEntity);

            // Calculate total amount
            totalAmount += product.getPrice() * orderItemRequest.getQuantity();
        }

        // Update order details
        foundData.setStatus(request.getStatus());
        foundData.setPaymentMethod(request.getPaymentMethod());
        foundData.setShippingAddress(request.getShippingAddress());
        foundData.setCustomerName(request.getCustomerName());
        foundData.setUserId(user);
        foundData.setTotalAmount(totalAmount);

        // Clear existing order items and add new ones
        foundData.getOrderItemEntity().clear();
        foundData.getOrderItemEntity().addAll(orderItemEntities);

        // Set order reference in order items
        for (OrderItemEntity orderItemEntity : orderItemEntities) {
            orderItemEntity.setOrder(foundData);
        }

        // Create order history entity
        OrderHistoryEntity orderHistoryEntity = new OrderHistoryEntity();
        orderHistoryEntity.setTotalAmount(foundData.getTotalAmount());
        orderHistoryEntity.setStatus(foundData.getStatus());
        orderHistoryEntity.setPaymentMethod(foundData.getPaymentMethod());
        orderHistoryEntity.setShippingAddress(foundData.getShippingAddress());
        orderHistoryEntity.setCustomerName(foundData.getCustomerName());
        orderHistoryEntity.setUserId(foundData.getUserId());
        orderHistoryEntity.setType(CrudTypeEnum.UPDATE);
        orderHistoryEntity.setOrder(foundData);

        try {

            // Save the updated order entity
            OrderEntity updatedOrder = this.orderRepository.save(foundData);
            // Create order history
            orderHistoryService.create(orderHistoryEntity);
            return updatedOrder;
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
    @Transactional(timeout = 10, rollbackFor = {Exception.class}, noRollbackFor = {AlreadyExistException.class})
    public OrderEntity restore(Long id, OrderRestore req) throws Exception {
        OrderEntity orderEntity = this.findOneWithSoftDeleted(id);

        if (this.orderRepository.existsByCustomerNameAndDeletedAtIsNull(req.getCustomerName()))
            throw new AlreadyExistException("Username has already exits!");

        orderEntity.setDeletedAt(null);
        orderEntity.setCustomerName(req.getCustomerName());

        // Create order history entity
        OrderHistoryEntity orderHistoryEntity = new OrderHistoryEntity();
        orderHistoryEntity.setTotalAmount(orderEntity.getTotalAmount());
        orderHistoryEntity.setStatus(orderEntity.getStatus());
        orderHistoryEntity.setPaymentMethod(orderEntity.getPaymentMethod());
        orderHistoryEntity.setShippingAddress(orderEntity.getShippingAddress());
        orderHistoryEntity.setCustomerName(orderEntity.getCustomerName());
        orderHistoryEntity.setUserId(orderEntity.getUserId());
        orderHistoryEntity.setType(CrudTypeEnum.RESTORE);
        orderHistoryEntity.setOrder(orderEntity);

        try {
            OrderEntity restoredOrder = this.orderRepository.save(orderEntity);
            orderHistoryService.create(orderHistoryEntity);
            return restoredOrder;
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }
    @Transactional(timeout = 10, rollbackFor = {Exception.class}, noRollbackFor = {AlreadyExistException.class})
    public OrderEntity delete(Long id) throws Exception {

        // Find OrderIEntity exits or not in db
        OrderEntity orderEntity = this.findOne(id);

        // If exit set to new Date
        orderEntity.setDeletedAt(new Date());


        // Create order history
        OrderHistoryEntity orderHistoryEntity = new OrderHistoryEntity();
        orderHistoryEntity.setTotalAmount(orderEntity.getTotalAmount());
        orderHistoryEntity.setStatus(orderEntity.getStatus());
        orderHistoryEntity.setPaymentMethod(orderEntity.getPaymentMethod());
        orderHistoryEntity.setShippingAddress(orderEntity.getShippingAddress());
        orderHistoryEntity.setCustomerName(orderEntity.getCustomerName());
        orderHistoryEntity.setUserId(orderEntity.getUserId());
        orderHistoryEntity.setType(CrudTypeEnum.DELETE);
        orderHistoryEntity.setOrder(orderEntity);

        try {
            OrderEntity deletedOrder = this.orderRepository.save(orderEntity);
            orderHistoryService.create(orderHistoryEntity);
            return deletedOrder;
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
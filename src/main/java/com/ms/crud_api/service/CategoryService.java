package com.ms.crud_api.service;

import com.ms.crud_api.exception.AlreadyExistException;
import com.ms.crud_api.exception.BadRequestException;
import com.ms.crud_api.exception.NotFoundException;
import com.ms.crud_api.model.entity.CategoryEntity;
import com.ms.crud_api.model.entity.OrderEntity;
import com.ms.crud_api.model.entity.ProductEntity;
import com.ms.crud_api.model.request.category.CategoryRequest;
import com.ms.crud_api.model.request.category.CategoryRestore;
import com.ms.crud_api.model.request.product.ProductRequest;
import com.ms.crud_api.repository.CategoryRepository;
import com.ms.crud_api.repository.ProductRepository;
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
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public CategoryEntity create(CategoryRequest request) throws Exception {
        List<ProductRequest> productRequests = request.getProducts();
        List<ProductEntity> productEntities = new ArrayList<>();

        CategoryEntity category = request.toEntity(productEntities);

        for (ProductRequest productRequest : productRequests) {
            ProductEntity productEntity = productRequest.toEntity(category);
            productEntities.add(productEntity);
        }

        if (this.categoryRepository.existsBynameAndDeletedAtIsNull(category.getName())) {
            throw new AlreadyExistException("Category name already exists!");
        }

        try {
            CategoryEntity savedCategory = this.categoryRepository.save(category);

            for (ProductEntity productEntity : productEntities) {
                this.productRepository.save(productEntity);
            }

            return savedCategory;
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    public CategoryEntity findOne(Long id) throws NotFoundException {
        return this.categoryRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new NotFoundException("Category not found"));
    }

    public CategoryEntity update(Long id, CategoryRequest request) throws Exception {
        // Retrieve the existing category entity from the database
        CategoryEntity foundData = this.findOne(id);

        // Check if the new category name already exists in the database
        if (!foundData.getName().equals(request.getName()) && this.categoryRepository.existsBynameAndDeletedAtIsNull(request.getName())) {
            throw new AlreadyExistException("Category name already exists!");
        }

        // Update the category entity with the new values from the request
        foundData.setName(request.getName());
        foundData.setDescription(request.getDescription());

        // Retrieve the existing product entities associated with the category
        List<ProductEntity> existingProducts = this.productRepository.findByCategoryEntity(foundData);

        // Update the existing product entities with the new values from the request
        List<ProductRequest> productRequests = request.getProducts();
        for (int i = 0; i < productRequests.size(); i++) {
            ProductRequest productRequest = productRequests.get(i);
            ProductEntity productEntity;
            if (i < existingProducts.size()) {
                productEntity = existingProducts.get(i);
            } else {
                productEntity = new ProductEntity();
                productEntity.setCategoryEntity(foundData);
                existingProducts.add(productEntity);
            }
            productEntity.setName(productRequest.getName());
            productEntity.setDescription(productRequest.getDescription());
            productEntity.setPrice(productRequest.getPrice());
            productEntity.setSizes(productRequest.getSizes());
            productEntity.setColors(productRequest.getColors());
            productEntity.setStockQuantity(productRequest.getStockQuantity());
        }

        try {
            // Save the updated category entity to the database
            CategoryEntity updatedCategory = this.categoryRepository.save(foundData);

            // Save the updated product entities to the database
            for (ProductEntity productEntity : existingProducts) {
                this.productRepository.save(productEntity);
            }

            // Return the updated category entity
            return updatedCategory;
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }



    public Page<CategoryEntity> findAll(int page, int limit, boolean isPage, String sort, boolean isTrash, Map<String, String> reqParam) throws BadRequestException {
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

        return this.categoryRepository.findAll((Specification<CategoryEntity>) (root, query, criteriaBuilder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, String> entry : reqParam.entrySet()) {
                if (entry.getKey().startsWith("q_")) {
                    String qKey = entry.getKey().split("q_", 2)[1];
                    String qValue = entry.getValue() == null ? "" : entry.getValue();
                    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get(qKey).as(String.class)), "%" + qValue.toUpperCase() + "%"));
                }
            }

            if (predicates.size() == 0) predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("name").as(String.class)), "%" + "" + "%"));

            return criteriaBuilder.and(
                    isTrash ? criteriaBuilder.isNotNull(root.get("deletedAt")) : criteriaBuilder.isNull(root.get("deletedAt")), criteriaBuilder.or(predicates.toArray(new Predicate[0]))
            );
        }, pageable);
    }

    public CategoryEntity delete(Long id) throws Exception {
        CategoryEntity categoryEntity = this.findOne(id);

        categoryEntity.setDeletedAt(new Date());

        try {
            return this.categoryRepository.save(categoryEntity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    public CategoryEntity findOneWithSoftDeleted(Long id) throws NotFoundException {
        return this.categoryRepository.findByIdAndDeletedAtIsNotNull(id).orElseThrow(() -> new NotFoundException("Category not found"));
    }

    public CategoryEntity restore(Long id, CategoryRestore req) throws Exception {
        // get category data from db by id
        CategoryEntity categoryEntity = this.findOneWithSoftDeleted(id);

        // check name from request exists or not in db
        if (this.categoryRepository.existsBynameAndDeletedAtIsNull(req.getName()))
            throw new AlreadyExistException("Category name has already exits!");

        // move deleted_at field to null value
        categoryEntity.setDeletedAt(null);
        categoryEntity.setName(req.getName());

        try {
            return this.categoryRepository.save(categoryEntity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    public CategoryEntity deleteFromTrash(Long id) throws Exception {
        CategoryEntity categoryEntity = this.findOneWithSoftDeleted(id);

        try {
            this.categoryRepository.delete(categoryEntity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return categoryEntity;
    }

    public CategoryEntity forceDelete(Long id) throws Exception {
        CategoryEntity categoryEntity = this.categoryRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new NotFoundException("Category not found"));

        try {
            this.categoryRepository.delete(categoryEntity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return categoryEntity;
    }
}
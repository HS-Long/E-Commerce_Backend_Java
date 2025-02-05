package com.ms.crud_api.service;

import com.ms.crud_api.exception.AlreadyExistException;
import com.ms.crud_api.exception.BadRequestException;
import com.ms.crud_api.exception.NotFoundException;
import com.ms.crud_api.model.entity.UserEntity;
import com.ms.crud_api.model.request.user.UserRequest;
import com.ms.crud_api.model.request.user.UserRestoreRequest;
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
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity create(UserRequest request) throws Exception {
        // prepare request to entity
        UserEntity data = request.toEntity();

        // check name from request exists or not in db
//        if (this.userRepository.existsByUsernameAndDeletedAtIsNull(data.getUsername()))
//            throw new AlreadyExistException("User name already exists!");

        try {
            // save entity
            return this.userRepository.save(data);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    public UserEntity findOne(Long id) throws NotFoundException {
        return this.userRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public UserEntity update(Long id, UserRequest request) throws Exception {
        // check from data from database and if it isn't exist then throw error
        UserEntity foundData = this.findOne(id);

        // check name from request exists or not in db
//        if (!Objects.equals(foundData.getFirstName(), request.getFirstName()))
            if (this.userRepository.existsByUsernameAndDeletedAtIsNull(request.getUsername()))
                throw new AlreadyExistException("User name already exists!");

        foundData.setEmail(request.getEmail());
        foundData.setUsername(request.getUsername());


        try {
            // update entity
            return this.userRepository.save(foundData);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    public Page<UserEntity> findAll(int page, int limit, boolean isPage, String sort, boolean isTrash, Map<String, String> reqParam) throws BadRequestException {
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

        return this.userRepository.findAll((Specification<UserEntity>) (root, query, criteriaBuilder) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, String> entry : reqParam.entrySet()) {
                if (entry.getKey().startsWith("q_")) {
                    String qKey = entry.getKey().split("q_", 2)[1];
                    String qValue = entry.getValue() == null ? "" : entry.getValue();
                    predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get(qKey).as(String.class)), "%" + qValue.toUpperCase() + "%"));
                }
            }

            if (predicates.size() == 0) predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("username").as(String.class)), "%" + "" + "%"));

            return criteriaBuilder.and(
                    isTrash ? criteriaBuilder.isNotNull(root.get("deletedAt")) : criteriaBuilder.isNull(root.get("deletedAt")), criteriaBuilder.or(predicates.toArray(new Predicate[0]))
            );
        }, pageable);

    }

    public UserEntity delete(Long id) throws Exception {
        UserEntity userEntity = this.findOne(id);

        userEntity.setDeletedAt(new Date());

        try {
            return this.userRepository.save(userEntity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
    }


    public UserEntity findOneWithSoftDeleted(Long id) throws NotFoundException {
        return this.userRepository.findByIdAndDeletedAtIsNotNull(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public UserEntity restore(Long id, UserRestoreRequest req) throws Exception {
        // get category data from db by id
        UserEntity userEntity = this.findOneWithSoftDeleted(id);

        // check name from request exists or not in db
        if (this.userRepository.existsByUsernameAndDeletedAtIsNull(req.getUsername()))
            throw new AlreadyExistException("Username has already exits!");

        // move deleted_at field to null value
        userEntity.setDeletedAt(null);
        userEntity.setUsername(req.getUsername());

        try {
            return this.userRepository.save(userEntity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }

    }

    public UserEntity deleteFromTrash(Long id) throws Exception {
        UserEntity userEntity = this.findOneWithSoftDeleted(id);

        try {
            this.userRepository.delete(userEntity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return userEntity;
    }

    public UserEntity ForceDelete(Long id) throws Exception {
        UserEntity userEntity = this.userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        try {
            this.userRepository.delete(userEntity);
        } catch (Exception ex) {
            throw new Exception(ex);
        }
        return userEntity;
    }

}

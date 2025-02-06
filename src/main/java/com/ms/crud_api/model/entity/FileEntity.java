package com.ms.crud_api.model.entity;


import com.ms.crud_api.constant.enums.TableNameConstant;
import com.ms.crud_api.infrastructure.model.entity.BaseSoftDeleteEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = TableNameConstant.FILE)
public class FileEntity extends BaseSoftDeleteEntity<Long> {

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, length = 100)
    private String originalName;

    @Column(nullable = false, length = 20)
    private String type;

    @Column(nullable = false)
    private Long size;

    public FileEntity(String name, String originalName, String type, Long size) {
        this.name = name;
        this.originalName = originalName;
        this.type = type;
        this.size = size;
    }

    public FileEntity(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}

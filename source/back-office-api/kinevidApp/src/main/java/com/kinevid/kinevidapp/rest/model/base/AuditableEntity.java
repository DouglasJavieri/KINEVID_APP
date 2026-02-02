package com.kinevid.kinevidapp.rest.model.base;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public abstract class AuditableEntity implements Serializable, Cloneable {
    @CreatedDate
    @Column(name = "created_date")
    protected Date createdDate;

    @LastModifiedDate
    @Column(name = "modified_date")
    protected Date modifiedDate;

    @CreatedBy
    @Column(name = "created_by")
    protected String createdBy;

    @LastModifiedBy
    @Column(name = "modified_by")
    protected String modifiedBy;

    @Version
    protected  Long version;

    protected boolean deleted = false;

    public Object clone()throws CloneNotSupportedException{
        return (AuditableEntity)super.clone();
    }

}

package com.ityu.bean.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.engine.spi.PersistentAttributeInterceptable;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * Created  on 2019/1/8 0002.
 *
 * @author enilu
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public abstract class BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreationTimestamp
    @Column(name = "create_time", columnDefinition = "DATETIME COMMENT '创建时间/注册时间'")
    private Date createTime;
    @Column(name = "create_by", columnDefinition = "bigint COMMENT '创建人'")
    @CreatedBy
    private String createBy;
    @UpdateTimestamp
    @Column(name = "modify_time", columnDefinition = "DATETIME COMMENT '最后更新时间'")
    private Date modifyTime;
    @LastModifiedBy
    @Column(name = "modify_by", columnDefinition = "bigint COMMENT '最后更新人'")
    private String modifyBy;
}

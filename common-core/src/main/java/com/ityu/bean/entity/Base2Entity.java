package com.ityu.bean.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
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
public abstract class Base2Entity implements Serializable {

    @Id
    @GeneratedValue(generator = "snowFlakeId")
    @GenericGenerator(name = "snowFlakeId", strategy = "com.ityu.config.SnowflakeId")
    private String id;
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

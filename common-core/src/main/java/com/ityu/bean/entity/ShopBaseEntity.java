package com.ityu.bean.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 前端业务实体基类
 * @author ：enilu
 * @date ：Created in 2019/10/29 19:09
 */

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public abstract class ShopBaseEntity implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @CreationTimestamp
    @Column(name = "create_time",columnDefinition="DATETIME COMMENT '创建时间/注册时间'")
    private Date createTime;
    @UpdateTimestamp
    @Column(name = "modify_time",columnDefinition="DATETIME COMMENT '最后更新时间'")
    private Date modifyTime;

}

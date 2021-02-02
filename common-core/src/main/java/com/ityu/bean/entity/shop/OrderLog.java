package com.ityu.bean.entity.shop;

import com.ityu.bean.entity.ShopBaseEntity;
import org.hibernate.annotations.Table;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;


/**
 * @author ：enilu
 * @date ：Created in 2020/2/6 22:27
 */
@Data
@Table(appliesTo = "t_shop_order_log",comment = "订单日志")
@Entity(name="t_shop_order_log")
@EntityListeners(AuditingEntityListener.class)
public class OrderLog extends ShopBaseEntity {
    @Column(name="id_order",columnDefinition = "BIGINT COMMENT '所属订单id'")
    private Long idOrder;
    @Column(columnDefinition = "VARCHAR(64) COMMENT '日志详情'")
    private String descript;
}

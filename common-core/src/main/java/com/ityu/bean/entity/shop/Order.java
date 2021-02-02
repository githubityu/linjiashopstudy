package com.ityu.bean.entity.shop;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ityu.bean.entity.ShopBaseEntity;
import com.ityu.bean.entity.system.Express;
import com.ityu.bean.enumeration.shop.OrderEnum;
import lombok.Data;
import org.hibernate.annotations.Table;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author ：enilu
 * @date ：Created in 2019/10/29 17:40
 */
@Data
@Table(appliesTo = "t_shop_order",comment = "订单")
@Entity(name="t_shop_order")
public class Order extends ShopBaseEntity {
    @Column(name="id_user",columnDefinition = "BIGINT COMMENT '用户id'")
    private Long idUser;
    @JoinColumn(name="id_user", referencedColumnName="id",insertable = false, updatable = false,foreignKey = @ForeignKey(name="none",value = ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private ShopUser user;
    @Column(columnDefinition = "VARCHAR(32) COMMENT '订单号'")
    private String orderSn;
    /**
     * @see
     */
    @Column(columnDefinition = "INT COMMENT '状态'")
    private Integer status;

    @Deprecated
    @Column(name="id_address",columnDefinition = "BIGINT COMMENT '收货信息'")
    private Long idAddress;
    @Deprecated
    @JoinColumn(name="id_address",  referencedColumnName="id",insertable = false, updatable = false,foreignKey = @ForeignKey(name="none",value = ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Address address;
    /**
     *  收件人信息使用 下面三个字段代替
     */
    @Column(columnDefinition = "VARCHAR(32) COMMENT '收件人'")
    private String consignee;
    @Column(columnDefinition = "VARCHAR(16) COMMENT '收件人电话'")
    private String mobile;
    @Column(columnDefinition = "VARCHAR(64) COMMENT '收件地址'")
    private String consigneeAddress;


    @OneToMany(cascade = {CascadeType.ALL})
    @JoinColumn(name = "id_order")
    @org.hibernate.annotations.ForeignKey(name="none")
    @JsonManagedReference
    private List<OrderItem> items;
    @Column(columnDefinition = "VARCHAR(64) COMMENT '订单备注'")
    private String message;
    @Column(columnDefinition = "VARCHAR(256) COMMENT '管理员备注'")
    private String adminMessage;
    @Column(columnDefinition = "VARCHAR(32) COMMENT '配送费用'")
    private BigDecimal shippingAmount;
    @Column(columnDefinition = "VARCHAR(32) COMMENT '交易金额'")
    private BigDecimal tradeAmount;
    @Column(columnDefinition = "VARCHAR(32) COMMENT '总金额'")
    private BigDecimal totalPrice;
    @Column(columnDefinition = "VARCHAR(32) COMMENT '优惠券抵扣金额'")
    private BigDecimal couponPrice;
    @Column(columnDefinition = "VARCHAR(32) COMMENT '实付金额'")
    private BigDecimal realPrice;
    @Column(columnDefinition = "DATETIME COMMENT '出库时间'")
    private Date shippingTime;
    @Column(name="id_express",columnDefinition = "BIGINT COMMENT '快递公司'")
    private Long idExpress;
    @JoinColumn(name="id_express",  referencedColumnName="id",insertable = false, updatable = false,foreignKey = @ForeignKey(name="none",value = ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Express express;
    @Column(columnDefinition = "VARCHAR(32) COMMENT '快递单号'")
    private String shippingSn;
    @Column(columnDefinition = "DATETIME COMMENT '确认收货时间'")
    private Date confirmReceivingTime;
    @Column(columnDefinition = "VARCHAR(16) COMMENT '实付类型:alipay,wechat'")
    private String payType;
    @Column(columnDefinition = "INT COMMENT '支付状态1:未付款;2:已付款,3:支付中'")
    private Integer payStatus;
    @Column(columnDefinition = "VARCHAR(32) COMMENT '支付流水号'")
    private String payId;
    @Column(columnDefinition = "VARCHAR(32) COMMENT '支付成功时间'")
    private Date payTime;


    public String getStatusName(){
        if(status!=null) {
            return OrderEnum.get(status).getValue();
        }
        return null;
    }
    public String getPayStatusName(){
        if(payStatus!=null) {
            return OrderEnum.getPayStatus(payStatus).getValue();
        }
        return null;
    }
    public String getPayTypeName(){
        if(payType!=null) {
            return OrderEnum.get(payType).getValue();
        }
        return null;
    }
    public Boolean hasPayed(){
        return OrderEnum.PayStatusEnum.UN_SEND.getId().equals(payStatus);
    }

}

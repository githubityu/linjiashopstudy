package com.ityu.bean.entity.shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ityu.bean.entity.BaseEntity;
import com.ityu.bean.entity.cms.Banner;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.Table;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

/**
 * @author ：enilu
 * @date ：Created in 11/15/2019 2:16 PM
 */
@Data
@Entity(name="t_shop_category_banner_rel")
@Table(appliesTo = "t_shop_category_banner_rel",comment = "类别banner关联表")
public class CategoryBannerRel extends BaseEntity {
    @Column(name = "id_category",columnDefinition = "BIGINT COMMENT '类别id'")
    private Long idCategory;
    @Column(name = "id_banner",columnDefinition = "BIGINT COMMENT 'banner id'")
    private Long idBanner;
    @JoinColumn(name="id_category", insertable = false, updatable = false,foreignKey = @ForeignKey(name="none",value = ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound
    private Category category;
    @JoinColumn(name="id_banner", insertable = false, updatable = false,foreignKey = @ForeignKey(name="none",value = ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Banner banner;

}

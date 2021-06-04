package com.ityu.bean.entity.cms;



import com.ityu.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity(name="t_cms_banner")
@Table(appliesTo = "t_cms_banner",comment = "广告banner")
@Data
public class Banner extends BaseEntity {
    @Column(columnDefinition = "VARCHAR(128) COMMENT '点击banner跳转到url'")
    private String url;
    @Column(columnDefinition = "VARCHAR(64) COMMENT '标题'")
    @NotBlank(message = "标题不能为空")
    private String title;
    @Column(columnDefinition = "VARCHAR(32) COMMENT '类型'")
    @NotBlank(message = "类型不能为空")
    private String type;
    @Column(columnDefinition = "BIGINT COMMENT 'banner图id'")
    private Long idFile;
    @Column(columnDefinition = "VARCHAR(64) COMMENT '界面'")
    private String page;
    @Column(columnDefinition = "VARCHAR(128) COMMENT '参数'")
    private String param;
}

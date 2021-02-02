package com.ityu.bean.entity.system;


import com.ityu.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Created  on 2018/4/2 0002.
 *
 * @author enilu
 */

@Entity(name = "t_sys_notice")
@Table(appliesTo = "t_sys_notice",comment = "通知")
@Data
public class Notice extends BaseEntity {
    @Column
    private String title;
    @Column
    private Integer type;
    @Column
    private String content;

}

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
@Entity(name="t_sys_dict")
@Table(appliesTo = "t_sys_dict",comment = "字典")
@Data
public class Dict extends BaseEntity {
    @Column
    private String num;
    @Column
    private Long pid;
    @Column
    private String name;
    @Column
    private String tips;

}

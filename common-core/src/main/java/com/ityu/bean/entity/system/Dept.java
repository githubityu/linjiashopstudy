package com.ityu.bean.entity.system;


import com.ityu.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

/**
 * Created  on 2018/4/2 0002.
 *
 * @author enilu
 */
@Entity(name="t_sys_dept")
@Table(appliesTo = "t_sys_dept",comment = "部门")
@Data
public class Dept extends BaseEntity {
    @Column
    private Integer num;
    @Column
    private Long pid;
    @Column
    private String pids;
    @Column
    @NotBlank(message = "部门简称不能为空")
    private String simplename;
    @Column
    @NotBlank(message = "部门全称不能为空")
    private String fullname;
    @Column
    private String tips;
    @Column
    private Integer version;
}

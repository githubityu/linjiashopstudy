package com.ityu.bean.entity.system;

import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.*;

/**
 * Created  on 2018/4/2 0002.
 *
 * @author enilu
 */
@Entity(name = "t_sys_relation")
@Table(appliesTo = "t_sys_relation", comment = "菜单角色关系")
@Data
public class Relation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long menuid;
    @Column
    private Long roleid;

}

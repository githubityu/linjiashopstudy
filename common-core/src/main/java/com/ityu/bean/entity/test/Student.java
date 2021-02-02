package com.ityu.bean.entity.test;

import com.ityu.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.*;
import java.util.Set;


/**
 * 使用 @JoinColumn
 * 定义外键（在从表上定义，name指的是 外键名(类名+id(主键),本表中的一个字段最好自己创建)，
 * referencedColumnName 默认指向另外一个表的主键，如果不是就需要该字段指定其他表字段）
 */
@Entity(name = "t_test_student")
@Table(appliesTo = "t_test_student", comment = "Student")
@Data
public class Student extends BaseEntity {
    private String name;
    private Integer age;

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Tuition tuition;

    @ManyToOne()
    @JoinColumn(name = "university_id")
    private University university;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(
            name = "student_course",
            joinColumns = {@JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "course_id")}
    )
    private Set<Course> courses;
}
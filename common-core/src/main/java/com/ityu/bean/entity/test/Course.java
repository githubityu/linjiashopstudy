package com.ityu.bean.entity.test;

import com.ityu.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.*;
import java.util.List;
import java.util.Set;


@Entity(name = "t_test_course")
@Table(appliesTo = "t_test_course", comment = "course")
@Data
public class Course extends BaseEntity {
    private String name;

    private Double fee;

    @ManyToMany(mappedBy = "courses")
    private Set<Student> students;
    
}
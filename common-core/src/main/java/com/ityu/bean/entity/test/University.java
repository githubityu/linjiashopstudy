package com.ityu.bean.entity.test;

import com.ityu.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.*;
import java.util.List;


@Entity(name = "t_test_university")
@Table(appliesTo = "t_test_university", comment = "university ")
@Data
public class University extends BaseEntity {
    private String name;

//    @OneToMany(mappedBy = "university", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Student> students;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "university_id")
    private List<Student> students;


}
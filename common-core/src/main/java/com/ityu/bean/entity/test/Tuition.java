package com.ityu.bean.entity.test;

import com.ityu.bean.entity.BaseEntity;
import lombok.Data;
import org.hibernate.annotations.Table;

import javax.persistence.*;


@Entity(name = "t_test_tuition")
@Table(appliesTo = "t_test_tuition", comment = "学费")
@Data
public class Tuition extends BaseEntity {
    private Double fee;

    // Que columna en la tabla Tuition tiene la FK

    //@JoinColumn(name = "student_id")
    //name_id
    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private Student student;

}
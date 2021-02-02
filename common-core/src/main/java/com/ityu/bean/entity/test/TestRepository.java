
package com.ityu.bean.entity.test;


import com.ityu.dao.BaseRepository;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

public interface TestRepository extends BaseRepository<Student,Long> {

    Optional<Student> findByAge(Integer age);

    Student  findStudent(Specification<Student> studentSpecification);
}

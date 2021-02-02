package com.ityu;

import com.ityu.bean.entity.test.Student;
import com.ityu.bean.entity.test.Tuition;
import com.ityu.dao.system.UserRepository;
import com.ityu.linjiamobileapi.MobileApiApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MobileApiApplication.class)
public class test {
    @Test
    public void test(){
        System.out.println("=======");
    }

    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Test
    public void queryBySql(){
        String sql = "select sex,count(1) as count from t_sys_user group by sex";
        List list = userRepository.queryBySql(sql, UserVo.class);
        System.out.println(Json.toJson(list));
    }
    @Test
    public void getBySql(){
        String sql = "select sex,count(1) as count from t_sys_user group by sex  having sex=1";
        UserVo ret = (UserVo) userRepository.getBySql(sql,UserVo.class);
        System.out.println(Json.toJson(ret));
    }
    @Test
    @Transactional
    @Rollback(false)
    public void check_sql_statement_when_persisting_in_one_to_one_bidirectional() {

        Student student = new Student();
        student.setName("Jonathan");

        Tuition tuition = new Tuition();
        tuition.setFee(150.0);
        tuition.setStudent(student);

        student.setTuition(tuition);

        entityManager.persist(student);
    }
}

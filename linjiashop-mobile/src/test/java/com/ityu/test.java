package com.ityu;

import com.ityu.dao.system.UserRepository;
import com.ityu.linjiamobileapi.MobileApiApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nutz.json.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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
}

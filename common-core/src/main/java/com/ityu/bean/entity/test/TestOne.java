package com.ityu.bean.entity.test;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
//@JsonIgnoreProperties(value = {"age"})
//序列化顺序
@JsonPropertyOrder({"name", "age", "sex"})
public class TestOne {
    public String name;
    public String sex;
    //@JsonIgnore
    public String age;
    @JsonBackReference
    public A a;


    public static void main(String[] args) {
        TestOne one = new TestOne().setAge("18").setName("zhangsan").setSex("nvde");
        //String json ="{\"name\":\"zhangsan\",\"sex\":\"nvde\",\"age\":\"18\"}";
        //String json2 = "{\"name\":\"zhangsan\",\"age\":\"18\",\"sex\":\"nvde\",\"a\":{\"name\":\"A\"}}";

        A a = new A().setName("A").setTestOne(new TestOne().setName("lisi").setSex("nande").setAge("28"));
        one.setA(a);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String s = objectMapper.writeValueAsString(one);
            System.out.println(s);
          //  TestOne testOne = objectMapper.readValue(json2, TestOne.class);
          //  System.out.println(testOne);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

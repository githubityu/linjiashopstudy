package com.ityu.bean.entity.test;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class A {
    public String name;
    @JsonManagedReference
    public TestOne testOne;
}

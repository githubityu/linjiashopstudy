package com.ityu.bean.entity.test;

import org.springframework.beans.factory.annotation.Value;

/**
 * 使用spring data jpa 的 Projection (投影映射）
 * 该部分是很有趣的一部分，简单容易操作， Projection 是要解决什么问题呢？
 * <p>
 * 当我们使用 spring data jpa 查询数据的时候，有时候不需要返回所有字段的数据，我们只需要个别字段数据，这样使用 Projection 也是不错的选择
 */
public interface StudentProjection {
    String getName();

    Integer getAge();

    @Value("#{target.name+'and age is'+target.age}")
    String getName2();
}

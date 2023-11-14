package com.ityu.common.bean.vo.query

import org.springframework.data.jpa.domain.Specification

/**
 * 将SearchFilter查询条件解析为jpa查询对象Predicate
 *
 * @author ：enilu
 * @date ：Created in 2019/6/30 16:04
 */
 object DynamicSpecifications {
    fun <T> bySearchFilter(
        filters: Collection<SearchFilter?>?,
        entityClazz: Class<T>?
    ): Specification<T> {
        return SimpleSpecification(filters)
    }
}

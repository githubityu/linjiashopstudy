package com.ityu.common.bean.vo.query

import com.ityu.common.utils.Maps
import com.ityu.common.utils.StringUtil

/**
 * 查询条件封装类
 *
 * @author ：enilu
 * @date ：Created in 2019/6/30 16:02
 */
class SearchFilter {
    enum class Operator {
        EQ,
        NE,
        LIKE,
        LIKEL,
        LIKER,
        GT,
        LT,
        GTE,
        LTE,
        IN,
        NOTIN,
        ISNULL,
        ISNOTNULL,
        BETWEEN
    }

    enum class Join {
        and,
        or
    }

    @JvmField
     open var join = Join.and
    @JvmField
     open var fieldName: String? = null
    @JvmField
     open var value: Any? = null
    @JvmField
     open var operator: Operator? = null

    constructor(fieldName: String?, operator: Operator?) {
        this.fieldName = fieldName
        this.operator = operator
    }

    constructor(fieldName: String?, operator: Operator?, value: Any?) {
        if (!StringUtil.isNullOrEmpty(value)) {
            this.fieldName = fieldName
            this.value = value
            this.operator = operator
        }
    }

    constructor(fieldName: String?, operator: Operator?, value: Any?, join: Join) {
        if (!StringUtil.isNullOrEmpty(value)) {
            this.fieldName = fieldName
            this.value = value
            this.operator = operator
            this.join = join
        }
    }

    companion object {
        fun build(fieldName: String?, value: Any?): SearchFilter {
            return SearchFilter(fieldName, Operator.EQ, value)
        }

        fun build(fieldName: String?, operator: Operator?): SearchFilter {
            return SearchFilter(fieldName, operator)
        }

        @JvmStatic
        fun build(fieldName: String?, operator: Operator?, value: Any?): SearchFilter {
            return SearchFilter(fieldName, operator, value)
        }

        fun build(fieldName: String?, value: Any?, join: Join): SearchFilter {
            return build(fieldName, Operator.EQ, value, join)
        }

        fun build(fieldName: String?, operator: Operator?, value: Any?, join: Join): SearchFilter {
            return SearchFilter(fieldName, operator, value, join)
        }

        /**
         * searchParams中key的格式为OPERATOR_FIELDNAME
         */
        fun parse(searchParams: Map<String, Any>): Map<String, SearchFilter> {
            val filters = Maps.newHashMap<String, SearchFilter>()
            for ((key, value) in searchParams) {
                // 过滤掉空值
                /*if (StringUtil.isBlank((String) value)) {
				continue;
			}*/

                // 拆分operator与filedAttribute
                val names = StringUtil.split(key, "_")
                require(names.size == 2) { "$key is not a valid search filter name" }
                val filedName = names[1]
                val operator = Operator.valueOf(names[0])

                // 创建searchFilter
                val filter = SearchFilter(filedName, operator, value)
                filters[key] = filter
            }
            return filters
        }
    }
}

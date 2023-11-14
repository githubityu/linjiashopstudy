package com.ityu.common.bean.vo.node

/**
 * 是否是菜单的枚举
 *
 * @author fengshuonan
 * @date 2017年6月1日22:50:11
 */
enum class IsMenu(//不是菜单的是按钮
     open var code: Int,  open var message: String
) {
    YES(1, "是"),
    NO(0, "不是");

    companion object {
        fun valueOf(status: Int?): String {
            return if (status == null) {
                ""
            } else {
                for (s in values()) {
                    if (s.code == status) {
                        return s.message
                    }
                }
                ""
            }
        }
    }
}

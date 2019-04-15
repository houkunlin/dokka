package com.test

/**
 * 用户
 * 用户信息实体类
 * @author 侯坤林
 */
data class User(
    /**
     * 整型
     */
    var userId: Int = 0,
    /**
     * 长整型
     */
    var dateTime: Long = 0,
    /**
     * 添加时间
     */
    var addTime: Long = 0,
    var isUser: Boolean = false
) : java.io.Serializable {
    /**
     * 更新时间
     */
    var updateTime: Long = 0
    /**
     * 测试字段
     */
    val test: Int = 0

    /**
     * 获取用户
     */
    fun getUser(): User {
        return User()
    }

    /**
     * 是否可登录
     */
    // 测试
    fun isCanLogin() {}

    companion object {
        /**
         * 类型
         */
        val type: String = "a"
    }
}
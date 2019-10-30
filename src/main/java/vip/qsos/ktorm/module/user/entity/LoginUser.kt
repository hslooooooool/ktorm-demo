package vip.qsos.ktorm.module.user.entity

/**
 * @author : 华清松
 * 登录用户
 */
data class LoginUser(
        var userId: Int = -1,
        var userName: String,
        var account: String,
        var password: String,
        var avatar: String? = null,
        var birth: String? = null,
        var sexuality: Int? = null
)
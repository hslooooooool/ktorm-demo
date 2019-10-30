package vip.qsos.ktorm.module.user.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import vip.qsos.ktorm.module.user.entity.LoginUser
import vip.qsos.ktorm.util.MResult

/**
 * @author : 华清松
 * @description : 用户相关接口
 */
interface ILoginUserModel {

    /**账号密码登录
     * @param account 账号
     * @param password 密码
     * @return 登录用户数据
     * */
    @GetMapping("/login")
    fun login(
            @RequestParam("account") account: String,
            @RequestParam("password") password: String
    ): MResult<LoginUser>

    /**账号密码注册
     * @param account 账号
     * @param password 密码
     * @return 注册用户数据
     * */
    @PostMapping("/register")
    fun register(
            @RequestParam("account") account: String,
            @RequestParam("password") password: String
    ): MResult<LoginUser>
}
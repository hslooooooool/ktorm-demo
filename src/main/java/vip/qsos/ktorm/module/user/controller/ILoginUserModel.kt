package vip.qsos.ktorm.module.user.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiSort
import org.springframework.web.bind.annotation.*
import vip.qsos.ktorm.module.user.entity.LoginUserBo
import vip.qsos.ktorm.util.MResult

@Api(tags = ["登录用户"])
@ApiSort(0)
@RequestMapping("/user")
interface ILoginUserModel {

    @ApiOperation(value = "账号密码登录")
    @GetMapping("/login")
    fun login(
            @RequestParam("account")
            @ApiParam(value = "账号", required = true)
            account: String,
            @RequestParam("password")
            @ApiParam(value = "密码", required = true)
            password: String
    ): MResult<LoginUserBo>

    @ApiOperation(value = "账号密码注册")
    @PostMapping("/register")
    fun register(
            @RequestParam("account")
            @ApiParam(value = "账号", required = true)
            account: String,
            @RequestParam("password")
            @ApiParam(value = "密码", required = true)
            password: String
    ): MResult<LoginUserBo>

    @PostMapping("/updateUser")
    @ApiOperation(value = "更新用户，仅更新自己的信息使用")
    fun updateUser(
            @RequestHeader
            @ApiParam(value = "登录用户ID", required = true)
            userId: Int,
            @ApiParam(name = "userName", value = "用户姓名")
            userName: String,
            @ApiParam(name = "avatar", value = "用户头像")
            avatar: String? = null,
            @ApiParam(name = "birth", value = "出生日期")
            birth: String? = null,
            @ApiParam(name = "sexuality", value = "性别,0女 1男 -1未知")
            sexuality: Int = -1
    ): MResult<Boolean>

}
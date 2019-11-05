package vip.qsos.ktorm.module.user.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiSort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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
}
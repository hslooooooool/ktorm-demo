package vip.qsos.ktorm.module.chat.entity.form

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "聊天用户表单数据")
data class FormCreateUser(
        @ApiModelProperty(name = "userName", value = "用户姓名")
        var userName: String,
        @ApiModelProperty(name = "avatar", value = "头像,http://www.qsos.vip/upload/2018/11/ic_launcher20181225044818498.png", dataType = "String")
        var avatar: String? = null,
        @ApiModelProperty(name = "birth", value = "出生,1969-05-05")
        var birth: String? = null,
        @ApiModelProperty(name = "sexuality", value = "性别,0女 1男 -1未知")
        var sexuality: Int = -1
)
package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.ktorm.module.IVo

/**
 * @author : 华清松
 * 聊天用户
 * @param userId 用户ID
 * @param userName 用户姓名
 * @param avatar 用户头像
 * @param birth 用户出生日期
 * @param sexuality 用户性别
 */
@ApiModel(description = "聊天用户")
data class ChatUserBo(
        @ApiModelProperty(value = "用户ID", required = true)
        var userId: Int = -1,
        @ApiModelProperty(value = "用户姓名", required = true)
        var userName: String,
        @ApiModelProperty(value = "用户头像")
        var avatar: String? = null,
        @ApiModelProperty(value = "出生日期")
        var birth: String? = null,
        @ApiModelProperty(value = "用户性别")
        var sexuality: Int? = -1
) : IVo<TableChatUser> {

    override fun toTable(): TableChatUser {
        return TableChatUser(
                userId = userId,
                userName = userName,
                avatar = avatar,
                birth = birth,
                sexuality = sexuality
        )
    }

    companion object {
        /**数据库表转为业务实体*/
        fun getBo(table: TableChatUser): ChatUserBo {
            return ChatUserBo(
                    userId = table.userId,
                    userName = table.userName,
                    avatar = table.avatar,
                    birth = table.birth,
                    sexuality = table.sexuality
            )
        }
    }
}
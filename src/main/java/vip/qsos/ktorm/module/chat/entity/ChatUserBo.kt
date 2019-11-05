package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.ktorm.module.IBo

@ApiModel(description = "聊天用户")
class ChatUserBo : IBo<TableChatUser> {
    @ApiModelProperty(value = "用户ID", required = true)
    var userId: Int = -1
    @ApiModelProperty(value = "用户姓名", required = true)
    var userName: String = ""
    @ApiModelProperty(value = "用户头像")
    var avatar: String? = null
    @ApiModelProperty(value = "出生日期")
    var birth: String? = null
    @ApiModelProperty(value = "用户性别")
    var sexuality: Int = -1

    constructor()
    constructor(
            userId: Int = -1, userName: String, avatar: String? = null,
            birth: String? = null, sexuality: Int = -1
    ) {
        this.userId = userId
        this.userName = userName
        this.avatar = avatar
        this.birth = birth
        this.sexuality = sexuality
    }

    override fun toTable(): TableChatUser {
        return TableChatUser(
                userId = userId,
                userName = userName,
                avatar = avatar,
                birth = birth,
                sexuality = sexuality
        )
    }

    override fun getBo(table: TableChatUser?): IBo<TableChatUser>? {
        return table?.let {
            ChatUserBo(
                    userId = table.userId,
                    userName = table.userName,
                    avatar = table.avatar,
                    birth = table.birth,
                    sexuality = table.sexuality
            )
        }
    }

}
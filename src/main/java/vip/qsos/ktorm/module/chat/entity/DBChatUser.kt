package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.schema.BaseTable
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import javax.persistence.*

private const val TAB_NAME = "t_chat_user"

/**
 * @author : 华清松
 * @description : 聊天用户表
 */
object DBChatUser : BaseTable<TableChatUser>(TAB_NAME) {
    val id by int("id").primaryKey()
    val userId by int("user_id")
    val userName by varchar("user_name")
    val avatar by varchar("avatar")
    val birth by varchar("birth")
    val sexuality by int("sexuality")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): TableChatUser {
        return TableChatUser(
                id = row[id]!!,
                userId = row[userId]!!,
                userName = row[userName] ?: "用户" + row[userId],
                avatar = row[avatar],
                birth = row[birth],
                sexuality = row[sexuality]
        )
    }
}

@javax.persistence.Entity
@javax.persistence.Table(
        name = TAB_NAME,
        indexes = [Index(name = "user_id", columnList = "user_id", unique = true)])
@ApiModel(value = "聊天用户实体")
data class TableChatUser(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(name = "userId", value = "自增ID", dataType = "Int")
        val id: Int,

        @Column(name = "user_id")
        @ApiModelProperty(name = "userId", value = "用户ID", dataType = "Int")
        val userId: Int,

        @Column(name = "user_name")
        @ApiModelProperty(name = "sessionId", value = "用户姓名", dataType = "String")
        val userName: String,

        @Column(name = "avatar")
        @ApiModelProperty(name = "avatar", value = "头像,http://www.qsos.vip/upload/2018/11/ic_launcher20181225044818498.png", dataType = "String")
        val avatar: String? = "http://www.qsos.vip/upload/2018/11/ic_launcher20181225044818498.png",

        @Column(name = "birth")
        @ApiModelProperty(name = "birth", value = "出生,1969-05-05", dataType = "String")
        val birth: String?,

        @Column(name = "sexuality")
        @ApiModelProperty(name = "sexuality", value = "性别,0女 1男 null未知", dataType = "Int")
        val sexuality: Int?
) {
    /**转化为业务实体*/
    fun toChatUser(): ChatUser {
        return ChatUser(
                userId = this.userId,
                userName = this.userName,
                avatar = this.avatar,
                birth = this.birth,
                sexuality = this.sexuality
        )
    }
}
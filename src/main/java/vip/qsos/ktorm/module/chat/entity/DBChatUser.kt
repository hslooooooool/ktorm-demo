package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.schema.BaseTable
import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

private const val TAB_NAME = "t_chat_user"

/**
 * @author : 华清松
 * @description : 聊天用户表
 */
object DBChatUser : BaseTable<TableChatUser>(TAB_NAME) {
    val userId by int("id").primaryKey()
    val userName by varchar("user_name")
    val avatar by varchar("avatar")
    val birth by varchar("birth")
    val sexuality by boolean("sexuality")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): TableChatUser {
        return TableChatUser(
                userId = row[userId]!!,
                userName = row[userName] ?: "用户" + row[userId],
                avatar = row[avatar],
                birth = row[birth],
                sexuality = row[sexuality]
        )
    }
}

@javax.persistence.Entity
@javax.persistence.Table(name = TAB_NAME)
@ApiModel(value = "聊天用户实体")
data class TableChatUser(
        @Id
        @Column(name = "user_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(name = "userId", value = "用户ID", dataType = "Int")
        val userId: Int,

        @Column(name = "user_name")
        @ApiModelProperty(name = "sessionId", value = "用户姓名", dataType = "String")
        val userName: String,

        @Column(name = "avatar")
        @ApiModelProperty(name = "avatar", value = "头像,http://www.avatar.com/avatar/0001.png", dataType = "String")
        val avatar: String?,

        @Column(name = "birth")
        @ApiModelProperty(name = "birth", value = "出生,1969-05-05", dataType = "String")
        val birth: String?,

        @Column(name = "sexuality")
        @ApiModelProperty(name = "sexuality", value = "性别,true(1)男 false(0)女 null未知", dataType = "Boolean")
        val sexuality: Boolean?

)
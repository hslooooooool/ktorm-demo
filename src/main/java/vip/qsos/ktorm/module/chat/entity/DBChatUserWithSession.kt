package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.schema.BaseTable
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.long
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

private const val TAB_NAME = "t_chat_user_with_session"

/**
 * @author : 华清松
 * @description : 聊天用户表
 */
object DBChatUserWithSession : BaseTable<TableChatUserWithSession>(TAB_NAME) {
    private val id by int("id").primaryKey()
    val userId by int("user_id")
    val sessionId by int("session_id")
    val createTime by long("create_time")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): TableChatUserWithSession {
        return TableChatUserWithSession(
                id = row[id]!!,
                userId = row[userId]!!,
                sessionId = row[sessionId]!!,
                createTime = row[createTime]!!
        )
    }
}

@javax.persistence.Entity
@javax.persistence.Table(name = TAB_NAME)
@ApiModel(value = "聊天用户与会话关系实体")
data class TableChatUserWithSession(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(name = "id", value = "用户与消息关系ID", dataType = "Int")
        var id: Int? = null,

        @Column(name = "user_id")
        @ApiModelProperty(name = "userId", value = "用户ID", dataType = "Int")
        val userId: Int,

        @Column(name = "session_id")
        @ApiModelProperty(name = "sessionId", value = "会话ID", dataType = "Int")
        val sessionId: Int,

        @Column(name = "create_time")
        @ApiModelProperty(name = "createTime", value = "消息创建时间,毫秒数", dataType = "Long")
        val createTime: Long
)
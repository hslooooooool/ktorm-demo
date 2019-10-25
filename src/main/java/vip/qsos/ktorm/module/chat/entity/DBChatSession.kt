package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.schema.BaseTable
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

private const val TAB_NAME = "t_chat_session"

/**
 * @author : 华清松
 * @description : 聊天会话表
 */
object DBChatSession : BaseTable<TableChatSession>(TAB_NAME) {
    val sessionId by int("id").primaryKey()
    val type by varchar("chat_type")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): TableChatSession {
        return TableChatSession(
                sessionId = row[sessionId]!!,
                type = row[type]!!
        )
    }
}

@javax.persistence.Entity
@javax.persistence.Table(name = TAB_NAME)
@ApiModel(value = "聊天会话实体")
data class TableChatSession(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(name = "userId", value = "用户ID", dataType = "Int")
        val sessionId: Int,

        @Column(name = "chat_type")
        @ApiModelProperty(name = "chat_type", value = "会话类型", dataType = "String")
        val type: String
)
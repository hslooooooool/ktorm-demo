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

private const val TAB_NAME = "t_chat_message"

/**
 * @author : 华清松
 * @date : 2019-05-17
 * @description : TODO 类说明，描述此类的类型和用途
 */
object DBChatMessage : BaseTable<TableChatMessage>(TAB_NAME) {
    val id by int("id").primaryKey()
    val sessionId by int("session_id")
    val sequence by int("sequence")
    val content by varchar("content")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): TableChatMessage {
        return TableChatMessage(
                id = row[id]!!,
                sessionId = row[sessionId] ?: -1,
                sequence = row[sequence] ?: -1,
                content = row[content] ?: ""
        )
    }
}

@javax.persistence.Entity
@javax.persistence.Table(name = TAB_NAME)
@ApiModel(value = "聊天消息实体")
data class TableChatMessage(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(name = "id", value = "聊天消息ID", dataType = "Int")
        var id: Int,

        @Column(name = "session_id")
        @ApiModelProperty(name = "sessionId", value = "会话ID", dataType = "Int")
        val sessionId: Int,

        @Column(name = "sequence")
        @ApiModelProperty(name = "sequence", value = "消息顺序", dataType = "Int")
        val sequence: Int,

        @Column(name = "content")
        @ApiModelProperty(name = "content", value = "聊天消息内容", dataType = "String")
        val content: String
)
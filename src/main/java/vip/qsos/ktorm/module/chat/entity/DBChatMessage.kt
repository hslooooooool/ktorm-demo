package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import vip.qsos.ktorm.module.AbsTable
import vip.qsos.ktorm.module.MBaseTable
import java.time.LocalDate
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
object DBChatMessage : MBaseTable<TableChatMessage>(TAB_NAME) {
    val messageId by int("id").primaryKey()
    val sessionId by int("session_id")
    val sequence by int("sequence")
    val content by varchar("content")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): TableChatMessage {
        return TableChatMessage(
                messageId = row[messageId]!!,
                sessionId = row[sessionId] ?: -1,
                sequence = row[sequence] ?: -1,
                content = row[content] ?: "",

                gmtCreate = row[gmtCreate]!!,
                gmtUpdate = row[gmtUpdate]!!,
                deleted = row[deleted]!!
        )
    }

    override fun add(t: TableChatMessage): Any {
        return this.insertAndGenerateKey {
            it.sessionId to t.sessionId
            it.sequence to t.sequence
            it.content to t.content
            it.gmtCreate to t.gmtCreate
            it.gmtUpdate to t.gmtUpdate
            it.deleted to t.deleted
        }
    }
}

@javax.persistence.Entity
@javax.persistence.Table(name = TAB_NAME)
@ApiModel(value = "聊天消息实体")
class TableChatMessage : AbsTable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var messageId: Int = -1

    @Column(name = "sequence")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "sequence", value = "消息顺序", dataType = "Int")
    var sequence: Int = -1

    @Column(name = "session_id")
    @ApiModelProperty(name = "sessionId", value = "会话ID", dataType = "Int")
    var sessionId: Int = -1

    @Column(name = "content")
    @ApiModelProperty(name = "content", value = "聊天消息内容", dataType = "String")
    var content: String = ""

    constructor()
    constructor(
            messageId: Int,
            sessionId: Int,
            sequence: Int,
            content: String,

            gmtCreate: LocalDate = LocalDate.now(),
            gmtUpdate: LocalDate = LocalDate.now(),
            deleted: Boolean = false
    ) {
        this.messageId = messageId
        this.sessionId = sessionId
        this.sequence = sequence
        this.content = content

        this.gmtCreate = gmtCreate
        this.gmtUpdate = gmtUpdate
        this.deleted = deleted
    }
}
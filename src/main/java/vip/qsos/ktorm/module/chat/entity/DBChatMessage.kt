package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.text
import vip.qsos.ktorm.module.AbsTable
import vip.qsos.ktorm.module.MBaseTable
import java.time.LocalDateTime
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
    val timeline by int("timeline")
    private val content by text("content")
    val cancelBack by boolean("cancel_back")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): TableChatMessage {
        return TableChatMessage(
                messageId = row[messageId]!!,
                sessionId = row[sessionId] ?: -1,
                timeline = row[timeline] ?: -1,
                cancelBack = row[cancelBack]!!,
                content = row[content] ?: "",
                gmtCreate = row[gmtCreate]!!,
                gmtUpdate = row[gmtUpdate]!!,
                deleted = row[deleted]!!
        )
    }

    override fun add(t: TableChatMessage): Any {
        return this.insertAndGenerateKey {
            it.sessionId to t.sessionId
            it.timeline to t.timeline
            it.content to t.content
            it.cancelBack to t.cancelBack
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

    @Column(name = "timeline")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "timeline", value = "消息时序")
    var timeline: Int = -1

    @Column(name = "session_id")
    @ApiModelProperty(name = "sessionId", value = "会话ID")
    var sessionId: Int = -1

    @Column(name = "content", length = 500)
    @ApiModelProperty(name = "content", value = "聊天消息内容")
    var content: String = ""

    @Column(name = "cancel_back")
    @ApiModelProperty(name = "cancelBack", value = "是否撤回")
    var cancelBack: Boolean = false

    constructor()
    constructor(
            messageId: Int,
            sessionId: Int,
            timeline: Int,
            cancelBack: Boolean = false,
            content: String,

            gmtCreate: LocalDateTime = LocalDateTime.now(),
            gmtUpdate: LocalDateTime = LocalDateTime.now(),
            deleted: Boolean = false
    ) {
        this.messageId = messageId
        this.sessionId = sessionId
        this.timeline = timeline
        this.content = content
        this.cancelBack = cancelBack

        this.gmtCreate = gmtCreate
        this.gmtUpdate = gmtUpdate
        this.deleted = deleted
    }
}
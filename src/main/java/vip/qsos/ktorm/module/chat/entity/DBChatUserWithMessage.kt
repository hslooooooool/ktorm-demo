package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.insert
import me.liuwj.ktorm.schema.int
import vip.qsos.ktorm.module.AbsTable
import vip.qsos.ktorm.module.MBaseTable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

private const val TAB_NAME = "t_chat_user_with_message"

/**
 * @author : 华清松
 * @description : 聊天用户表
 */
object DBChatUserWithMessage : MBaseTable<TableChatUserWithMessage>(TAB_NAME) {
    val id by int("id").primaryKey()
    val userId by int("user_id")
    val messageId by int("message_id")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): TableChatUserWithMessage {
        return TableChatUserWithMessage(
                id = row[id]!!,
                userId = row[userId]!!,
                messageId = row[messageId]!!,

                gmtCreate = row[gmtCreate]!!,
                gmtUpdate = row[gmtUpdate]!!,
                deleted = row[deleted]!!
        )
    }

    override fun add(t: TableChatUserWithMessage): Any {
        return this.insert {
            it.userId to t.userId
            it.messageId to t.messageId
            it.gmtCreate to t.gmtCreate
            it.gmtUpdate to t.gmtUpdate
            it.deleted to t.deleted
        }
    }
}

@javax.persistence.Entity
@javax.persistence.Table(name = TAB_NAME)
@ApiModel(value = "聊天用户与消息关系实体")
class TableChatUserWithMessage : AbsTable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "id", value = "用户与消息关系ID", dataType = "Int")
    var id: Int = -1

    @Column(name = "user_id")
    @ApiModelProperty(name = "userId", value = "用户ID", dataType = "Int")
    var userId: Int = -1

    @Column(name = "message_id")
    @ApiModelProperty(name = "messageId", value = "消息ID", dataType = "Int")
    var messageId: Int = -1

    constructor()
    constructor(
            id: Int = -1,
            userId: Int,
            messageId: Int,

            gmtCreate: LocalDateTime = LocalDateTime.now(),
            gmtUpdate: LocalDateTime = LocalDateTime.now(),
            deleted: Boolean = false
    ) {
        this.id = id
        this.userId = userId
        this.messageId = messageId

        this.gmtCreate = gmtCreate
        this.gmtUpdate = gmtUpdate
        this.deleted = deleted
    }
}
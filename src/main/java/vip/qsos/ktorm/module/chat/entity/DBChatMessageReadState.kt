package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.insert
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.text
import vip.qsos.ktorm.module.AbsTable
import vip.qsos.ktorm.module.MBaseTable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Id

private const val TAB_NAME = "t_chat_message_read_state"

/**
 * @author : 华清松
 * @description : 聊天消息已读记录表
 */
object DBChatMessageReadState : MBaseTable<TableChatMessageReadState>(TAB_NAME) {
    val messageId by int("message_id").primaryKey()
    val readIds by text("read_ids")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): TableChatMessageReadState {
        return TableChatMessageReadState(
                messageId = row[messageId]!!,
                readIds = row[readIds]!!,

                gmtCreate = row[gmtCreate]!!,
                gmtUpdate = row[gmtUpdate]!!,
                deleted = row[deleted]!!
        )
    }

    override fun add(t: TableChatMessageReadState): Any {
        return insert {
            it.messageId to t.messageId
            it.readIds to t.readIds

            it.gmtCreate to t.gmtCreate
            it.gmtUpdate to t.gmtUpdate
            it.deleted to t.deleted
        }
    }
}

@javax.persistence.Entity
@javax.persistence.Table(name = TAB_NAME)
@ApiModel(value = "聊天消息已读记录实体")
class TableChatMessageReadState : AbsTable {
    @Id
    @Column(name = "message_id")
    @ApiModelProperty(name = "messageId", value = "消息ID")
    var messageId: Int = -1

    @Column(name = "read_ids")
    @ApiModelProperty(name = "readIds", value = "已读用户ID清单")
    var readIds: String = ""

    constructor()
    constructor(
            messageId: Int,
            readIds: String,

            gmtCreate: LocalDateTime = LocalDateTime.now(),
            gmtUpdate: LocalDateTime = LocalDateTime.now(),
            deleted: Boolean = false
    ) {
        this.messageId = messageId
        this.readIds = readIds

        this.gmtCreate = gmtCreate
        this.gmtUpdate = gmtUpdate
        this.deleted = deleted
    }
}
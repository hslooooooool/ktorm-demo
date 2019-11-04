package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.schema.int
import vip.qsos.ktorm.module.AbsTable
import vip.qsos.ktorm.module.MBaseTable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

private const val TAB_NAME = "t_chat_user_with_session"

/**
 * @author : 华清松
 * @description : 聊天用户表
 */
object DBChatUserWithSession : MBaseTable<TableChatUserWithSession>(TAB_NAME) {
    private val id by int("id").primaryKey()
    val userId by int("user_id")
    val sessionId by int("session_id")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): TableChatUserWithSession {
        return TableChatUserWithSession(
                id = row[id]!!,
                userId = row[userId]!!,
                sessionId = row[sessionId]!!,

                gmtCreate = row[gmtCreate]!!,
                gmtUpdate = row[gmtUpdate]!!,
                deleted = row[deleted]!!
        )
    }

    override fun add(t: TableChatUserWithSession): Any {
        return this.insertAndGenerateKey {
            it.userId to t.userId
            it.sessionId to t.sessionId
            it.gmtCreate to t.gmtCreate
            it.gmtUpdate to t.gmtUpdate
            it.deleted to t.deleted
        }
    }
}

@javax.persistence.Entity
@javax.persistence.Table(name = TAB_NAME)
@ApiModel(value = "聊天用户与会话关系实体")
class TableChatUserWithSession : AbsTable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "id", value = "用户与消息关系ID")
    var id: Int = -1

    @Column(name = "user_id")
    @ApiModelProperty(name = "userId", value = "用户ID")
    var userId: Int = -1

    @Column(name = "session_id")
    @ApiModelProperty(name = "sessionId", value = "会话ID")
    var sessionId: Int = -1

    constructor()
    constructor(
            id: Int = -1,
            userId: Int,
            sessionId: Int,

            gmtCreate: LocalDateTime = LocalDateTime.now(),
            gmtUpdate: LocalDateTime = LocalDateTime.now(),
            deleted: Boolean = false
    ) {
        this.id = id
        this.userId = userId
        this.sessionId = sessionId
        this.gmtCreate = gmtCreate
        this.gmtUpdate = gmtUpdate
        this.deleted = deleted
    }
}
package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModelProperty
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import vip.qsos.ktorm.module.AbsTable
import vip.qsos.ktorm.module.MBaseTable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

private const val TAB_NAME = "t_chat_session"

/**
 * @author : 华清松
 * @description : 聊天会话表
 */
object DBChatSession : MBaseTable<TableChatSession>(TAB_NAME) {
    val sessionId by int("id").primaryKey()
    val type by int("chat_type")
    val hashCode by varchar("hash_code")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): TableChatSession {
        return TableChatSession(
                sessionId = row[sessionId]!!,
                type = ChatSessionType.getEnumByIndex(row[type]!!),
                hashCode = row[hashCode]!!,

                gmtCreate = row[gmtCreate]!!,
                gmtUpdate = row[gmtUpdate]!!,
                deleted = row[deleted]!!
        )
    }

    override fun add(t: TableChatSession): Any {
        return this.insertAndGenerateKey {
            it.type to t.type.ordinal
            it.hashCode to t.hashCode
            it.gmtCreate to t.gmtCreate
            it.gmtUpdate to t.gmtUpdate
            it.deleted to t.deleted
        }
    }
}

@javax.persistence.Entity
@javax.persistence.Table(name = TAB_NAME)
class TableChatSession : AbsTable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "sessionId", value = "会话ID")
    var sessionId: Int = -1

    @Column(name = "chat_type")
    @ApiModelProperty(name = "type", value = "会话类型")
    var type: ChatSessionType = ChatSessionType.SINGLE

    @Column(name = "hash_code", unique = true)
    @ApiModelProperty(name = "hashCode", value = "会话唯一标识，以会话内成员id正序排列拼接所得，保证相同成员仅创建一个会话")
    var hashCode: String = ""

    constructor()
    constructor(
            sessionId: Int,
            type: ChatSessionType,
            hashCode: String,

            gmtCreate: LocalDateTime = LocalDateTime.now(),
            gmtUpdate: LocalDateTime = LocalDateTime.now(),
            deleted: Boolean = false
    ) {
        this.sessionId = sessionId
        this.type = type
        this.hashCode = hashCode

        this.gmtCreate = gmtCreate
        this.gmtUpdate = gmtUpdate
        this.deleted = deleted
    }
}
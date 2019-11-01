package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import vip.qsos.ktorm.module.AbsTable
import vip.qsos.ktorm.module.MBaseTable
import java.time.LocalDate
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
                type = ChatType.getEnumByIndex(row[type]!!),
                hashCode = row[hashCode]!!,

                gmtCreate = row[DBChatMessage.gmtCreate]!!,
                gmtUpdate = row[DBChatMessage.gmtUpdate]!!,
                deleted = row[DBChatMessage.deleted]!!
        )
    }
}

@javax.persistence.Entity
@javax.persistence.Table(name = TAB_NAME)
@ApiModel(value = "聊天会话实体")
class TableChatSession : AbsTable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "sessionId", value = "会话ID")
    var sessionId: Int = -1

    @Column(name = "chat_type")
    @ApiModelProperty(name = "type", value = "会话类型", dataType = "Enum")
    var type: ChatType = ChatType.SINGLE

    @Column(name = "hash_code", unique = true)
    @ApiModelProperty(name = "hashCode", value = "会话唯一判定值")
    var hashCode: String = ""

    constructor()
    constructor(
            sessionId: Int,
            type: ChatType,
            hashCode: String,

            gmtCreate: LocalDate = LocalDate.now(),
            gmtUpdate: LocalDate = LocalDate.now(),
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
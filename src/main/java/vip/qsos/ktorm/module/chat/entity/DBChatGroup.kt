package vip.qsos.ktorm.module.chat.entity

import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.insert
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import org.apache.commons.lang.StringUtils
import vip.qsos.ktorm.module.AbsTable
import vip.qsos.ktorm.module.MBaseTable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Id

private const val TAB_NAME = "t_chat_group"

/**
 * @author : 华清松
 * @description : 聊天群表
 */
object DBChatGroup : MBaseTable<TableChatGroup>(TAB_NAME) {
    val groupId by int("id").primaryKey()

    val name by varchar("name")
    val avatar by varchar("avatar")
    val notice by varchar("notice")
    val lastMessageId by int("last_message_id")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): TableChatGroup {
        return TableChatGroup(
                groupId = row[groupId]!!,
                name = row[name]!!,
                avatar = row[avatar]!!,
                notice = row[notice],
                lastMessageId = row[lastMessageId],
                gmtCreate = row[gmtCreate]!!,
                gmtUpdate = row[gmtUpdate]!!,
                deleted = row[deleted]!!
        )
    }

    override fun add(t: TableChatGroup): Any {
        this.insert {
            it.groupId to t.groupId
            it.name to t.name
            it.avatar to t.avatar
            it.notice to t.notice
            it.lastMessageId to t.lastMessageId
            it.gmtCreate to t.gmtCreate
            it.gmtUpdate to t.gmtUpdate
            it.deleted to t.deleted
        }
        return t.groupId
    }
}

@javax.persistence.Entity
@javax.persistence.Table(name = TAB_NAME)
class TableChatGroup : AbsTable {

    @Id
    @Column(name = "id", unique = true)
    var groupId: Int = -1

    @Column(name = "name")
    var name: String = ""

    @Column(name = "avatar")
    var avatar: String? = null
        get() {
            return if (StringUtils.isEmpty(field)) {
                "http://www.qsos.vip/upload/2018/11/ic_launcher20181225044818498.png"
            } else {
                field
            }
        }

    @Column(name = "notice")
    var notice: String? = null

    @Column(name = "last_message_id")
    var lastMessageId: Int? = null

    constructor()
    constructor(
            groupId: Int,
            name: String,
            avatar: String? = null,
            notice: String?,
            lastMessageId: Int? = null,

            gmtCreate: LocalDateTime = LocalDateTime.now(),
            gmtUpdate: LocalDateTime = LocalDateTime.now(),
            deleted: Boolean = false
    ) {
        this.groupId = groupId
        this.name = name
        this.avatar = avatar
        this.notice = notice
        this.lastMessageId = lastMessageId

        this.gmtCreate = gmtCreate
        this.gmtUpdate = gmtUpdate
        this.deleted = deleted
    }
}
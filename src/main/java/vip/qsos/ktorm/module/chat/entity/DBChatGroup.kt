package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import vip.qsos.ktorm.module.AbsTable
import vip.qsos.ktorm.module.MBaseTable
import vip.qsos.ktorm.module.file.entity.DBFileResource
import java.time.LocalDate
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

                gmtCreate = row[DBFileResource.gmtCreate]!!,
                gmtUpdate = row[DBFileResource.gmtUpdate]!!,
                deleted = row[DBFileResource.deleted]!!
        )
    }

    override fun add(t: TableChatGroup): Any {
        return this.insertAndGenerateKey {
            it.name to t.name
            it.avatar to t.avatar
            it.notice to t.notice
            it.lastMessageId to t.lastMessageId
            it.gmtCreate to t.gmtCreate
            it.gmtUpdate to t.gmtUpdate
            it.deleted to t.deleted
        }
    }
}

@javax.persistence.Entity
@javax.persistence.Table(name = TAB_NAME)
@ApiModel(value = "聊天群实体")
class TableChatGroup : AbsTable {

    @Id
    @Column(name = "id")
    @ApiModelProperty(name = "groupId", value = "聊天群ID，同sessionId")
    var groupId: Int = -1

    @Column(name = "name")
    @ApiModelProperty(name = "name", value = "群名称")
    var name: String = ""

    @Column(name = "avatar")
    @ApiModelProperty(name = "avatar", value = "群封面,默认http://www.qsos.vip/upload/2018/11/ic_launcher20181225044818498.png")
    var avatar: String = ""

    @Column(name = "notice")
    @ApiModelProperty(name = "notice", value = "群公告")
    var notice: String? = ""

    @Column(name = "last_message_id")
    @ApiModelProperty(name = "lastMessageId", value = "最后一条消息ID")
    var lastMessageId: Int? = null

    constructor()
    constructor(
            groupId: Int,
            name: String,
            avatar: String = "http://www.qsos.vip/upload/2018/11/ic_launcher20181225044818498.png",
            notice: String?,
            lastMessageId: Int? = null,

            gmtCreate: LocalDate = LocalDate.now(),
            gmtUpdate: LocalDate = LocalDate.now(),
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
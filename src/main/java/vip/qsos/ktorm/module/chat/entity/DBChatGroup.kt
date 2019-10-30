package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.schema.BaseTable
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.long
import me.liuwj.ktorm.schema.varchar
import javax.persistence.Column
import javax.persistence.Id

private const val TAB_NAME = "t_chat_group"

/**
 * @author : 华清松
 * @description : 聊天群表
 */
object DBChatGroup : BaseTable<TableChatGroup>(TAB_NAME) {
    val groupId by int("id").primaryKey()
    val name by varchar("name")
    val avatar by varchar("avatar")
    val createTime by long("create_time")
    val notice by varchar("notice")
    val lastMessageId by int("last_message_id")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): TableChatGroup {
        return TableChatGroup(
                groupId = row[groupId]!!,
                name = row[name]!!,
                avatar = row[avatar]!!,
                createTime = row[createTime]!!,
                notice = row[notice],
                lastMessageId = row[lastMessageId]
        )
    }
}

@javax.persistence.Entity
@javax.persistence.Table(name = TAB_NAME)
@ApiModel(value = "聊天群实体")
data class TableChatGroup(
        @Id
        @Column(name = "id")
        @ApiModelProperty(name = "groupId", value = "聊天群ID，同sessionId")
        val groupId: Int,

        @Column(name = "name")
        @ApiModelProperty(name = "name", value = "群名称")
        val name: String,

        @Column(name = "create_time")
        @ApiModelProperty(name = "createTime", value = "创建时间")
        val createTime: Long,

        @Column(name = "avatar")
        @ApiModelProperty(name = "avatar", value = "群封面,http://www.qsos.vip/upload/2018/11/ic_launcher20181225044818498.png")
        val avatar: String? = "http://www.qsos.vip/upload/2018/11/ic_launcher20181225044818498.png",

        @Column(name = "notice")
        @ApiModelProperty(name = "notice", value = "群公告")
        val notice: String? = null,

        @Column(name = "last_message_id")
        @ApiModelProperty(name = "lastMessageId", value = "最后一条消息ID")
        val lastMessageId: Int? = null
)
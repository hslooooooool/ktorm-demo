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
    val createTime by long("create_time")
    val notice by varchar("notice")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): TableChatGroup {
        return TableChatGroup(
                groupId = row[groupId]!!,
                name = row[name]!!,
                createTime = row[createTime]!!,
                notice = row[notice]
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

        @Column(name = "notice")
        @ApiModelProperty(name = "notice", value = "群公告", dataType = "String")
        val notice: String? = null
)
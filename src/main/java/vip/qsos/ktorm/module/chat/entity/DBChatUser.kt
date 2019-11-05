package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModel
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import org.apache.commons.lang.StringUtils
import vip.qsos.ktorm.module.AbsTable
import vip.qsos.ktorm.module.MBaseTable
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

private const val TAB_NAME = "t_chat_user"

/**
 * @author : 华清松
 * @description : 聊天用户表
 */
object DBChatUser : MBaseTable<TableChatUser>(TAB_NAME) {
    val userId by int("id").primaryKey()
    val userName by varchar("name")
    val avatar by varchar("avatar")
    val birth by varchar("birth")
    val sexuality by int("sexuality")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): TableChatUser {
        return TableChatUser(
                userId = row[userId]!!,
                userName = row[userName] ?: "用户" + row[userId],
                avatar = row[avatar],
                birth = row[birth],
                sexuality = row[sexuality]!!,

                gmtCreate = row[gmtCreate]!!,
                gmtUpdate = row[gmtUpdate]!!,
                deleted = row[deleted]!!
        )
    }

    override fun add(t: TableChatUser): Any {
        return this.insertAndGenerateKey {
            it.userName to t.userName
            it.avatar to t.avatar
            it.birth to t.birth
            it.sexuality to t.sexuality
            it.gmtCreate to t.gmtCreate
            it.gmtUpdate to t.gmtUpdate
            it.deleted to t.deleted
        }
    }
}

@javax.persistence.Entity
@javax.persistence.Table(name = TAB_NAME)
@ApiModel(value = "聊天用户实体")
class TableChatUser : AbsTable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var userId: Int = -1

    @Column(name = "name", nullable = false)
    var userName: String = ""

    @Column(name = "avatar")
    var avatar: String? = null
        get() {
            return if (StringUtils.isEmpty(field)) {
                "http://www.qsos.vip/upload/2018/11/ic_launcher20181225044818498.png"
            } else {
                field
            }
        }

    @Column(name = "birth")
    var birth: String? = ""

    @Column(name = "sexuality", nullable = false)
    var sexuality: Int = -1

    constructor()
    constructor(
            userId: Int = -1,
            userName: String,
            avatar: String? = null,
            birth: String? = null,
            sexuality: Int = -1,

            gmtCreate: LocalDateTime = LocalDateTime.now(),
            gmtUpdate: LocalDateTime = LocalDateTime.now(),
            deleted: Boolean = false
    ) {
        this.userId = userId
        this.userName = userName
        this.avatar = avatar
        this.birth = birth
        this.sexuality = sexuality

        this.gmtCreate = gmtCreate
        this.gmtUpdate = gmtUpdate
        this.deleted = deleted
    }
}
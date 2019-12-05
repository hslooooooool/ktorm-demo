package vip.qsos.ktorm.module.file.entity

import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import vip.qsos.ktorm.module.AbsTable
import vip.qsos.ktorm.module.MBaseTable
import java.time.LocalDateTime
import javax.persistence.*

private const val TAB_NAME = "sys_resource"

/**
 * @author : 华清松
 * @description : 文件资源表
 */
object DBFileResource : MBaseTable<TableFileResource>(TAB_NAME) {
    val fileId by int("id").primaryKey()
    val avatar by varchar("avatar")
    val url by varchar("file_url")
    val filename by varchar("file_name")
    val type by varchar("file_type")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): TableFileResource {
        return TableFileResource(
                fileId = row[fileId]!!,
                avatar = row[avatar]!!,
                url = row[url]!!,
                filename = row[filename]!!,
                type = row[type],

                gmtCreate = row[gmtCreate]!!,
                gmtUpdate = row[gmtUpdate]!!,
                deleted = row[deleted]!!
        )
    }

    override fun add(t: TableFileResource): Any {
        return this.insertAndGenerateKey {
            it.avatar to t.avatar
            it.url to t.url
            it.filename to t.filename
            it.type to t.type
            it.gmtCreate to t.gmtCreate
            it.gmtUpdate to t.gmtUpdate
            it.deleted to t.deleted
        }
    }
}

@Entity
@Table(name = TAB_NAME)
class TableFileResource : AbsTable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var fileId: Int = -1

    @Column(name = "avatar")
    var avatar: String? = null

    @Column(name = "file_url")
    var url: String? = null

    @Column(name = "file_name")
    var filename: String? = null

    @Column(name = "file_type")
    var type: String? = null

    constructor()
    constructor(
            fileId: Int = -1,
            avatar: String?,
            url: String?,
            filename: String?,
            type: String?,

            gmtCreate: LocalDateTime = LocalDateTime.now(),
            gmtUpdate: LocalDateTime = LocalDateTime.now(),
            deleted: Boolean = false
    ) {
        this.fileId = fileId
        this.avatar = avatar
        this.url = url
        this.filename = filename
        this.type = type

        this.gmtCreate = gmtCreate
        this.gmtUpdate = gmtUpdate
        this.deleted = deleted
    }
}
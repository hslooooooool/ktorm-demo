package vip.qsos.ktorm.module.file.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import vip.qsos.ktorm.module.AbsTable
import vip.qsos.ktorm.module.MBaseTable
import java.time.LocalDate
import javax.persistence.*

private const val TAB_NAME = "sys_resource"

/**
 * @author : 华清松
 * @description : 文件资源表
 */
object DBFileResource : MBaseTable<TableFileResource>(TAB_NAME) {
    val fileId by int("id").primaryKey()
    val url by varchar("file_url")
    val filename by varchar("file_name")
    val type by varchar("type")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): TableFileResource {
        return TableFileResource(
                fileId = row[fileId]!!,
                url = row[url]!!,
                filename = row[filename]!!,
                type = row[type]!!,

                gmtCreate = row[gmtCreate]!!,
                gmtUpdate = row[gmtUpdate]!!,
                deleted = row[deleted]!!
        )
    }

    override fun add(t: TableFileResource): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

@Entity
@Table(name = TAB_NAME)
@ApiModel(value = "文件对象")
class TableFileResource : AbsTable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(name = "fileId", value = "文件ID")
    var fileId: Int = -1

    @Column(name = "file_url")
    @ApiModelProperty(name = "url", value = "文件链接")
    var url: String? = null

    @Column(name = "file_name")
    @ApiModelProperty(name = "filename", value = "文件名称")
    var filename: String? = null

    @Column(name = "file_type")
    @ApiModelProperty(name = "type", value = "文件类型")
    var type: String? = null

    constructor()
    constructor(
            fileId: Int = -1,
            url: String?,
            filename: String?,
            type: String?,

            gmtCreate: LocalDate = LocalDate.now(),
            gmtUpdate: LocalDate = LocalDate.now(),
            deleted: Boolean = false
    ) {
        this.fileId = fileId
        this.url = url
        this.filename = filename
        this.type = type

        this.gmtCreate = gmtCreate
        this.gmtUpdate = gmtUpdate
        this.deleted = deleted
    }
}

data class HttpFileEntity(
        var url: String?,
        var filename: String?,
        var type: String? = null
)
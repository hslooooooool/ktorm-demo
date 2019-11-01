package vip.qsos.ktorm.module

import io.swagger.annotations.ApiModelProperty
import me.liuwj.ktorm.dsl.insertAndGenerateKey
import me.liuwj.ktorm.schema.BaseTable
import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.date
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.MappedSuperclass

/**
 * @author : 华清松
 * @description : 数据库表通用字段
 */
@MappedSuperclass
abstract class AbsTable(
        @Column(name = "gmt_create", nullable = false)
        @ApiModelProperty(name = "gmtCreate", value = "创建时间")
        var gmtCreate: LocalDate = LocalDate.now(),

        @Column(name = "gmt_update", nullable = false)
        @ApiModelProperty(name = "gmtUpdate", value = "更新时间")
        var gmtUpdate: LocalDate = LocalDate.now(),

        @Column(name = "deleted", nullable = false)
        @ApiModelProperty(name = "deleted", value = "逻辑删除")
        var deleted: Boolean = false
)

abstract class MBaseTable<T : AbsTable>(name: String) : BaseTable<T>(name) {
    val gmtCreate by date("gmt_create")
    val gmtUpdate by date("gmt_update")
    val deleted by boolean("deleted")

    fun add(t: T): Any {
        return this.insertAndGenerateKey {
            it to t
        }
    }

}
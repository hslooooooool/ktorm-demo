package vip.qsos.ktorm.module

import me.liuwj.ktorm.schema.BaseTable
import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.datetime
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.MappedSuperclass

/**
 * @author : 华清松
 * @description : 数据库表通用字段
 */
@MappedSuperclass
abstract class AbsTable(
        @Column(name = "gmt_create", nullable = false)
        var gmtCreate: LocalDateTime = LocalDateTime.now(),

        @Column(name = "gmt_update", nullable = false)
        var gmtUpdate: LocalDateTime = LocalDateTime.now(),

        @Column(name = "deleted", nullable = false)
        var deleted: Boolean = false
)

abstract class MBaseTable<T : AbsTable>(name: String) : BaseTable<T>(name) {
    val gmtCreate by datetime("gmt_create")
    val gmtUpdate by datetime("gmt_update")
    val deleted by boolean("deleted")

    abstract fun add(t: T): Any

}
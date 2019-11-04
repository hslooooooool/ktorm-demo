package vip.qsos.ktorm.module

import me.liuwj.ktorm.schema.BaseTable
import me.liuwj.ktorm.schema.boolean
import me.liuwj.ktorm.schema.date
import org.hibernate.annotations.DynamicInsert
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.MappedSuperclass

/**
 * @author : 华清松
 * @description : 数据库表通用字段
 */
@DynamicInsert
@DynamicUpdate
@MappedSuperclass
abstract class AbsTable(
        @Column(name = "gmt_create", nullable = false)
        var gmtCreate: LocalDate = LocalDate.now(),

        @Column(name = "gmt_update", nullable = false)
        var gmtUpdate: LocalDate = LocalDate.now(),

        @Column(name = "deleted", nullable = false)
        var deleted: Boolean = false
)

abstract class MBaseTable<T : AbsTable>(name: String) : BaseTable<T>(name) {
    val gmtCreate by date("gmt_create")
    val gmtUpdate by date("gmt_update")
    val deleted by boolean("deleted")

    abstract fun add(t: T): Any

}
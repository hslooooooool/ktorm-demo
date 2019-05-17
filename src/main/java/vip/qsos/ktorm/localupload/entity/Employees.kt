package vip.qsos.ktorm.localupload.entity

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.*
import java.time.LocalDate

/**
 * @author : 华清松
 * @date : 2019-05-17
 * @description : TODO 类说明，描述此类的类型和用途
 */
object Employees : Table<Employee>("t_employee") {
    val id by int("id").primaryKey().bindTo { it.id }
    val name by varchar("name").bindTo { it.name }
    val job by varchar("job").bindTo { it.job }
    val managerId by int("manager_id").bindTo { it.manager?.id }
    val hireDate by date("hire_date").bindTo { it.hireDate }
    val salary by long("salary").bindTo { it.salary }
}

interface Employee : Entity<Employee> {

    companion object : Entity.Factory<Employee>()

    val id: Int?

    var name: String

    var job: String?

    var manager: Employee?

    var hireDate: LocalDate?

    var salary: Long?

}
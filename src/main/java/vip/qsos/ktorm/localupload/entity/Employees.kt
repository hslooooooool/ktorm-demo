package vip.qsos.ktorm.localupload.entity

import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.*
import java.time.LocalDate

/**
 * @author : 华清松
 * @date : 2019-05-17
 * @description : TODO 类说明，描述此类的类型和用途
 */
object Employees : Table<Employee>("T_EMPLOYEE") {
    val id by int("id").primaryKey()
    val name by varchar("name")
    val job by varchar("job")
    val managerId by int("manager_id")
    val hireDate by date("hire_date")
    val salary by long("salary")
    val departmentId by int("department_id"
    )
}

interface Employee : Entity<Employee> {

    companion object : Entity.Factory<Employee>()

    val id: Int?

    var name: String

    var job: String

    var manager: Employee?

    var hireDate: LocalDate

    var salary: Long

}
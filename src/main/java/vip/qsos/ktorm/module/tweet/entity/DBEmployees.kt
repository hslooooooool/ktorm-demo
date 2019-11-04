package vip.qsos.ktorm.module.tweet.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import me.liuwj.ktorm.entity.Entity
import me.liuwj.ktorm.schema.Table
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

private const val TAB_NAME = "t_employee"

/**
 * @author : 华清松
 * @date : 2019-05-17
 * @description : TODO 类说明，描述此类的类型和用途
 */
object DBEmployees : Table<TableEmployee>(TAB_NAME) {
    val id by int("id").primaryKey().bindTo { it.id }
    val name by varchar("name").bindTo { it.name }
    val job by varchar("job").bindTo { it.job }
    val head by varchar("head").bindTo { it.head }
    val managerId by int("manager_id").bindTo { it.managerId }
}

@javax.persistence.Entity
@javax.persistence.Table(name = TAB_NAME)
@ApiModel(description = "雇员")
data class Employee(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(name = "id", value = "雇员ID")
        var id: Int? = null,
        @Column(name = "name")
        @ApiModelProperty(name = "name", value = "雇员名称")
        var name: String,
        @Column(name = "manager_id")
        @ApiModelProperty(name = "managerId", value = "管理者ID")
        var managerId: Int?,
        @Column(name = "job")
        @ApiModelProperty(name = "job", value = "雇员职位")
        var job: String?,
        @Column(name = "head")
        @ApiModelProperty(name = "head", value = "雇员头像")
        var head: String?
) {
    fun toTable(): TableEmployee {
        val mTableEmployee = TableEmployee()
        mTableEmployee.id = id
        mTableEmployee.name = name
        mTableEmployee.managerId = managerId
        mTableEmployee.job = job
        mTableEmployee.head = head
        return mTableEmployee
    }
}

interface TableEmployee : Entity<TableEmployee> {
    companion object : Entity.Factory<TableEmployee>()

    var id: Int?
    var name: String
    var managerId: Int?
    var job: String?
    var head: String?
}
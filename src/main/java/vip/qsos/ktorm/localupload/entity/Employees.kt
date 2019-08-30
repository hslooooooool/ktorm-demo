package vip.qsos.ktorm.localupload.entity

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
object Employees : Table<IEmployee>(TAB_NAME) {
    val id by int("id").primaryKey().bindTo { it.id }
    val name by varchar("name").bindTo { it.name }
    val job by varchar("job").bindTo { it.job }
    val head by varchar("head").bindTo { it.head }
    val managerId by int("manager_id").bindTo { it.managerId }
}

interface IEmployee : Entity<IEmployee> {

    companion object : Entity.Factory<IEmployee>()

    val id: Int?
    var name: String
    var managerId: Int?
    var job: String?
    var head: String?
}

@javax.persistence.Entity
@javax.persistence.Table(name = TAB_NAME)
@ApiModel(value = "雇员实体")
data class TableEmployee(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(name = "id", value = "雇员ID", dataType = "Int")
        var id: Int?,
        @Column(name = "name")
        @ApiModelProperty(name = "name", value = "雇员名称", dataType = "String")
        var name: String,
        @Column(name = "manager_id")
        @ApiModelProperty(name = "managerId", value = "管理者ID", dataType = "Int")
        var managerId: Int?,
        @Column(name = "job")
        var job: String?,
        @Column(name = "head")
        var head: String?
) {
    companion object {
        fun trans(e: IEmployee): TableEmployee {
            return TableEmployee(e.id, e.name, e.managerId, e.job, e.head)
        }
    }
}
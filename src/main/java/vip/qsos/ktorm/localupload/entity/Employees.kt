package vip.qsos.ktorm.localupload.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.schema.BaseTable
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
object Employees : BaseTable<TableEmployee>(TAB_NAME) {
    val id by int("id").primaryKey()
    val name by varchar("name")
    val job by varchar("job")
    val head by varchar("head")
    val managerId by int("manager_id")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): TableEmployee {
        return TableEmployee(
                id = row[id],
                name = row[name] ?: "",
                job = row[job] ?: "",
                head = row[head] ?: "",
                managerId = row[managerId]
        )
    }
}

@javax.persistence.Entity
@javax.persistence.Table(name = TAB_NAME)
@ApiModel(value = "雇员实体")
data class TableEmployee(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(name = "id", value = "雇员ID", dataType = "Int")
        var id: Int? = null,
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
)
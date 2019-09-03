package vip.qsos.ktorm.localupload.controller

import io.swagger.annotations.ApiOperation
import me.liuwj.ktorm.dsl.delete
import me.liuwj.ktorm.dsl.deleteAll
import me.liuwj.ktorm.dsl.eq
import me.liuwj.ktorm.dsl.update
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.findAll
import me.liuwj.ktorm.entity.findOne
import org.springframework.web.bind.annotation.*
import vip.qsos.ktorm.localupload.entity.Employees
import vip.qsos.ktorm.localupload.entity.IEmployee
import vip.qsos.ktorm.localupload.entity.TableEmployee
import vip.qsos.ktorm.util.Result

@RestController
class TestController : BaseController() {

    @PostMapping("/add")
    @ApiOperation(value = "测试接口")
    fun add(): Result<TableEmployee> {
        val e = IEmployee()
        e.name = "Name"
        e.job = "JOB2"
        e.head = "http://img.sccnn.com/bimg/338/42729.jpg"
        e.managerId = 1
        val result = TableEmployee(Employees.add(e), e.name, e.managerId, e.job, e.head)
        println("新增1条数据")
        return Result<TableEmployee>().result(result)
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "测试接口")
    fun delete(): Result<String> {
        Employees.delete { it.name.eq("JOB2") }
        println("删除1条数据")
        return Result<String>().result("删除成功")
    }

    @DeleteMapping("/clear")
    @ApiOperation(value = "测试接口")
    fun clear(): Result<String> {
        Employees.deleteAll()
        println("清除数据")
        return Result<String>().result("清除数据")
    }

    @PutMapping("/update")
    @ApiOperation(value = "测试接口")
    fun update(@RequestBody em: IEmployee): Result<IEmployee> {
        Employees.update {
            it.name to em.name
            it.job to em.job
            it.managerId to em.managerId

            where {
                it.id eq em.id!!
            }
        }
        println("更新数据")
        return Result<IEmployee>().result(em)
    }

    @GetMapping("/list")
    @ApiOperation(value = "测试接口")
    fun findAll(): Result<List<TableEmployee>> {
        val testList = Employees.findAll().map { TableEmployee.trans(it) }
        println("查询到数据 ${testList.size} 条")
        return Result<List<TableEmployee>>().result(testList)
    }

    @GetMapping("/one")
    @ApiOperation(value = "测试接口")
    fun findOne(): Result<TableEmployee> {
        val one = Employees.findOne {
            it.name eq "Name"
        }
        return if (one == null) {
            Result<TableEmployee>().error(500, "无法找到")
        } else {
            TableEmployee.trans(one)
            Result<TableEmployee>().result(TableEmployee.trans(one))
        }

    }

}
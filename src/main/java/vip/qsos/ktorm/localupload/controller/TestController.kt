package vip.qsos.ktorm.localupload.controller

import io.swagger.annotations.ApiOperation
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.findAll
import me.liuwj.ktorm.entity.findById
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import vip.qsos.ktorm.localupload.entity.Employees
import vip.qsos.ktorm.localupload.entity.IEmployee
import vip.qsos.ktorm.localupload.entity.TableEmployee
import vip.qsos.ktorm.util.Result

@RestController
class TestController : BaseController() {

    @GetMapping("/list")
    @ApiOperation(value = "测试接口", notes = "仅用于框架测试连通性")
    fun test(): Result<List<TableEmployee>> {
        val e = IEmployee()
        e.name = "Name"
        e.job = "JOB"
        e.head = "http://blog.qsos.vip/upload/2018/11/ic_launcher20181225044818498.png"
        e.managerId = 1
        Employees.add(e)
        println("新增1条数据")

        val testList = Employees.findAll().map { TableEmployee.trans(it) }

        println("查询到数据 ${testList.size} 条")
        return Result<List<TableEmployee>>().result(testList)
    }

    @GetMapping("/one")
    @ApiOperation(value = "测试接口", notes = "仅用于框架测试连通性")
    fun getOne(): Result<TableEmployee> {
        Employees.findById(1).let {
            return if (it == null) {
                Result<TableEmployee>().error(500, "无法找到")
            } else {
                TableEmployee.trans(it)
                Result<TableEmployee>().result(TableEmployee.trans(it))
            }
        }

    }

}
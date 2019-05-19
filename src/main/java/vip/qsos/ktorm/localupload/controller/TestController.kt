package vip.qsos.ktorm.localupload.controller

import io.swagger.annotations.ApiOperation
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.findAll
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import vip.qsos.ktorm.localupload.entity.Employees
import vip.qsos.ktorm.localupload.entity.IEmployee
import vip.qsos.ktorm.localupload.entity.TableEmployee
import vip.qsos.ktorm.util.Result

@RestController
class TestController : BaseController() {

    @GetMapping("/test")
    @ApiOperation(value = "测试接口", notes = "仅用于框架测试连通性")
    fun test(): Result<TableEmployee> {
        val e = IEmployee()
        e.name = "Name"
        e.job = "JOB"
        Employees.add(e)
        println("新增1条数据")

        val testList = Employees.findAll().map { TableEmployee.trans(it) }

        println("查询到数据 ${testList.size} 条")
        return Result<TableEmployee>().result(testList)
    }

}
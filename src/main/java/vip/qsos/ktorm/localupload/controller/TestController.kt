package vip.qsos.ktorm.localupload.controller

import com.sun.tracing.dtrace.ModuleName
import io.swagger.annotations.ApiOperation
import me.liuwj.ktorm.entity.add
import me.liuwj.ktorm.entity.findAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import vip.qsos.ktorm.localupload.entity.Employee
import vip.qsos.ktorm.localupload.entity.Employees
import vip.qsos.ktorm.util.Result

@ModuleName("TEST")
@RestController
class TestController
@Autowired
constructor() : BaseController() {

    @GetMapping("/test")
    @ApiOperation(value = "测试接口", notes = "仅用于框架测试连通性")
    fun test(): Result {
        for (i in 0..4) {
            val e = Employee()
            e.name = "Name$i"
            e.job = "JOB$i"
            Employees.add(e)
            println("新增数据 $i")
        }
        val testList = Employees.findAll()
        println("查询到数据 ${testList.size} 条")
        return Result.ok("数据库查询成功").add("results", testList)
    }

}
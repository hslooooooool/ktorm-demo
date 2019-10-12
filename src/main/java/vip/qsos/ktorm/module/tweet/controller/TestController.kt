package vip.qsos.ktorm.module.tweet.controller

import io.swagger.annotations.ApiOperation
import me.liuwj.ktorm.dsl.*
import me.liuwj.ktorm.entity.findAll
import me.liuwj.ktorm.entity.findOne
import org.springframework.web.bind.annotation.*
import vip.qsos.ktorm.module.tweet.entity.DBEmployees
import vip.qsos.ktorm.module.tweet.entity.TableEmployee
import vip.qsos.ktorm.util.MResult

@RestController
@RequestMapping("/tweet")
open class TestController {

    @PostMapping("/add")
    @ApiOperation(value = "测试接口")
    open fun add(): MResult<TableEmployee> {
        val e = TableEmployee(name = "Name", job = "JOB2", head = "http://img.sccnn.com/bimg/338/42729.jpg", managerId = 1)
        val result = DBEmployees.insert {
            it.name to e.name
            it.head to e.head
            it.job to e.job
            it.managerId to e.managerId
        }
        e.id = result
        println("新增1条数据")
        return MResult<TableEmployee>().result(e)
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "测试接口")
    open fun delete(): MResult<String> {
        DBEmployees.delete { it.name.eq("JOB2") }
        println("删除1条数据")
        return MResult<String>().result("删除成功")
    }

    @DeleteMapping("/clear")
    @ApiOperation(value = "测试接口")
    open fun clear(): MResult<String> {
        DBEmployees.deleteAll()
        println("清除数据")
        return MResult<String>().result("清除数据")
    }

    @PutMapping("/update")
    @ApiOperation(value = "测试接口")
    open fun update(@RequestBody em: TableEmployee): MResult<TableEmployee> {
        DBEmployees.update {
            it.name to em.name
            it.job to em.job
            it.managerId to em.managerId

            where {
                it.id eq em.id!!
            }
        }
        println("更新数据")
        return MResult<TableEmployee>().result(em)
    }

    @GetMapping("/list")
    @ApiOperation(value = "测试接口")
    open fun findAll(): MResult<List<TableEmployee>> {
        val testList = DBEmployees.findAll()
        println("查询到数据 ${testList.size} 条")
        return MResult<List<TableEmployee>>().result(testList)
    }

    @GetMapping("/one")
    @ApiOperation(value = "测试接口")
    open fun findOne(): MResult<TableEmployee> {
        val one = DBEmployees.findOne {
            it.name eq "Name"
        }
        return if (one == null) {
            MResult<TableEmployee>().error(500, "无法找到")
        } else {
            MResult<TableEmployee>().result(one)
        }

    }

}
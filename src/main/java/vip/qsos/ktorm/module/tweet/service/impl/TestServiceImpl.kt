package vip.qsos.ktorm.module.tweet.service.impl

import me.liuwj.ktorm.entity.findAll
import org.springframework.stereotype.Service
import vip.qsos.ktorm.module.tweet.entity.DBEmployees
import vip.qsos.ktorm.module.tweet.entity.TableEmployee
import vip.qsos.ktorm.module.tweet.service.TestService

/**
 * @author : 华清松
 * @date : 2019-05-17
 * @description : TODO 类说明，描述此类的类型和用途
 */
@Service
open class TestServiceImpl : TestService {

    override fun findAll(): List<TableEmployee> {
        return DBEmployees.findAll()
    }

    override fun inset() {

    }
}

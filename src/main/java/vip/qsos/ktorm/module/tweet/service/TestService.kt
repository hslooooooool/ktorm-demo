package vip.qsos.ktorm.module.tweet.service

import vip.qsos.ktorm.module.tweet.entity.TableEmployee

/**
 * @author : 华清松
 * @date : 2019-05-17
 * @description : TODO 类说明，描述此类的类型和用途
 */
interface TestService {
    fun findAll(): List<TableEmployee>

    fun inset()
}
package vip.qsos.ktorm.localupload.service

import vip.qsos.ktorm.localupload.entity.TableEmployee

/**
 * @author : 华清松
 * @date : 2019-05-17
 * @description : TODO 类说明，描述此类的类型和用途
 */
interface TestService {
    fun findAll(): List<TableEmployee>

    fun inset()
}
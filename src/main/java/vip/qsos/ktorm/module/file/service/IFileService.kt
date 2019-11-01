package vip.qsos.ktorm.module.file.service

import vip.qsos.ktorm.module.file.entity.HttpFileEntity
import vip.qsos.ktorm.module.file.entity.TableFileResource

/**
 * @author : 华清松
 * @date : 2018/12/28
 * @description : TODO类说明
 */
interface IFileService {

    fun save(files: List<TableFileResource>): List<HttpFileEntity>
}
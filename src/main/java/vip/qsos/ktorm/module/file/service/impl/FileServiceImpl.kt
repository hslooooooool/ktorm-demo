package vip.qsos.ktorm.module.file.service.impl

import me.liuwj.ktorm.dsl.insert
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import vip.qsos.ktorm.exception.BaseException
import vip.qsos.ktorm.module.file.entity.DBFileResource
import vip.qsos.ktorm.module.file.entity.HttpFileEntity
import vip.qsos.ktorm.module.file.entity.TableFileResource
import vip.qsos.ktorm.module.file.service.IFileService

/**
 * @author : 华清松
 * @date : 2018/12/28
 * @description : TODO类说明
 */
@Service("FileService")
open class FileServiceImpl : IFileService {

    @Transactional(rollbackFor = [BaseException::class])
    override fun save(files: List<TableFileResource>): List<HttpFileEntity> {
        val result = arrayListOf<HttpFileEntity>()
        files.forEach { file ->
            DBFileResource.insert {
                it to file
            }
            result.add(HttpFileEntity(file.url, file.filename))
        }
        return result
    }
}

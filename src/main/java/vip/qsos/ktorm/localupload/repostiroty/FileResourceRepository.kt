package vip.qsos.ktorm.localupload.repostiroty

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import vip.qsos.ktorm.localupload.entity.FileResourceEntity

/**
 * @author : 华清松
 * @date : 2019-05-17
 * @description : TODO 类说明，描述此类的类型和用途
 */
interface FileResourceRepository : JpaRepository<FileResourceEntity, Long>, JpaSpecificationExecutor<FileResourceEntity>
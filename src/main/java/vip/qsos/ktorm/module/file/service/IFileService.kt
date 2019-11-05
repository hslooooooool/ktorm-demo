package vip.qsos.ktorm.module.file.service

import org.springframework.web.multipart.MultipartFile
import vip.qsos.ktorm.module.file.entity.FileResourceBo
import javax.servlet.http.HttpServletResponse

/**
 * @author : 华清松
 * @description : 文件数据库操作
 */
interface IFileService {
    val mFileIOService: IFileIOService
    /**新增文件*/
    fun upload(files: List<MultipartFile>): List<FileResourceBo>

    /**文件下载*/
    fun downLoad(httpResponse: HttpServletResponse, url: String)
}
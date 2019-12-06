package vip.qsos.ktorm.module.file.service

import org.springframework.web.multipart.MultipartFile
import vip.qsos.ktorm.exception.BaseException
import vip.qsos.ktorm.module.file.entity.FileResourceBo
import vip.qsos.ktorm.module.file.entity.TableFileResource
import java.io.IOException
import javax.servlet.http.HttpServletResponse

/**
 * @author : 华清松
 * @description : 文件数据IO操作
 */
interface IFileIOService {

    /**下载资源*/
    @Throws(BaseException::class)
    fun getData(httpResponse: HttpServletResponse, url: String)

    /**保存资源*/
    @Throws(BaseException::class)
    fun saveData(multipartFile: List<MultipartFile>): List<FileResourceBo>

}
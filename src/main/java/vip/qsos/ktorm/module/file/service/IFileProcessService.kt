package vip.qsos.ktorm.module.file.service

import org.springframework.web.multipart.MultipartFile
import vip.qsos.ktorm.exception.BaseException
import vip.qsos.ktorm.module.file.entity.HttpFileEntity
import vip.qsos.ktorm.module.file.entity.TableFileResource
import java.io.IOException
import javax.servlet.http.HttpServletResponse

/**
 * @author : 华清松
 * @date : 2018/12/28
 * @description : TODO类说明
 */
interface IFileProcessService {

    /**下载资源*/
    @Throws(IOException::class)
    fun getData(httpResponse: HttpServletResponse, url: String)

    /**保存资源*/
    @Throws(BaseException::class)
    fun saveData(multipartFile: Array<MultipartFile>): List<TableFileResource>


}
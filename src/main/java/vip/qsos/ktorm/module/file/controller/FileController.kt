package vip.qsos.ktorm.module.file.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.multipart.MultipartHttpServletRequest
import vip.qsos.ktorm.exception.BaseException
import vip.qsos.ktorm.module.file.entity.FileResourceBo
import vip.qsos.ktorm.module.file.service.IFileService
import vip.qsos.ktorm.util.LogUtils
import vip.qsos.ktorm.util.MResult
import javax.servlet.http.HttpServletResponse

@RestController
class FileController @Autowired constructor(
        private val fileService: IFileService
) : IFileController {

    override fun upLoad(file: MultipartFile): MResult<FileResourceBo> {
        val result = fileService.upload(arrayListOf(file))
        result.forEach {
            LogUtils.i("上传了文件${it.filename}")
        }
        return MResult<FileResourceBo>().result(result[0])
    }

    override fun upLoads(request: MultipartHttpServletRequest): MResult<List<FileResourceBo>> {
        val files = request.multiFileMap["file"]
        if (files.isNullOrEmpty()) {
            throw BaseException("文件上传不能为空")
        }
        val result = fileService.upload(files)
        result.forEach {
            LogUtils.i("上传了文件${it.filename}")
        }
        return MResult<List<FileResourceBo>>().result(result)
    }

    override fun downLoad(httpResponse: HttpServletResponse, url: String) {
        fileService.downLoad(httpResponse, url)
    }

}

package vip.qsos.ktorm.module.file.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import vip.qsos.ktorm.module.file.entity.FileResourceBo
import vip.qsos.ktorm.util.MResult
import javax.servlet.http.HttpServletResponse

@Api(tags = ["文件接口"])
@RequestMapping("/upload")
interface IFileController {

    @PostMapping("/file", consumes = ["multipart/*"], headers = ["content-type=multipart/form-data"])
    @ApiOperation(value = "单文件上传")
    fun upLoad(@RequestParam("file") file: MultipartFile): MResult<FileResourceBo>

    @PostMapping("/files", consumes = ["multipart/*"], headers = ["content-type=multipart/form-data"])
    @ApiOperation(value = "多文件上传")
    fun upLoads(@RequestParam("files") files: Array<MultipartFile>): MResult<List<FileResourceBo>>

    @GetMapping("/file")
    @ApiOperation(value = "单文件下载")
    fun downLoad(httpResponse: HttpServletResponse, url: String)

}

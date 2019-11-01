package vip.qsos.ktorm.module.file.controller

import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import vip.qsos.ktorm.module.BaseController
import vip.qsos.ktorm.module.file.entity.HttpFileEntity
import vip.qsos.ktorm.module.file.service.IFileProcessService
import vip.qsos.ktorm.module.file.service.IFileService
import vip.qsos.ktorm.util.MResult
import javax.servlet.http.HttpServletResponse

/**
 * @author : 华清松
 * @date : 2018/12/28
 * @description : TODO类说明
 */
@RestController
@RequestMapping("/upload")
class FileController @Autowired constructor(
        private val fileService: IFileService,
        private val fileFileProcessService: IFileProcessService
) : BaseController() {

    @PostMapping("/file", consumes = ["multipart/*"], headers = ["content-type=multipart/form-data"])
    @ApiOperation(value = "单文件上传")
    @ApiImplicitParam("文件", name = "multipartFile", type = "MultipartFile")
    fun upLoad(@RequestPart multipartFile: MultipartFile): MResult<HttpFileEntity> {
        return getResult(multipartFile)
    }

    @PostMapping("/files", consumes = ["multipart/*"], headers = ["content-type=multipart/form-data"])
    @ApiOperation(value = "多文件上传")
    fun upLoads(@RequestPart multipartFile: Array<MultipartFile>): MResult<List<HttpFileEntity>> {
        return getResult(multipartFile)
    }

    @GetMapping("/file")
    fun downLoad(httpResponse: HttpServletResponse, url: String) {
        fileFileProcessService.getData(httpResponse, url)
    }

    private fun getResult(multipartFile: MultipartFile): MResult<HttpFileEntity> {
        val fileList = fileFileProcessService.saveData(arrayOf(multipartFile))
        val result = fileService.save(fileList)
        return MResult<HttpFileEntity>().result(result[0])
    }

    private fun getResult(multipartFile: Array<MultipartFile>): MResult<List<HttpFileEntity>> {
        val fileList = fileFileProcessService.saveData(multipartFile)
        val result = fileService.save(fileList)
        return MResult<List<HttpFileEntity>>().result(result)
    }
}

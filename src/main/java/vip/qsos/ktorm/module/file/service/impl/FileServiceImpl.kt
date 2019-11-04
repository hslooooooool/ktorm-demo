package vip.qsos.ktorm.module.file.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import vip.qsos.ktorm.exception.BaseException
import vip.qsos.ktorm.module.file.entity.DBFileResource
import vip.qsos.ktorm.module.file.entity.FileResourceBo
import vip.qsos.ktorm.module.file.service.IFileIOService
import vip.qsos.ktorm.module.file.service.IFileService
import javax.servlet.http.HttpServletResponse

@Service
open class FileServiceImpl @Autowired constructor(
        override val mFileIOService: IFileIOService
) : IFileService {

    @Transactional(rollbackFor = [BaseException::class])
    override fun upload(files: Array<MultipartFile>): List<FileResourceBo> {
        val saveFiles = mFileIOService.saveData(files)
        val result = arrayListOf<FileResourceBo>()
        saveFiles.forEach {
            val table = it.toTable()
            table.fileId = DBFileResource.add(table) as Int
            result.add(FileResourceBo().getBo(table) as FileResourceBo)
        }
        return result
    }

    override fun downLoad(httpResponse: HttpServletResponse, url: String) {
        mFileIOService.getData(httpResponse, url)
    }
}

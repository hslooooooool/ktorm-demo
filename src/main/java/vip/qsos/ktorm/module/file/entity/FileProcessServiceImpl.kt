package vip.qsos.ktorm.module.file.entity

import net.coobird.thumbnailator.Thumbnails
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import vip.qsos.ktorm.exception.BaseException
import vip.qsos.ktorm.module.file.service.IFileProcessService
import vip.qsos.ktorm.util.DateUtils
import java.io.*
import java.util.*
import javax.servlet.http.HttpServletResponse

/**
 * @author : 华清松
 * @date : 2018/12/20
 * @description : 文件保存处理
 */
@Service("FileProcessService")
@ConfigurationProperties(prefix = "stream.http.multipart")
open class FileProcessServiceImpl(private var location: String? = null) : IFileProcessService {

    /**下载资源*/
    @Throws(IOException::class)
    override fun getData(httpResponse: HttpServletResponse, url: String) {
        var fileInputStream: FileInputStream? = null
        var outputStream: OutputStream? = null
        try {
            val fileName = url.substring(url.lastIndexOf(File.separator) + 1)
            httpResponse.characterEncoding = "utf-8"
            httpResponse.contentType = "application/x-msdownload"
            // 设置文件名
            httpResponse.setHeader("Content-Disposition", "attachment;filename=$fileName")
            fileInputStream = FileInputStream(url)
            outputStream = httpResponse.outputStream
            val data = ByteArray(1024)
            var len: Int = fileInputStream.read(data)
            while (len != -1) {
                len = fileInputStream.read(data)
                outputStream!!.write(data, 0, len)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fileInputStream?.close()
            outputStream?.close()
        }
    }

    /**保存资源*/
    @Throws(BaseException::class)
    override fun saveData(multipartFile: Array<MultipartFile>): List<TableFileResource> {
        val fileList = ArrayList<TableFileResource>()
        var outputStream: FileOutputStream? = null
        var inputStream: InputStream? = null
        if (multipartFile.isEmpty()) {
            throw BaseException("文件上传不能为空")
        }
        for (file in multipartFile) {
            val dateFolder = DateUtils.format(Date(), "yyyyMMdd")
            val folderUrl = location!! + dateFolder!!
            val folder = File(folderUrl)
            if (!folder.exists()) {
                folder.mkdirs()
            }
            val fileInfo = getUUIDFileName(file)
            val uuidFileName = fileInfo[0] + "." + fileInfo[1]
            val originalUrl = "$folderUrl/$uuidFileName"
            inputStream = file.inputStream
            outputStream = FileOutputStream(originalUrl)
            val data = ByteArray(1024)
            var len: Int = inputStream!!.read(data)
            while (len != -1) {
                len = inputStream.read(data)
                outputStream.write(data, 0, len)
            }
            when (fileInfo[1].toLowerCase()) {
                "jpg", "jpeg", "png" -> {
                    // 如果是图片，保存一份缩略图
                    val changUrl = "$folderUrl/min_$uuidFileName"
                    changPicture(originalUrl, changUrl)
                }
                else -> {
                }
            }

            fileList.add(TableFileResource(
                    url = "resource/$dateFolder/$uuidFileName",
                    filename = uuidFileName,
                    type = fileInfo[1]
            ))
        }
        inputStream!!.close()
        outputStream!!.close()
        return fileList
    }

    /**将原图压缩成256*256*/
    @Throws(BaseException::class)
    private fun changPicture(originalUrl: String, changeUrl: String) {
        Thumbnails.of(originalUrl).size(256, 256).keepAspectRatio(true).toFile(changeUrl)
    }

    /**获得拼接的uuid*/
    @Throws(BaseException::class)
    private fun getUUIDFileName(file: MultipartFile): Array<String> {
        // 生成uuid
        val uuid = UUID.randomUUID().toString().replace("-".toRegex(), "")
        // 文件名后缀
        val suffix = file.originalFilename!!.substring(file.originalFilename!!.lastIndexOf("."))
        return arrayOf(uuid, suffix)
    }
}

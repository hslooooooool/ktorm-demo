package vip.qsos.ktorm.module.file.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.ktorm.module.IBo

@ApiModel(value = "文件对象")
class FileResourceBo : IBo<TableFileResource> {
    @ApiModelProperty(name = "url", value = "文件链接")
    var url: String? = null
    @ApiModelProperty(name = "filename", value = "文件名称")
    var filename: String? = null
    @ApiModelProperty(name = "type", value = "文件类型")
    var type: String? = null

    constructor()
    constructor(url: String? = null, filename: String? = null, type: String? = null) {
        this.url = url
        this.filename = filename
        this.type = type
    }

    override fun toTable(): TableFileResource {
        return TableFileResource(
                url = url,
                filename = filename,
                type = type
        )
    }

    override fun getBo(table: TableFileResource?): IBo<TableFileResource>? {
        return table?.let {
            FileResourceBo(
                    // FIXME 配置访问HOST
                    url = "http://192.168.1.9:8085/" + table.url,
                    filename = table.filename,
                    type = table.type
            )
        }
    }
}
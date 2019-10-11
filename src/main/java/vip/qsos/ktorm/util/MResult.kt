package vip.qsos.ktorm.util

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

/**
 * @author : 华清松
 * @date : 2019-05-17
 * @description : 统一返回结果
 */
@ApiModel(value = "统一返回结果")
class MResult<T> {
    @ApiModelProperty(value = "返回码", example = "200", notes = "成功：200")
    var code: Int = 200
    @ApiModelProperty(value = "返回信息", example = "请求成功", notes = "请求结果消息")
    var msg: String = "请求成功"
    @ApiModelProperty(value = "请求结果", notes = "请求结果数据")
    var data: T? = null

    fun result(data: T): MResult<T> {
        this.data = data
        return this
    }

    fun error(code: Int, msg: String): MResult<T> {
        this.code = code
        this.msg = msg
        this.data = null
        return this
    }
}
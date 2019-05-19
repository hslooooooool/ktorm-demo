package vip.qsos.ktorm.util

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import java.util.*

/**
 * @author : 华清松
 * @date : 2019-05-17
 * @description : 统一返回结果
 */
@ApiModel(value = "统一返回结果")
class Result : HashMap<String, Any>() {
    @ApiModelProperty(value = "返回码", example = "200", notes = "成功：200")
    var code: Int = 200
    @ApiModelProperty(value = "返回信息", example = "请求成功", notes = "请求结果消息")
    var msg: String = "请求成功"

    init {
        put("code", code)
        put("msg", msg)
    }

    fun add(key: String, value: Any): Result {
        this[key] = value
        return this
    }

    companion object {
        private const val serialVersionUID = 1L

        @JvmOverloads
        fun error(msg: String = "未知异常，请联系管理员"): Result {
            val result = Result()
            result["code"] = 500
            result["msg"] = msg
            return result
        }

        fun ok(msg: String): Result {
            val result = Result()
            result["msg"] = msg
            return result
        }
    }
}

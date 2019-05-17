package vip.qsos.ktorm.util

import java.util.*

/**
 * @author : 华清松
 * @date : 2019-05-17
 * @description : 统一返回结果
 */
class Result : HashMap<String, Any>() {
    init {
        put("code", 200)
        put("msg", "success")
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

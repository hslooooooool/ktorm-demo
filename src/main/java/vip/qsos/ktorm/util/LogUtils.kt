package vip.qsos.ktorm.util

import org.slf4j.LoggerFactory

/**
 * @author : 华清松
 * @date : 2019-05-19
 * @description : 日志工具类
 */
object LogUtils {

    private val instance = LoggerFactory.getLogger("APP日志")

    fun i(msg: String) {
        instance.info(msg)
    }

    fun w(msg: String) {
        instance.warn(msg)
    }

    fun d(msg: String) {
        instance.debug(msg)
    }

    fun e(msg: String) {
        instance.error(msg)
    }

}
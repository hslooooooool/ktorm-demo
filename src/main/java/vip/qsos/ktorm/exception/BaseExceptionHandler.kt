package vip.qsos.ktorm.exception

import org.springframework.dao.DuplicateKeyException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import vip.qsos.ktorm.util.LogUtils
import vip.qsos.ktorm.util.MResult

/**
 * @author : 华清松
 * @date : 2019-05-17
 * @description : 全局异常处理
 */
@RestControllerAdvice
class BaseExceptionHandler {

    @ExceptionHandler(BaseException::class)
    fun handleRRException(e: BaseException): MResult<Nothing> {
        return MResult<Nothing>().error(e.code, e.message ?: "服务器异常")
    }

    @ExceptionHandler(DuplicateKeyException::class)
    fun handleDuplicateKeyException(e: DuplicateKeyException): MResult<Nothing> {
        LogUtils.e(e.message ?: "未知异常")
        return MResult<Nothing>().error(500, "数据错误")
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): MResult<Nothing> {
        LogUtils.e(e.message ?: "未知异常")
        return MResult<Nothing>().error(500, "服务器异常")
    }
}

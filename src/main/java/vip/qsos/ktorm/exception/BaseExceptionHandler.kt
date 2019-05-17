package vip.qsos.ktorm.exception

import org.slf4j.LoggerFactory
import org.springframework.dao.DuplicateKeyException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import vip.qsos.ktorm.util.Result

/**
 * @author : 华清松
 * @date : 2019-05-17
 * @description : TODO 类说明，描述此类的类型和用途
 */
@RestControllerAdvice
class BaseExceptionHandler {
    private val logger = LoggerFactory.getLogger(javaClass)

    @ExceptionHandler(BaseException::class)
    fun handleRRException(e: BaseException): Result {
        val result = Result()
        result["code"] = e.code
        result["msg"] = e.message ?: "未知异常"
        return result
    }

    @ExceptionHandler(DuplicateKeyException::class)
    fun handleDuplicateKeyException(e: DuplicateKeyException): Result {
        logger.error(e.message, e)
        return Result.error("数据库中已存在该记录")
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): Result {
        logger.error(e.message, e)
        return Result.error()
    }
}

package vip.qsos.ktorm.exception

/**
 * @author : 华清松
 * @date : 2019-05-17
 * @description : 全局异常捕获
 */
class BaseException : RuntimeException {

    var msg: String? = null
    var code = 500

    constructor(msg: String) : super(msg) {
        this.msg = msg
    }

    constructor(msg: String, e: Throwable) : super(msg, e) {
        this.msg = msg
    }

    constructor(msg: String, code: Int) : super(msg) {
        this.msg = msg
        this.code = code
    }

}

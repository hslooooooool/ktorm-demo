package vip.qsos.ktorm.interseptor


import org.apache.commons.lang.StringUtils
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import vip.qsos.ktorm.annotation.Login
import vip.qsos.ktorm.exception.BaseException
import vip.qsos.ktorm.util.JwtUtils
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author : 华清松
 * @date : 2019-05-19
 * @description : Token 验证 AOP 处理
 */
@Component
class AuthorizationInterceptor : HandlerInterceptorAdapter() {

    override fun preHandle(request: HttpServletRequest?, response: HttpServletResponse?, handler: Any?): Boolean {
        val annotation: Login?
        if (handler is HandlerMethod) {
            annotation = handler.getMethodAnnotation(Login::class.java)
        } else {
            return true
        }

        if (annotation == null) {
            return true
        }

        // 获取用户凭证
        var token = request!!.getHeader(JwtUtils.header)
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(JwtUtils.header)
        }

        // 凭证为空
        if (StringUtils.isBlank(token)) {
            throw BaseException(JwtUtils.header + "不能为空", HttpStatus.UNAUTHORIZED.value())
        }

        val claims = JwtUtils.getClaimByToken(token)
        if (claims == null || JwtUtils.isTokenExpired(claims.expiration)) {
            throw BaseException(JwtUtils.header + "失效，请重新登录", HttpStatus.UNAUTHORIZED.value())
        }

        // 设置 userId 到 request 里，后续根据 userId ，获取用户信息
        request.setAttribute("userId", claims.subject.toLong())

        return true
    }

}

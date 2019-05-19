package vip.qsos.ktorm.util

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.*

/**
 * @author : 华清松
 * @date : 2019-05-19
 * @description : jwt 工具类
 */
@Component
@ConfigurationProperties("app.jwt")
object JwtUtils {

    var secret: String? = null
    var expireTime: Long = 0L
    var header: String? = null

    /**
     * 生成jwt token
     */
    fun generateToken(userId: Long?): String {
        val nowDate = Date()
        // 过期时间
        val expireDate = Date(nowDate.time + expireTime * 1000)

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject("$userId")
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }

    fun getClaimByToken(token: String): Claims? {
        return try {
            Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .body
        } catch (e: Exception) {
            LogUtils.d("Token 校验异常 $e")
            null
        }

    }

    /**
     * token是否过期
     *
     * @return true：过期
     */
    fun isTokenExpired(expiration: Date): Boolean {
        return expiration.before(Date())
    }

}

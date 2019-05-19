package vip.qsos.ktorm.annotation

/**
 * @author : 华清松
 * @date : 2019-05-19
 * @description : app 登录效验，在接口处增加此注解后将自动校验用户是否已登录（token未失效），即可访问接口
 */
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class Login

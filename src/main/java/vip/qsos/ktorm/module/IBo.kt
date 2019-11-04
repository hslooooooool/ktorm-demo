package vip.qsos.ktorm.module

/**
 * @author : 华清松
 * @description : 基础业务实体接口
 */
interface IBo<T> {
    /**业务实体转化为数据库表*/
    fun toTable(): T

    /**数据库表转为业务实体*/
    fun getBo(table: T?): IBo<T>?
}
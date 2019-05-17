package vip.qsos.ktorm

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * @author : 华清松
 * @date : 2018/12/20
 * @description : 启动类
 */
@SpringBootApplication
open class FileStreamApplication

fun main(args: Array<String>) {
    SpringApplication.run(FileStreamApplication::class.java, *args)
}
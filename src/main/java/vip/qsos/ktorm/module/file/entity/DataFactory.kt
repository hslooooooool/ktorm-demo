package vip.qsos.ktorm.module.file.entity

/**
 * @author: 华清松
 * @date: 2018/4/12
 * @description: 文件类型工厂类
 */
object DataFactory {

    fun produce(): FileProcessServiceImpl {
        return FileProcessServiceImpl()
    }
}

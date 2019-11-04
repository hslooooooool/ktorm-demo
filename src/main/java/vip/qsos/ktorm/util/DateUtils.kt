package vip.qsos.ktorm.util

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * @author : 华清松
 * @date : 2018/12/20
 * @description : 日期处理
 */
object DateUtils {

    fun format(date: Date?, pattern: String): String? {
        if (date != null) {
            val df = SimpleDateFormat(pattern)
            return df.format(date)
        }
        return null
    }

    fun format(date: LocalDateTime): String {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return date.format(dateTimeFormatter)
    }
}

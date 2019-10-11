package vip.qsos.ktorm.module.chat.entity

/**
 * @author : 华清松
 * 聊天类型
 * @sample ChatType.SINGLE 单聊
 * @sample ChatType.GROUP 群聊
 * @sample ChatType.NOTICE 公告
 * @sample ChatType.SUBSCRIPTION 订阅
 */
enum class ChatType {
    SINGLE,
    GROUP,
    NOTICE,
    SUBSCRIPTION
}
package vip.qsos.ktorm.module.chat.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import vip.qsos.ktorm.module.chat.entity.ChatMessageBo
import vip.qsos.ktorm.module.chat.entity.ChatMessageInfoBo
import vip.qsos.ktorm.module.chat.entity.ChatMessageReadStatusBo
import vip.qsos.ktorm.module.chat.service.IChatService
import vip.qsos.ktorm.util.MResult

@RestController
open class ChatMessageController @Autowired constructor(
        private val mChatMessageService: IChatService.IMessage
) : IChatController.IMessage {

    override fun getMessageById(userId: Int, messageId: Int): MResult<ChatMessageBo> {
        val result = mChatMessageService.getMessageById(userId, messageId)
        return MResult<ChatMessageBo>().result(result)
    }

    override fun getMessageListByIds(userId: Int, messageIds: List<Int>): MResult<List<ChatMessageBo>> {
        val result = mChatMessageService.getMessageListByIds(userId, messageIds)
        return MResult<List<ChatMessageBo>>().result(result)
    }

    override fun getMessageListBySessionId(userId: Int, sessionId: Int): MResult<List<ChatMessageInfoBo>> {
        val result = mChatMessageService.getMessageListBySessionId(userId, sessionId)
        return MResult<List<ChatMessageInfoBo>>().result(result)
    }

    override fun getMessageListBySessionIdAndTimeline(userId: Int, sessionId: Int, timeline: Int?, next: Boolean, size: Int): MResult<List<ChatMessageInfoBo>> {
        val result = mChatMessageService.getMessageListBySessionIdAndTimeline(userId, sessionId, timeline, next, size)
        return MResult<List<ChatMessageInfoBo>>().result(result)
    }

    override fun getMessageListByUserId(userId: Int): MResult<List<ChatMessageInfoBo>> {
        val result = mChatMessageService.getMessageListByUserId(userId)
        return MResult<List<ChatMessageInfoBo>>().result(result)
    }

    override fun sendMessage(userId: Int, message: ChatMessageBo): MResult<ChatMessageBo> {
        val result = mChatMessageService.sendMessage(userId, message)
        return MResult<ChatMessageBo>().result(result)
    }

    override fun readMessage(userId: Int, messageId: Int): MResult<ChatMessageReadStatusBo> {
        val result = mChatMessageService.readMessage(userId, messageId)
        if (result.readStatus) {
            pushMessageRead(userId, messageId, result.readNum)
        }
        return MResult<ChatMessageReadStatusBo>().result(result)
    }

    override fun deleteMessage(userId: Int, messageId: Int): MResult<Boolean> {
        val result = mChatMessageService.deleteMessage(userId, messageId)
        return MResult<Boolean>().result(result)
    }

    override fun pushMessageRead(userId: Int, messageId: Int, readNum: Int) {

    }
}
package vip.qsos.ktorm.module.chat.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import vip.qsos.ktorm.module.chat.entity.ChatMessageBo
import vip.qsos.ktorm.module.chat.entity.ChatMessageInfoBo
import vip.qsos.ktorm.module.chat.service.IChatService
import vip.qsos.ktorm.util.MResult

@RestController
open class ChatMessageController @Autowired constructor(
        private val mChatMessageService: IChatService.IMessage
) : IChatController.IMessage {

    override fun getMessageById(messageId: Int): MResult<ChatMessageBo> {
        val result = mChatMessageService.getMessageById(messageId)
        return MResult<ChatMessageBo>().result(result)
    }

    override fun getMessageListByIds(messageIds: List<Int>): MResult<List<ChatMessageBo>> {
        val result = mChatMessageService.getMessageListByIds(messageIds)
        return MResult<List<ChatMessageBo>>().result(result)
    }

    override fun getMessageListBySessionId(sessionId: Int): MResult<List<ChatMessageInfoBo>> {
        val result = mChatMessageService.getMessageListBySessionId(sessionId)
        return MResult<List<ChatMessageInfoBo>>().result(result)
    }

    override fun getMessageListBySessionIdAndTimeline(sessionId: Int, timeline: Int): MResult<List<ChatMessageInfoBo>> {
        val result = mChatMessageService.getMessageListBySessionIdAndTimeline(sessionId, timeline)
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

    override fun deleteMessage(messageId: Int): MResult<Boolean> {
        val result = mChatMessageService.deleteMessage(messageId)
        return MResult<Boolean>().result(result)
    }

}
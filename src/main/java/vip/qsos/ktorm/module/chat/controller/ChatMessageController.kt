package vip.qsos.ktorm.module.chat.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import vip.qsos.ktorm.module.chat.entity.ChatMessage
import vip.qsos.ktorm.module.chat.entity.ChatMessageBo
import vip.qsos.ktorm.module.chat.service.IChatService
import vip.qsos.ktorm.util.MResult

@RestController
open class ChatMessageController(
        @Autowired
        private val mChatMessageService: IChatService.IMessage
) : IChatController.IMessage {

    override fun getMessageById(messageId: Int): MResult<ChatMessage> {
        val result = mChatMessageService.getMessageById(messageId)
        return MResult<ChatMessage>().result(result)
    }

    override fun getMessageListBySessionId(sessionId: Int): MResult<List<ChatMessageBo>> {
        val result = mChatMessageService.getMessageListBySessionId(sessionId)
        return MResult<List<ChatMessageBo>>().result(result)
    }

    override fun getMessageListByUserId(userId: Int): MResult<List<ChatMessageBo>> {
        val result = mChatMessageService.getMessageListByUserId(userId)
        return MResult<List<ChatMessageBo>>().result(result)
    }

    override fun sendMessage(userId: Int, message: ChatMessage): MResult<ChatMessage> {
        val result = mChatMessageService.sendMessage(userId, message)
        return MResult<ChatMessage>().result(result)
    }

    override fun deleteMessage(messageId: Int): MResult<Boolean> {
        val result = mChatMessageService.deleteMessage(messageId)
        return MResult<Boolean>().result(result)
    }

}
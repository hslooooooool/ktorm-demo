package vip.qsos.ktorm.module.chat.controller

import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import vip.qsos.ktorm.module.chat.entity.*
import vip.qsos.ktorm.util.MResult

/**
 * @author : 华清松
 * 聊天接口定义
 */
interface IChatModel {

    interface Base {

        /**获取会话数据
         * @param sessionId 会话ID
         * @return 会话数据
         * */
        fun getSessionById(sessionId: Int): MResult<ChatSession>

        /**获取消息数据
         * @param messageId 消息ID
         * @return 消息数据
         * */
        fun getMessageById(messageId: Int): MResult<ChatMessage>

        /**获取用户数据
         * @param userId 用户ID
         * @return 用户数据
         * */
        fun getUserById(@RequestHeader(value = "userId") userId: Int): MResult<ChatUser>

        /**获取聊天群数据
         * @param groupId 聊天群ID
         * @return 聊天群数据
         * */
        fun getGroupById(groupId: Int): MResult<ChatGroup>

    }

    interface Get {

        /**获取会话对应的聊天群数据
         * @param sessionId 会话ID
         * @return 聊天群数据
         * */
        fun getGroupByBySessionId(sessionId: Int): MResult<ChatGroup>

        /**获取会话下的用户列表
         * @param sessionId 会话ID
         * @return 用户列表
         * */
        fun getUserListBySessionId(sessionId: Int): MResult<List<ChatUser>>

        /**获取会话下的消息列表
         * @param sessionId 会话ID
         * @return 会话下的消息列表
         * */
        fun getMessageListBySessionId(sessionId: Int): MResult<List<MChatMessage>>

        /**获取用户发送的消息
         * @param userId 用户ID
         * @return 用户发送的消息
         * */
        fun getMessageListByUserId(@RequestHeader(value = "userId") userId: Int): MResult<List<MChatMessage>>

        /**获取用户订阅的会话
         * @param userId 用户ID
         * @return 用户订阅的会话
         * */
        fun getSessionListByUserId(@RequestHeader(value = "userId") userId: Int): MResult<List<ChatSession>>

    }

    interface Post {

        /**创建用户
         * @param user 用户
         * @return 用户信息
         * */
        fun createUser(@RequestBody user: ChatUser): MResult<ChatUser>

        /**发送消息
         * @param message 消息数据
         * @return 消息数据
         * */
        fun sendMessage(@RequestHeader(value = "userId") userId: Int, @RequestBody message: ChatMessage): MResult<ChatMessage>

        /**创建会话,可同时往会话发送一条消息,适用于发起单聊/群聊/分享等场景
         * @param userIdList 用户ID集合
         * @param message 发送的消息
         * @return 会话数据
         * */
        fun createSession(@RequestHeader(value = "userId") userId: Int, userIdList: List<Int>, @RequestBody message: ChatMessage? = null): MResult<ChatSession>

        /**往已有会话中增加用户
         * @param userIdList 被添加用户ID集合
         * @param sessionId 会话ID
         * @return 加入的会话数据
         * */
        fun addUserListToSession(userIdList: List<Int>, sessionId: Int): MResult<ChatSession>

        /**更新聊天群公告
         * @param notice 需更新的聊天群公告
         * @return 已更新的聊天群数据
         * */
        fun updateGroupNotice(notice: String): MResult<ChatGroup>

        /**更新聊天群名称
         * @param name 需更新的聊天群名称
         * @return 已更新的聊天群数据
         * */
        fun updateGroupName(name: String): MResult<ChatGroup>

    }

    interface Delete {

        /**解散会话
         * @param sessionId 会话ID
         * */
        fun deleteSession(sessionId: Int): MResult<Boolean>

        /**将用户移除会话
         * @param sessionId 会话ID
         * @param userId 需要移除的用户ID
         * */
        fun deleteUser(sessionId: Int, @RequestHeader(value = "userId") userId: Int): MResult<Boolean>

        /**撤回消息
         * @param messageId 消息ID
         * */
        fun deleteMessage(messageId: Int): MResult<Boolean>

    }
}

interface IChatModelConfig : IChatModel.Delete, IChatModel.Post, IChatModel.Get, IChatModel.Base
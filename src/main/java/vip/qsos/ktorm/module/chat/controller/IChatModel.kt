package vip.qsos.ktorm.module.chat.controller

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
        fun getSessionById(sessionId: Long): MResult<ChatSession>

        /**获取消息数据
         * @param messageId 消息ID
         * @return 消息数据
         * */
        fun getMessageById(messageId: Long): MResult<ChatMessage>

        /**获取用户数据
         * @param userId 用户ID
         * @return 用户数据
         * */
        fun getUserById(userId: Long): MResult<ChatUser>

        /**获取聊天群数据
         * @param groupId 聊天群ID
         * @return 聊天群数据
         * */
        fun getGroupById(groupId: Long): MResult<ChatGroup>

        /**获取消息内容数据
         * @param contentId 消息内容ID
         * @return 消息内容数据
         * */
        fun getContentById(contentId: Long): MResult<ChatContent>

    }

    interface Get {

        /**获取会话对应的聊天群数据
         * @param sessionId 会话ID
         * @return 聊天群数据
         * */
        fun getGroupByBySessionId(sessionId: Long): MResult<ChatGroup>

        /**获取会话下的用户列表
         * @param sessionId 会话ID
         * @return 用户列表
         * */
        fun getUserListBySessionId(sessionId: Long): MResult<List<ChatUser>>

        /**获取会话下的消息列表
         * @param sessionId 会话ID
         * @return 会话下的消息列表
         * */
        fun getMessageListBySessionId(sessionId: Long): MResult<List<ChatMessage>>

        /**获取用户发送的消息
         * @param userId 用户ID
         * @return 用户发送的消息
         * */
        fun getMessageListByUserId(userId: Long): MResult<List<ChatMessage>>

        /**获取用户订阅的会话
         * @param userId 用户ID
         * @return 用户订阅的会话
         * */
        fun getSessionListByUserId(userId: Long): MResult<List<ChatSession>>

    }

    interface Post {

        /**发送消息
         * @param message 消息数据
         * @return 消息数据
         * */
        fun sendMessage(message: ChatMessage): MResult<ChatMessage>

        /**创建会话,可同时往会话发送一条消息,适用于发起单聊/群聊/分享等场景
         * @param userIdList 用户ID集合
         * @param message 发送的消息
         * @return 会话数据
         * */
        fun createSession(userIdList: List<Long>, message: ChatMessage? = null): MResult<ChatSession>

        /**往已有会话中增加用户
         * @param userIdList 被添加用户ID集合
         * @param sessionId 会话ID
         * @return 加入的会话数据
         * */
        fun addUserListToSession(userIdList: List<Long>, sessionId: Long): MResult<ChatSession>

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
        fun deleteSession(sessionId: Long): MResult<Boolean>

        /**将用户移除会话
         * @param sessionId 会话ID
         * @param userId 需要移除的用户ID
         * */
        fun deleteUser(sessionId: Long, userId: Long): MResult<Boolean>

        /**撤回消息
         * @param messageId 消息ID
         * */
        fun deleteMessage(messageId: Long): MResult<Boolean>

    }
}

interface IChatModelConfig : IChatModel.Delete, IChatModel.Post, IChatModel.Get, IChatModel.Base
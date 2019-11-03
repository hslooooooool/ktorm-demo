package vip.qsos.ktorm.module.chat.service

import vip.qsos.ktorm.module.chat.entity.*
import vip.qsos.ktorm.module.chat.entity.form.FormCreateSession
import vip.qsos.ktorm.module.chat.entity.form.FormCreateUser

/**
 * @author : 华清松
 * 聊天接口定义
 */
interface IChatService {

    interface IUser {

        /**创建用户
         * @param user 用户
         * @return 用户信息
         * */
        fun createUser(user: FormCreateUser): ChatUserBo

        /**获取所有用户
         * @return 所有用户
         * */
        fun getAllUser(userId: Int): List<ChatUserBo>

        /**获取用户数据
         * @param userId 用户ID
         * @return 用户数据
         * */
        fun getUserById(userId: Int): ChatUserBo

        /**获取会话下的用户列表
         * @param sessionId 会话ID
         * @return 用户列表
         * */
        fun getUserListBySessionId(sessionId: Int): List<ChatUserBo>

        /**删除用户
         * @param userId 用户ID
         * */
        fun deleteUser(userId: Int): Boolean

    }

    interface ISession {

        /**创建会话,可同时往会话发送一条消息,适用于发起单聊/群聊/分享等场景
         * @param data 会话信息
         * @return 会话数据
         * */
        fun createSession(userId: Int, data: FormCreateSession): ChatSessionBo

        /**TODO【算法】校验会话是否已存在，判断条件：userIdList 范围内的用户拥有同一个 session 会话*/
        fun hasSession(userIdList: List<Int>): ChatSessionBo

        /**往已有会话中增加用户
         * @param userIdList 被添加用户ID集合
         * @param sessionId 会话ID
         * @return 加入的会话数据
         * */
        fun addUserListToSession(userId: Int, userIdList: List<Int>, sessionId: Int): ChatSessionBo

        /**获取会话数据
         * @param sessionId 会话ID
         * @return 会话数据
         * */
        fun getSessionById(sessionId: Int): ChatSessionBo

        /**获取用户订阅的会话
         * @param userId 用户ID
         * @return 用户订阅的会话
         * */
        fun getSessionListByUserId(userId: Int): List<ChatSessionBo>

        /**解散会话
         * @param sessionId 会话ID
         * */
        fun deleteSession(sessionId: Int): Boolean

    }

    interface IMessage {

        /**发送消息
         * @param message 消息数据
         * @return 消息数据
         * */
        fun sendMessage(userId: Int, message: ChatMessage): ChatMessage

        /**获取消息数据
         * @param messageId 消息ID
         * @return 消息数据
         * */
        fun getMessageById(messageId: Int): ChatMessage

        /**获取会话下的消息列表
         * @param sessionId 会话ID
         * @return 会话下的消息列表
         * */
        fun getMessageListBySessionId(sessionId: Int): List<ChatMessageBo>

        /**获取用户发送的消息
         * @param userId 用户ID
         * @return 用户发送的消息
         * */
        fun getMessageListByUserId(userId: Int): List<ChatMessageBo>

        /**撤回消息
         * @param messageId 消息ID
         * */
        fun deleteMessage(messageId: Int): Boolean

    }

    interface IGroup {
        /**获取聊天群数据
         * @param groupId 聊天群ID
         * @return 聊天群数据
         * */
        fun getGroupById(groupId: Int): ChatGroupBo

        /**获取用户所在的群列表
         * @param userId 用户ID
         * @return 群列表
         * */
        fun getGroupWithMe(userId: Int): List<ChatGroupBo>

        /**获取会话对应的聊天群数据
         * @param sessionId 会话ID
         * @return 聊天群数据
         * */
        fun getGroupByBySessionId(sessionId: Int): ChatGroupBo

        /**更新聊天群公告
         * @param notice 需更新的聊天群公告
         * @return 已更新的聊天群数据
         * */
        fun updateGroupNotice(notice: String): ChatGroupBo

        /**更新聊天群名称
         * @param name 需更新的聊天群名称
         * @return 已更新的聊天群数据
         * */
        fun updateGroupName(name: String): ChatGroupBo

    }
}
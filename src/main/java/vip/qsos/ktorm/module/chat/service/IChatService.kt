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
         * @param userId 登录用户ID
         * @param message 消息数据
         * @return 消息数据
         * */
        fun sendMessage(userId: Int, message: ChatMessageBo): ChatMessageBo

        /**获取消息数据
         * @param userId 登录用户ID
         * @param messageId 消息ID
         * @return 消息数据
         * */
        fun getMessageById(userId: Int, messageId: Int): ChatMessageBo

        /**获取消息列表数据
         * @param userId 登录用户ID
         * @param messageIds 消息ID集合
         * @return 消息列表数据
         * */
        fun getMessageListByIds(userId: Int, messageIds: List<Int>): List<ChatMessageBo>

        /**获取会话下的消息列表
         * @param userId 登录用户ID
         * @param sessionId 会话ID
         * @return 会话下的消息列表
         * */
        fun getMessageListBySessionId(userId: Int, sessionId: Int): List<ChatMessageInfoBo>

        /**获取会话下的消息列表，时序以后
         * @param userId 登录用户ID
         * @param sessionId 会话ID
         * @param timeline 消息时序
         * @param next 请求消息时序以下的消息
         * @param page 请求页码
         * @param size 请求每页数量
         * @return 会话下的消息列表
         * */
        fun getMessageListBySessionIdAndTimeline(userId: Int, sessionId: Int, timeline: Int?, next: Boolean = true, page: Int = 1, size: Int = 20): List<ChatMessageInfoBo>

        /**获取用户发送的消息
         * @param userId 登录用户ID
         * @return 用户发送的消息
         * */
        fun getMessageListByUserId(userId: Int): List<ChatMessageInfoBo>

        /**添加消息与用户关系
         * @param userId 登录用户ID
         * @param messageId 消息ID
         * @return 添加是否成功
         * */
        fun addUserWithMessage(userId: Int, messageId: Int): Boolean

        /**添加消息读取状态
         * @param userId 登录用户ID
         * @param messageId 消息ID
         * @return 添加是否成功
         * */
        fun addMessageReadStatus(userId: Int, messageId: Int): Boolean

        /**获取消息读取状态
         * @param userId 登录用户ID
         * @param messageId 消息ID
         * @return 已读状态
         * */
        fun getMessageReadStatus(userId: Int, messageId: Int): ChatMessageReadStatusBo

        /**更新消息已读状态
         * @param userId 登录用户ID
         * @param messageId 已读消息ID
         * */
        fun readMessage(userId: Int, messageId: Int): ChatMessageReadStatusBo

        /**撤回消息
         * @param userId 登录用户ID
         * @param messageId 消息ID
         * */
        fun deleteMessage(userId: Int, messageId: Int): Boolean

    }

    /**聊天消息群服务实现*/
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

        /**更新群最后一条消息
         * @param sessionId 会话ID
         * @param lastMessageId 最后一条消息ID
         * @param lastTimeline 最后一条消息的时间线
         * */
        fun updateGroupLastTimeline(userId: Int, sessionId: Int, lastMessageId: Int, lastTimeline: Int): ChatGroupBo

    }
}
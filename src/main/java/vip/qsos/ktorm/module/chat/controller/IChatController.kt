package vip.qsos.ktorm.module.chat.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import io.swagger.annotations.ApiSort
import org.springframework.web.bind.annotation.*
import vip.qsos.ktorm.module.chat.entity.*
import vip.qsos.ktorm.module.chat.entity.form.FormCreateSession
import vip.qsos.ktorm.module.chat.entity.form.FormCreateUser
import vip.qsos.ktorm.util.MResult

/**
 * @author : 华清松
 * 聊天接口定义
 */
interface IChatController {

    @Api(tags = ["聊天用户"])
    @ApiSort(1)
    @RequestMapping("/chat/user")
    interface IUser {

        @PostMapping("/createUser")
        @ApiOperation(value = "创建用户")
        fun createUser(
                @RequestBody user: FormCreateUser
        ): MResult<ChatUserBo>

        @GetMapping("/getAllUser")
        @ApiOperation(value = "获取所有用户")
        fun getAllUser(
                @RequestHeader
                @ApiParam(value = "登录用户ID", required = true)
                userId: Int
        ): MResult<List<ChatUserBo>>

        @GetMapping("/getUserById")
        @ApiOperation(value = "获取用户信息")
        fun getUserById(
                @RequestHeader
                @ApiParam(value = "登录用户ID", required = true)
                userId: Int
        ): MResult<ChatUserBo>

        @GetMapping("/getUserListBySessionId")
        @ApiOperation(value = "获取会话下的用户列表")
        fun getUserListBySessionId(
                @RequestParam
                @ApiParam(value = "会话ID", required = true)
                sessionId: Int
        ): MResult<List<ChatUserBo>>

        @DeleteMapping
        @ApiOperation(value = "删除用户")
        fun deleteUser(
                @ApiParam(value = "待删用户ID", required = true)
                @RequestParam
                userId: Int
        ): MResult<Boolean>

    }

    @Api(tags = ["聊天会话"])
    @ApiSort(2)
    @RequestMapping("/chat/session")
    interface ISession {

        @PostMapping("/createSession")
        @ApiOperation(value = "创建会话")
        fun createSession(
                @ApiParam(value = "登录用户ID", required = true)
                @RequestHeader
                userId: Int,
                @RequestBody
                data: FormCreateSession
        ): MResult<ChatSessionBo>

        @PostMapping("/addUserListToSession")
        @ApiOperation(value = "会话中增加用户")
        fun addUserListToSession(
                @RequestHeader
                @ApiParam(value = "登录用户ID", required = true)
                userId: Int,
                @RequestParam
                @ApiParam(value = "待加入的用户ID集合", required = true)
                userIdList: List<Int>,
                @RequestParam
                @ApiParam(value = "加入的会话ID", required = true)
                sessionId: Int
        ): MResult<ChatSessionBo>

        @GetMapping("/getSessionById")
        @ApiOperation(value = "获取会话信息")
        fun getSessionById(
                @RequestParam
                @ApiParam(value = "会话ID", required = true)
                sessionId: Int
        ): MResult<ChatSessionBo>

        @GetMapping("/getSessionListByUserId")
        @ApiOperation(value = "获取用户订阅的会话")
        fun getSessionListByUserId(
                @RequestHeader
                @ApiParam(value = "登录用户ID", required = true)
                userId: Int
        ): MResult<List<ChatSessionBo>>

        @DeleteMapping
        @ApiOperation(value = "删除会话")
        fun deleteSession(
                @RequestParam
                @ApiParam(value = "待删除的会话ID", required = true)
                sessionId: Int
        ): MResult<Boolean>

    }

    @Api(tags = ["聊天消息"])
    @ApiSort(3)
    @RequestMapping("/chat/message")
    interface IMessage {

        @PostMapping("/sendMessage")
        @ApiOperation(value = "发送消息")
        fun sendMessage(
                @RequestHeader
                @ApiParam(value = "登录用户ID", required = true)
                userId: Int,
                @RequestBody message: ChatMessageBo
        ): MResult<ChatMessageBo>

        @GetMapping("/getMessageById")
        @ApiOperation(value = "获取消息数据")
        fun getMessageById(
                @RequestHeader
                @ApiParam(value = "登录用户ID", required = true)
                userId: Int,
                @RequestParam(name = "messageId")
                @ApiParam(value = "消息ID", required = true)
                messageId: Int
        ): MResult<ChatMessageBo>

        @GetMapping("/getMessageListByIds")
        @ApiOperation(value = "获取消息列表数据")
        fun getMessageListByIds(
                @RequestHeader
                @ApiParam(value = "登录用户ID", required = true)
                userId: Int,
                @RequestParam(name = "messageIds")
                @ApiParam(value = "消息ID集合", required = true)
                messageIds: List<Int>
        ): MResult<List<ChatMessageBo>>

        @GetMapping("/getMessageListBySessionId")
        @ApiOperation(value = "获取会话下的消息列表")
        fun getMessageListBySessionId(
                @RequestHeader
                @ApiParam(value = "登录用户ID", required = true)
                userId: Int,
                @RequestParam
                @ApiParam(value = "会话ID", required = true)
                sessionId: Int
        ): MResult<List<ChatMessageInfoBo>>

        @GetMapping("/getMessageListBySessionIdAndTimeline")
        @ApiOperation(value = "获取会话下的消息列表")
        fun getMessageListBySessionIdAndTimeline(
                @RequestHeader
                @ApiParam(value = "登录用户ID", required = true)
                userId: Int,
                @RequestParam
                @ApiParam(value = "会话ID", required = true)
                sessionId: Int,
                @RequestParam
                @ApiParam(value = "时序")
                timeline: Int? = null,
                @RequestParam(required = false)
                @ApiParam(value = "是否下一页")
                next: Boolean = true,
                @RequestParam(required = false)
                @ApiParam(value = "页数")
                page: Int = 1,
                @RequestParam(required = false)
                @ApiParam(value = "每页数量")
                size: Int = 20
        ): MResult<List<ChatMessageInfoBo>>

        @GetMapping("/getMessageListByUserId")
        @ApiOperation(value = "获取用户发送的消息")
        fun getMessageListByUserId(
                @RequestHeader
                @ApiParam(value = "登录用户ID", required = true)
                userId: Int
        ): MResult<List<ChatMessageInfoBo>>

        @PostMapping("/readMessage")
        @ApiOperation(value = "更新消息已读状态")
        fun readMessage(
                @RequestHeader
                @ApiParam(value = "登录用户ID", required = true)
                userId: Int,
                @RequestParam
                @ApiParam(value = "已读消息ID", required = true)
                messageId: Int
        ): MResult<ChatMessageReadStatusBo>

        @DeleteMapping("/deleteMessage")
        @ApiOperation(value = "删除(撤销)消息")
        fun deleteMessage(
                @RequestHeader
                @ApiParam(value = "登录用户ID", required = true)
                userId: Int,
                @RequestParam
                @ApiParam(value = "待删除的消息ID", required = true)
                messageId: Int
        ): MResult<Boolean>

        /**提送消息读取数更新*/
        fun pushMessageRead(userId: Int, messageId: Int, readNum: Int)
    }

    @Api(tags = ["聊天群"])
    @ApiSort(4)
    @RequestMapping("/chat/group")
    interface IGroup {

        @GetMapping("/getGroupById")
        @ApiOperation(value = "获取群信息")
        fun getGroupById(
                @RequestParam
                @ApiParam(value = "群ID", required = true)
                groupId: Int
        ): MResult<ChatGroupBo>

        @GetMapping("/getGroupWithMe")
        @ApiOperation(value = "获取用户所在的所有群列表")
        fun getGroupWithMe(
                @RequestHeader
                @ApiParam(value = "登录用户ID", required = true)
                userId: Int
        ): MResult<List<ChatGroupBo>>

        @GetMapping("/getGroupByBySessionId")
        @ApiOperation(value = "获取会话对应的群信息")
        fun getGroupByBySessionId(
                @RequestParam
                @ApiParam(value = "会话ID", required = true)
                sessionId: Int
        ): MResult<ChatGroupBo>

        @PutMapping("/updateGroupNotice")
        @ApiOperation(value = "更新群公告")
        fun updateGroupNotice(
                @RequestParam
                @ApiParam(value = "公告内容", required = true)
                notice: String
        ): MResult<ChatGroupBo>

        @PutMapping("/updateGroupName")
        @ApiOperation(value = "更新群名称")
        fun updateGroupName(
                @RequestParam
                @ApiParam(value = "群名称", required = true)
                name: String
        ): MResult<ChatGroupBo>

    }
}
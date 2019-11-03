package vip.qsos.ktorm.module.chat.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiOperation
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
    @RequestMapping("/chat/user")
    interface IUser {

        @PostMapping("/createUser")
        @ApiOperation(value = "创建用户")
        fun createUser(@RequestBody user: FormCreateUser): MResult<ChatUserBo>

        @GetMapping("/getAllUser")
        @ApiOperation(value = "获取所有用户")
        fun getAllUser(@RequestHeader userId: Int): MResult<List<ChatUserBo>>

        @GetMapping("/getUserById")
        @ApiOperation(value = "获取用户信息")
        fun getUserById(@RequestHeader userId: Int): MResult<ChatUserBo>

        @GetMapping("/getUserListBySessionId")
        @ApiOperation(value = "获取会话下的用户列表")
        fun getUserListBySessionId(sessionId: Int): MResult<List<ChatUserBo>>

        @DeleteMapping
        @ApiOperation(value = "删除用户")
        fun deleteUser(@RequestParam userId: Int): MResult<Boolean>

    }

    @Api(tags = ["聊天会话"])
    @RequestMapping("/chat/session")
    interface ISession {

        @PostMapping("/createSession")
        @ApiOperation(value = "创建会话")
        fun createSession(
                @RequestHeader userId: Int,
                @RequestBody data: FormCreateSession
        ): MResult<ChatSessionBo>

        @PostMapping("/addUserListToSession")
        @ApiOperation(value = "会话中增加用户")
        fun addUserListToSession(
                @RequestHeader userId: Int,
                @RequestParam userIdList: List<Int>,
                @RequestParam sessionId: Int
        ): MResult<ChatSessionBo>

        @GetMapping("/getSessionById")
        @ApiOperation(value = "获取会话信息")
        fun getSessionById(sessionId: Int): MResult<ChatSessionBo>

        @GetMapping("/getSessionListByUserId")
        @ApiOperation(value = "获取用户订阅的会话")
        fun getSessionListByUserId(@RequestHeader userId: Int): MResult<List<ChatSessionBo>>

        @DeleteMapping
        @ApiOperation(value = "删除会话")
        fun deleteSession(sessionId: Int): MResult<Boolean>

    }

    @Api(tags = ["聊天消息"])
    @RequestMapping("/chat/message")
    interface IMessage {

        @PostMapping("/sendMessage")
        @ApiOperation(value = "发送消息")
        fun sendMessage(
                @RequestHeader userId: Int,
                @RequestBody message: ChatMessage
        ): MResult<ChatMessage>

        @GetMapping("/getMessageById")
        @ApiOperation(value = "获取消息数据")
        fun getMessageById(messageId: Int): MResult<ChatMessage>

        @GetMapping("/getMessageListBySessionId")
        @ApiOperation(value = "获取会话下的消息列表")
        fun getMessageListBySessionId(sessionId: Int): MResult<List<ChatMessageBo>>

        @GetMapping("/getMessageListByUserId")
        @ApiOperation(value = "获取用户发送的消息")
        fun getMessageListByUserId(@RequestHeader userId: Int): MResult<List<ChatMessageBo>>

        @DeleteMapping
        @ApiOperation(value = "删除(撤销)消息")
        fun deleteMessage(messageId: Int): MResult<Boolean>

    }

    @Api(tags = ["聊天群"])
    @RequestMapping("/chat/group")
    interface IGroup {

        @GetMapping
        @ApiOperation(value = "获取群信息")
        @ApiImplicitParam(name = "groupId", value = "群ID", required = true)
        fun getGroupById(@RequestParam groupId: Int): MResult<ChatGroupBo>

        @GetMapping("/getGroupWithMe")
        @ApiOperation(value = "获取用户所在的所有群列表")
        fun getGroupWithMe(@RequestHeader userId: Int): MResult<List<ChatGroupBo>>

        @GetMapping("/getGroupByBySessionId")
        @ApiOperation(value = "获取会话对应的群信息")
        @ApiImplicitParam(name = "sessionId", value = "会话ID", required = true)
        fun getGroupByBySessionId(@RequestParam sessionId: Int): MResult<ChatGroupBo>

        @PutMapping("/updateGroupNotice")
        @ApiOperation(value = "更新群公告")
        fun updateGroupNotice(notice: String): MResult<ChatGroupBo>

        @PutMapping("/updateGroupName")
        @ApiOperation(value = "更新群名称")
        fun updateGroupName(name: String): MResult<ChatGroupBo>

    }
}
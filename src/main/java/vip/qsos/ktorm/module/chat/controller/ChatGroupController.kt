package vip.qsos.ktorm.module.chat.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RestController
import vip.qsos.ktorm.module.chat.entity.ChatGroupBo
import vip.qsos.ktorm.module.chat.service.IChatService
import vip.qsos.ktorm.util.MResult

@RestController
open class ChatGroupController @Autowired constructor(
        private val mChatGroupService: IChatService.IGroup
) : IChatController.IGroup {

    override fun getGroupById(groupId: Int): MResult<ChatGroupBo> {
        val result = mChatGroupService.getGroupById(groupId)
        return MResult<ChatGroupBo>().result(result)
    }

    override fun getGroupWithMe(userId: Int): MResult<List<ChatGroupBo>> {
        val result = mChatGroupService.getGroupWithMe(userId)
        return MResult<List<ChatGroupBo>>().result(result)
    }

    override fun getGroupByBySessionId(sessionId: Int): MResult<ChatGroupBo> {
        val result = mChatGroupService.getGroupByBySessionId(sessionId)
        return MResult<ChatGroupBo>().result(result)
    }

    override fun updateGroupNotice(notice: String): MResult<ChatGroupBo> {
        val result = mChatGroupService.updateGroupNotice(notice)
        return MResult<ChatGroupBo>().result(result)
    }

    override fun updateGroupName(name: String): MResult<ChatGroupBo> {
        val result = mChatGroupService.updateGroupName(name)
        return MResult<ChatGroupBo>().result(result)
    }

}
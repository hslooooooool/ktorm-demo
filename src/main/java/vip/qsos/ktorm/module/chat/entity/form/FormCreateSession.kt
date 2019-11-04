package vip.qsos.ktorm.module.chat.entity.form

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import vip.qsos.ktorm.module.chat.entity.ChatMessageBo

/**创建会话表单数据
 * @param userIdList 用户ID集合
 * @param message 发送的消息，可为空
 * */
@ApiModel(description = "创建会话表单数据")
data class FormCreateSession(
        @ApiModelProperty(value = "用户ID集合", required = true)
        val userIdList: List<Int>,
        @ApiModelProperty(value = "消息", required = false)
        val message: ChatMessageBo? = null
)
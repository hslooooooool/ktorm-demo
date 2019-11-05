package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(description = "聊天消息内容")
data class ChatContentBo(
        @ApiModelProperty(value = "聊天消息内容Map", required = true, example = "{'contentType':0,'content':'文本消息内容'}")
        var fields: Map<String, Any?>? = null
)
package vip.qsos.ktorm.module.chat.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import org.springframework.web.bind.annotation.RequestParam

/**
 * @author : 华清松
 * 聊天消息内容
 * @param fields 内容map集合
 */
@ApiModel(description = "聊天消息内容")
data class ChatContentBo(
        @RequestParam
        @ApiModelProperty(value = "聊天消息内容Map", required = true, example = "{'contentType':1,'content':'文本消息内容'}")
        var fields: Map<String, Any?>? = null
)
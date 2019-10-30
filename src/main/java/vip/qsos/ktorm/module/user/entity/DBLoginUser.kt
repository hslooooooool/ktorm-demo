package vip.qsos.ktorm.module.user.entity

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import me.liuwj.ktorm.dsl.QueryRowSet
import me.liuwj.ktorm.schema.BaseTable
import me.liuwj.ktorm.schema.int
import me.liuwj.ktorm.schema.varchar
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

private const val TAB_NAME = "t_login_user"

/**
 * @author : 华清松
 * @description : 登录用户表
 */
object DBLoginUser : BaseTable<TableLoginUser>(TAB_NAME) {
    val userId by int("id").primaryKey()
    val userName by varchar("user_name")
    val account by varchar("account")
    val password by varchar("password")
    val avatar by varchar("avatar")
    val birth by varchar("birth")
    val sexuality by int("sexuality")

    override fun doCreateEntity(row: QueryRowSet, withReferences: Boolean): TableLoginUser {
        return TableLoginUser(
                userId = row[userId]!!,
                userName = row[userName] ?: "用户" + row[userId],
                account = row[account]!!,
                password = row[password]!!,
                avatar = row[avatar],
                birth = row[birth],
                sexuality = row[sexuality]
        )
    }
}

@javax.persistence.Entity
@javax.persistence.Table(name = TAB_NAME)
@ApiModel(value = "聊天群实体")
data class TableLoginUser(
        @Id
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @ApiModelProperty(name = "userId", value = "用户ID")
        val userId: Int,

        @Column(name = "user_name")
        @ApiModelProperty(name = "sessionId", value = "用户姓名")
        val userName: String,

        @Column(name = "account")
        @ApiModelProperty(name = "account", value = "用户账号")
        val account: String,

        @Column(name = "password")
        @ApiModelProperty(name = "password", value = "用户密码")
        val password: String,

        @Column(name = "avatar")
        @ApiModelProperty(name = "avatar", value = "头像,http://www.qsos.vip/upload/2018/11/ic_launcher20181225044818498.png")
        val avatar: String? = "http://www.qsos.vip/upload/2018/11/ic_launcher20181225044818498.png",

        @Column(name = "birth")
        @ApiModelProperty(name = "birth", value = "出生,1969-05-05")
        val birth: String? = null,

        @Column(name = "sexuality")
        @ApiModelProperty(name = "sexuality", value = "性别,0女 1男 null未知")
        val sexuality: Int? = null
) {
    /**转化为业务实体*/
    fun toLoginUser(): LoginUser {
        return LoginUser(
                userId = this.userId,
                account = this.account,
                password = this.password,
                userName = this.userName,
                avatar = this.avatar,
                birth = this.birth,
                sexuality = this.sexuality
        )
    }
}
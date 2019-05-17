package vip.qsos.ktorm.localupload.entity

import java.util.*
import javax.annotation.sql.DataSourceDefinition
import javax.persistence.*

/**
 * @author : 华清松
 * @date : 2019-05-17
 * @description : TODO 类说明，描述此类的类型和用途
 */
@Entity
@Table(name = "sys_resource")
class FileResourceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null

    @Column(name = "original_url")
    var originalUrl: String? = null
    @Column(name = "create_date")
    var createDate: Date? = null
}

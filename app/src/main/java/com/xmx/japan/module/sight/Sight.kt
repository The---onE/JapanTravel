package com.xmx.japan.module.sight

import android.content.ContentValues
import android.database.Cursor

import com.avos.avoscloud.AVObject
import com.xmx.japan.common.data.sync.ISyncEntity

import java.util.Date

/**
 * Created by xmx on 2016/6/1.
 * 测试SQLite数据库与LeanCloud数据库同步实体
 */
class Sight : ISyncEntity {
    // 同步ID，手动生成实体默认为空串
    override var cloudId: String = ""
    // 本地数据库ID，手动生成实体默认为-1
    var id: Long = -1

    var name: String? = null // 商品名
    var detail: String? = null // 商品详情
    var category: String? = null // 类别
    var buyFlag: String? = null // 是否已购【0:是 1:否】
    var time: Date? = Date()

    // 将实体转化为可插入或更新至云端数据库的数据
    override fun getContent(tableName: String): AVObject {
        val obj = AVObject(tableName)
        // 有ID用于更新数据，无ID用于插入数据
        if (cloudId.isNotBlank()) {
            obj.objectId = cloudId
        }
        obj.put("Name", name)
        obj.put("Detail", detail)
        obj.put("Category", category)
        obj.put("BuyFlag", buyFlag)
        obj.put("Time", time)
        return obj
    }

    // 将从云端查询的数据转化为实体
    override fun convertToEntity(obj: AVObject): Sight {
        val entity = Sight()
        entity.cloudId = obj.objectId
        entity.name = obj.getString("Name")
        entity.detail = obj.getString("Detail")
        entity.category = obj.getString("Category")
        entity.buyFlag = obj.getString("BuyFlag")
        entity.time = obj.getDate("Time")
        return entity
    }

    // 本地数据库中的表结构
    override fun tableFields(): String {
        return "ID integer not null primary key autoincrement, " +
                "CLOUD_ID text not null, " +
                "Name text, " +
                "Detail text, " +
                "Category text, " +
                "BuyFlag text, " +
                "Time integer not null default(0)"
    }

    // 将实体转化为可插入或更新至本地数据库的数据
    override fun getContent(): ContentValues {
        val content = ContentValues()
        // 有ID用于更新数据，无ID用于插入数据
        if (id > 0) {
            content.put("ID", id)
        }
        // 保存同步ID用于与云端同步
        if (cloudId.isNotBlank()) {
            content.put("CLOUD_ID", cloudId)
        }
        content.put("Name", name)
        content.put("Detail", detail)
        content.put("Category", category)
        content.put("BuyFlag", buyFlag)
        content.put("Time", time?.time)
        return content
    }

    // 将从本地数据库查询的数据转化为实体
    override fun convertToEntity(c: Cursor): Sight {
        val entity = Sight()
        entity.id = c.getLong(0)
        entity.cloudId = c.getString(1)
        entity.name = c.getString(2)
        entity.detail = c.getString(3)
        entity.category = c.getString(4)
        entity.buyFlag = c.getString(5)
        entity.time = Date(c.getLong(6))

        return entity
    }
}

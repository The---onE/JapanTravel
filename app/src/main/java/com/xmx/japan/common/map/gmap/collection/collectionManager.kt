package com.xmx.japan.common.map.gmap.collection

import com.google.android.gms.maps.model.LatLng
import com.xmx.japan.common.data.cloud.BaseCloudEntityManager

/**
 * Created by The_onE on 2017/2/28.
 * 收藏管理器，单例对象
 */
object collectionManager : BaseCloudEntityManager<Collection>(), ICollectionManager<Collection> {
    fun changeTable(name: String) {
        if (name.isNotBlank()) {
            tableName = name
        }
    }

    init {
        tableName = "JapanPOI" // 表名
        entityTemplate = Collection(LatLng(0.0, 0.0), "", "", "") // 实体模版
        userField = "User" // 用户字段
    }
}
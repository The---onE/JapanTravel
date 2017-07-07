package com.xmx.japan.common.map.gmap.route

import com.google.android.gms.maps.model.LatLng
import com.xmx.japan.common.data.cloud.BaseCloudEntityManager
import com.xmx.japan.common.map.gmap.collection.IRouteManager

/**
 * Created by The_onE on 2017/2/28.
 * 路线管理器，单例对象
 */
object routeManager : BaseCloudEntityManager<Route>(), IRouteManager<Route> {
    fun changeTable(name: String) {
        if (name.isNotBlank()) {
            tableName = name
        }
    }

    init {
        tableName = "JapanRoute" // 表名
        entityTemplate = Route(LatLng(0.0, 0.0), LatLng(0.0, 0.0), "", 0, 0f) // 实体模版
        userField = "User" // 用户字段
    }
}
package com.xmx.japan.common.user

import com.avos.avoscloud.AVObject

/**
 * Created by The_onE on 2017/1/22.
 * 用户数据实体
 */
class UserData {
    var objectId: String? = null // ID
    var username: String? = null // 用户名
    var nickname: String? = null // 昵称
    var subscribing: List<*>? = null // 消息推送所订阅频道列表

    companion object {
        /**
         * 将LeanCloud对象转化为实体
         * @param[o] 云端用户数据LeanCloud对象
         * @return 转化后的实体
         */
        fun convert(o: AVObject): UserData {
            val data = UserData()
            data.objectId = o.objectId
            data.username = o.getString("username")
            data.nickname = o.getString("nickname")
            data.subscribing = o.getList("subscribing") as? List<*>

            return data
        }
    }
}

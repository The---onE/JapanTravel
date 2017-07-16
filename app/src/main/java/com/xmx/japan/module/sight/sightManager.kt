package com.xmx.japan.module.sight

/**
 * Created by The_onE on 2016/2/24.
 * 测试同步数据管理器，单例对象
 */
object sightManager {

    // 实体管理器版本，不一致时需更新
    private var syncVersion: Long = 0
    // 自身版本，提示调用者是否有更新
    var version = System.currentTimeMillis()
        private set
    // 实体列表
    private var sightList: List<Sight>? = null

    // 获取数据
    val data: List<Sight>?
        get() = sightList

    var category: String? = null
        set(value) {
            field = value
        }

    var buyFlag: String? = null
        set(value) {
            field = value
        }

    /**
     * 更新数据，若实体有更新则同步更新
     * @return 管理器自身当前版本
     */
    fun updateData(): Long {
        val entityManager = sightEntityManager
        // //判断实体是否有更新
        // 多应用同时操作数据库不判断，直接进行覆盖
        //if (entityManager.sqlManager.version !== syncVersion) {
        syncVersion = entityManager.sqlManager.version
        // 将所有数据按时间降序排列
//        sightList = entityManager.sqlManager.selectAll("Time", false)
        val con = ArrayList<String>()
        if (!category.isNullOrBlank()) {
            con.add("Category like '%$category%'")
        }
        if (!buyFlag.isNullOrBlank()) {
            con.add("BuyFlag like '%$buyFlag%'")
        }
        val array = Array<String>(con.size, { k -> "" })
        sightList = entityManager.sqlManager.selectByCondition("Time", false,
                *con.toArray(array))
        // 数据更新
        version++
        //}
        return version
    }
}

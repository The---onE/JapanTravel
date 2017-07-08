package com.xmx.japan.module.shop

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.AdapterView

import com.xmx.japan.R
import com.xmx.japan.base.activity.BaseTempActivity
import kotlinx.android.synthetic.main.activity_shop.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*

/**
 * Created by xmx on 2016/6/1.
 * 测试SQLite与LeanCloud数据同步管理页面
 */
class ShopActivity : BaseTempActivity() {
    // 数据列表适配器
    private var shopAdapter: ShopAdapter? = null

    private var buyFlag = false

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_shop)
        EventBus.getDefault().register(this)

        fab.attachToListView(listSync)

        shopManager.updateData()
        shopAdapter = ShopAdapter(this, ArrayList<Shop>())
        listSync.adapter = shopAdapter
    }

    override fun setListener() {
        fab.setOnClickListener {
            val dialog = ShopDialog()
            dialog.initDialog(this)
            dialog.show(fragmentManager, "SHOP")
        }
        listSync.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val sync = shopAdapter?.getItem(i) as Shop

            val builder = AlertDialog.Builder(this@ShopActivity)
            builder.setTitle("提示")
            if (sync.buyFlag != "1") {
                builder.setMessage("已经购买了吗？")
                builder.setPositiveButton("确认") { dialogInterface, i ->
                    val update = HashMap<String, Any>()
                    update.put("BuyFlag", "1")
                    // 更新数据
                    shopEntityManager.updateData(sync.cloudId, update,
                            success = {
                                user ->
                                showToast(R.string.update_success)
                                // 刷新列表
                                shopManager.updateData()
                                shopAdapter?.updateList(shopManager.data!!)
                            },
                            error = shopEntityManager.defaultError(this),
                            syncError = shopEntityManager.defaultSyncError(this)
                    )
                }
            } else {
                builder.setMessage("尚未购买吗？")
                builder.setPositiveButton("确认") { dialogInterface, i ->
                    val update = HashMap<String, Any>()
                    update.put("BuyFlag", "0")
                    // 更新数据
                    shopEntityManager.updateData(sync.cloudId, update,
                            success = {
                                user ->
                                showToast(R.string.update_success)
                                // 刷新列表
                                shopManager.updateData()
                                shopAdapter?.updateList(shopManager.data!!)
                            },
                            error = shopEntityManager.defaultError(this),
                            syncError = shopEntityManager.defaultSyncError(this)
                    )
                }
            }
            builder.setNeutralButton("取消") { dialogInterface, i -> dialogInterface.dismiss() }
            builder.show()
        }

        // 长按提示更新或删除数据
        listSync.onItemLongClickListener = AdapterView.OnItemLongClickListener { adapterView, view, i, l ->
            val sync = shopAdapter?.getItem(i) as Shop

            val builder = AlertDialog.Builder(this@ShopActivity)
            builder.setMessage("要更新该记录吗？")
            builder.setTitle("提示")
            builder.setNegativeButton("删除") { dialogInterface, i ->
                // 删除数据
                shopEntityManager.deleteData(sync.cloudId,
                        success = {
                            user ->
                            showToast(R.string.delete_success)
                            // 刷新列表
                            shopManager.updateData()
                            shopAdapter?.updateList(shopManager.data!!)
                        },
                        error = shopEntityManager.defaultError(this),
                        syncError = shopEntityManager.defaultSyncError(this)
                )
            }
            builder.setPositiveButton("更新") { dialogInterface, i ->
                val dialog = ShopDialog()
                dialog.initDialog(this, sync)
                dialog.show(fragmentManager, "SHOP")
            }
            builder.setNeutralButton("取消") { dialogInterface, i -> dialogInterface.dismiss() }
            builder.show()
            true
        }

        btnSearch.setOnClickListener {
            val con = editCondition.text.toString()
            if (!con.isNullOrBlank()) {
                shopManager.category = con
            } else {
                shopManager.category = null
            }
            updateData()
        }

        tvFlag.setOnClickListener {
            if (buyFlag) {
                shopManager.buyFlag = "0"
                tvFlag.text = "未购"
                buyFlag = false
            } else {
                shopManager.buyFlag = null
                tvFlag.text = "全部"
                buyFlag = true
            }
            updateData()
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        // 加载数据
        shopEntityManager.syncFromCloud(null,
                success = {
                    user, entities ->
                    shopManager.updateData()
                    shopAdapter?.updateList(shopManager.data!!)
                    showToast(R.string.sync_success)
                },
                error = shopEntityManager.defaultError(this),
                syncError = shopEntityManager.defaultSyncError(this)
        )
    }

    fun updateData() {
        shopManager.updateData()
        shopAdapter?.updateList(shopManager.data!!)
    }

    @Subscribe
    fun onEvent(event: ShopEvent) {
        updateData()
    }
}

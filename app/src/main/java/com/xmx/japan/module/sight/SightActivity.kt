package com.xmx.japan.module.sight

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.AdapterView

import com.xmx.japan.R
import com.xmx.japan.base.activity.BaseTempActivity
import kotlinx.android.synthetic.main.activity_sight.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*
import android.content.Context.CLIPBOARD_SERVICE
import android.content.ClipData
import android.support.v4.content.ContextCompat.startActivity
import android.content.Intent
import android.net.Uri


/**
 * Created by xmx on 2016/6/1.
 * 测试SQLite与LeanCloud数据同步管理页面
 */
class SightActivity : BaseTempActivity() {
    // 数据列表适配器
    private var sightAdapter: SightAdapter? = null

    private var buyFlag = false

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_sight)
        EventBus.getDefault().register(this)

        fab.attachToListView(listSync)

        sightManager.updateData()
        sightAdapter = SightAdapter(this, ArrayList<Sight>())
        listSync.adapter = sightAdapter
    }

    override fun setListener() {
        fab.setOnClickListener {
            val dialog = SightDialog()
            dialog.initDialog(this)
            dialog.show(fragmentManager, "SHOP")
        }
        listSync.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val sync = sightAdapter?.getItem(i) as Sight
//            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//            val clip = ClipData.newPlainText(sync.name, sync.name)
//            clipboard.primaryClip = clip;
//            showToast("${sync.name} 已复制到剪切板")
            val gmmIntentUri = Uri.parse("geo:0,0?q=${sync.name}")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.`package` = "com.google.android.apps.maps"
            startActivity(mapIntent)
        }

        // 长按提示更新或删除数据
        listSync.onItemLongClickListener = AdapterView.OnItemLongClickListener { adapterView, view, i, l ->
            val sync = sightAdapter?.getItem(i) as Sight

            val builder = AlertDialog.Builder(this@SightActivity)
            builder.setMessage("要更新该记录吗？")
            builder.setTitle("提示")
            builder.setNegativeButton("删除") { dialogInterface, i ->
                // 删除数据
                sightEntityManager.deleteData(sync.cloudId,
                        success = {
                            user ->
                            showToast(R.string.delete_success)
                            // 刷新列表
                            sightManager.updateData()
                            sightAdapter?.updateList(sightManager.data!!)
                        },
                        error = sightEntityManager.defaultError(this),
                        syncError = sightEntityManager.defaultSyncError(this)
                )
            }
            builder.setPositiveButton("更新") { dialogInterface, i ->
                val dialog = SightDialog()
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
                sightManager.category = con
            } else {
                sightManager.category = null
            }
            updateData()
        }

        tvFlag.setOnClickListener {
            if (buyFlag) {
                sightManager.buyFlag = "0"
                tvFlag.text = "未购"
                buyFlag = false
            } else {
                sightManager.buyFlag = null
                tvFlag.text = "全部"
                buyFlag = true
            }
            updateData()
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        // 加载数据
        sightEntityManager.syncFromCloud(null,
                success = {
                    user, entities ->
                    sightManager.updateData()
                    sightAdapter?.updateList(sightManager.data!!)
                    showToast(R.string.sync_success)
                },
                error = sightEntityManager.defaultError(this),
                syncError = sightEntityManager.defaultSyncError(this)
        )
    }

    fun updateData() {
        sightManager.updateData()
        sightAdapter?.updateList(sightManager.data!!)
    }

    @Subscribe
    fun onEvent(event: SightEvent) {
        updateData()
    }
}

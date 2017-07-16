package com.xmx.japan.module.sight

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xmx.japan.R
import com.xmx.japan.base.dialog.BaseDialog
import kotlinx.android.synthetic.main.dialog_sight.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by The_onE on 2017/5/22.
 * 测试
 */

class SightDialog : BaseDialog() {
    var addFlag = true
    var mSight: Sight? = null

    override fun initDialog(context: Context) {
        mContext = context
        addFlag = true
    }

    fun initDialog(context: Context, sight: Sight) {
        mContext = context
        addFlag = false
        mSight = sight
    }

    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.dialog_sight, container)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        mSight?.apply {
            editName.setText(mSight!!.name)
            editDetaile.setText(mSight!!.detail)
            editCategory.setText(mSight!!.category)
        }
    }

    override fun setListener(view: View) {
        view.findViewById(R.id.btnOK).setOnClickListener {
            val name = editName.text.toString()
            val detail = editDetaile.text.toString()
            val category = editCategory.text.toString()

            if (addFlag) {
                val sight = Sight()
                sight.name = name
                sight.detail = detail
                sight.category = category
                sight.buyFlag = "0"
                sight.time = Date()

                sightEntityManager.insertData(sight,
                        success = {
                            user, objectId ->
                            showToast(R.string.add_success)
                            EventBus.getDefault().post(SightEvent())
                            dismiss()
                        },
                        error = sightEntityManager.defaultError(mContext!!),
                        syncError = sightEntityManager.defaultSyncError(mContext!!)
                )
            } else {
                val update = HashMap<String, Any>()
                update.put("Name", name)
                update.put("Detail", detail)
                update.put("Category", category)
                // 更新数据
                sightEntityManager.updateData(mSight!!.cloudId, update,
                        success = {
                            user ->
                            showToast(R.string.update_success)
                            EventBus.getDefault().post(SightEvent())
                            dismiss()
                        },
                        error = sightEntityManager.defaultError(mContext!!),
                        syncError = sightEntityManager.defaultSyncError(mContext!!)
                )
            }
        }

        view.findViewById(R.id.btnCancel).setOnClickListener {
            dismiss()
        }
    }

    override fun processLogic(view: View, savedInstanceState: Bundle?) {

    }
}

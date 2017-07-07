package com.xmx.japan.module.shop

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xmx.japan.R
import com.xmx.japan.base.dialog.BaseDialog
import kotlinx.android.synthetic.main.dialog_shop.*
import org.greenrobot.eventbus.EventBus
import java.util.*

/**
 * Created by The_onE on 2017/5/22.
 * 测试
 */

class ShopDialog : BaseDialog() {
    var addFlag = true
    var mShop: Shop? = null

    override fun initDialog(context: Context) {
        mContext = context
        addFlag = true
    }

    fun initDialog(context: Context, shop: Shop) {
        mContext = context
        addFlag = false
        mShop = shop
    }

    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.dialog_shop, container)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {
        mShop?.apply {
            editName.setText(mShop!!.name)
            editDetaile.setText(mShop!!.detail)
            editCategory.setText(mShop!!.category)
        }
    }

    override fun setListener(view: View) {
        view.findViewById(R.id.btnOK).setOnClickListener {
            val name = editName.text.toString()
            val detail = editDetaile.text.toString()
            val category = editCategory.text.toString()

            if (addFlag) {
                val shop = Shop()
                shop.name = name
                shop.detail = detail
                shop.category = category
                shop.buyFlag = "0"
                shop.time = Date()

                shopEntityManager.insertData(shop,
                        success = {
                            user, objectId ->
                            showToast(R.string.add_success)
                            EventBus.getDefault().post(ShopEvent())
                            dismiss()
                        },
                        error = shopEntityManager.defaultError(mContext!!),
                        syncError = shopEntityManager.defaultSyncError(mContext!!)
                )
            } else {
                val update = HashMap<String, Any>()
                update.put("Name", name)
                update.put("Detail", detail)
                update.put("Category", category)
                // 更新数据
                shopEntityManager.updateData(mShop!!.cloudId, update,
                        success = {
                            user ->
                            showToast(R.string.update_success)
                            EventBus.getDefault().post(ShopEvent())
                            dismiss()
                        },
                        error = shopEntityManager.defaultError(mContext!!),
                        syncError = shopEntityManager.defaultSyncError(mContext!!)
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

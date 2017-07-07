package com.xmx.japan.module.shop

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.xmx.japan.R
import com.xmx.japan.common.data.BaseEntityAdapter

import java.text.SimpleDateFormat

/**
 * Created by The_onE on 2016/3/27.
 * 测试SQLite数据库与LeanCloud数据库同步实体适配器
 */
class ShopAdapter(context: Context, data: List<Shop>) : BaseEntityAdapter<Shop>(context, data) {

    // 复用列表中的项
    private class ViewHolder {
        internal var view: View? = null
        internal var name: TextView? = null
        internal var detail: TextView? = null
        internal var category: TextView? = null
    }

    // 将数据填充到列表项中
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: ViewHolder?
        // 是否有可复用的项
        if (view == null) {
            // 若没有则创建新的ViewHolder
            view = LayoutInflater.from(mContext).inflate(R.layout.item_shop, null)
            if (view != null) {
                holder = ViewHolder()
                holder.view = view
                holder.name = view.findViewById(R.id.itemName) as TextView
                holder.detail = view.findViewById(R.id.itemDetail) as TextView
                holder.category = view.findViewById(R.id.itemCategory) as TextView
                view.tag = holder
            } else {
                holder = null
            }
        } else {
            holder = view.tag as ViewHolder
        }

        // 将数据显示在列表项中
        if (position < mData.size) {
            if (holder != null) {
                val sync = mData[position]
                if (sync.buyFlag == "1") {
                    holder.view!!.setBackgroundColor(Color.parseColor("#BBFFCC"))
                } else {
                    holder.view!!.setBackgroundColor(Color.parseColor("#FFFFFF"))
                }
                holder.name!!.text = sync.name
                holder.detail!!.text = sync.detail
                holder.category!!.text = sync.category
//                val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//                val timeString = df.format(sync.time)
//                holder.time!!.text = timeString
            }
        } else {
            holder?.name!!.text = "加载失败"
        }

        return view!!
    }
}
package com.xmx.japan.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.xmx.japan.R
import com.xmx.japan.module.log.OperationLogActivity
import com.xmx.japan.base.fragment.BaseFragment
import com.xmx.japan.module.map.google.GMapActivity
import com.xmx.japan.module.shop.ShopActivity
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * Created by The_onE on 2017/1/18.
 * 测试各零散组件是否运行正常，演示其使用方法
 */
class HomeFragment : BaseFragment() {
    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }

    override fun setListener(view: View) {
        // 谷歌地图
        btnGMap.setOnClickListener {
            startActivity(GMapActivity::class.java)
        }

        // 购物清单
        btnShop.setOnClickListener {
            startActivity(ShopActivity::class.java)
        }

        // 查看日志
        btnShowOperationLog.setOnClickListener {
            startActivity(OperationLogActivity::class.java)
        }
    }

    override fun processLogic(view: View, savedInstanceState: Bundle?) {

    }
}
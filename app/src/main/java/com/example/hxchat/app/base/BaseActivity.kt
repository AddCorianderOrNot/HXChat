package com.example.hxchat.app.base

import android.content.res.Resources
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle
import com.example.hxchat.R
import com.example.hxchat.app.App
import com.example.hxchat.app.Constants
import com.example.hxchat.app.event.AppViewModel
import com.example.hxchat.app.event.EventViewModel
import com.example.hxchat.app.ext.dismissLoadingExt
import com.example.hxchat.app.ext.showLoadingExt
import com.example.hxchat.app.util.Event
import com.example.hxchat.app.util.ToastUtils
import com.example.hxchat.data.model.bean.Operator
import com.king.easychat.netty.packet.Packet
import com.king.easychat.netty.packet.PacketType
import me.hgj.jetpackmvvm.base.activity.BaseVmActivity
import me.hgj.jetpackmvvm.base.activity.BaseVmDbActivity
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.getAppViewModel
import me.jessyan.autosize.AutoSizeCompat
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber

/**
 * code:Pbihao
 * date:2020.10.4
 */
abstract class BaseActivity<VM: BaseViewModel, DB : ViewDataBinding> :BaseVmDbActivity<VM, DB>(){

    val appViewModel: AppViewModel by lazy { getAppViewModel<AppViewModel>() }
    val eventViewModel: EventViewModel by lazy { getAppViewModel<EventViewModel>() }

    var isStop = true

    abstract override fun layoutId(): Int

    abstract override fun initView(savedInstanceState: Bundle?)

    override fun createObserver() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(event: Operator){
        Timber.d("event:${event.event}")
        if(!isStop) {
            when (event.event) {
                Constants.EVENT_NETTY_DISCONNECT -> showToast(R.string.tips_netty_disconnect)
                Constants.EVENT_NETTY_RECONNECT -> showToast(R.string.tips_netty_reconnect)
            }
        }
    }

    fun showToast(@StringRes resId: Int) {
        ToastUtils.showToast(this, resId)
    }



    /**
     * 等待框
     */
    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    override fun dismissLoading() {
        dismissLoadingExt()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Event.registerEvent(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        Event.unregisterEvent(this)
    }

    override fun onResume() {
        isStop = false
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        isStop = true
    }
    fun getApp(): App = application as  App


    fun getUserEmail() = appViewModel.userInfo.value?.email ?: ""
    fun getUserAvatar() = appViewModel.userInfo.value?.icon ?: ""
}
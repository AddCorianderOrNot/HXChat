package com.example.hxchat.viewmodel.request

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.hxchat.app.network.apiService
import com.example.hxchat.data.model.bean.User
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 *Created by Pbihao
 * on 2020/11/26
 */
//这个类用来向服务器询问，某个好友最后一次阅读的时间，用来判断这条消息是否已读
class RequestLastReadtime : BaseViewModel(){
    var lastReadTime : MutableLiveData<ResultState<Long>> = MutableLiveData()

    /**
     * 用来更新我最后一次阅读朋友的时间是消息的
     */
    fun sendLastReadTime(myEmail:String?, friendEmail:String?){
        val time = System.currentTimeMillis()//把这个给到服务器
    }

    /**
     * 用来获取朋友最后一次阅读我的消息的时间
     */
    fun getLastReadTime(maEmail:String?, friendEmail: String?){
        val time = System.currentTimeMillis()
        lastReadTime.postValue(ResultState.onAppSuccess(time))
    }
}
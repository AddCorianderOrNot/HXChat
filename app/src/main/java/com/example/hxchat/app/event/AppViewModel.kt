package com.example.hxchat.app.event

import com.example.hxchat.app.util.CacheUtil
import com.example.hxchat.data.model.bean.UserInfo
import com.example.hxchat.data.packet.resp.LoginResp
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData

class AppViewModel:BaseViewModel(){

    var userInfo = UnPeekLiveData<UserInfo>()

    var friendEmail = UnPeekLiveData<String>()

    var isLogin =  UnPeekLiveData<Boolean>()

    init {
        userInfo.value = CacheUtil.getUser()
    }

    fun getFriendEmail() : String? = friendEmail.value

    fun changeUserInfo(user: UserInfo){
        CacheUtil.setUser(user)
        CacheUtil.setIsLogin(true)
        userInfo.postValue(user)
    }
}
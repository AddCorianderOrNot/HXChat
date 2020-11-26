package com.example.hxchat.viewmodel.request

import android.app.Application
import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.hxchat.app.network.apiService
import com.example.hxchat.app.network.stateCallback.ListDataiState
import com.example.hxchat.data.model.bean.User
import com.example.hxchat.data.repository.HttpRequestCoroutline
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState
import org.greenrobot.eventbus.EventBus


/**
 *Created by Pbihao
 * on 2020/10/8
 */
class RequestFriendsViewModel : BaseViewModel(){
    var friendsData : MutableLiveData<ResultState<ArrayList<User>>> = MutableLiveData()
    var newFriendsData : MutableLiveData<ResultState<ArrayList<User>>> = MutableLiveData()

    /**
     * 获取用户的好友列表
     * 如果是已经同意了的好友，那么user类中的
     * var newFriend:Boolean = false
     * 这个值为false
     * 否则的话为true
     */
    fun getfriends(){
        Log.d("friendsData", "拉取")
        request(
            { apiService.getFriends() },
            friendsData
        )
    }

    /**
     * 获得新的好友申请的列表
     */
    fun getNewFriend(){
        val user = User(
        "1234567@qq.com",
        "Pbihao",
        "",
        "",
            ""
        )
        val list = ArrayList<User>()
        list.add(user)
        newFriendsData.postValue(ResultState.onAppSuccess(list))
    }

    /**
     * 同意了添加好友
     */
    fun acceptNewFriend(myEmail:String, friendEmail:String){

    }

    /**
     * 拒绝了添加好友
     */
    fun cancelNewFriend(myEmail: String, friendEmail:String){

    }
}
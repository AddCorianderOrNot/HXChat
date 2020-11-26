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
     * remark这个值为null
     * 否则的话为"new friend"
     */
    fun getfriends(){
        Log.d("friendsData", "拉取")
        request(
            { apiService.getFriends() },
            friendsData
        )
    }


    /**
     * 同意了添加好友
     */
    fun acceptNewFriend(myEmail:String?, friendEmail:String?){

    }

    /**
     * 拒绝了添加好友
     */
    fun cancelNewFriend(myEmail: String?, friendEmail:String?){

    }
}
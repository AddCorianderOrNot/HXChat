package com.example.hxchat.viewmodel.request

import android.text.method.MultiTapKeyListener
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.hxchat.app.network.apiService
import com.example.hxchat.data.model.bean.User
import com.example.hxchat.data.model.bean.UserInfo
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject


/**
 *Created by Pbihao
 * on 2020/10/12
 */
class RequestUserInfoViewModel : BaseViewModel(){
    var user : MutableLiveData<ResultState<ArrayList<User>>> = MutableLiveData()
    var addFriendsUserInfo : MutableLiveData<ResultState<User>> = MutableLiveData()
    var userInfo: MutableLiveData<ResultState<UserInfo>> = MutableLiveData()

    /**
     * 请求添加好友
     */
    fun addFriend(email: String) {
        Log.d("addFriend:", email)
        val addFriendParm = JSONObject()
        addFriendParm.put("friend_id", email)
        request(
            {
                apiService.addFriend(
                    RequestBody.create(
                        MediaType.parse("application/json"),
                        addFriendParm.toString()
                    )
                )
            },
            addFriendsUserInfo
        )
    }

    /**
     * 修改个人的签名
     * 网络请求的时候需要显示dialog的提示
     */
    fun changeUserSignature(signature:String){
        request(
            { apiService.update("signture", signature) },
            userInfo
        )
    }

    /**
     * 修改个人的昵称
     * * 网络请求的时候需要显示dialog的提示
     */
    fun changeNickname(nickName:String){
        request(
            { apiService.update("nickName", nickName) },
            userInfo
        )
    }
}
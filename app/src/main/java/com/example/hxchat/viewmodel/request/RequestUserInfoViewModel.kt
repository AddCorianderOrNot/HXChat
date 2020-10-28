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
import java.io.File


/**
 *Created by Pbihao
 * on 2020/10/12
 */
class RequestUserInfoViewModel : BaseViewModel(){
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

    fun changeAvatar(file: File?){
        val  user = UserInfo("1435343052@qq.com", "nickName", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1602675229475&di=4bafc6967f56dbefaa7e34b9f919ac87&imgtype=0&src=http%3A%2F%2Fc-ssl.duitang.com%2Fuploads%2Fitem%2F201910%2F04%2F20191004114347_coqyw.thumb.400_0.jpeg",
            "123", "123", "好累啊")
        userInfo.postValue(ResultState.onAppSuccess(user))
    }
}
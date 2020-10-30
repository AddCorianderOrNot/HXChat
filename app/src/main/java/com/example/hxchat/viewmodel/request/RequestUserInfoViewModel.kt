package com.example.hxchat.viewmodel.request

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.hxchat.app.network.apiService
import com.example.hxchat.data.model.bean.User
import com.example.hxchat.data.model.bean.UserInfo
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState
import okhttp3.MediaType
import okhttp3.MultipartBody
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
    fun changeUserSignature(signature: String){
        request(
            { apiService.update("signature", signature) },
            userInfo
        )
    }

    /**
     * 修改个人的昵称
     * * 网络请求的时候需要显示dialog的提示
     */
    fun changeNickname(nickName: String){
        request(
            { apiService.update("nickname", nickName) },
            userInfo
        )
    }

    fun changeAvatar(file: File){
        Log.d("image", file.toString())
        val textType = MediaType.parse("text/plain") // 文本类型
        val key = RequestBody.create(textType, "image")
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
        val body = MultipartBody.Part.createFormData("value", file.name, requestFile)
        request(
            { apiService.update(key, body) },
            userInfo
        )
    }
}
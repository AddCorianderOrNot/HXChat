package com.example.hxchat.viewmodel.request

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bumptech.glide.request.RequestCoordinator
import com.example.hxchat.app.network.apiService
import com.example.hxchat.app.util.Event.sendEvent
import com.example.hxchat.data.model.bean.User
import com.example.hxchat.data.model.bean.UserInfo
import com.example.hxchat.data.packet.resp.LogoutResp
import com.example.hxchat.data.repository.HttpRequestCoroutline
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

/**
 *Created by Pbihao
 * on 2020/10/6
 */

class RequestLoginRegisterViewModel : BaseViewModel(){

    //自动脱壳过滤处理请求结果，判断结果是否成功
    var loginResult = MutableLiveData<ResultState<UserInfo>>()
    //判断是否操作成功
    //操作包括退出登录，修改头像，修改昵称，修改签名等
    var requestSucsess  = MutableLiveData<ResultState<Boolean>>()

    // 需要实现自动登录
    fun login(){
        request(
            { apiService.login()},
            loginResult
        )
    }

    fun loginReq(email: String, password: String) {
        val parm = JSONObject()
        parm.put("email", email)
        parm.put("password", password)
        val requestBody = RequestBody.create(
            MediaType.parse("application/json"),
            parm.toString()
        )
        Log.d("loginReq", requestBody.toString())
        request(
            { apiService.login(requestBody) },
            loginResult,
            true,
            "正在登陆..."
        )
    }


    fun registerAndLogin(nickname: String, email: String, password: String) {
        request(
            { HttpRequestCoroutline.register(nickname, email, password) },
            loginResult,
            true,
            "正在注册..."
        )
    }

    // 退出登录请求
    fun quitLogin(user:UserInfo?){
        if (user == null){
            requestSucsess.postValue(ResultState.onAppSuccess(true))
            return
        }

        requestSucsess.postValue(ResultState.onAppSuccess(true))
    }
}
package com.example.hxchat.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.example.hxchat.data.model.bean.UserInfo
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.state.ResultState


/**
 *Created by Pbihao
 * on 2020/10/30
 */
class RequestChangePasswordViewModel: BaseViewModel(){
    var changePasswordResult = MutableLiveData<ResultState<UserInfo>>()

    /*
    * 传入过去的密码和新的密码，如果修改密码成功，则改变changePasswordResult的值
    * 失败的可能结果为原密码与用户的密码不一致
    * 请求的时候需要显示等待框
    * */
    fun changePassword(UsedPassword:String, newPassword:String){

    }
}
package com.example.hxchat.ui.fragment.userinfo

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.PermissionUtils.permission
import com.example.hxchat.R
import com.example.hxchat.app.Constants
import com.example.hxchat.app.base.BaseFragment
import com.example.hxchat.app.ext.nav
import com.example.hxchat.app.ext.showMessage
import com.example.hxchat.app.util.CacheUtil
import com.example.hxchat.data.model.bean.UserInfo
import com.example.hxchat.databinding.FragmentMeUserInfoBinding
import com.example.hxchat.viewmodel.request.RequestLoginRegisterViewModel
import com.example.hxchat.viewmodel.state.MeUserInfoViewModel
import com.king.easychat.glide.GlideEngine
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import kotlinx.android.synthetic.main.home_toolbar.*
import me.hgj.jetpackmvvm.ext.download.ShareDownLoadUtil.putBoolean
import me.hgj.jetpackmvvm.ext.navigateAction
import me.hgj.jetpackmvvm.ext.parseState
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.hxchat.app.util.Event
import com.example.hxchat.data.packet.resp.LogoutResp
import com.tbruyelle.rxpermissions2.RxPermissions
import java.io.File

/**
 *Created by Pbihao
 * on 2020/10/11
 */

class meUserInfoFragment: BaseFragment<MeUserInfoViewModel, FragmentMeUserInfoBinding>(){
    private var userInfo:UserInfo? = null
    private val requestLoginRegisterViewModel:RequestLoginRegisterViewModel by  viewModels()
    override fun layoutId(): Int = R.layout.fragment_me_user_info
    override fun initView(savedInstanceState: Bundle?) {
        ivLeft.setOnClickListener{
            nav().navigateUp()
        }
        userInfo = CacheUtil.getUser()
        mDatabind.data = userInfo
        mDatabind.click = ProxyClick()
    }

    override fun createObserver() {
        requestLoginRegisterViewModel.requestSucsess.observe(viewLifecycleOwner, Observer { resultState ->
            parseState(resultState, {
                Log.d("meUserInfo:", "fuck")
                CacheUtil.setUser(null)
                CacheUtil.setIsLogin(false)
                appViewModel.isLogin.postValue(false)
                appViewModel.userInfo.postValue(null)
                nav().navigateUp()
            }, {
                showMessage(it.errorMsg)
            })
        })
        appViewModel.userInfo.observe(viewLifecycleOwner, Observer {
            userInfo = it
            mDatabind.data = it
        })
    }


    private fun obtainSelectPhoto(data: Intent?):Uri?{
        val result = Matisse.obtainPathResult(data)
        return Uri.fromFile(File(result[0]))
    }

    inner class ProxyClick{
        fun quitLogin(){
            Log.d("123", "tuichudenglu")
            eventViewModel.quitLogin.postValue(true)
            requestLoginRegisterViewModel.quitLogin(appViewModel.userInfo.value)
        }
        fun toEditNickname(){
            nav().navigateAction(R.id.action_meUserInfoFragment_to_changeUserInfoFragment, Bundle().apply {
                putInt(Constants.KEY_TYPE, Constants.ChangeMeNickName)
            })
        }

        @SuppressLint("CheckResult")
        fun toEditIcon(){
            rxPermission.request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe{
                    if(it){
                        Matisse.from(activity)
                            .choose(MimeType.ofImage())
                            .maxSelectable(1)
                            .gridExpectedSize(resources.getDimensionPixelSize(R.dimen.size_120dp))
                            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                            .thumbnailScale(0.85f)
                            .imageEngine(GlideEngine())
                            .forResult(Constants.REQ_SELECT_PHOTO)
                    }
                }
            Log.d("select", "选择了一张图片")
        }
        fun toEditSgnature(){
            nav().navigateAction(R.id.action_meUserInfoFragment_to_changeUserInfoFragment, Bundle().apply {
                putInt(Constants.KEY_TYPE, Constants.ChangeMeSignature)
            })
        }
    }
}
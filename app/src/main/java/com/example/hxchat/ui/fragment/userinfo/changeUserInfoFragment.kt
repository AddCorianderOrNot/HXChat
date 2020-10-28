package com.example.hxchat.ui.fragment.userinfo

import android.os.Bundle
import android.provider.SyncStateContract
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.hxchat.R
import com.example.hxchat.app.Constants
import com.example.hxchat.app.base.BaseFragment
import com.example.hxchat.app.ext.nav
import com.example.hxchat.app.ext.showMessage
import com.example.hxchat.databinding.FragmentChangeUserInfoBinding
import com.example.hxchat.viewmodel.request.RequestUserInfoViewModel
import com.example.hxchat.viewmodel.state.ChangeUserInfoViewModel
import kotlinx.android.synthetic.main.fragment_change_user_info.*
import kotlinx.android.synthetic.main.home_toolbar.*
import me.hgj.jetpackmvvm.ext.parseState


/**
 *Created by Pbihao
 * on 2020/10/15
 */
class changeUserInfoFragment : BaseFragment<ChangeUserInfoViewModel, FragmentChangeUserInfoBinding>(){
    override fun layoutId(): Int = R.layout.fragment_change_user_info
    private val requestUserInfoViewModel:RequestUserInfoViewModel by viewModels()
    private var change_type:Int = 0
    override fun initView(savedInstanceState: Bundle?) {
        ivLeft.setOnClickListener{
            nav().navigateUp()
        }
        ivRight.setImageResource(R.drawable.btn_save_selector)
        ivRight.setOnClickListener{clickSave()}

        arguments?.run {
            getInt(Constants.KEY_TYPE)?.let {
                change_type = it
            }
        }

        when(change_type){
            Constants.ChangeMeSignature->{
                tvTitle.text = "修改签名"
                etContent.hint = "填写您的签名"
            }
            Constants.ChangeMeNickName->{
                tvTitle.text = "修改昵称"
                etContent.hint = "请输入新的昵称"
            }
        }
    }

    override fun createObserver() {
        requestUserInfoViewModel.userInfo.observe(viewLifecycleOwner, Observer { resultState->
            parseState(resultState,{
                appViewModel.changeUserInfo(it)
                nav().navigateUp()
            },{
                showMessage("修改失败")
            })
        })
    }

    fun clickSave(){
        val content:String = etContent.text.toString()
        when(change_type){
            Constants.ChangeMeSignature->{
                requestUserInfoViewModel.changeUserSignature(content)
            }
            Constants.ChangeMeNickName->{
                requestUserInfoViewModel.changeNickname(content)
            }
        }
    }
}
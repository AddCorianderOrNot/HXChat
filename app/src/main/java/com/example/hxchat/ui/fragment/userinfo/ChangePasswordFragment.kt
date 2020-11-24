package com.example.hxchat.ui.fragment.userinfo

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.hxchat.R
import com.example.hxchat.app.base.BaseFragment
import com.example.hxchat.app.ext.nav
import com.example.hxchat.app.ext.showMessage
import com.example.hxchat.app.util.CacheUtil
import com.example.hxchat.databinding.FragmentChangePasswordBinding
import com.example.hxchat.ui.fragment.login.RegisterFragment
import com.example.hxchat.viewmodel.request.RequestChangePasswordViewModel
import com.example.hxchat.viewmodel.request.RequestUserInfoViewModel
import com.example.hxchat.viewmodel.state.ChangePasswordViewModel
import kotlinx.android.synthetic.main.center_toolbar.*
import kotlinx.android.synthetic.main.center_toolbar.view.*
import me.hgj.jetpackmvvm.ext.navigateAction
import me.hgj.jetpackmvvm.ext.parseState


/**
 *Created by Pbihao
 * on 2020/10/30
 */

class ChangePasswordFragment: BaseFragment<ChangePasswordViewModel, FragmentChangePasswordBinding>(){
    override fun layoutId(): Int = R.layout.fragment_change_password
    private val requestChangePasswordViewModel: RequestChangePasswordViewModel by viewModels()
    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel
        mDatabind.click = CLickProxy()

        center_toolbar.tvTitle.text = "注册 "
        center_toolbar.ivLeft.setOnClickListener{
            nav().navigateUp()
        }
    }

    override fun createObserver() {
        requestChangePasswordViewModel.changePasswordResult.observe(viewLifecycleOwner, Observer { resultState ->
            parseState(resultState, {
                appViewModel.changeUserInfo(it)
                nav().navigateUp()
            },{
                showMessage(it.errorMsg)
            })
        })
    }

    inner class CLickProxy(){
        fun confirm(){
            when{
                mViewModel.usedPassword.get().isEmpty() -> showMessage("请填写原密码")
                mViewModel.password2.get().isEmpty() -> showMessage("请填写确认密码")
                mViewModel.password.get().length < 6 -> showMessage("密码最少6位")
                mViewModel.password.get() != mViewModel.password2.get() -> showMessage("密码不一致")
                else -> requestChangePasswordViewModel.changePassword(
                    mViewModel.usedPassword.get(),
                    mViewModel.password.get()
                )
            }
        }
    }
}
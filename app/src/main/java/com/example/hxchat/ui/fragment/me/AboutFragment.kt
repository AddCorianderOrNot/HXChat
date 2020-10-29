package com.example.hxchat.ui.fragment.me

import android.os.Bundle
import com.example.hxchat.R
import com.example.hxchat.app.base.BaseFragment
import com.example.hxchat.app.ext.nav
import com.example.hxchat.databinding.FragmentAboutBinding
import com.example.hxchat.viewmodel.state.MeViewModel
import kotlinx.android.synthetic.main.fragment_about.*


/**
 *Created by Pbihao
 * on 2020/10/4
 */

class AboutFragment : BaseFragment<MeViewModel, FragmentAboutBinding>(){

    override fun layoutId() : Int = R.layout.fragment_about

    override fun initView(savedInstanceState: Bundle?) {
        toolbar.setNavigationOnClickListener {
            nav().navigateUp()
        }

    }



    inner class ProxyClick{

    }
}
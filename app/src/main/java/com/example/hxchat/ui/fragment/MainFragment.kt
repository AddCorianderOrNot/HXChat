package com.example.hxchat.ui.fragment

import android.os.Bundle
import com.example.hxchat.R
import com.example.hxchat.app.base.BaseFragment
import com.example.hxchat.app.ext.init
import com.example.hxchat.app.ext.initMain
import com.example.hxchat.databinding.FragmentMainBinding
import com.example.hxchat.viewmodel.state.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*

/**
 *Created by Pbihao
 * on 2020/10/4
 */

@Suppress("DEPRECATION")
class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>(){

    override fun layoutId(): Int = R.layout.fragment_main

    override fun initView(savedInstanceState: Bundle?) {
        mainViewpager.initMain(this)

        mainBottom.init{
            when(it){
                R.id.menu_home -> mainViewpager.setCurrentItem(0, false)
                R.id.menu_friend -> mainViewpager.setCurrentItem(1, false)
                R.id.menu_me -> mainViewpager.setCurrentItem(2, false)
            }
        }


    }

}
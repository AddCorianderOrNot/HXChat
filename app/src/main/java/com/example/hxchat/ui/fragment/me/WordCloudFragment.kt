package com.example.hxchat.ui.fragment.me

import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.hxchat.R
import com.example.hxchat.app.base.BaseFragment
import com.example.hxchat.app.ext.nav
import com.example.hxchat.app.ext.showMessage
import com.example.hxchat.databinding.FragmentWordCloudBinding
import com.example.hxchat.viewmodel.request.RequestWordCloudViewModel
import com.example.hxchat.viewmodel.state.MeViewModel
import kotlinx.android.synthetic.main.center_toolbar.*
import kotlinx.android.synthetic.main.center_toolbar.view.ivLeft
import kotlinx.android.synthetic.main.center_toolbar.view.tvTitle
import kotlinx.android.synthetic.main.fragment_word_cloud.*
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.screenHeight
import me.hgj.jetpackmvvm.ext.util.screenWidth
import net.alhazmy13.wordcloud.ColorTemplate
import net.alhazmy13.wordcloud.WordCloud
import kotlin.random.Random


/**
 *Created by Pbihao
 * on 2020/10/4
 */

class WordCloudFragment : BaseFragment<MeViewModel, FragmentWordCloudBinding>(){

    val requestWordCloud:RequestWordCloudViewModel by viewModels()

    override fun layoutId() : Int = R.layout.fragment_word_cloud

    override fun initView(savedInstanceState: Bundle?) {
        center_toolbar.tvTitle.text = "词云"
        center_toolbar.ivLeft.setOnClickListener{
            nav().navigateUp()
        }

        requestWordCloud.get_wordCloud()
    }

    override fun createObserver() {
        requestWordCloud.wordCloud.observe(viewLifecycleOwner, Observer {resultState ->

            parseState(resultState,{
                activity?.runOnUiThread(Runnable {
                    wordCloud.setDataSet(it)
                    wordCloud.setSize(400,700)
                    wordCloud.setColors(ColorTemplate.MATERIAL_COLORS)
                    wordCloud.notifyDataSetChanged()
                })

            },{
                showMessage(it.errorMsg)
            })
        })

    }

}
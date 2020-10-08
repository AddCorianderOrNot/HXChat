package com.example.hxchat.ui.fragment.friends

import android.os.Bundle
import android.view.View
import com.example.hxchat.ui.adapter.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.hxchat.R
import com.example.hxchat.app.base.BaseFragment
import com.example.hxchat.data.model.bean.User
import com.example.hxchat.data.packet.resp.AcceptResp
import com.example.hxchat.databinding.FragmentFriendsBinding
import com.example.hxchat.viewmodel.state.FriendsViewModel
import com.king.frame.mvvmframe.bean.Resource
import kotlinx.android.synthetic.main.fragment_friends.*
import kotlinx.android.synthetic.main.home_toolbar.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *Created by Pbihao
 * on 2020/10/4
 */

class FriendsFragment:BaseFragment<FriendsViewModel, FragmentFriendsBinding>(), View.OnClickListener{

    val mAdapter by lazy { BindingAdapter<User>(R.layout.rv_user_item) }

    override fun layoutId() : Int = R.layout.fragment_friends

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel

        tvTitle.text = "好友"

        srl.setColorSchemeResources(R.color.colorAccent)

        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))

        rv.adapter = mAdapter

        mAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener{adapter, view, position ->
            clickItem(mAdapter.getItem(position)!!)
        }

        mViewModel.retry()
    }

    override fun createObserver() {
        mViewModel.run {
            friendsLiveData.observe(viewLifecycleOwner, Observer {
                it?.let {
                    mAdapter.replaceData(it)
                }
            })
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: AcceptResp){
        if(event.success){//同意添加好友-刷新数据
            mViewModel.retry()
        }

    }

    fun clickItem(data: User){

    }

    private fun setEmpty(){
        if (mAdapter.emptyView == null){
            mAdapter.setEmptyView(R.layout.layout_em, rv)
        }
    }

    override fun onClick(p0: View?) {

    }
}
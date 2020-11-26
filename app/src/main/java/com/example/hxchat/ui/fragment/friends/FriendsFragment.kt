package com.example.hxchat.ui.fragment.friends

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.hxchat.ui.adapter.BindingAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.hxchat.R
import com.example.hxchat.app.base.BaseFragment
import com.example.hxchat.data.model.bean.User
import com.example.hxchat.data.packet.resp.AcceptResp
import com.example.hxchat.databinding.FragmentFriendsBinding
import com.example.hxchat.viewmodel.request.RequestFriendsViewModel
import com.example.hxchat.viewmodel.state.FriendsViewModel
import com.example.hxchat.viewmodel.state.UsersViewModel
import kotlinx.android.synthetic.main.fragment_friends.rv
import kotlinx.android.synthetic.main.fragment_friends.srl
import kotlinx.android.synthetic.main.home_toolbar.*
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.navigateAction
import me.hgj.jetpackmvvm.ext.parseState
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *Created by Pbihao
 * on 2020/10/4
 */

class FriendsFragment:BaseFragment<FriendsViewModel, FragmentFriendsBinding>(), View.OnClickListener{

    private val  requestFriendsViewModel: RequestFriendsViewModel by viewModels()
    //private val usersViewModel = UsersViewModel(application = Application())
    private val usersViewModel : UsersViewModel by activityViewModels<UsersViewModel>()

    val mAdapter by lazy { BindingAdapter<User>(R.layout.rv_user_item) }

    override fun layoutId() : Int = R.layout.fragment_friends

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = requestFriendsViewModel
        tvTitle.text = "好友"

        ivRight.setImageResource(R.drawable.btn_search_selector)
        ivRight.setOnClickListener(this)


        srl.setColorSchemeResources(R.color.colorAccent)

        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        rv.adapter = mAdapter

        mAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener{adapter, view, position ->
            clickItem(mAdapter.getItem(position)!!)
        }

        requestFriendsViewModel.getfriends()
    }

    override fun createObserver() {


        requestFriendsViewModel.friendsData.observe(viewLifecycleOwner, Observer { resultState ->
            parseState(resultState, {
                mAdapter.replaceData(it)
                if (it.size > 0) {
                    usersViewModel.saveUsers(it)
                }
                srl.isRefreshing = false
            }, {
                srl.isRefreshing = false
            })
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: AcceptResp){
        if(event.success){//同意添加好友-刷新数据
            requestFriendsViewModel.getfriends()
        }
    }

    fun clickItem(data: User){
        nav().navigateAction(R.id.action_mainfragment_to_friendUserInfoFragment, Bundle().apply {
            putParcelable("user", data)
            putBoolean("isAlreadyFriend", true)
        })
    }

    override fun onResume() {
        requestFriendsViewModel.getfriends()
        super.onResume()
    }


    private fun setEmpty(){
        if (mAdapter.emptyView == null){
            mAdapter.setEmptyView(R.layout.layout_em, rv)
        }
    }

    private fun clickSearch(){
        nav().navigateAction(R.id.action_mainfragment_to_searchFragment)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivRight -> clickSearch()
        }
    }
}
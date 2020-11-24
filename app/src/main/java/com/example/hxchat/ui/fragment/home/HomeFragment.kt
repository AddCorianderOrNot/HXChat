package com.example.hxchat.ui.fragment.home

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.hxchat.R
import com.example.hxchat.app.Constants
import com.example.hxchat.app.base.BaseFragment
import com.example.hxchat.app.ext.jumpByLogin
import com.example.hxchat.app.ext.nav
import com.example.hxchat.data.model.bean.Message
import com.example.hxchat.data.model.bean.Operator
import com.example.hxchat.databinding.FragmentHomeBinding
import com.example.hxchat.ui.activity.chat.ChatActivity
import com.example.hxchat.ui.adapter.MessageAdapter
import com.example.hxchat.viewmodel.state.HomeViewModel
import com.example.hxchat.viewmodel.state.MessageViewModel
import com.king.easychat.netty.packet.Packet
import com.king.easychat.netty.packet.PacketType
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.home_toolbar.*
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.ext.navigateAction
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 *Created by Pbihao
 * on 2020/10/4
 */

class HomeFragment: BaseFragment<HomeViewModel, FragmentHomeBinding>(), View.OnClickListener{

    override fun layoutId(): Int = R.layout.fragment_home

    //private val messageViewModel : MessageViewModel by lazy {  ViewModelProvider(this).get(MessageViewModel::class.java) }
    private val messageViewModel : MessageViewModel by activityViewModels<MessageViewModel>()

    val mAdapter by lazy {MessageAdapter(getUserEmail(), messageViewModel)}

    var isRefresh = true

    var onTotalCountCallback: OnTotalCountCallback? = null

    interface OnTotalCountCallback{
        fun onTotalCountChanged(count: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTotalCountCallback){
            onTotalCountCallback = context
        }
    }


    override fun initView(savedInstanceState: Bundle?) {

        ivRight.setImageResource(R.drawable.btn_search_selector)
        ivRight.setOnClickListener(this)

        tvTitle.setText(R.string.menu_message)
        srl.setColorSchemeResources(R.color.colorAccent)
        srl.setOnRefreshListener {
            messageViewModel.delay(getUserEmail(), 0)
        }
        isRefresh = true

        rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        rv.adapter = mAdapter

        mAdapter.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener{ adapter, view, position ->
            when(view.id){
                R.id.clContent -> clickItem(mAdapter.getItem(position)!!)
                R.id.llDelete -> clickDeleteItem(mAdapter.getItem(position)!!)
                R.id.llMarkRead -> clickMarkRead(mAdapter.getItem(position)!!)
            }
        }


        if(appViewModel.isLogin.value == null || appViewModel.isLogin.value == false){
            nav().jumpByLogin {
                nav().navigateAction(R.id.action_mainfragment_to_meUserInfoFragment2)
            }
        }
    }

    override fun createObserver() {
        messageViewModel.run {
            lastMessageLiveData.observe(viewLifecycleOwner, Observer{
                srl.isRefreshing = false
                mAdapter.curTime = System.currentTimeMillis()
                mAdapter.replaceData(it)
            })
            totalCountLiveData.observe(viewLifecycleOwner, Observer {
                srl.isRefreshing = false
                onTotalCountCallback?.onTotalCountChanged(it)
            })
        }

    }

    private fun setEmpty(){
        if(mAdapter.emptyView == null){
            mAdapter.setEmptyView(R.layout.layout_em, rv)
        }
    }

    private fun requestData(){
        if (isRefresh){
            messageViewModel.delay(getUserEmail(), 200)
        }
    }

    override fun onResume() {
        isRefresh = true
        super.onResume()
        requestData()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if(!hidden){
            requestData()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: Packet){
        when(event.packetType()){
            PacketType.SEND_MESSAGE_RESP -> requestData()
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: Operator){
        when(event.event){
            Constants.EVENT_REFRESH_MESSAGE_COUNT -> requestData()
        }
    }

    fun startChatActivity(data: Message){
        val intent = newIntent(data.getShowName()!!, ChatActivity::class.java)
        intent.putExtra(Constants.KEY_ID, data.id)
        intent.putExtra(Constants.KEY_IMAGE_URL, data.avatar)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()

    }

    override fun onDestroy() {
        isRefresh = false
        super.onDestroy()
    }

    fun clickMarkRead(data: Message){
        Log.d("read", "点击了标记消息已读")
        Log.d("read", data.toString())
        messageViewModel.updateMessageRead(getUserEmail(),data.id!!)
    }

    fun clickItem(data: Message){
        startChatActivity(data)
    }

    fun clickDeleteItem(data: Message){
        messageViewModel.deleteLatestChat(getUserEmail(), data)
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
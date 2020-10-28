package com.example.hxchat.ui.activity

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.hxchat.R
import com.example.hxchat.app.Constants
import com.example.hxchat.app.base.BaseActivity
import com.example.hxchat.app.ext.nav
import com.example.hxchat.app.util.CacheUtil
import com.example.hxchat.data.model.bean.Operator
import com.example.hxchat.data.packet.resp.LogoutResp
import com.example.hxchat.data.packet.resp.MessageResp
import com.example.hxchat.databinding.ActivityMainBinding
import com.example.hxchat.service.JWebSocketClient
import com.example.hxchat.service.JWebSocketClientService
import com.example.hxchat.viewmodel.request.RequestUserInfoViewModel
import com.example.hxchat.viewmodel.state.MainViewModel
import com.example.hxchat.viewmodel.state.MessageViewModel
import com.example.hxchat.viewmodel.state.UsersViewModel
import com.king.easychat.netty.packet.Packet
import com.king.easychat.netty.packet.PacketType
import com.yalantis.ucrop.UCrop
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.fragment_chat.*
import me.hgj.jetpackmvvm.ext.parseState
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import timber.log.Timber
import java.io.File

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {
    override fun layoutId(): Int = R.layout.activity_main

    var avatarFile: File? = null

    private val messageViewModel : MessageViewModel by lazy {  MessageViewModel(application) }
    private val requestUserInfoViewModel:RequestUserInfoViewModel by viewModels()
    private val usersViewModel: UsersViewModel by lazy { ViewModelProvider(this).get(UsersViewModel::class.java) }

    private var mContext: Context? = null
    private lateinit var client: JWebSocketClient
    private lateinit var binder: JWebSocketClientService.JWebSocketClientBinder
    private lateinit var jWebSClientService: JWebSocketClientService

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            Log.e("MainActivity", "服务与活动成功绑定")
            binder = iBinder as JWebSocketClientService.JWebSocketClientBinder
            jWebSClientService = binder.getService()
            client = jWebSClientService.client
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            Log.e("MainActivity", "服务与活动成功断开")
        }
    }


    /**
     * 绑定服务
     */
    private fun bindService() {
        val bindIntent = Intent(mContext, JWebSocketClientService::class.java)
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE)
    }



    /**
     * 启动服务（websocket客户端服务）
     */
    private fun startJWebSClientService() {
        val intent = Intent(mContext, JWebSocketClientService::class.java)
        startService(intent)
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun createObserver() {

        requestUserInfoViewModel.userInfo.observe(this, Observer {resultState ->
            parseState(resultState,{
                appViewModel.changeUserInfo(it)
            })
        })

        appViewModel.userInfo.observe(this, Observer {
            appViewModel.isLogin.value?.let{
                Log.d("it", "lala")
                if (it){
                    mContext = this@MainActivity
                    Log.d("it:", it.toString())
                    //启动服务
                    startJWebSClientService()
                    //绑定服务
                    bindService()
                }

            }
        })
        eventViewModel.quitLogin.observe(this, Observer {

            Log.d("unbindService", "123")
            val intent = Intent(mContext, JWebSocketClientService::class.java)
            unbindService(serviceConnection)
            stopService(intent)
        })
    }



    private fun handleMessageRead(){
        appViewModel.friendEmail.value?.let {
            messageViewModel.updateMessageRead(getUserEmail(),it)
        }
        appViewModel.friendEmail.postValue(null)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMessageEvent(event: Operator){
        when (event.event) {
            Constants.EVENT_UPDATE_MESSAGE_READ -> handleMessageRead()
        }
    }
    /*
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: Packet){
        when(event.packetType()){
            PacketType.SEND_MESSAGE_RESP -> handleMessageResp(event as MessageResp)
        }
    }
     */

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageResp){
        handleMessageResp(event)
    }

    private fun handleMessageResp(data: MessageResp){
        data?.let {
            Log.d("event", data.toString())
            data.isSelf()
            val read = data.sender == appViewModel.friendEmail.value
            if (!data.isSender){
                messageViewModel.saveMessage(getUserEmail(), data.sender!!, null,null, read, data)
            }else{
                messageViewModel.saveMessage(getUserEmail(), data.receiver!!, null,null, read, data)
            }

        }
    }

    /*****************************************处理用户选择了一张图像作为自己的头像*******************************************************/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            when(requestCode){
                Constants.REQ_SELECT_PHOTO -> {
                    Log.d("photo", "选择了一张图片")
                    cropRawPhoto(obtainSelectPhoto(data))
                }
                Constants.REQ_CROP_PHOTO -> {
                    Log.d("photo","裁剪图片结束")
                    avatarFile?.let { requestUserInfoViewModel.changeAvatar(it) }
                }
            }
        }
    }

    private fun obtainSelectPhoto(data: Intent?): Uri?{
        val result = Matisse.obtainPathResult(data)
        return Uri.fromFile(File(result[0]))
    }

    /**
     * 使用UCrop进行图片剪裁
     *
     * @param uri
     */
    private fun cropRawPhoto(uri: Uri?) {
        uri?.let {
            val options = UCrop.Options()
            //        options.setToolbarTitle("裁剪");
            // 修改标题栏颜色
            options.setToolbarColor(ContextCompat.getColor(this,R.color.colorPrimary))
            options.setToolbarWidgetColor(ContextCompat.getColor(this,R.color.white))
            // 修改状态栏颜色
            options.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
            // 隐藏底部工具
            options.setHideBottomControls(true)
            // 图片格式
            options.setCompressionFormat(Bitmap.CompressFormat.JPEG)
            // 设置图片压缩质量
            options.setCompressionQuality(100)
            // 是否让用户调整范围(默认false)，如果开启，可能会造成剪切的图片的长宽比不是设定的
            // 如果不开启，用户不能拖动选框，只能缩放图片
            options.setFreeStyleCropEnabled(false)
            options.setCircleDimmedLayer(true)
            avatarFile = File(getApp().getPath(), System.currentTimeMillis().toString() + ".jpg")

            // 设置源uri及目标uri
            UCrop.of(uri,Uri.fromFile(avatarFile))
                // 长宽比
                .withAspectRatio(1f, 1f)
                // 图片大小
                .withMaxResultSize(300, 300)
                // 配置参数
                .withOptions(options)
                .start(this,Constants.REQ_CROP_PHOTO)
        }
    }


}
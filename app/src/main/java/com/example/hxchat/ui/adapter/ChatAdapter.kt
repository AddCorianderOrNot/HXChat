package com.example.hxchat.ui.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.example.hxchat.R
import com.example.hxchat.data.packet.resp.MessageResp
import com.king.easychat.glide.ImageLoader
import com.example.hxchat.BR

/**
 *Created by Pbihao
 * on 2020/10/9
 */
class ChatAdapter(var friendImageUrl: String?, var myImageUrl : String?): BaseMultiItemQuickAdapter<MessageResp, BindingHolder>(null){
    var curTime = System.currentTimeMillis()

    init {
        addItemType(MessageResp.Left, R.layout.rv_chat_item)
        addItemType(MessageResp.Right, R.layout.rv_chat_right_item)
    }

    override fun convert(helper: BindingHolder, item: MessageResp?) {
        helper.mBinding?.let {
            when(item?.itemType){
                MessageResp.Left -> ImageLoader.displayImage(mContext,helper.getView(R.id.ivAvatar),friendImageUrl,R.mipmap.ic_me_user_image)
                MessageResp.Right -> ImageLoader.displayImage(mContext,helper.getView(R.id.ivAvatar),myImageUrl,R.mipmap.ic_me_user_image)
            }
            helper.addOnClickListener(R.id.ivContent)
            helper.addOnClickListener(R.id.ivAvatar)
            it.setVariable(BR.curTime,curTime)
            it.setVariable(BR.data,item)
            it.executePendingBindings()
        }
    }

}
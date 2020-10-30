package com.example.hxchat.ui.adapter

import com.example.hxchat.data.model.bean.Message
import com.example.hxchat.R
import com.example.hxchat.ui.view.DragBubbleView
import com.example.hxchat.viewmodel.state.HomeViewModel
import com.example.hxchat.BR
import com.example.hxchat.viewmodel.state.MessageViewModel


/**
 *Created by Pbihao
 * on 2020/10/9
 */
class MessageAdapter(val email: String, val viewModel: MessageViewModel): BindingAdapter<Message>(R.layout.rv_message_item) {
    var curTime = System.currentTimeMillis()

    override fun convert(helper: BindingHolder, item: Message) {
        helper.getView<DragBubbleView>(R.id.dbvCount).setOnBubbleStateListener(object : DragBubbleView.OnBubbleStateListener{
            override fun onDrag() {

            }

            override fun onMove() {

            }

            override fun onRestore() {

            }

            override fun onDismiss() {
                viewModel.updateMessageRead(email,item.id!!)
            }
        })

        helper.addOnClickListener(R.id.clContent)
        helper.addOnClickListener(R.id.llDelete)
        helper.addOnClickListener(R.id.llMarkRead)

        helper.mBinding?.let {
            it.setVariable(BR.curTime, curTime)
            it.setVariable(BR.data, item)
            it.executePendingBindings()
        }
    }
}
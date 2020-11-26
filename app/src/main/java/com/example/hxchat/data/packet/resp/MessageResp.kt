package com.example.hxchat.data.packet.resp

import android.os.Parcelable
import android.util.Log
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.hxchat.app.util.CacheUtil
import com.example.hxchat.data.model.bean.MessageDbo
import com.example.hxchat.data.model.bean.RecentChat
import com.king.easychat.netty.packet.MessageType
import com.king.easychat.netty.packet.Packet
import com.king.easychat.netty.packet.PacketType
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

/**
 * @author Zed
 * date: 2019/08/19.
 * description:
 */
@Parcelize


class MessageResp(val sender : String?,val receiver:String?, val senderName : String?, val message : String, var isSender: Boolean = false, val messageType : Int, var haveRead:Boolean = false) : Packet(), MultiItemEntity,
    Parcelable {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0



    companion object{
        const val Left = 1
        const val Right = 2
    }

    override fun getItemType(): Int {
        return if(isSender) Right else Left
    }

//    @Expose val msg = AES.decrypt(message,dateTime + "ab").toString()

    override fun packetType(): Int {
        return PacketType.SEND_MESSAGE_RESP
    }

    fun getMsg(): String?{
        return message
    }

    override fun toString(): String {
        return "MessageResp(sender='$sender', senderName='$senderName', message='$message', receiver='$receiver', isSender='$isSender',msg='${getMsg()}') ${super.toString()}"
    }

    fun isSelf() {
        if(sender == CacheUtil.getUser()?.email) {
            isSender = true
        }
        Log.d("isSelf", isSender.toString())
    }


    fun toMessageDbo(userId: String,friendId: String?,read: Boolean): MessageDbo{
        var receiver = if(isSender) friendId else sender
        var data = MessageDbo(userId,sender,receiver,message,isSender,messageType,dateTime,senderName,read)
        data.dateTime = dateTime
        return data
    }


    fun isImage():Boolean{
        return messageType == MessageType.IMAGE
    }

}

package com.example.hxchat.data.packet.resp

import com.king.easychat.netty.packet.Packet
import com.king.easychat.netty.packet.PacketType

/**
 * @author Zed
 * date: 2019/08/19.
 * description:
 */
class LogoutResp : Packet() {

    override fun packetType(): Int {
        return PacketType.LOGOUT_RESP
    }



}

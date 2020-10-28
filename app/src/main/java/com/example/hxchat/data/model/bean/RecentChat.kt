package com.example.hxchat.data.model.bean

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 *Created by Pbihao
 * on 2020/10/8
 */
@Entity(indices = [Index(value = ["chatId"], unique = true)])
class RecentChat(val email: String, val chatId: String,var showName: String?,var avatar: String?,var dateTime: String){

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    override fun toString(): String {
        return "RecentChat(email='$email', chatId='$chatId', showName=$showName, avatar=$avatar, dateTime='$dateTime', id=$id)"
    }
}
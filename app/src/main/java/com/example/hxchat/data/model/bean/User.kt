package com.example.hxchat.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 *Created by Pbihao
 * on 2020/10/8
 */

@Entity(indices = [Index(value = ["email"], unique = true)])
@Parcelize
@SuppressLint("ParcelCreator")
class User(
    val email: String,
    var nickname: String,
    var icon: String?,
    var signature: String?,
    var remark: String? = null
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    fun getShowName(): String {
        remark?.let {
            return it
        }
        return nickname
    }

    override fun toString(): String {
        return "User(email=$email, nickname=$nickname, icon=$icon, signature=$signature)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (email != other.email) return false

        return true
    }

    override fun hashCode(): Int {
        return email.hashCode()
    }


}
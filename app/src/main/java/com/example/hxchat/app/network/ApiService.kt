package com.example.hxchat.app.network

import com.example.hxchat.data.model.bean.ApiResponse
import com.example.hxchat.data.model.bean.User
import com.example.hxchat.data.model.bean.UserInfo
import com.example.hxchat.data.packet.resp.MessageResp
import me.hgj.jetpackmvvm.state.ResultState
import okhttp3.RequestBody
import retrofit2.http.*
import java.io.File

/**
 *Created by Pbihao
 * on 2020/10/6
 */

interface ApiService {
    companion object {
        const val SERVER_URL = "http://www.brotherye.site:8080"
    }

    /**
     * 登录
     */
    @POST("session")
    suspend fun login(
        @Body body: RequestBody
    ): ApiResponse<UserInfo>

    /**
     * 自动登录
     */
    @GET("session")
    suspend fun login(
    ): ApiResponse<UserInfo>

    /**
     * 注册
     */
    @POST("user")
    suspend fun register(
        @Body body: RequestBody
    ): ApiResponse<Any>

    /**
     * 更新用户
     */
    @FormUrlEncoded
    @PATCH("user")
    suspend fun update(
        @Field("key") key: String,
        @Field("value") value: String
    ): ApiResponse<UserInfo>

    /**
     * 更新用户头像
     */
    @Multipart
    @PATCH("user")
    suspend fun update(
        @Field("key") key: String,
        @Field("value") value: File
    ): ApiResponse<UserInfo>

    /**
     * 搜索用户
     */
    @GET("user")
    suspend fun search(
        @Query("nickname") nickname: String
    ): ApiResponse<ArrayList<User>>

    /**
     * 添加好友
     */
    @POST("friend")
    suspend fun addFriend(
        @Body body: RequestBody
    ): ApiResponse<User>

    /**
     * 获得好友列表
     */
    @GET("friend")
    suspend fun getFriends(): ApiResponse<ArrayList<User>>

    /**
     * 发送消息
     */
    @POST("message")
    suspend fun sendMessage(
        @Body body: RequestBody
    ): ApiResponse<MessageResp>

    /**
     * 接收消息
     */
    @GET("message")
    suspend fun receiveMessage(): ApiResponse<ArrayList<MessageResp>>
}
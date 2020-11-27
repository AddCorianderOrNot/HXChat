package com.example.hxchat.app.network

import com.example.hxchat.data.model.bean.ApiResponse
import com.example.hxchat.data.model.bean.User
import com.example.hxchat.data.model.bean.UserInfo
import com.example.hxchat.data.packet.resp.MessageResp
import net.alhazmy13.wordcloud.WordCloud
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


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
        @Part("key") key : RequestBody,
        @Part part : MultipartBody.Part
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

    /**
     * 拉取词云
     */
    @GET("message/wordcloud")
    suspend fun getWordcloud(): ApiResponse<ArrayList<String>>

    /**
     * 更新阅读时间
     */
    @POST("friend/time")
    suspend fun updateReadTime(
        @Body body: RequestBody
    ): ApiResponse<Any>

    /**
     * 获取阅读时间
     */
    @GET("friend/time")
    suspend fun getReadTime(
        @Query("friend_id") friend: String?
    ): ApiResponse<Long>

}
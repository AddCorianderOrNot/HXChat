package com.example.hxchat.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.example.hxchat.R
import com.example.hxchat.app.ext.nav
import com.example.hxchat.app.ext.showMessage
import com.example.hxchat.app.network.apiService
import com.example.hxchat.app.util.CacheUtil
import com.example.hxchat.data.model.bean.User
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.navigateAction
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState
import net.alhazmy13.wordcloud.WordCloud
import kotlin.random.Random


/**
 *Created by Pbihao
 * on 2020/11/24
 */

class RequestWordCloudViewModel: BaseViewModel(){
    var wordCloud : MutableLiveData<ResultState<ArrayList<WordCloud>>> = MutableLiveData()
    var word : MutableLiveData<ResultState<ArrayList<String>>> = MutableLiveData()

    //发送请求词云的请求
    fun get_wordCloud(){
        request(
            { apiService.getWordcloud() },
            word
        )

        val list:ArrayList<WordCloud> = ArrayList()
        val random = Random(123)

        list.add(WordCloud("1", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("fg", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("sadwe", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("sadwe", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("sadwe", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("sadwe", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("sadwe", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("sadwe", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("sadwe", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("sadwe", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("sadwe", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("sadwe", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("sadwe", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("sadwe", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("sadwe", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("sadwe", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("sadwe", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("sadwe", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("sadwe", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("sadwe", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大
        list.add(WordCloud("sadwe", random.nextInt(50)))//第一个字符串是显示的词云的信息，第二个weight是相对大小 数值越大 现实的字体越大

        wordCloud.postValue(ResultState.Success<ArrayList<WordCloud>>(list))
    }
}
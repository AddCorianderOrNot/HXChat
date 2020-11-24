package com.example.hxchat.viewmodel.state

import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.databind.StringObservableField


/**
 *Created by Pbihao
 * on 2020/10/29
 */

class ChangePasswordViewModel: BaseViewModel(){
    var usedPassword = StringObservableField()

    var password = StringObservableField()

    var password2 = StringObservableField()
}
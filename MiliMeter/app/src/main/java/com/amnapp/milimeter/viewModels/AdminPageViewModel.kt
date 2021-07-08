package com.amnapp.milimeter.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amnapp.milimeter.AccountManager
import com.amnapp.milimeter.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AdminPageViewModel: ViewModel() {
    var parentName: MutableLiveData<String> = MutableLiveData<String>()
    var pathList: MutableLiveData<MutableList<UserData>> = MutableLiveData<MutableList<UserData>>()// 유저데이터의 리스트로 현재 경로를 나타냄
    var subUserList: MutableLiveData<MutableList<UserData>> = MutableLiveData<MutableList<UserData>>()// 현재 부모의 하위유저의 리스트
    val myUd = UserData.getInstance()

    init {
        parentName.value = myUd.userName
        pathList.value = mutableListOf(myUd)
        CoroutineScope(Dispatchers.IO).launch{
            val am = AccountManager()
            Log.d("asd","vm")
            subUserList.postValue(myUd.indexHashCode?.let { am.findChildAccount(it) })
        }
    }


}
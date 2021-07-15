package com.amnapp.milimeter.viewModels

import android.app.ProgressDialog
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
    var parentName: MutableLiveData<String> = MutableLiveData<String>()//리스트의 부모가 누구인지를 표시하는 부분에 보이는 이름
    var pathList: MutableLiveData<MutableList<UserData>> = MutableLiveData<MutableList<UserData>>()// 유저데이터의 리스트로 현재 경로를 나타냄
    var subUserList: MutableLiveData<MutableList<UserData>> = MutableLiveData<MutableList<UserData>>()// 현재 부모의 하위유저의 리스트
    val currUd = UserData.getInstance()

    init {
        parentName.value = currUd.userName
        pathList.value = mutableListOf(currUd)
        CoroutineScope(Dispatchers.IO).launch{
            val am = AccountManager()
            subUserList.postValue(currUd.indexHashCode?.let { am.findChildAccount(it) })
        }
    }

    fun downDirectory(newParent: UserData){
        parentName.value = newParent.userName
        val newPath = pathList.value
        newPath?.add(newParent)
        pathList.value = newPath // pathList.value.add()를 해도 참조주소가 바뀌는 게 아니기 때문에 이런 식으로 해줘야 변화가 관찰됨
        CoroutineScope(Dispatchers.IO).launch{
            val am = AccountManager()
            subUserList.postValue(newParent.indexHashCode?.let { am.findChildAccount(it) })
        }
    }

    fun upDirectory(){
        val preParent = pathList.value?.get(pathList.value!!.size - 2)
        parentName.value = preParent?.userName
        val newPath = pathList.value
        newPath?.removeAt(pathList.value!!.size - 1)
        pathList.value = newPath // pathList.value.add()를 해도 참조주소가 바뀌는 게 아니기 때문에 이런 식으로 해줘야 변화가 관찰됨
        CoroutineScope(Dispatchers.IO).launch{
            val am = AccountManager()
            subUserList.postValue(preParent?.indexHashCode?.let { am.findChildAccount(it) })
        }
    }
}
package com.konkuk17.messenger_example.Main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IdViewModel : ViewModel(){
    val selectednum = MutableLiveData<Int>()

    fun setLiveData(num: Int){
        selectednum.value = num
    }


}
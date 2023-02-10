package com.dianavintila.day1ofandroid

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel(){
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()
}
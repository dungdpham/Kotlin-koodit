package com.example.androidintro

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GreetingViewModel : ViewModel() {
    init {
        Log.d("XXX", "GreetingViewModel()")
    }

    private val _n = MutableStateFlow(0)
    val n = _n.asStateFlow()

    fun inc() {
        _n.value += 1
        Log.d("XXX", "GreetingViewModel $n")
    }

    fun bigInc() {
        _n.value += 10
        Log.d("XXX", "GreetingViewModel $n")
    }
}

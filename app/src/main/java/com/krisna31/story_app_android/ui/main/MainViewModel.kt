package com.krisna31.story_app_android.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.krisna31.story_app_android.data.user.UserModel
import com.krisna31.story_app_android.data.user.UserPreference
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun getApiToken(): LiveData<String> {
        return pref.getApiToken().asLiveData()
    }

}
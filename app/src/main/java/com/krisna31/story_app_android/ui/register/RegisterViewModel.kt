package com.krisna31.story_app_android.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.krisna31.story_app_android.data.api.config.ApiConfig
import com.krisna31.story_app_android.data.api.response.RegisterResponse
import com.krisna31.story_app_android.data.user.UserModel
import com.krisna31.story_app_android.data.user.UserPreference
import kotlinx.coroutines.launch

class RegisterViewModel(private val pref: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: MutableLiveData<String?> = _errorMessage

    fun register(name: String, email: String, password: String) {
        _isLoading.value = true
        val registerRequest =
            ApiConfig.getApiService().register(name, email, password)
        registerRequest.enqueue(object : retrofit2.Callback<RegisterResponse> {
            override fun onResponse(
                call: retrofit2.Call<RegisterResponse>,
                response: retrofit2.Response<RegisterResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _errorMessage.value = null
                    val body = response.body()
                    if (body != null) {
                        viewModelScope.launch {
                            pref.saveUser(UserModel(name, ""))
                        }
                    }
                } else {
                    _errorMessage.value = response.message()
                }
            }

            override fun onFailure(call: retrofit2.Call<RegisterResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
            }
        })
    }
}
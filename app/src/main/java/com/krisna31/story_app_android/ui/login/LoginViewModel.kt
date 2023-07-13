package com.krisna31.story_app_android.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.krisna31.story_app_android.data.api.config.ApiConfig
import com.krisna31.story_app_android.data.api.response.LoginResponse
import com.krisna31.story_app_android.data.api.response.RegisterResponse
import com.krisna31.story_app_android.data.user.UserModel
import com.krisna31.story_app_android.data.user.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call

class LoginViewModel(private val pref: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        val registerRequest =
            ApiConfig.getApiService().login(email, password)
        registerRequest.enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(
                call: retrofit2.Call<LoginResponse>,
                response: retrofit2.Response<LoginResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _errorMessage.value = null
                    val body = response.body()
                    if (body != null) {
                        viewModelScope.launch {
                            pref.login(body.loginResult.token)
                        }
                    }
                } else {
                    _errorMessage.value = response.message()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
            }
        })
    }
}
package com.krisna31.story_app_android.ui.detail_story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.krisna31.story_app_android.data.api.config.ApiConfig
import com.krisna31.story_app_android.data.api.response.DetailStoryResponse
import com.krisna31.story_app_android.data.api.response.Story
import com.krisna31.story_app_android.data.user.UserModel
import com.krisna31.story_app_android.data.user.UserPreference

class DetailStoryViewModel(private val pref: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _story = MutableLiveData<Story>()
    val story: LiveData<Story> = _story

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getStory(apiToken: String, id: String) {
        _isLoading.value = true
        val storyRequest = ApiConfig.getApiService().getStory("Bearer $apiToken", id)
        storyRequest.enqueue(object : retrofit2.Callback<DetailStoryResponse> {
            override fun onResponse(
                call: retrofit2.Call<DetailStoryResponse>,
                response: retrofit2.Response<DetailStoryResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _errorMessage.value = null
                    val body = response.body()
                    if (body != null) {
                        _story.value = body.story
                    }
                } else {
                    _errorMessage.value = response.message()
                }
            }

            override fun onFailure(call: retrofit2.Call<DetailStoryResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = t.message
            }
        })
    }

}
package com.krisna31.story_app_android.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.krisna31.story_app_android.data.api.response.ListStoryItem
import com.krisna31.story_app_android.data.paging_source.StoryRepository
import com.krisna31.story_app_android.data.user.UserModel
import com.krisna31.story_app_android.data.user.UserPreference
import kotlinx.coroutines.launch

class MainViewModel(
    private val pref: UserPreference,
    private val storyRepository: StoryRepository
) :
    ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _story = MutableLiveData<List<ListStoryItem>>()
    val story: LiveData<List<ListStoryItem>> = _story

//    val stories: LiveData<PagingData<ListStoryItem>> =
//        storyRepository.getStory().cachedIn(viewModelScope)

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }

    fun getStories(apiToken: String): LiveData<PagingData<ListStoryItem>> {
        return storyRepository.getStory(apiToken).cachedIn(viewModelScope)
//        val storyRequest = ApiConfig.getApiService().getStories("Bearer $apiToken")
//        storyRequest.enqueue(object : retrofit2.Callback<StoryResponse> {
//            override fun onResponse(
//                call: retrofit2.Call<StoryResponse>,
//                response: retrofit2.Response<StoryResponse>
//            ) {
//                _isLoading.value = false
//                if (response.isSuccessful) {
//                    _errorMessage.value = null
//                    val body = response.body()
//                    if (body != null) {
//                        _story.value = body.listStory
//                    }
//                } else {
//                    _errorMessage.value = response.message()
//                }
//            }
//
//            override fun onFailure(call: retrofit2.Call<StoryResponse>, t: Throwable) {
//                _isLoading.value = false
//                _errorMessage.value = t.message
//            }
//        })
    }

}
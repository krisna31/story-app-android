package com.krisna31.story_app_android.data.paging_source

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.krisna31.story_app_android.data.api.config.ApiService
import com.krisna31.story_app_android.data.api.response.ListStoryItem
import com.krisna31.story_app_android.data.db.StoryDatabase

class StoryRepository(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService
) {
    fun getStory(apiToken: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, apiToken)
            }
        ).liveData
    }
}
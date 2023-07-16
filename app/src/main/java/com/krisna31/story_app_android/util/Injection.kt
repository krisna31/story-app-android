package com.krisna31.story_app_android.util

import android.content.Context
import com.krisna31.story_app_android.data.api.config.ApiConfig
import com.krisna31.story_app_android.data.db.StoryDatabase
import com.krisna31.story_app_android.data.paging_source.StoryRepository

object Injection {
    fun provideRepository(context: Context?): StoryRepository {
        val database = StoryDatabase.getDatabase(context!!)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService)
    }
}
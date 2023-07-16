package com.krisna31.story_app_android.data.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.krisna31.story_app_android.data.api.config.ApiService
import com.krisna31.story_app_android.data.api.response.ListStoryItem
import com.krisna31.story_app_android.data.api.response.StoryResponse

class StoryPagingSource(private val apiService: ApiService, private val apiToken: String) :
    PagingSource<Int, ListStoryItem>() {


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData: StoryResponse =
                apiService.getStories(page, params.loadSize, "Bearer $apiToken")

            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.listStory.isEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}
package com.krisna31.story_app_android.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.krisna31.story_app_android.data.api.response.ListStoryItem
import com.krisna31.story_app_android.data.paging_source.StoryRepository
import com.krisna31.story_app_android.data.user.UserPreference
import com.krisna31.story_app_android.utils.DataDummy
import com.krisna31.story_app_android.utils.MainDispatcherRule
import com.krisna31.story_app_android.utils.MyListUpdateCallback
import com.krisna31.story_app_android.utils.MyPagingSource
import com.krisna31.story_app_android.utils.getOrAwaitValue
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var preference: UserPreference
    private lateinit var repository: StoryRepository

    private val testDispatcher = TestCoroutineDispatcher()

    private val apiToken = "Fake Token"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        preference = mock(UserPreference::class.java)
        repository = mock(StoryRepository::class.java)
        mainViewModel = MainViewModel(preference, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when GetStories returns data, ensure data is not null and has correct count`() =
        runTest {
            val expectedStory = DataDummy.generateDummyNewsEntity()
            val data = MyPagingSource.snapshot(expectedStory)
            val stories = MutableLiveData<PagingData<ListStoryItem>>()
            stories.value = data
            `when`(repository.getStory(apiToken)).thenReturn(stories)
            val actualData = mainViewModel.getStories(apiToken).getOrAwaitValue()

            val asyncPagingDataDiffer = AsyncPagingDataDiffer(
                diffCallback = MainAdapter.DIFF_CALLBACK,
                updateCallback = MyListUpdateCallback(),
                mainDispatcher = Dispatchers.Unconfined,
                workerDispatcher = Dispatchers.Unconfined,
            )
            asyncPagingDataDiffer.submitData(actualData)
            val storiesData = asyncPagingDataDiffer.snapshot().items

            val expectedFirstData = expectedStory[0]
            val actualFirstData = storiesData[0]

            verify(repository, times(1)).getStory(apiToken)
            verify(preference, never()).getUser()
            Assert.assertNotNull(storiesData)
            assertEquals(expectedStory.size, storiesData.size)
            assertEquals(expectedFirstData, actualFirstData)
        }

    @Test
    fun `when GetStories returns no data, ensure data count is zero`() =
        runTest {
            val expectedStory = emptyList<ListStoryItem>()
            val data = MyPagingSource.snapshot(expectedStory)
            val stories = MutableLiveData<PagingData<ListStoryItem>>()
            stories.value = data
            `when`(repository.getStory(apiToken)).thenReturn(stories)
            val actualData = mainViewModel.getStories(apiToken).getOrAwaitValue()

            val asyncPagingDataDiffer = AsyncPagingDataDiffer(
                diffCallback = MainAdapter.DIFF_CALLBACK,
                updateCallback = MyListUpdateCallback(),
                mainDispatcher = Dispatchers.Unconfined,
                workerDispatcher = Dispatchers.Unconfined,
            )
            asyncPagingDataDiffer.submitData(actualData)
            val storiesData = asyncPagingDataDiffer.snapshot().items

            verify(repository).getStory(apiToken)
            verify(preference, never()).getUser()
            Assert.assertNotNull(storiesData)
            Assert.assertEquals(0, storiesData.size)
        }
}
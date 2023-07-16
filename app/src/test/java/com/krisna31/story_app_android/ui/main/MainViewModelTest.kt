package com.krisna31.story_app_android.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.krisna31.story_app_android.data.api.response.ListStoryItem
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
import org.mockito.Mockito
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var mainViewModel: MainViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    private val apiToken = "Fake Token"

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mainViewModel = Mockito.mock(MainViewModel::class.java)

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
            `when`(mainViewModel.getStories(apiToken)).thenReturn(stories)
            val actualData = mainViewModel.getStories(apiToken).getOrAwaitValue()

            val asyncPagingDataDiffer = AsyncPagingDataDiffer(
                diffCallback = MainAdapter.DIFF_CALLBACK,
                updateCallback = MyListUpdateCallback(),
                mainDispatcher = Dispatchers.Unconfined,
                workerDispatcher = Dispatchers.Unconfined,
            )
            asyncPagingDataDiffer.submitData(actualData)
            val storiesData = asyncPagingDataDiffer.snapshot().items

            val expectedFirstDataId = expectedStory[0].id
            val actualFirstDataId = storiesData[0].id

            Mockito.verify(mainViewModel).getStories(apiToken)
            Assert.assertNotNull(storiesData)
            assertEquals(expectedStory.size, storiesData.size)
            assertEquals(expectedFirstDataId, actualFirstDataId)
        }

    @Test
    fun `when GetStories returns no data, ensure data count is zero`() =
        runTest {
            val expectedStory = emptyList<ListStoryItem>()
            val data = MyPagingSource.snapshot(expectedStory)
            val stories = MutableLiveData<PagingData<ListStoryItem>>()
            stories.value = data
            `when`(mainViewModel.getStories(apiToken)).thenReturn(stories)
            val actualData = mainViewModel.getStories(apiToken).getOrAwaitValue()

            val asyncPagingDataDiffer = AsyncPagingDataDiffer(
                diffCallback = MainAdapter.DIFF_CALLBACK,
                updateCallback = MyListUpdateCallback(),
                mainDispatcher = Dispatchers.Unconfined,
                workerDispatcher = Dispatchers.Unconfined,
            )
            asyncPagingDataDiffer.submitData(actualData)
            val storiesData = asyncPagingDataDiffer.snapshot().items

            Mockito.verify(mainViewModel).getStories(apiToken)
            Assert.assertNotNull(storiesData)
            Assert.assertEquals(0, storiesData.size)
        }
}
package com.fadhlansulistiyo.codingstoryapp.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.fadhlansulistiyo.codingstoryapp.DataDummy
import com.fadhlansulistiyo.codingstoryapp.MainDispatcherRule
import com.fadhlansulistiyo.codingstoryapp.data.UserRepository
import com.fadhlansulistiyo.codingstoryapp.data.model.ListStoriesItem
import com.fadhlansulistiyo.codingstoryapp.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var repository: UserRepository

    @Test
    fun `when Get Stories Should Not Null and Return Data`() = runTest {
        val dummyStories = DataDummy.generateDummyStoriesResponse()
        val data: PagingData<ListStoriesItem> = StoriesPagingSource.snapshot(dummyStories)
        val expectedStories = MutableLiveData<PagingData<ListStoriesItem>>()
        expectedStories.value = data
        Mockito.`when`(repository.getStories()).thenReturn(expectedStories)

        val viewModel = HomeViewModel(repository)
        val actualStories: PagingData<ListStoriesItem> = viewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStories)
        assertNotNull(differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
        assertEquals(dummyStories[0], differ.snapshot()[0])
    }

    @Test
    fun `when Get Stories Empty Should Return No Data`() = runTest {
        val data: PagingData<ListStoriesItem> = PagingData.from(emptyList())
        val expectedStories = MutableLiveData<PagingData<ListStoriesItem>>()
        expectedStories.value = data
        Mockito.`when`(repository.getStories()).thenReturn(expectedStories)

        val viewModel = HomeViewModel(repository)
        val actualStories: PagingData<ListStoriesItem> = viewModel.stories.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = ListStoryAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStories)

        assertEquals(0, differ.snapshot().size)
    }

    class StoriesPagingSource : PagingSource<Int, LiveData<List<ListStoriesItem>>>() {
        companion object {
            fun snapshot(items: List<ListStoriesItem>): PagingData<ListStoriesItem> {
                return PagingData.from(items)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoriesItem>>>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoriesItem>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}
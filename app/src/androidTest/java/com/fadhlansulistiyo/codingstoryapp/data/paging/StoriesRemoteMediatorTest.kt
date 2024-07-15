package com.fadhlansulistiyo.codingstoryapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fadhlansulistiyo.codingstoryapp.data.db.StoriesDatabase
import com.fadhlansulistiyo.codingstoryapp.data.model.ListStoriesItem
import com.fadhlansulistiyo.codingstoryapp.data.response.AddStoryResponse
import com.fadhlansulistiyo.codingstoryapp.data.response.DetailStoriesResponse
import com.fadhlansulistiyo.codingstoryapp.data.response.ListStoryItem
import com.fadhlansulistiyo.codingstoryapp.data.response.LoginResponse
import com.fadhlansulistiyo.codingstoryapp.data.response.RegisterResponse
import com.fadhlansulistiyo.codingstoryapp.data.response.StoryResponse
import com.fadhlansulistiyo.codingstoryapp.data.retrofit.ApiService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.After
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalPagingApi
@RunWith(AndroidJUnit4::class)
class StoriesRemoteMediatorTest {

    private var mockApi: ApiService = FakeApiService()
    private var mockDb: StoriesDatabase = Room.inMemoryDatabaseBuilder(
        ApplicationProvider.getApplicationContext(),
        StoriesDatabase::class.java
    ).allowMainThreadQueries().build()

    @Test
    fun refreshLoadReturnsSuccessResultWhenMoreDataIsPresent() = runTest {
        val remoteMediator = StoriesRemoteMediator(
            mockDb,
            mockApi
        )

        val pagingState = PagingState<Int, ListStoriesItem>(
            listOf(),
            null,
            PagingConfig(10),
            10
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)
        assertTrue(result is RemoteMediator.MediatorResult.Success)
        assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @After
    fun tearDown() {
        mockDb.clearAllTables()
    }
}

class FakeApiService : ApiService {
    override suspend fun register(name: String, email: String, password: String): RegisterResponse {
        throw NotImplementedError("Not implemented for testing")
    }

    override suspend fun login(email: String, password: String): LoginResponse {
        throw NotImplementedError("Not implemented for testing")
    }

    override suspend fun getStories(page: Int?, size: Int?): StoryResponse {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0 until 100) {
            val story = ListStoryItem(
                "story-Q1L_F3QlhdKTSgTw",
                "https://story-api.dicoding.dev/images/stories/photos-1721002649525_1e6c1ef7b0062a05ad6e.jpg",
                "2024-07-15T00:17:29.534Z",
                "Fadhlan",
                "cekcek",
                null,
                null
            )
            items.add(story)
        }
        return StoryResponse(items.subList((page!! - 1) * size!!, (page - 1) * size + size))
    }

    override suspend fun getDetailStories(id: String): DetailStoriesResponse {
        throw NotImplementedError("Not implemented for testing")
    }

    override suspend fun uploadStory(
        file: MultipartBody.Part,
        description: RequestBody,
        lat: Double?,
        lon: Double?
    ): AddStoryResponse {
        throw NotImplementedError("Not implemented for testing")
    }

    override suspend fun getStoriesWithLocation(location: Int): StoryResponse {
        throw NotImplementedError("Not implemented for testing")
    }

}
package com.fadhlansulistiyo.codingstoryapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.fadhlansulistiyo.codingstoryapp.data.db.StoriesDatabase
import com.fadhlansulistiyo.codingstoryapp.data.model.ListStoriesItem
import com.fadhlansulistiyo.codingstoryapp.data.model.UserModel
import com.fadhlansulistiyo.codingstoryapp.data.paging.StoriesRemoteMediator
import com.fadhlansulistiyo.codingstoryapp.data.pref.UserPreference
import com.fadhlansulistiyo.codingstoryapp.data.response.AddStoryResponse
import com.fadhlansulistiyo.codingstoryapp.data.response.DetailStoriesResponse
import com.fadhlansulistiyo.codingstoryapp.data.response.ErrorResponse
import com.fadhlansulistiyo.codingstoryapp.data.response.LoginResponse
import com.fadhlansulistiyo.codingstoryapp.data.response.RegisterResponse
import com.fadhlansulistiyo.codingstoryapp.data.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
    private val database: StoriesDatabase
) {

    suspend fun login(email: String, password: String): LoginResponse {
        return try {
            val response = apiService.login(email, password)
            val token = response.loginResult?.token.toString()
            saveSession(UserModel(email, token))
            response
        } catch (e: HttpException) {
            throw handleHttpException(e)
        }
    }

    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return try {
            apiService.register(name, email, password)
        } catch (e: HttpException) {
            throw handleHttpException(e)
        }
    }

    fun getStories(): LiveData<PagingData<ListStoriesItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                initialLoadSize = 10,
                prefetchDistance = 1,
            ),
            remoteMediator = StoriesRemoteMediator(database, apiService),
            pagingSourceFactory = {
                database.storiesDao().getAllStories()
            }
        ).liveData
    }

    suspend fun getDetailStories(id: String): DetailStoriesResponse {
        return try {
            apiService.getDetailStories(id)
        } catch (e: HttpException) {
            throw handleHttpException(e)
        }
    }

    suspend fun uploadStory(
        multipartFile: MultipartBody.Part,
        description: RequestBody
    ): AddStoryResponse {
        return try {
            apiService.uploadStory(multipartFile, description)
        } catch (e: HttpException) {
            throw handleHttpException(e)
        }
    }

    private suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    private fun handleHttpException(e: HttpException): Exception {
        val errorBody = e.response()?.errorBody()?.string()
        val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
        return Exception(errorResponse.message.toString())
    }

    companion object {
        fun getInstance(userPreference: UserPreference, apiService: ApiService, database: StoriesDatabase) =
            UserRepository(userPreference, apiService, database)
    }
}
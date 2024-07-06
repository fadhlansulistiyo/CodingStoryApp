package com.fadhlansulistiyo.codingstoryapp.ui.detailstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.fadhlansulistiyo.codingstoryapp.data.ResultState
import com.fadhlansulistiyo.codingstoryapp.data.UserRepository
import com.fadhlansulistiyo.codingstoryapp.data.response.DetailStoriesResponse

class DetailStoriesViewModel(private val repository: UserRepository) : ViewModel() {
    fun getDetailStories(id: String): LiveData<ResultState<DetailStoriesResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = repository.getDetailStories(id)
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }
}
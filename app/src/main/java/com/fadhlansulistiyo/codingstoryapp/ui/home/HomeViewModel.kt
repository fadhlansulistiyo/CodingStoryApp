package com.fadhlansulistiyo.codingstoryapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.fadhlansulistiyo.codingstoryapp.data.ResultState
import com.fadhlansulistiyo.codingstoryapp.data.UserRepository
import com.fadhlansulistiyo.codingstoryapp.data.response.StoryResponse
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getStories(): LiveData<ResultState<StoryResponse>> = liveData{
        emit(ResultState.Loading)
        try {
            val response = repository.getStories()
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }

}
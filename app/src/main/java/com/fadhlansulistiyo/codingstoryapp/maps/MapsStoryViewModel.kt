package com.fadhlansulistiyo.codingstoryapp.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fadhlansulistiyo.codingstoryapp.data.ResultState
import com.fadhlansulistiyo.codingstoryapp.data.UserRepository
import com.fadhlansulistiyo.codingstoryapp.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MapsStoryViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _mapsStories = MutableLiveData<ResultState<List<ListStoryItem>>>()
    val mapsStories: LiveData<ResultState<List<ListStoryItem>>> get() = _mapsStories

    init {
        fetchStoriesWithLocation()
    }

    private fun fetchStoriesWithLocation() = viewModelScope.launch {
        _mapsStories.value = ResultState.Loading
        try {
            val response = userRepository.getStoriesWithLocation()
            _mapsStories.value = ResultState.Success(response.listStory)
        } catch (e: Exception) {
            _mapsStories.value = ResultState.Error(e.message ?: "Unknown error")
        }
    }
}
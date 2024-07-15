package com.fadhlansulistiyo.codingstoryapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fadhlansulistiyo.codingstoryapp.data.UserRepository
import com.fadhlansulistiyo.codingstoryapp.data.model.ListStoriesItem
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: UserRepository) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    val stories: LiveData<PagingData<ListStoriesItem>> =
        repository.getStories().cachedIn(viewModelScope)

    private val _hasNewData = MediatorLiveData<Boolean>()
    val hasNewData: LiveData<Boolean> = _hasNewData

    init {
        _hasNewData.addSource(stories) {
            _hasNewData.value = true
        }
    }
}
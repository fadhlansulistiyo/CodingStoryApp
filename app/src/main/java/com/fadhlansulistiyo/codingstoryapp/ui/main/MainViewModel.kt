package com.fadhlansulistiyo.codingstoryapp.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fadhlansulistiyo.codingstoryapp.data.UserRepository
import com.fadhlansulistiyo.codingstoryapp.data.model.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> = repository.getSession().asLiveData()
}
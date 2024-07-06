package com.fadhlansulistiyo.codingstoryapp.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.fadhlansulistiyo.codingstoryapp.data.ResultState
import com.fadhlansulistiyo.codingstoryapp.data.UserRepository
import com.fadhlansulistiyo.codingstoryapp.data.response.RegisterResponse

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(
        name: String,
        email: String,
        password: String
    ): LiveData<ResultState<RegisterResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = repository.register(name, email, password)
            emit(ResultState.Success(response))
        } catch (e: Exception) {
            emit(ResultState.Error(e.message ?: "Unknown error"))
        }
    }
}
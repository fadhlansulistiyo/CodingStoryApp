package com.fadhlansulistiyo.codingstoryapp.ui.login

import androidx.lifecycle.ViewModel
import com.fadhlansulistiyo.codingstoryapp.data.UserRepository

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    fun login(email: String, password: String) = repository.login(email, password)
}
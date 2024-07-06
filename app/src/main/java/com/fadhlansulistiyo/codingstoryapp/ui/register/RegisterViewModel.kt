package com.fadhlansulistiyo.codingstoryapp.ui.register

import androidx.lifecycle.ViewModel
import com.fadhlansulistiyo.codingstoryapp.data.UserRepository

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(name: String, email: String, password: String) =
        repository.register(name, email, password)

}
package com.fadhlansulistiyo.codingstoryapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.fadhlansulistiyo.codingstoryapp.data.UserRepository
import com.fadhlansulistiyo.codingstoryapp.di.Injection
import com.fadhlansulistiyo.codingstoryapp.ui.addstory.AddStoryViewModel
import com.fadhlansulistiyo.codingstoryapp.ui.detailstory.DetailStoriesViewModel
import com.fadhlansulistiyo.codingstoryapp.ui.home.HomeViewModel
import com.fadhlansulistiyo.codingstoryapp.ui.login.LoginViewModel
import com.fadhlansulistiyo.codingstoryapp.ui.main.MainViewModel
import com.fadhlansulistiyo.codingstoryapp.ui.register.RegisterViewModel

class ViewModelFactory(private val repository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(repository) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository) as T
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> RegisterViewModel(repository) as T
            modelClass.isAssignableFrom(DetailStoriesViewModel::class.java) -> DetailStoriesViewModel(repository) as T
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> AddStoryViewModel(repository) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        fun getInstance(context: Context) = ViewModelFactory(Injection.provideRepository(context))
    }
}
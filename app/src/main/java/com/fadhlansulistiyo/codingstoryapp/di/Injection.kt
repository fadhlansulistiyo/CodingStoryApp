package com.fadhlansulistiyo.codingstoryapp.di

import android.content.Context
import com.fadhlansulistiyo.codingstoryapp.data.UserRepository
import com.fadhlansulistiyo.codingstoryapp.data.db.StoriesDatabase
import com.fadhlansulistiyo.codingstoryapp.data.pref.UserPreference
import com.fadhlansulistiyo.codingstoryapp.data.pref.dataStore
import com.fadhlansulistiyo.codingstoryapp.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val database = StoriesDatabase.getInstance(context)
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(pref, apiService, database)
    }
}
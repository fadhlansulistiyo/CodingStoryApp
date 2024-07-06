package com.fadhlansulistiyo.codingstoryapp.data

import androidx.lifecycle.liveData
import com.fadhlansulistiyo.codingstoryapp.data.model.UserModel
import com.fadhlansulistiyo.codingstoryapp.data.pref.UserPreference
import com.fadhlansulistiyo.codingstoryapp.data.response.LoginResponse
import com.fadhlansulistiyo.codingstoryapp.data.response.RegisterResponse
import com.fadhlansulistiyo.codingstoryapp.data.retrofit.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService
) {
    /*Session*/
    private suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    fun register(name: String, email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val successResponse = apiService.register(name, email, password)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }

    fun login(email: String, password: String) = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.login(email, password)
            val token = response.loginResult?.token.toString()
            saveSession(UserModel(email, token))
            emit(ResultState.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(ResultState.Error(errorResponse.message.toString()))
        }
    }

    companion object {
        fun getInstance(userPreference: UserPreference, apiService: ApiService) =
            UserRepository(userPreference, apiService)
    }
}
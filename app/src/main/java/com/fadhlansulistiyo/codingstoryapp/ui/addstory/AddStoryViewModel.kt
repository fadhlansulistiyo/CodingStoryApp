package com.fadhlansulistiyo.codingstoryapp.ui.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.fadhlansulistiyo.codingstoryapp.data.ResultState
import com.fadhlansulistiyo.codingstoryapp.data.UserRepository
import com.fadhlansulistiyo.codingstoryapp.data.response.AddStoryResponse
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryViewModel(private val repository: UserRepository) : ViewModel() {

    fun uploadStory(imageFile: File, description: String): LiveData<ResultState<AddStoryResponse>> =
        liveData {
            emit(ResultState.Loading)
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            try {
                val response = repository.uploadStory(multipartBody, requestBody)
                emit(ResultState.Success(response))
            } catch (e: Exception) {
                emit(ResultState.Error(e.message ?: "Unknown error"))
            }
        }

}
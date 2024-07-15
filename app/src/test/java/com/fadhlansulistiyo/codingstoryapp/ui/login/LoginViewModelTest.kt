package com.fadhlansulistiyo.codingstoryapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.fadhlansulistiyo.codingstoryapp.MainDispatcherRule
import com.fadhlansulistiyo.codingstoryapp.data.ResultState
import com.fadhlansulistiyo.codingstoryapp.data.UserRepository
import com.fadhlansulistiyo.codingstoryapp.data.response.LoginResponse
import com.fadhlansulistiyo.codingstoryapp.data.response.LoginResult
import com.fadhlansulistiyo.codingstoryapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    private val name = "fadhlan"
    private val userId = "123"
    private val token = "token"
    private val email = "fadhlan@example.com"
    private val password = "qwertyuiop"

    @Mock
    private lateinit var repository: UserRepository

    @Test
    fun `when Login Successful Should Return Success`() = runTest {
        val dummyResponse = LoginResponse(
            LoginResult(
                name = name,
                userId = userId,
                token = token
            )
        )

        val loginLiveData = MutableLiveData<ResultState<LoginResponse>>()
        loginLiveData.value = ResultState.Success(dummyResponse)

        Mockito.`when`(repository.login(email, password)).thenReturn(dummyResponse)

        val viewModel = LoginViewModel(repository)
        val resultLiveData = viewModel.login(email, password)

        resultLiveData.observeForever { result ->
            if (result is ResultState.Loading) {
                // Ignore
            }

            if (result is ResultState.Success) {
                assertEquals(dummyResponse, result.data)
            }
        }

        val result = resultLiveData.getOrAwaitValue()

        assertTrue(result is ResultState.Success)
        assertEquals(dummyResponse, (result as ResultState.Success).data)
    }

    @Test
    fun `when Login Failed Should Return Error`() = runTest {
        val errorMessage = "error message"
        val loginLiveData = MutableLiveData<ResultState<LoginResponse>>()
        loginLiveData.value = ResultState.Error(errorMessage)

        Mockito.`when`(repository.login(email, password)).thenThrow(RuntimeException(errorMessage))

        val viewModel = LoginViewModel(repository)

        val resultLiveData = viewModel.login(email, password)

        resultLiveData.observeForever { result ->
            if (result is ResultState.Loading) {
                // Ignore
            }

            if (result is ResultState.Error) {
                assertEquals(errorMessage, result.error)
            }
        }

        val result = resultLiveData.getOrAwaitValue()

        assertTrue(result is ResultState.Error)
        assertEquals(errorMessage, (result as ResultState.Error).error)
    }
}
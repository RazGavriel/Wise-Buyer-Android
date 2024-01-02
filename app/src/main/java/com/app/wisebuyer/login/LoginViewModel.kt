package com.app.wisebuyer.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean>
        get() = _loginResult

    fun loginUser(credentials: UserCredentials) {
        // In a real-world scenario, you would perform actual login logic here.
        // For this example, let's use simple mock data.
        val mockUsername = "matan"
        val mockPassword = "123"

        val isLoginSuccessful = credentials.username == mockUsername &&
                credentials.password == mockPassword

        _loginResult.value = isLoginSuccessful
    }
}
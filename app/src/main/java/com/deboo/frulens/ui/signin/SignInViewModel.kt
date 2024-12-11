package com.deboo.frulens.ui.signin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.deboo.frulens.data.LoginPreferences
import com.deboo.frulens.data.loginDataStore
import kotlinx.coroutines.launch

class SignInViewModel(application: Application) : AndroidViewModel(application) {

    private val loginPreferences = LoginPreferences.getInstance(application.loginDataStore)

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus

    // Function to perform login (this could be an API call, but here we'll simulate success)
    fun loginUser() {
        viewModelScope.launch {
            // Save login status as true when login is successful
            loginPreferences.saveStatus(true)
            _loginStatus.postValue(true) // Update the UI that the user is logged in
        }
    }

    // Function to check login status (can be used for checking if the user is already logged in)
    fun checkLoginStatus() {
        viewModelScope.launch {
            loginPreferences.getStatus().collect { isLoggedIn ->
                _loginStatus.postValue(isLoggedIn)
            }
        }
    }
}
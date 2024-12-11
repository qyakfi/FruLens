package com.deboo.frulens.ui.signin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.deboo.frulens.data.LoginPreferences
import com.deboo.frulens.data.loginDataStore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignInViewModel(application: Application) : AndroidViewModel(application) {

    private val loginPreferences = LoginPreferences.getInstance(application.loginDataStore)
    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> get() = _loginStatus
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _isSuccess = MutableLiveData<Boolean>()
    val isSuccess: LiveData<Boolean>
        get() = _isSuccess

    // Function to perform login
    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                showLoading(true)
                if (result.user != null) {
                    // Login successful, save status and update UI
                    loginPreferences.saveStatus(true)
                    _loginStatus.postValue(true)
                    _isSuccess.postValue(true)
                    showLoading(false)
                    Log.d("SignUp", "Account created successfully for: $email")
                } else {
                    _loginStatus.postValue(false)
                }
            } catch (e: Exception) {
                _loginStatus.postValue(false)
                // Handle error, maybe show a Toast in your Activity
                e.printStackTrace()
            }
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

    private fun showLoading(state: Boolean) {
        _isLoading.postValue(state)
    }
}

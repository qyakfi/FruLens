package com.deboo.frulens.ui.signin

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.deboo.frulens.MainActivity
import com.deboo.frulens.databinding.ActivitySigninBinding
import com.deboo.frulens.ui.signup.SignUpActivity


class SignInActivity : AppCompatActivity() {
    private lateinit var bind : ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setupView()
        setupViewModel()
        setupListener()
        playAnimation()
    }

    private fun setupViewModel() {
    }

    private fun setupView() {
        bind.etEmail.setTextInputLayout(bind.tilEmail)
        bind.etPassword.setTextInputLayout(bind.tilPassword)
    }

    private fun setupListener() {
        bind.btnRegister.setOnClickListener{
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }
        bind.btnLogin.setOnClickListener{
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    private fun playAnimation() {
        val email =
            ObjectAnimator.ofFloat(bind.tilEmail, View.ALPHA, 1f)
                .setDuration(250)
        val password =
            ObjectAnimator.ofFloat(bind.tilPassword, View.ALPHA, 1f)
                .setDuration(250)
        val register =
            ObjectAnimator.ofFloat(bind.btnRegister, View.ALPHA, 1f)
                .setDuration(250)
        val login =
            ObjectAnimator.ofFloat(bind.btnLogin, View.ALPHA, 1f)
                .setDuration(250)

        AnimatorSet().apply {
            playSequentially(
                email,
                password,
                login,
                register
            )
            startDelay = 250
        }.start()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            bind.cvProgress.visibility = View.VISIBLE
        } else {
            bind.cvProgress.visibility = View.GONE
        }
    }
}
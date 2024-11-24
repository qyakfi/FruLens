package com.deboo.frulens.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.deboo.frulens.databinding.ActivitySignupBinding
import com.deboo.frulens.ui.signin.SignInActivity

class SignUpActivity : AppCompatActivity() {
    private lateinit var bind : ActivitySignupBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySignupBinding.inflate(layoutInflater)
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
        bind.etName.setTextInputLayout(bind.tilName)
    }

    private fun setupListener() {
        bind.btnLogin.setOnClickListener{
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
        }
        bind.btnRegister.setOnClickListener{
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }
    }

    private fun playAnimation() {
        val name =
            ObjectAnimator.ofFloat(bind.tilName, View.ALPHA, 1f)
                .setDuration(250)
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
                name,
                email,
                password,
                register,
                login
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
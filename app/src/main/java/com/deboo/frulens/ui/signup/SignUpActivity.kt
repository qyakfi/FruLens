package com.deboo.frulens.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.deboo.frulens.databinding.ActivitySignupBinding
import com.deboo.frulens.ui.signin.SignInActivity
import com.deboo.frulens.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    private lateinit var bind: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setupView()
        setupListener()
        playAnimation()

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
    }

    private fun setupView() {
        bind.etEmail.setTextInputLayout(bind.tilEmail)
        bind.etPassword.setTextInputLayout(bind.tilPassword)
        bind.etName.setTextInputLayout(bind.tilName)
    }

    private fun setupListener() {
        // Login button listener
        bind.btnLogin.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

        // Register button listener
        bind.btnRegister.setOnClickListener {
            val name = bind.etName.text.toString()
            val email = bind.etEmail.text.toString()
            val password = bind.etPassword.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                registerUser(name, email, password)
            }
        }
    }

    private fun registerUser(name: String, email: String, password: String) {
        showLoading(true)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    // Registration successful, navigate to the main activity
                    val user = auth.currentUser
                    user?.let {
                        Toast.makeText(this, "Welcome, ${user.email}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish() // Close the registration activity
                    }
                } else {
                    // Registration failed, show error message
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun playAnimation() {
        val name = ObjectAnimator.ofFloat(bind.tilName, View.ALPHA, 1f).setDuration(250)
        val email = ObjectAnimator.ofFloat(bind.tilEmail, View.ALPHA, 1f).setDuration(250)
        val password = ObjectAnimator.ofFloat(bind.tilPassword, View.ALPHA, 1f).setDuration(250)
        val register = ObjectAnimator.ofFloat(bind.btnRegister, View.ALPHA, 1f).setDuration(250)
        val login = ObjectAnimator.ofFloat(bind.btnLogin, View.ALPHA, 1f).setDuration(250)

        AnimatorSet().apply {
            playSequentially(name, email, password, register, login)
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

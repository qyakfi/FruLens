package com.deboo.frulens.ui.signin

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.deboo.frulens.MainActivity
import com.deboo.frulens.databinding.ActivitySigninBinding
import com.deboo.frulens.ui.signup.SignUpActivity
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var bind: ActivitySigninBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(bind.root)
        setupView()
        setupListener()
        playAnimation()

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
    }

    private fun setupView() {
        // Ensure that your TextInputLayouts are correctly initialized in the layout
        bind.etEmail.setTextInputLayout(bind.tilEmail)
        bind.etPassword.setTextInputLayout(bind.tilPassword)
    }

    private fun setupListener() {
        // Register Button click listener
        bind.btnRegister.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        // Login Button click listener
        bind.btnLogin.setOnClickListener {
            val email = bind.etEmail.text.toString()
            val password = bind.etPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                signInUser(email, password)
            }
        }
    }

    private fun signInUser(email: String, password: String) {
        showLoading(true)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                showLoading(false)
                if (task.isSuccessful) {
                    // Sign-in successful, navigate to MainActivity
                    val user = auth.currentUser
                    if (user != null) {
                        Toast.makeText(this, "Welcome ${user.email}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish() // Close the sign-in activity
                    }
                } else {
                    // Sign-in failed, show error message
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun playAnimation() {
        val email =
            ObjectAnimator.ofFloat(bind.tilEmail, View.ALPHA, 1f).setDuration(250)
        val password =
            ObjectAnimator.ofFloat(bind.tilPassword, View.ALPHA, 1f).setDuration(250)
        val register =
            ObjectAnimator.ofFloat(bind.btnRegister, View.ALPHA, 1f).setDuration(250)
        val login =
            ObjectAnimator.ofFloat(bind.btnLogin, View.ALPHA, 1f).setDuration(250)

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

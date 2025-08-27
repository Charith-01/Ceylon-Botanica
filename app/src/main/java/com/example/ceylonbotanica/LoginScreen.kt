package com.example.ceylonbotanica

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_screen)

        // Edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // Inputs
        val tilEmail = findViewById<TextInputLayout>(R.id.tilEmail)
        val tilPassword = findViewById<TextInputLayout>(R.id.tilPassword)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)

        // Clear errors while typing
        etEmail.doOnTextChanged { _, _, _, _ -> tilEmail.error = null }
        etPassword.doOnTextChanged { _, _, _, _ -> tilPassword.error = null }

        // Sign In -> validate, then navigate to HomeScreen
        findViewById<Button>(R.id.signinbtn).setOnClickListener {
            val email = etEmail.text?.toString()?.trim().orEmpty()
            val password = etPassword.text?.toString()?.trim().orEmpty()

            var valid = true

            // Email validation
            if (email.isEmpty()) {
                tilEmail.error = "Email is required"
                valid = false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tilEmail.error = "Enter a valid email"
                valid = false
            } else {
                tilEmail.error = null
            }

            // Password validation
            if (password.isEmpty()) {
                tilPassword.error = "Password is required"
                valid = false
            } else if (password.length < 6) {
                tilPassword.error = "Password must be at least 6 characters"
                valid = false
            } else {
                tilPassword.error = null
            }

            if (!valid) return@setOnClickListener

            // All good → go to HomeScreen (clear back stack)
            val intent = Intent(this, HomeScreen::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(intent)
            overridePendingTransition(R.anim.fade_through_in, R.anim.fade_through_out)
        }

        // Sign Up text -> RegistrationScreen
        findViewById<TextView>(R.id.btnsignup2).setOnClickListener {
            startActivity(Intent(this, RegistrationScreen::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}

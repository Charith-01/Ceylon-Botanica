package com.example.ceylonbotanica

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.checkbox.MaterialCheckBox
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegistrationScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration_screen)

        // Edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // Inputs
        val tilFullName = findViewById<TextInputLayout>(R.id.tilFullName)
        val tilEmail = findViewById<TextInputLayout>(R.id.tilEmail)
        val tilAddress = findViewById<TextInputLayout>(R.id.tilAddress)
        val tilPassword = findViewById<TextInputLayout>(R.id.tilPassword)

        val etFullName = findViewById<TextInputEditText>(R.id.etFullName)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etAddress = findViewById<TextInputEditText>(R.id.etAddress)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)

        val cbAgree = findViewById<MaterialCheckBox>(R.id.cbAgree)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)

        // Clear errors while typing
        etFullName.doOnTextChanged { _, _, _, _ -> tilFullName.error = null }
        etEmail.doOnTextChanged { _, _, _, _ -> tilEmail.error = null }
        etAddress.doOnTextChanged { _, _, _, _ -> tilAddress.error = null }
        etPassword.doOnTextChanged { _, _, _, _ -> tilPassword.error = null }

        // Sign Up -> validate then go to VerificationScreen
        btnSignUp.setOnClickListener {
            val fullName = etFullName.text?.toString()?.trim().orEmpty()
            val email = etEmail.text?.toString()?.trim().orEmpty()
            val address = etAddress.text?.toString()?.trim().orEmpty()
            val password = etPassword.text?.toString()?.trim().orEmpty()

            var valid = true

            // Full name
            if (fullName.isEmpty()) {
                tilFullName.error = "Full name is required"
                valid = false
            } else if (fullName.length < 2) {
                tilFullName.error = "Enter your full name"
                valid = false
            }

            // Email
            if (email.isEmpty()) {
                tilEmail.error = "Email is required"
                valid = false
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                tilEmail.error = "Enter a valid email"
                valid = false
            }

            // Address
            if (address.isEmpty()) {
                tilAddress.error = "Address is required"
                valid = false
            }

            // Password
            if (password.isEmpty()) {
                tilPassword.error = "Password is required"
                valid = false
            } else if (password.length < 6) {
                tilPassword.error = "Password must be at least 6 characters"
                valid = false
            }

            // Terms
            if (!cbAgree.isChecked) {
                Toast.makeText(this, "Please agree to the Terms and Conditions", Toast.LENGTH_SHORT).show()
                valid = false
            }

            if (!valid) return@setOnClickListener

            // All good → navigate
            startActivity(Intent(this, VerificationScreen::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        // "Sign In" text -> LoginScreen
        findViewById<TextView>(R.id.btnSignIn).setOnClickListener {
            startActivity(Intent(this, LoginScreen::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}

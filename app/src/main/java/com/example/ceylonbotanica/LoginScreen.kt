package com.example.ceylonbotanica

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login_screen)

        // Edge-to-edge insets (optional, matches your other screens)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // --- Clicks ---
        // Sign In -> HomeScreen (clear back stack so back won’t return to Login)
        findViewById<Button>(R.id.signinbtn).setOnClickListener {
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

package com.example.ceylonbotanica

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.ceylonbotanica.home.HomeFragment
import com.example.ceylonbotanica.profile.ProfileFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            delay(3000)
            startActivity(Intent(this@MainActivity, OnboardScreen1::class.java))
            finish()
        }
    }
}

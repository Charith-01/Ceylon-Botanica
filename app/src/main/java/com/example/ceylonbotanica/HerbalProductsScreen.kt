package com.example.ceylonbotanica

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HerbalProductsScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_herbal_products_screen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // Back icon → HomeScreen
        findViewById<ImageView>(R.id.imageView3).setOnClickListener { goBackToHome() }

        // Product 3 image → BlueLotusTeaProductScreen
        findViewById<ImageView>(R.id.ivProduct3).setOnClickListener {
            startActivity(Intent(this, BlueLotusTeaProductScreen::class.java))
            overridePendingTransition(R.anim.fade_through_in, R.anim.fade_through_out)
        }

        // System back → go Home
        onBackPressedDispatcher.addCallback(this) { goBackToHome() }
    }

    private fun goBackToHome() {
        if (isTaskRoot) {
            startActivity(
                Intent(this, HomeScreen::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            )
        }
        finish()
        overridePendingTransition(R.anim.fade_through_in, R.anim.fade_through_out)
    }
}

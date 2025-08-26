package com.example.ceylonbotanica

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BlueLotusTeaProductScreen : AppCompatActivity() {

    private var qty = 1
    private val MIN_QTY = 1
    private val MAX_QTY = 99
    // $18.00 in cents
    private val unitPriceCents = 1800

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_blue_lotus_tea_product_screen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        val btnPlus = findViewById<TextView>(R.id.btnPlus)
        val btnMinus = findViewById<TextView>(R.id.btnMinus)
        val tvQty = findViewById<TextView>(R.id.tvQty)
        val tvNewPrice = findViewById<TextView>(R.id.tvNewPrice)

        fun formatCents(cents: Int): String {
            val dollars = cents / 100
            val rem = cents % 100
            return if (rem == 0) "$$dollars" else "$$dollars.${rem.toString().padStart(2, '0')}"
        }

        fun render() {
            tvQty.text = qty.toString()
            val totalCents = qty * unitPriceCents
            tvNewPrice.text = formatCents(totalCents)
            btnMinus.isEnabled = qty > MIN_QTY
            btnMinus.alpha = if (btnMinus.isEnabled) 1f else 0.4f
        }

        render()

        btnPlus.setOnClickListener {
            if (qty < MAX_QTY) {
                qty++
                render()
            }
        }

        btnMinus.setOnClickListener {
            if (qty > MIN_QTY) {
                qty--
                render()
            }
        }

        // Add to Cart → toast + navigate HomeScreen (quick fade-through)
        findViewById<View>(R.id.btnAddToCart).setOnClickListener {
            Toast.makeText(this, "Added to Cart", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, HomeScreen::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_through_in, R.anim.fade_through_out)
            finish()
        }

        // Back icon → finish with same animation
        findViewById<ImageView?>(R.id.imageView13)?.setOnClickListener { goBack() }

        // System back
        onBackPressedDispatcher.addCallback(this) { goBack() }
    }

    private fun goBack() {
        finish()
        overridePendingTransition(R.anim.fade_through_in, R.anim.fade_through_out)
    }
}

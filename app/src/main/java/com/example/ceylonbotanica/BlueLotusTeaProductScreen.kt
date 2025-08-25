package com.example.ceylonbotanica

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class BlueLotusTeaProductScreen : AppCompatActivity() {

    private var qty = 1
    private val MIN_QTY = 1
    private val MAX_QTY = 99

    // $18.00 in cents to keep math exact
    private val unitPriceCents = 1800

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blue_lotus_tea_product_screen) // your layout id

        val btnPlus = findViewById<TextView>(R.id.btnPlus)
        val btnMinus = findViewById<TextView>(R.id.btnMinus)
        val tvQty = findViewById<TextView>(R.id.tvQty)
        val tvNewPrice = findViewById<TextView>(R.id.tvNewPrice)

        fun formatCents(cents: Int): String {
            val dollars = cents / 100
            val rem = cents % 100
            return if (rem == 0) "$$dollars" else "$$dollars.${rem.toString().padStart(2,'0')}"
        }

        fun render() {
            tvQty.text = qty.toString()
            val totalCents = qty * unitPriceCents
            tvNewPrice.text = formatCents(totalCents)   // $18, $36, $54, ...
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
    }
}


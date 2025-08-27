package com.example.ceylonbotanica

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Calendar

class PaymentGatewayScreen : AppCompatActivity() {

    private lateinit var itemViews: List<Pair<View, ImageView>>
    private var selectedIndex = 2

    // Form fields
    private lateinit var tilName: TextInputLayout
    private lateinit var tilCard: TextInputLayout
    private lateinit var tilExpMonth: TextInputLayout
    private lateinit var tilExpYear: TextInputLayout
    private lateinit var tilCvc: TextInputLayout

    private lateinit var etName: TextInputEditText
    private lateinit var etCard: TextInputEditText
    private lateinit var etExpMonth: TextInputEditText
    private lateinit var etExpYear: TextInputEditText
    private lateinit var etCvc: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_payment_gateway_screen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // Back → Cart tab in HomeScreen
        findViewById<ImageView?>(R.id.ivBack)?.setOnClickListener { backToCartTab() }
        onBackPressedDispatcher.addCallback(this) { backToCartTab() }

        tilName = findViewById(R.id.tilName)
        tilCard = findViewById(R.id.tilCard)
        tilExpMonth = findViewById(R.id.tilExpMonth)
        tilExpYear = findViewById(R.id.tilExpYear)
        tilCvc = findViewById(R.id.tilCvc)

        etName = findViewById(R.id.etName)
        etCard = findViewById(R.id.etCard)
        etExpMonth = findViewById(R.id.etExpMonth)
        etExpYear = findViewById(R.id.etExpYear)
        etCvc = findViewById(R.id.etCvc)

        addClearErrorOnType(tilName, etName)
        addClearErrorOnType(tilCard, etCard)
        addClearErrorOnType(tilExpMonth, etExpMonth)
        addClearErrorOnType(tilExpYear, etExpYear)
        addClearErrorOnType(tilCvc, etCvc)

        findViewById<View>(R.id.btnPayNow).setOnClickListener {
            if (validateForm()) {
                startActivity(Intent(this, PaymentVerificationScreen::class.java))
                overridePendingTransition(R.anim.fade_through_in, R.anim.fade_through_out)
            }
        }

        val pmPaypal = findViewById<View>(R.id.pmPaypal)
        val pmVisa = findViewById<View>(R.id.pmVisa)
        val pmMaster = findViewById<View>(R.id.pmMastercard)
        val pmDiners = findViewById<View>(R.id.pmDiners)
        val pmAmex = findViewById<View>(R.id.pmAmex)

        val ivSelPaypal = findViewById<ImageView>(R.id.ivSelPaypal)
        val ivSelVisa = findViewById<ImageView>(R.id.ivSelVisa)
        val ivSelMaster = findViewById<ImageView>(R.id.ivSelMastercard)
        val ivSelDiners = findViewById<ImageView>(R.id.ivSelDiners)
        val ivSelAmex = findViewById<ImageView>(R.id.ivSelAmex)

        itemViews = listOf(
            pmPaypal to ivSelPaypal,
            pmVisa to ivSelVisa,
            pmMaster to ivSelMaster,
            pmDiners to ivSelDiners,
            pmAmex to ivSelAmex
        )
        itemViews.forEachIndexed { index, (container, _) ->
            container.setOnClickListener { select(index) }
        }
        select(selectedIndex)
    }


    private fun validateForm(): Boolean {
        var ok = true

        val name = etName.text?.toString()?.trim().orEmpty()
        if (name.length < 2) {
            tilName.error = "Enter your full name"
            ok = false
        } else tilName.error = null

        val cardDigits = etCard.text?.toString().orEmpty().filter { it.isDigit() }
        if (cardDigits.length !in 13..19) {
            tilCard.error = "Enter a valid card number"
            ok = false
        } else tilCard.error = null

        val month = etExpMonth.text?.toString()?.toIntOrNull()
        if (month == null || month !in 1..12) {
            tilExpMonth.error = "Invalid month"
            ok = false
        } else tilExpMonth.error = null

        val yearRaw = etExpYear.text?.toString().orEmpty()
        val year = yearRaw.toIntOrNull()
        val now = Calendar.getInstance()
        val currYearFull = now.get(Calendar.YEAR) // 2025
        val currMonth = now.get(Calendar.MONTH) + 1 // 1..12

        val yearFull = when {
            year == null -> null
            year in 0..99 -> 2000 + year           // treat 2-digit as 20xx
            year in 2000..2099 -> year
            else -> null
        }

        var expValid = true
        if (yearFull == null) {
            tilExpYear.error = "Invalid year"
            expValid = false
            ok = false
        } else {

            val notExpired = (yearFull > currYearFull) ||
                    (yearFull == currYearFull && (month ?: 0) >= currMonth)
            if (!notExpired) {
                tilExpYear.error = "Card expired"
                expValid = false
                ok = false
            } else tilExpYear.error = null
        }

        val cvc = etCvc.text?.toString().orEmpty().filter { it.isDigit() }
        if (cvc.length !in 3..4) {
            tilCvc.error = "Invalid CVC"
            ok = false
        } else tilCvc.error = null

        if (!ok) {
            when {
                tilName.error != null -> etName.requestFocus()
                tilCard.error != null -> etCard.requestFocus()
                tilExpMonth.error != null -> etExpMonth.requestFocus()
                tilExpYear.error != null && expValid.not() -> etExpYear.requestFocus()
                tilCvc.error != null -> etCvc.requestFocus()
            }
        }
        return ok
    }

    private fun addClearErrorOnType(til: TextInputLayout, et: TextInputEditText) {
        et.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!til.error.isNullOrEmpty()) til.error = null
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }


    private fun select(index: Int) {
        selectedIndex = index
        itemViews.forEachIndexed { i, (container, badge) ->
            val isSelected = i == index
            badge.visibility = if (isSelected) View.VISIBLE else View.GONE
            container.isSelected = isSelected
        }
    }

    private fun backToCartTab() {
        startActivity(
            Intent(this, HomeScreen::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                .putExtra("open_tab", "CART")
        )
        overridePendingTransition(R.anim.fade_through_in, R.anim.fade_through_out)
        finish()
    }
}

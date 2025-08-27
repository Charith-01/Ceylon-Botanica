package com.example.ceylonbotanica

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class VerificationScreen : AppCompatActivity() {

    private lateinit var et1: EditText
    private lateinit var et2: EditText
    private lateinit var et3: EditText
    private lateinit var et4: EditText
    private lateinit var et5: EditText
    private lateinit var et6: EditText
    private lateinit var btnVerify: Button
    private lateinit var fields: List<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_verification_screen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById<View>(R.id.main)) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }

        // Bind
        et1 = findViewById(R.id.etOtp1)
        et2 = findViewById(R.id.etOtp2)
        et3 = findViewById(R.id.etOtp3)
        et4 = findViewById(R.id.etOtp4)
        et5 = findViewById(R.id.etOtp5)
        et6 = findViewById(R.id.etOtp6)
        btnVerify = findViewById(R.id.btnCntinue)
        fields = listOf(et1, et2, et3, et4, et5, et6)

        setupOtpInputs()

        updateButtonState()

        btnVerify.setOnClickListener {
            val code = collectCode()
            if (code.length == fields.size) {
                startActivity(Intent(this, VerificationSuccessScreen::class.java))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            } else {
                Toast.makeText(this, "Please enter the 6-digit code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupOtpInputs() {
        fields.forEachIndexed { index, et ->
            et.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val text = (s ?: "").toString()

                    if (text.length > 1) {
                        val digits = text.filter { it.isDigit() }
                        if (digits.isNotEmpty()) {
                            var j = 0
                            for (k in index until fields.size) {
                                if (j < digits.length) {
                                    fields[k].setText(digits[j].toString())
                                    j++
                                } else break
                            }

                            val lastIdx = (index + digits.length - 1).coerceAtMost(fields.lastIndex)
                            fields[lastIdx].requestFocus()
                        } else {
                            et.setText("")
                        }
                    } else if (text.length == 1) {

                        if (!text[0].isDigit()) {
                            et.setText("")
                        } else if (index < fields.lastIndex) {
                            fields[index + 1].requestFocus()
                        }
                    }
                    updateButtonState()
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            et.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_DEL &&
                    et.text.isNullOrEmpty() &&
                    index > 0
                ) {
                    val prev = fields[index - 1]
                    prev.setText("")
                    prev.requestFocus()
                    updateButtonState()
                    true
                } else {
                    false
                }
            }
        }

        et1.requestFocus()
    }

    private fun collectCode(): String =
        fields.joinToString(separator = "") { it.text?.toString()?.trim().orEmpty() }

    private fun updateButtonState() {
        val enabled = collectCode().length == fields.size
        btnVerify.isEnabled = enabled
        btnVerify.alpha = if (enabled) 1f else 0.5f
    }
}

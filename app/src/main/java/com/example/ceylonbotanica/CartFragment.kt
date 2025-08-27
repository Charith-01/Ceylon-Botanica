package com.example.ceylonbotanica.cart

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ceylonbotanica.R
import com.example.ceylonbotanica.PaymentGatewayScreen
import com.google.android.material.button.MaterialButton
import java.text.DecimalFormat
import kotlin.math.max

class CartFragment : Fragment() {

    // Item 1
    private lateinit var tvNewPrice1: TextView
    private lateinit var tvQty1: TextView
    private lateinit var btnPlus1: TextView
    private lateinit var btnMinus1: TextView

    // Item 2
    private lateinit var tvNewPrice2: TextView
    private lateinit var tvQty2: TextView
    private lateinit var btnPlus2: TextView
    private lateinit var btnMinus2: TextView

    // Summary
    private lateinit var tvSubtotal: TextView
    private lateinit var tvDiscount: TextView
    private lateinit var tvDelivery: TextView
    private lateinit var tvVat: TextView
    private lateinit var tvTotal: TextView
    private lateinit var btnCheckout: MaterialButton

    // Unit prices & qty
    private var unitPrice1 = 0.0
    private var unitPrice2 = 0.0
    private var qty1 = 1
    private var qty2 = 1

    private val moneyFmt = DecimalFormat("#0.##")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.activity_cart_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back button -> let HomeScreen switch to Home tab
        view.findViewById<ImageView>(R.id.btnBack)?.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // Bind views
        tvNewPrice1 = view.findViewById(R.id.tvNewPrice1)
        tvQty1 = view.findViewById(R.id.tvQty1)
        btnPlus1 = view.findViewById(R.id.btnPlus1)
        btnMinus1 = view.findViewById(R.id.btnMinus1)

        tvNewPrice2 = view.findViewById(R.id.tvNewPrice2)
        tvQty2 = view.findViewById(R.id.tvQty2)
        btnPlus2 = view.findViewById(R.id.btnPlus2)
        btnMinus2 = view.findViewById(R.id.btnMinus2)

        tvSubtotal = view.findViewById(R.id.tvSubtotal)
        tvDiscount = view.findViewById(R.id.tvDiscount)
        tvDelivery = view.findViewById(R.id.tvDelivery)
        tvVat = view.findViewById(R.id.tvVat)
        tvTotal = view.findViewById(R.id.tvTotal)
        btnCheckout = view.findViewById(R.id.btnCheckout)

        // Capture unit prices BEFORE we mutate labels
        unitPrice1 = parseMoney(tvNewPrice1.text.toString()) // $18 → 18.0
        unitPrice2 = parseMoney(tvNewPrice2.text.toString()) // $40 → 40.0

        qty1 = tvQty1.text.toString().toIntOrNull() ?: 1
        qty2 = tvQty2.text.toString().toIntOrNull() ?: 1

        // Initial render
        updateLine1()
        updateLine2()
        updateSummary()

        // Listeners
        btnPlus1.setOnClickListener { qty1++; updateLine1(); updateSummary() }
        btnMinus1.setOnClickListener { qty1 = max(1, qty1 - 1); updateLine1(); updateSummary() }

        btnPlus2.setOnClickListener { qty2++; updateLine2(); updateSummary() }
        btnMinus2.setOnClickListener { qty2 = max(1, qty2 - 1); updateLine2(); updateSummary() }

        // Checkout → PaymentGatewayScreen
        btnCheckout.setOnClickListener {
            startActivity(Intent(requireContext(), PaymentGatewayScreen::class.java))
            requireActivity().overridePendingTransition(
                R.anim.fade_through_in,
                R.anim.fade_through_out
            )
        }
    }

    private fun updateLine1() {
        tvQty1.text = qty1.toString()
        tvNewPrice1.text = formatMoney(unitPrice1 * qty1)   // show line total
    }

    private fun updateLine2() {
        tvQty2.text = qty2.toString()
        tvNewPrice2.text = formatMoney(unitPrice2 * qty2)   // show line total
    }

    private fun updateSummary() {
        val subtotal = unitPrice1 * qty1 + unitPrice2 * qty2
        val discount = parseMoney(tvDiscount.text.toString())   // fixed $ amount
        val delivery = parseMoney(tvDelivery.text.toString())
        val vat = parseMoney(tvVat.text.toString())

        val total = subtotal - discount + delivery + vat

        tvSubtotal.text = formatMoney(subtotal)
        tvTotal.text = formatMoney(total)
        btnCheckout.text = "Checkout (${formatMoney(total)})"
    }

    private fun parseMoney(s: String): Double {
        val clean = s.replace(Regex("[^0-9.]"), "")
        return clean.toDoubleOrNull() ?: 0.0
    }

    private fun formatMoney(v: Double): String = "$" + moneyFmt.format(v)
}

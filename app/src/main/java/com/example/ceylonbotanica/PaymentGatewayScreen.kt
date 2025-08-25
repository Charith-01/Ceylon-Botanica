package com.example.ceylonbotanica

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PaymentGatewayScreen : AppCompatActivity() {

    private lateinit var itemViews: List<Pair<View, ImageView>>
    private var selectedIndex = 2 // 0=PayPal, 1=Visa, 2=Mastercard (default), 3=Diners, 4=Amex

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_gateway_screen)

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

        // set listeners
        itemViews.forEachIndexed { index, (container, _) ->
            container.setOnClickListener { select(index) }
        }

        // default selection (Mastercard)
        select(selectedIndex)
    }

    private fun select(index: Int) {
        selectedIndex = index
        itemViews.forEachIndexed { i, (container, badge) ->
            val isSelected = i == index
            badge.visibility = if (isSelected) View.VISIBLE else View.GONE
            container.isSelected = isSelected
        }
    }
}

package com.example.ceylonbotanica

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.core.widget.NestedScrollView

class HomeScreen : AppCompatActivity() {

    // Tabs
    private lateinit var tabHome: LinearLayout
    private lateinit var tabWishlist: LinearLayout
    private lateinit var tabCart: LinearLayout
    private lateinit var tabProfile: LinearLayout

    // Icons
    private lateinit var ivHome: ImageView
    private lateinit var ivWishlist: ImageView
    private lateinit var ivCart: ImageView
    private lateinit var ivProfile: ImageView

    // Labels
    private lateinit var tvHome: TextView
    private lateinit var tvWishlist: TextView
    private lateinit var tvCart: TextView
    private lateinit var tvProfile: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)

        // Insets
        val scroll = findViewById<NestedScrollView>(R.id.scroll)
        val bottomNav = findViewById<LinearLayout>(R.id.bottomNav)
        ViewCompat.setOnApplyWindowInsetsListener(scroll) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = bars.top)
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(bottomNav) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(bottom = bars.bottom)
            insets
        }

        // Tabs
        tabHome = findViewById(R.id.tabHome)
        tabWishlist = findViewById(R.id.tabWishlist)
        tabCart = findViewById(R.id.tabCart)
        tabProfile = findViewById(R.id.tabProfile)

        // Icons
        ivHome = findViewById(R.id.ivHome)
        ivWishlist = findViewById(R.id.ivWishlist)
        ivCart = findViewById(R.id.ivCart)
        ivProfile = findViewById(R.id.ivProfile)

        // Labels
        tvHome = findViewById(R.id.tvHome)
        tvWishlist = findViewById(R.id.tvWishlist)
        tvCart = findViewById(R.id.tvCart)
        tvProfile = findViewById(R.id.tvProfile)

        // Labels always visible & always bold
        val labelColor = ContextCompat.getColor(this, android.R.color.white)
        listOf(tvHome, tvWishlist, tvCart, tvProfile).forEach { tv ->
            tv.visibility = View.VISIBLE
            tv.typeface = Typeface.DEFAULT_BOLD
            tv.setTextColor(labelColor)
        }

        // Default tab
        setSelectedTab(Tab.HOME)

        // Clicks
        tabHome.setOnClickListener { setSelectedTab(Tab.HOME) }
        tabWishlist.setOnClickListener { setSelectedTab(Tab.WISHLIST) }
        tabCart.setOnClickListener { setSelectedTab(Tab.CART) }
        tabProfile.setOnClickListener { setSelectedTab(Tab.PROFILE) }
    }

    private fun setSelectedTab(tab: Tab) {
        // Toggle icon selector state and give a small emphasis to the selected one
        fun style(icon: ImageView, selected: Boolean) {
            icon.isSelected = selected            // swaps non-fill ↔ fill via selector
            icon.scaleX = if (selected) 1.08f else 1.0f
            icon.scaleY = if (selected) 1.08f else 1.0f
        }

        style(ivHome, tab == Tab.HOME)
        style(ivWishlist, tab == Tab.WISHLIST)
        style(ivCart, tab == Tab.CART)
        style(ivProfile, tab == Tab.PROFILE)
        // Labels remain bold & same color in all states by design
    }

    private enum class Tab { HOME, WISHLIST, CART, PROFILE }
}

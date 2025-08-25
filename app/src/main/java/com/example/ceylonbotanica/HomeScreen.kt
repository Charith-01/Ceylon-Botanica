package com.example.ceylonbotanica

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.ceylonbotanica.cart.CartFragment
import com.example.ceylonbotanica.home.HomeFragment
import com.example.ceylonbotanica.profile.ProfileFragment
import com.example.ceylonbotanica.wishlist.WishlistFragment

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

    private val KEY_SELECTED_TAB = "selected_tab"
    private var currentTab: Tab = Tab.HOME

    private val tagHome = "frag_home"
    private val tagWishlist = "frag_wishlist"
    private val tagCart = "frag_cart"
    private val tagProfile = "frag_profile"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_screen)

        // Insets just for container & bottom bar
        val contentContainer = findViewById<View>(R.id.contentContainer)
        val bottomNav = findViewById<LinearLayout>(R.id.bottomNav)

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

        // Labels always visible & bold
        val labelColor = ContextCompat.getColor(this, android.R.color.white)
        listOf(tvHome, tvWishlist, tvCart, tvProfile).forEach { tv ->
            tv.visibility = View.VISIBLE
            tv.typeface = Typeface.DEFAULT_BOLD
            tv.setTextColor(labelColor)
        }

        // Prepare fragments once
        ensureFragmentsAdded()

        // Restore selected tab (e.g., after rotation)
        currentTab = savedInstanceState?.getString(KEY_SELECTED_TAB)?.let { Tab.valueOf(it) } ?: Tab.HOME
        setSelectedTab(currentTab, firstTime = true)

        // Clicks
        tabHome.setOnClickListener { setSelectedTab(Tab.HOME) }
        tabWishlist.setOnClickListener { setSelectedTab(Tab.WISHLIST) }
        tabCart.setOnClickListener { setSelectedTab(Tab.CART) }
        tabProfile.setOnClickListener { setSelectedTab(Tab.PROFILE) }

        // Back press: if not on Home, go Home; else default behavior (finish)
        onBackPressedDispatcher.addCallback(this) {
            if (currentTab != Tab.HOME) {
                setSelectedTab(Tab.HOME)
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun ensureFragmentsAdded() {
        val fm = supportFragmentManager
        val tx = fm.beginTransaction().setReorderingAllowed(true)

        val home = fm.findFragmentByTag(tagHome) ?: HomeFragment().also {
            tx.add(R.id.contentContainer, it, tagHome)
        }
        val wishlist = fm.findFragmentByTag(tagWishlist) ?: WishlistFragment().also {
            tx.add(R.id.contentContainer, it, tagWishlist).hide(it)
        }
        val cart = fm.findFragmentByTag(tagCart) ?: CartFragment().also {
            tx.add(R.id.contentContainer, it, tagCart).hide(it)
        }
        val profile = fm.findFragmentByTag(tagProfile) ?: ProfileFragment().also {
            tx.add(R.id.contentContainer, it, tagProfile).hide(it)
        }

        tx.commitNow() // commit immediately so they exist before first switch
    }

    private fun setSelectedTab(tab: Tab, firstTime: Boolean = false) {
        currentTab = tab

        // Toggle icon emphasis
        fun style(icon: ImageView, selected: Boolean) {
            icon.isSelected = selected
            icon.scaleX = if (selected) 1.08f else 1.0f
            icon.scaleY = if (selected) 1.08f else 1.0f
        }
        style(ivHome, tab == Tab.HOME)
        style(ivWishlist, tab == Tab.WISHLIST)
        style(ivCart, tab == Tab.CART)
        style(ivProfile, tab == Tab.PROFILE)

        // Show/hide fragments
        val fm = supportFragmentManager
        val tx = fm.beginTransaction().setReorderingAllowed(true)

        // Optional: quick crossfade animations (skip on first render)
        if (!firstTime) {
            tx.setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
        }

        fun show(tag: String) = fm.findFragmentByTag(tag)?.let { tx.show(it) }
        fun hide(tag: String) = fm.findFragmentByTag(tag)?.let { tx.hide(it) }

        when (tab) {
            Tab.HOME -> {
                show(tagHome); hide(tagWishlist); hide(tagCart); hide(tagProfile)
            }
            Tab.WISHLIST -> {
                hide(tagHome); show(tagWishlist); hide(tagCart); hide(tagProfile)
            }
            Tab.CART -> {
                hide(tagHome); hide(tagWishlist); show(tagCart); hide(tagProfile)
            }
            Tab.PROFILE -> {
                hide(tagHome); hide(tagWishlist); hide(tagCart); show(tagProfile)
            }
        }
        tx.commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_SELECTED_TAB, currentTab.name)
        super.onSaveInstanceState(outState)
    }

    private enum class Tab { HOME, WISHLIST, CART, PROFILE }
}

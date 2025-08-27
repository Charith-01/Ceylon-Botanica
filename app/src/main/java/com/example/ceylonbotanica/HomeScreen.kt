package com.example.ceylonbotanica

import android.content.Intent
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

        val bottomNav = findViewById<LinearLayout>(R.id.bottomNav)
        ViewCompat.setOnApplyWindowInsetsListener(bottomNav) { v, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(bottom = bars.bottom)
            insets
        }

        tabHome = findViewById(R.id.tabHome)
        tabWishlist = findViewById(R.id.tabWishlist)
        tabCart = findViewById(R.id.tabCart)
        tabProfile = findViewById(R.id.tabProfile)

        ivHome = findViewById(R.id.ivHome)
        ivWishlist = findViewById(R.id.ivWishlist)
        ivCart = findViewById(R.id.ivCart)
        ivProfile = findViewById(R.id.ivProfile)

        tvHome = findViewById(R.id.tvHome)
        tvWishlist = findViewById(R.id.tvWishlist)
        tvCart = findViewById(R.id.tvCart)
        tvProfile = findViewById(R.id.tvProfile)

        val labelColor = ContextCompat.getColor(this, android.R.color.white)
        listOf(tvHome, tvWishlist, tvCart, tvProfile).forEach { tv ->
            tv.visibility = View.VISIBLE
            tv.typeface = Typeface.DEFAULT_BOLD
            tv.setTextColor(labelColor)
        }

        ensureFragmentsAdded()

        val requestedByIntent = parseTabExtra(intent)
        currentTab = savedInstanceState?.getString(KEY_SELECTED_TAB)
            ?.let { runCatching { Tab.valueOf(it) }.getOrNull() }
            ?: requestedByIntent
                    ?: Tab.HOME

        setSelectedTab(currentTab, firstTime = true)

        tabHome.setOnClickListener { setSelectedTab(Tab.HOME) }
        tabWishlist.setOnClickListener { setSelectedTab(Tab.WISHLIST) }
        tabCart.setOnClickListener { setSelectedTab(Tab.CART) }
        tabProfile.setOnClickListener { setSelectedTab(Tab.PROFILE) }

        onBackPressedDispatcher.addCallback(this) {
            if (currentTab != Tab.HOME) {
                setSelectedTab(Tab.HOME)
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        parseTabExtra(intent)?.let { setSelectedTab(it) }
    }

    private fun parseTabExtra(intent: Intent?): Tab? {
        val name = intent?.getStringExtra("open_tab") ?: return null
        return runCatching { Tab.valueOf(name) }.getOrNull()
    }

    private fun ensureFragmentsAdded() {
        val fm = supportFragmentManager
        val tx = fm.beginTransaction().setReorderingAllowed(true)

        var home = fm.findFragmentByTag(tagHome)
        if (home == null) {
            home = HomeFragment()
            tx.add(R.id.contentContainer, home, tagHome)
        }

        var wishlist = fm.findFragmentByTag(tagWishlist)
        if (wishlist == null) {
            wishlist = WishlistFragment()
            tx.add(R.id.contentContainer, wishlist, tagWishlist)
        }
        tx.hide(wishlist!!)

        var cart = fm.findFragmentByTag(tagCart)
        if (cart == null) {
            cart = CartFragment()
            tx.add(R.id.contentContainer, cart, tagCart)
        }
        tx.hide(cart!!)

        var profile = fm.findFragmentByTag(tagProfile)
        if (profile == null) {
            profile = ProfileFragment()
            tx.add(R.id.contentContainer, profile, tagProfile)
        }
        tx.hide(profile!!)

        tx.commitNow()
    }

    private fun setSelectedTab(tab: Tab, firstTime: Boolean = false) {
        if (!firstTime && tab == currentTab) return
        currentTab = tab

        // Smooth, quick icon emphasis (120ms)
        fun style(icon: ImageView, selected: Boolean) {
            icon.isSelected = selected
            icon.animate()
                .scaleX(if (selected) 1.08f else 1.0f)
                .scaleY(if (selected) 1.08f else 1.0f)
                .setDuration(120)
                .start()
        }
        style(ivHome, tab == Tab.HOME)
        style(ivWishlist, tab == Tab.WISHLIST)
        style(ivCart, tab == Tab.CART)
        style(ivProfile, tab == Tab.PROFILE)

        val fm = supportFragmentManager
        val tx = fm.beginTransaction().setReorderingAllowed(true)

        // Fast fade-through animations
        if (!firstTime) {
            tx.setCustomAnimations(
                R.anim.fade_through_in,   // enter
                R.anim.fade_through_out,  // exit
                R.anim.fade_through_in,   // popEnter
                R.anim.fade_through_out   // popExit
            )
        }

        fun show(tag: String) = fm.findFragmentByTag(tag)?.let { tx.show(it) }
        fun hide(tag: String) = fm.findFragmentByTag(tag)?.let { tx.hide(it) }

        when (tab) {
            Tab.HOME -> { show(tagHome); hide(tagWishlist); hide(tagCart); hide(tagProfile) }
            Tab.WISHLIST -> { hide(tagHome); show(tagWishlist); hide(tagCart); hide(tagProfile) }
            Tab.CART -> { hide(tagHome); hide(tagWishlist); show(tagCart); hide(tagProfile) }
            Tab.PROFILE -> { hide(tagHome); hide(tagWishlist); hide(tagCart); show(tagProfile) }
        }
        tx.commit()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(KEY_SELECTED_TAB, currentTab.name)
        super.onSaveInstanceState(outState)
    }

    private enum class Tab { HOME, WISHLIST, CART, PROFILE }
}

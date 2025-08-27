package com.example.ceylonbotanica.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.example.ceylonbotanica.HerbalProductsScreen
import com.example.ceylonbotanica.HomeScreen
import com.example.ceylonbotanica.R

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = inflater.inflate(R.layout.activity_home_fragment, container, false)

        val scroll = v.findViewById<View>(R.id.scroll)
        ViewCompat.setOnApplyWindowInsetsListener(scroll) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = bars.top)
            insets
        }

        v.findViewById<View>(R.id.catHerbalTea)?.setOnClickListener {
            startActivity(Intent(requireContext(), HerbalProductsScreen::class.java))
            requireActivity().overridePendingTransition(
                R.anim.fade_through_in,
                R.anim.fade_through_out
            )
        }
        
        return v
    }
}

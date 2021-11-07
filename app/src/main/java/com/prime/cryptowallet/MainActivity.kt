package com.prime.cryptowallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.prime.cryptowallet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.featuredItemsViewPager.adapter =
            CreditCardItemAdapter(listOf("mtn", "glo", "airtel"))
        configureCreditCardItemsLayout(binding.featuredItemsViewPager)

        binding.rippleBackground.setOnClickListener {
            startActivity(Intent(this, NewCardActivity::class.java))
        }

        // TODO remove
        startActivity(Intent(this, NewCardActivity::class.java))
    }

    private fun configureCreditCardItemsLayout(featuredItemsViewPager: ViewPager2) {
        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.spacing_large)
        val offsetPx = 45//resources.getDimensionPixelOffset(R.dimen.spacing_large)
        with(featuredItemsViewPager) {
            offscreenPageLimit = 3
            setPageTransformer { page, position ->
                val viewPager = page.parent.parent as ViewPager2
                val offset = position * -(2 * offsetPx + pageMarginPx)
                page.translationX = if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL)
                    -offset else offset
            }
        }
    }
}
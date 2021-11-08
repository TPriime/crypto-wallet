package com.prime.cryptowallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.os.HandlerCompat.postDelayed
import com.prime.cryptowallet.databinding.ActivityCongratsBinding

class CongratsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCongratsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.animationView.apply {
            postDelayed({
                visibility = View.VISIBLE
                playAnimation()
            }, 200)
        }

        binding.backButton.setOnClickListener { finish() }
    }
}
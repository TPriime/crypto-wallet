package com.prime.cryptowallet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.prime.cryptowallet.databinding.ActivityNewCardBinding

import android.animation.Animator

import android.animation.AnimatorListenerAdapter

import android.view.animation.AccelerateDecelerateInterpolator

import android.view.animation.DecelerateInterpolator

import android.animation.ObjectAnimator




class NewCardActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewCardBinding
    lateinit var cardInputState: CardInputState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }

        cardInputState = CardInputState.values().first()
        checkState()
        configureUI()
    }

    override fun onSupportNavigateUp(): Boolean {
        return if(cardInputState == CardInputState.CardNumber) {
            finish()
            true
        } else {
            prevState()
            false
        }
    }

    override fun onBackPressed() {
//        super.onBackPressed()
        onSupportNavigateUp()
    }

    private fun configureUI() {
        binding.apply {
            cardNumberField.onTextChanged {
                cardNumber.setTextColor(resources.getColor(
                    if(it.isNullOrEmpty()) R.color.grey_light else R.color.white))
                cardNumber.setText(if(it.isNullOrEmpty()) "**** **** **** ****" else groupStrInFours(it))
                if(it?.length?:0 >= 16) nextState()
            }

            cardHolderField.onTextChanged {
                cardHolder.setTextColor(resources.getColor(
                    if(it.isNullOrEmpty()) R.color.grey_light else R.color.white))
                cardHolder.setText(if(it.isNullOrEmpty()) "CARDHOLDER NAME" else it.toString().toUpperCase())
                if(it?.length?:0 >= 17) nextState()
            }

            cardExpiryField.onTextChanged {
                cardExpiry.setTextColor(resources.getColor(
                    if(it.isNullOrEmpty()) R.color.grey_light else R.color.white))
                cardExpiry.setText(if(it.isNullOrEmpty()) "VALID TILL" else it)
                if(it?.length?:0 >= 5) nextState()
            }

            cardCvvField.onTextChanged {
//                cardCvv.setTextColor(resources.getColor(
//                    if(it.isNullOrEmpty()) R.color.grey_light else R.color.white))
//                cardCvv.setText(if(it.isNullOrEmpty()) "VALID TILL" else it)
//                if(it?.length?:0 >= 3) nextState()
            }
        }

    }


    private fun checkState() {
        binding.apply {
            cardNumberFrame.isVisible = cardInputState == CardInputState.CardNumber
            cardHolderFrame.isVisible = cardInputState == CardInputState.CardHolderName
            cardExpiryFrame.isVisible = cardInputState == CardInputState.Expiry
            cardCvvFrame.isVisible = cardInputState == CardInputState.CVV
        }
    }

    private fun nextState() {
        if(cardInputState==CardInputState.Expiry || true) flip()
        else if(cardInputState==CardInputState.values().last()) {
            // TODO next screen
            return
        }
        cardInputState = CardInputState.values()[cardInputState.ordinal + 1]
        checkState()
    }

    private fun prevState() {
        if(cardInputState==CardInputState.CardNumber) {
            // TODO prev screen
            return
        }
        cardInputState = CardInputState.values()[cardInputState.ordinal - 1]
        if(cardInputState==CardInputState.Expiry || true) reverseFlip()
        checkState()
    }

    private fun flip() {
        val oa1 = ObjectAnimator.ofFloat(binding.creditCard, "scaleX", 1f, 0f)
        val oa2 = ObjectAnimator.ofFloat(binding.creditCard, "scaleX", 0f, 1f)
        oa1.interpolator = DecelerateInterpolator()
        oa2.interpolator = AccelerateDecelerateInterpolator()
        oa1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                // imageView.setImageResource(R.drawable.frontSide)
                binding.cardFront.isVisible = false
                binding.cardBack.isVisible = true
                oa2.start()
            }
        })
        oa1.start()
    }

    private fun reverseFlip() {
        val oa1 = ObjectAnimator.ofFloat(binding.creditCard, "scaleX", 1f, 0f)
        val oa2 = ObjectAnimator.ofFloat(binding.creditCard, "scaleX", 0f, 1f)
        oa1.interpolator = DecelerateInterpolator()
        oa2.interpolator = AccelerateDecelerateInterpolator()
        oa1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                // imageView.setImageResource(R.drawable.frontSide)
                binding.cardFront.isVisible = true
                binding.cardBack.isVisible = false
                oa2.start()
            }
        })
        oa1.start()
    }

    private fun groupStrInFours(str: CharSequence): String {
        var newStr = ""
        str.forEachIndexed { index, c ->
            newStr += if(index%4==0 && index>0) "  $c" else c
        }
        return newStr
    }
}


fun EditText.onTextChanged(onTextChangeListener: (s: CharSequence?) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {}
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChangeListener(s)
        }
    })
}
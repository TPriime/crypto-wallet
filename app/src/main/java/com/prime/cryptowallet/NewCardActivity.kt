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
import android.content.Context
import android.content.Intent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isGone


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

        // TODO remove
//        startActivity(Intent(this, CongratsActivity::class.java))
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
                    if(it.isNullOrEmpty()) R.color.grey_light else R.color.white_80))
                cardNumber.setText(if(it.isNullOrEmpty()) "**** **** **** ****" else groupStrInFours(it))
//                if(it?.length?:0 >= 16) nextState()
            }

            cardHolderField.onTextChanged {
                cardHolder.editText?.setText(if(it.isNullOrEmpty()) "" else it.toString().toUpperCase())
//                if(it?.length?:0 >= 17) nextState()
            }

            cardExpiryField.onTextChanged {
                cardExpiry.editText?.setText(if(it.isNullOrEmpty()) "" else addSlash(it))
//                if(it?.length?:0 >= 5) nextState()
            }

            cardCvvField.onTextChanged {
                cardCvv.setText(if(it.isNullOrEmpty()) "CVV" else it)
//                if(it?.length?:0 >= 3) nextState()
            }
        }

        // set fields to respond to Enter key
        binding.apply {
            listOf(cardNumberField, cardHolderField, cardExpiryField, cardCvvField).forEach {
                it.setOnEditorActionListener { textView, i, keyEvent ->
                    onEnterKeyPressed(i)
                }
            }
        }

        // final frame config
        binding.apply {
            addCardButton.setOnClickListener {
                startActivity(Intent(this@NewCardActivity, CongratsActivity::class.java))
                finish()
            }
            cancelButton.setOnClickListener { finish() }
        }
    }

    private fun onEnterKeyPressed(actionId: Int): Boolean {
        return if (actionId == EditorInfo.IME_ACTION_DONE) {
            nextState()
            true
        } else false
    }


    private fun checkState() {
        binding.apply {
            cardNumberFrame.isVisible = cardInputState == CardInputState.CardNumber
            cardHolderFrame.isVisible = cardInputState == CardInputState.CardHolderName
            cardExpiryFrame.isVisible = cardInputState == CardInputState.Expiry
            cardCvvFrame.isVisible = cardInputState == CardInputState.CVV
            finalFrame.isVisible = cardInputState == CardInputState.Finish

            listOf(cardNumberFrame, cardHolderFrame, cardExpiryFrame, cardCvvFrame, finalFrame).forEach {
                if(it.isVisible) it.requestFocus()
            }

            if(cardInputState == CardInputState.values().last()) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
            }
        }
    }

    private fun nextState() {
        if(cardInputState==CardInputState.Expiry) flip()
        else if(cardInputState==CardInputState.CVV) reverseFlip()
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
        if(cardInputState==CardInputState.CVV) flip()
        if(cardInputState==CardInputState.Expiry) reverseFlip()
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

    private fun groupStrInFours(str: CharSequence, doubleSpace: Boolean = false): String {
        var newStr = ""
        var space = if(doubleSpace) "  " else " "
        str.forEachIndexed { index, c ->
            newStr += if(index%4==0 && index>0) "$space$c" else c
        }
        return newStr
    }

    private fun addSlash(str: CharSequence): String {
        var newStr = str
        if(str.length>2) {
            val chars1 = str.subSequence(0, 2)
            val chars2 = str.subSequence(2, str.length)
            newStr = "$chars1/$chars2"
        }
        return newStr.toString()
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
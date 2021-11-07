package com.prime.cryptowallet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.prime.cryptowallet.databinding.ActivityNewCardBinding

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
            }

            cardExpiryField.onTextChanged {
                cardExpiry.setTextColor(resources.getColor(
                    if(it.isNullOrEmpty()) R.color.grey_light else R.color.white))
                cardExpiry.setText(if(it.isNullOrEmpty()) "VALID TILL" else it)
                if(it?.length?:0 >= 5) nextState()
            }
        }

    }


    private fun checkState() {
        binding.apply {
            cardNumberField.isVisible = cardInputState == CardInputState.CardNumber
            cardHolderField.isVisible = cardInputState == CardInputState.CardHolderName
            cardExpiryField.isVisible = cardInputState == CardInputState.Expiry
//            cardCVVField.isVisible = cardInputState == CardInputState.CVV
        }
    }

    private fun nextState() {
        if(cardInputState==CardInputState.CVV) {
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
        checkState()
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
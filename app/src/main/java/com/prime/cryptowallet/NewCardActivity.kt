package com.prime.cryptowallet

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.prime.cryptowallet.databinding.ActivityNewCardBinding

class NewCardActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewCardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }

        configureUI()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun configureUI() {
        binding.apply {
            cardNumberField.onTextChanged {
                cardNumber.setTextColor(resources.getColor(
                    if(it.isNullOrEmpty()) R.color.grey_light else R.color.white))
                cardNumber.setText(if(it.isNullOrEmpty()) "**** **** **** ****" else groupStrInFours(it))
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
            }
        }

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
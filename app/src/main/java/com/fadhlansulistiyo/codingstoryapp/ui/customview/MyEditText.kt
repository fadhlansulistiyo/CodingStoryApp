package com.fadhlansulistiyo.codingstoryapp.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import com.fadhlansulistiyo.codingstoryapp.ui.util.Validator

class MyEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    private var validator: Validator? = null

    fun setValidator(validator: Validator) {
        this.validator = validator
    }

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Do Nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validator?.let {
                    if (!it.validate(s.toString())) {
                        setError(it.getErrorMessage(), null)
                    } else {
                        error = null
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // Do Nothing
            }
        })
    }
}
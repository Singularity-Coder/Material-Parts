package com.singularitycoder.materiallooksxml

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

object AppUtils {

    fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(editable: Editable?) = afterTextChanged.invoke(editable.toString())
        })
    }
}
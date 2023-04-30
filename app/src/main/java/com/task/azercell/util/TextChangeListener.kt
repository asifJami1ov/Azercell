package com.task.azercell

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.onTextChangeListener(onTextChanged: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Do nothing
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onTextChanged(s?.toString() ?: "")        }
    })
}
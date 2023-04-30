package com.task.azercell.util

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class PatternInputFilter(private val pattern: String="[^A-Za-z ]", private val maxLength: Int = 20) : InputFilter {


    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence? {
        val filteredSource = source.subSequence(start, end).replace(Regex(pattern), "")

        val newText = dest.subSequence(0, dstart).toString() + filteredSource + dest.subSequence(dend, dest.length)

        if (newText.length <= maxLength) {
            return filteredSource
        } else {
            val validLength = maxLength - (dest.length - (dend - dstart))
            return if (validLength > 0) {
                filteredSource.subSequence(0, validLength)
            } else {
                ""
            }
        }
    }
}
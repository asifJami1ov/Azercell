package com.task.azercell.util

import android.text.InputFilter
import android.text.Spanned

class DoubleInputFilter : InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence {
        val destText = dest.toString()
        val sourceText = source.subSequence(start, end).toString()
        val resultingText = destText.substring(0, dstart) + sourceText + destText.substring(dend)

        val decimalIndex = resultingText.indexOf('.')

        if (decimalIndex != -1) {
            val integerPartLength = decimalIndex
            val decimalPartLength = resultingText.length - decimalIndex - 1

            if (integerPartLength > 6 || decimalPartLength > 2) {
                return ""
            }
        } else if (resultingText.length > 6) {
            return ""
        }

        val filtered = sourceText.mapNotNull { c ->
            when (c) {
                in '0'..'9' -> c
                '.' -> if (!destText.contains('.')) c else null
                else -> '.'
            }
        }.joinToString("")

        if (resultingText.startsWith("00")) {
            return ""
        }

        if (dstart == 0 && filtered.startsWith(".") && !destText.startsWith("0.")) {
            return "0."
        }

        if (dstart == 0 && filtered.startsWith('0') && destText.isNotEmpty() && destText[0] != '.') {
            return filtered.substring(1)
        }

        return filtered
    }
}

package com.task.azercell.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import com.google.gson.Gson

object Helpers {
    @JvmField
    var gson = Gson()

    @JvmStatic
    fun hideSoftKeyboard(activity: Activity?) {
        try {
            activity?.let {
                val view = it.currentFocus ?: View(it)
                val inputMethodManager = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        } catch (ignored: Exception) {
        }
    }

    @JvmStatic
    fun EditText.showSoftKeyboard(activity: Activity?) {
        try {
            val inputMethodManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
        } catch (ignored: Exception) {
        }
    }



    @JvmStatic
    fun returnCurrency(currency: String): String = when (currency) {
        "AZN", "944" -> "₼"
        "USD", "840" -> "$"
        "EUR", "978" -> "€"
        "RUB", "643" -> "₽"
        "GBP", "0" -> "£"
        else -> "₼"
    }

fun Double.format()=String.format("%1$.2f", this)

    /*
     * Returns `obj` if `obj` is not null, and `defVal` if `obj` is null.
     * */
    @JvmStatic
    fun <T> defaultIfNull(obj: T?, defVal: T): T = (obj ?: defVal)



}
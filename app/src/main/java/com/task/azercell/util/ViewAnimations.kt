package com.task.azercell.util

import android.annotation.SuppressLint
import android.view.View
import android.widget.TextView
import kotlinx.coroutines.*

fun View.animateView(
    startAlpha: Float = 0.6f,
    startDuration: Long = 150,
    endDuration: Long = 350,
    lambda: () -> Unit
) {
    this.apply {
        animate().alpha(startAlpha).setDuration(startDuration).withEndAction {
            lambda()
            animate().alpha(1f).setDuration(endDuration).start()
        }.start()
    }
}

@SuppressLint("SetTextI18n")
fun TextView.animateText(text: String?, timeMillis: Long = 70,lambda: () -> Unit) {
    this.text = ""
    CoroutineScope(Dispatchers.Main).launch {
       var deferred= CoroutineScope(Dispatchers.Main).async {
        text?.forEach {
            this@animateText.text =
                "${this@animateText.text}$it"
            delay(timeMillis)
        }
        }
        deferred.await()
        lambda()
    }
}

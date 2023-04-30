package com.task.azercell.util

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.task.azercell.R
import com.task.azercell.model.HistoryModel


@BindingAdapter("app:image")
fun setImage(view: ImageView, drawable: Int) {
    view.setImageResource(drawable)
}

@BindingAdapter("app:highlight")
fun setHighlight(view: TextView, historyModel: HistoryModel) {
    view.apply {
        text = historyModel.amount
        if (historyModel.amount.startsWith('-')) {
            setTextColor(
                ContextCompat.getColor(
                    this.context,
                    R.color.light_red
                )
            )
            val myCustomFont = ResourcesCompat.getFont(this.context, R.font.proximanova_medium)
            typeface = myCustomFont
            textSize = 17f
        } else if (historyModel.amount.startsWith('+')) {
            setTextColor(
                ContextCompat.getColor(
                    this.context,
                    R.color.blue
                )
            )
            val myCustomFont = ResourcesCompat.getFont(this.context, R.font.proximanova_medium)
            typeface = myCustomFont
            textSize = 17f
        } else {
            setTextColor(
                ContextCompat.getColor(
                    this.context,
                    R.color.white_500
                )
            )
            val myCustomFont = ResourcesCompat.getFont(this.context, R.font.proximanova_regular)
            typeface = myCustomFont
            textSize = 16f
        }
    }
}


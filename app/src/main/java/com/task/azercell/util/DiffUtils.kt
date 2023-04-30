package com.task.azercell.util

import androidx.recyclerview.widget.DiffUtil
import com.task.azercell.model.CardModel
import com.task.azercell.model.Currency
import com.task.azercell.model.HistoryModel

class CardDiffCallback : DiffUtil.ItemCallback<CardModel>() {
    override fun areItemsTheSame(oldItem: CardModel, newItem: CardModel) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: CardModel, newItem: CardModel) =
        oldItem == newItem
}


class TextDiffCallback : DiffUtil.ItemCallback<Currency>() {
    override fun areItemsTheSame(oldItem: Currency, newItem: Currency) =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: Currency, newItem: Currency) =
        oldItem == newItem
}

class HistoryCallback : DiffUtil.ItemCallback<HistoryModel>() {
    override fun areItemsTheSame(oldItem: HistoryModel, newItem: HistoryModel) =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: HistoryModel, newItem: HistoryModel) =
        oldItem == newItem
}
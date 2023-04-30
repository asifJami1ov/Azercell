package com.task.azercell.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.islamkhsh.CardSliderAdapter
import com.task.azercell.R
import com.task.azercell.model.CardModel

class CardAdapter(
    private val cards: List<CardModel>,
    private val clickListener: OnSliderItemClickListener
) :
    CardSliderAdapter<CardAdapter.CardViewHolder>() {

    interface OnSliderItemClickListener {
        fun onSliderItemClick(id: String)
    }

    override fun bindVH(holder: CardViewHolder, position: Int) {
        val card = cards[position]
        holder.itemView.apply {
            findViewById<ImageView>(R.id.cardImage).setImageResource(card.cardColor.drawableResId)
            findViewById<ImageView>(R.id.cardType).setImageResource(card.cardType.drawableResId)
            findViewById<TextView>(R.id.cardNumber).text = card.getHiddenCardNumber()
            findViewById<TextView>(R.id.expireDate).text = card.cardExpireDate
            findViewById<TextView>(R.id.amount).text = card.getAmountWithCurrencyName()
            findViewById<TextView>(R.id.productType).text = card.productType.productName
        }
        holder.itemView.setOnClickListener {
            clickListener.onSliderItemClick(card.id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun getItemCount() = cards.size


    class CardViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
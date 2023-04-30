package com.task.azercell.data

import com.task.azercell.model.CardModel
import com.task.azercell.model.ClientModel
import com.task.azercell.util.CardException

interface PreferencesHelperFacade {
    fun addCard(card: CardModel)

    @Throws(CardException::class)
    fun deleteCard(id: String)
    fun getCards(): List<CardModel>
    fun updateCard(card:CardModel)

    fun setClient(clientModel: ClientModel)
    fun getClient(): ClientModel?

}
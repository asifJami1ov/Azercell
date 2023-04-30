package com.task.azercell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.azercell.model.CardModel
import com.task.azercell.data.PreferencesHelperFacade
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNewCardViewModel @Inject constructor(
    private val shared: PreferencesHelperFacade
) : ViewModel() {

    fun addCard(card: CardModel) {
        viewModelScope.launch {
            val client = shared.getClient()
            if (client != null)
                card.cardHolderName = client.getClientFullName()
            shared.addCard(card)
        }
    }

}
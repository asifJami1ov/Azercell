package com.task.azercell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.azercell.data.PreferencesHelperFacade
import com.task.azercell.model.*
import com.task.azercell.model.Currency
import com.task.azercell.util.Helpers
import com.task.azercell.util.Helpers.format
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TransferViewModel
@Inject constructor(private val shared: PreferencesHelperFacade) :
    ViewModel() {


    private val _cardList = MutableStateFlow<List<CardModel>?>(null)
    val cardList = _cardList.asStateFlow()

    private val _client = MutableStateFlow<ClientModel?>(null)
    val client = _client.asStateFlow()

    init {
        loadCardList()
        loadClientData()
    }

    private fun loadCardList() {
        viewModelScope.launch {
            val cardList = shared.getCards()
            _cardList.value = cardList
        }
    }

    private fun loadClientData() {
        viewModelScope.launch {
            val client = shared.getClient()
            _client.value = client
        }
    }

    fun pay(payFrom: CardModel, enrolTo: CardModel, amount: Double, currency: Currency) {
        val date = SimpleDateFormat("dd.yyyy", Locale.getDefault()).format(Date())

        mockHistoryData.add(
            0, HistoryModel(
                "- ${Helpers.returnCurrency(currency.toString())} ${amount.format()}",
                "${payFrom.cardName} to ${enrolTo.cardName}", date, HistoryDrawable.TRANSFER
            )
        )
    }


}
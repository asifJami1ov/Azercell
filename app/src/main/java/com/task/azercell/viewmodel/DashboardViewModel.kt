package com.task.azercell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.azercell.model.CardModel
import com.task.azercell.data.PreferencesHelperFacade
import com.task.azercell.model.ClientModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel
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

     fun loadCardList() {
         _cardList.value= listOf<CardModel>()
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


}
package com.task.azercell.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.azercell.data.PreferencesHelperFacade
import com.task.azercell.generateCard
import com.task.azercell.model.ClientModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val shared: PreferencesHelperFacade
) : ViewModel() {

    fun authenticateClient(name: String, surname: String, phoneNumber: String, birthday: String) {
        viewModelScope.launch {
            val client = ClientModel(
                name = name,
                surname = surname,
                phoneNumber = phoneNumber,
                birthday = birthday,
            )
            shared.setClient(client)
            addCardToClient(client.getClientFullName())
        }
    }

    private fun addCardToClient(fullName: String) {
        shared.addCard(generateCard(fullName))
        shared.addCard(generateCard(fullName))
    }

}
package com.task.azercell.model

@kotlinx.serialization.Serializable
data class ClientModel(
    val name: String,
    val surname: String,
    val phoneNumber: String,
    val birthday: String,
) {
    fun getClientFullName(): String {
        return "$name $surname"
    }
}

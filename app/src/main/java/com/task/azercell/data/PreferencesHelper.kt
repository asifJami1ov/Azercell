package com.task.azercell.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.task.azercell.model.CardModel
import com.task.azercell.model.ClientModel
import com.task.azercell.util.CardException
import com.task.azercell.util.Helpers
import com.task.azercell.util.fromJson
import com.task.azercell.util.json
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject


class PreferencesHelper @Inject constructor(
    @ApplicationContext private val context: Context
) : PreferencesHelperFacade {

    companion object {
        const val CARDS = "cards"
        const val CLIENT = "client"
    }

    private var mPrefs: SharedPreferences

    init {
        mPrefs = EncryptedSharedPreferences.create(
            context,
            "secret_credentials",
            getMasterKey(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }


    private fun getMasterKey(): MasterKey {
        return MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

    }

    override fun addCard(card: CardModel) {
        val cardListJson = mPrefs.getString(CARDS, "")
        val cardList = Helpers.defaultIfNull(
            cardListJson?.fromJson<ArrayList<CardModel>>(),
            mutableListOf()
        )
        cardList.add(0, card)
        mPrefs.edit().remove(CARDS).apply()
        mPrefs.edit().putString(CARDS, cardList.json()).apply()
    }

    @Throws(CardException::class)
    override fun deleteCard(id: String) {
        val cardListJson = mPrefs.getString(CARDS, "")
        val cardList = Helpers.defaultIfNull(
            cardListJson?.fromJson<ArrayList<CardModel>>(),
            mutableListOf()
        )
        if (cardList.size > 2)
            cardList.remove(cardList.find { it.id == id })
        else throw CardException("Card can't be deleted \n Try to add Card")

        mPrefs.edit().remove(CARDS).apply()
        mPrefs.edit().putString(CARDS, cardList.json()).apply()
    }

    override fun getCards(): List<CardModel> {
        val cardListJson = mPrefs.getString(CARDS, "")
        return Helpers.defaultIfNull(
            cardListJson?.fromJson<ArrayList<CardModel>>(),
            ArrayList()
        )
    }

    override fun updateCard(card: CardModel) {
        val cardListJson = mPrefs.getString(CARDS, "")
        val cardList = Helpers.defaultIfNull(
            cardListJson?.fromJson<ArrayList<CardModel>>(),
            mutableListOf()
        )
        cardList.map {
            if (it.id == card.id) {
                it.cardName = card.cardName
                it.cardColor = card.cardColor
            }
        }
        mPrefs.edit().remove(CARDS).apply()
        mPrefs.edit().putString(CARDS, cardList.json()).apply()
    }

    override fun setClient(clientModel: ClientModel) {

        mPrefs.edit().remove(CLIENT).apply()
        mPrefs.edit().putString(CLIENT, clientModel.json()).apply()

    }

    override fun getClient(): ClientModel? {
        val clientJson = mPrefs.getString(CLIENT, "")
        return Helpers.defaultIfNull(
            clientJson?.fromJson(),
            null
        )
    }


}
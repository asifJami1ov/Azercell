package com.task.azercell

import com.task.azercell.model.*
import com.task.azercell.model.Currency
import java.util.*

fun generateCard(fullName: String): CardModel {
    val cardType = CardType.values().toList().shuffled().first()
    val productType = ProductType.values().toList().shuffled().first()
    val cardName = "$cardType ${listOf("Debit", "Credit").shuffled().first()}"
    val currency = Currency.values().toList().shuffled().first()
    val cardColor = CardColor.values().toList().shuffled().first()


    return generateCard(
        cardType,
        productType,
        cardName,
        fullName,
        currency,
        cardColor
    )
}

fun generateCard(): CardModel {
    return generateCard(
        CardType.VISA,
        ProductType.PLATINUM,
        "VISA Debit",
        "",
        Currency.AZN,
        CardColor.BLUE
    )
}

fun generateCard(
    cardType: CardType,
    productType: ProductType,
    cardName: String,
    cardHolderName: String,
    currency: Currency,
    cardColor: CardColor
): CardModel {
    return CardModel(
        id = UUID.randomUUID().toString(),
        cardType = cardType,
        productType = productType,
        cardName = cardName,
        cardHolderName = cardHolderName,
        cardAmount = 10.00,
        cardNumber = getRandomNumber(16,true),
        cardExpireDate = "10/30",
        cardCVV = getRandomNumber(3),
        currency = currency,
        cardColor = cardColor
    )
}

fun getRandomNumber(size: Int, gap: Boolean = false): String {
    val sb = StringBuilder()
    for (i in 1..size) {
        val digit: Int = (0..9).random()
        sb.append(digit)
        if (gap && i % 4 == 0) sb.append(" ")
    }
    return sb.toString()
}

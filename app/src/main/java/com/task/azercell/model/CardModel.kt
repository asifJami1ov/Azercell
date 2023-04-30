package com.task.azercell.model

import com.task.azercell.R
import com.task.azercell.util.Helpers
import com.task.azercell.util.Helpers.format


@kotlinx.serialization.Serializable
enum class CardType(val drawableResId: Int, val drawableResId12: Int) {
    MASTERCARD(R.drawable.ic_mastercard, R.drawable.ic_mastercard_12),
    VISA(R.drawable.ic_visa, R.drawable.ic_visa_12)
}

@kotlinx.serialization.Serializable
enum class Currency {
    AZN,
    USD,
    EUR
}

enum class CardPurpose {
    PAY_FROM,
    ENROLL_TO
}

@kotlinx.serialization.Serializable
enum class ProductType(val productName: String) {
    PLATINUM("Platinum"),
    GOLD("Gold")
}

@kotlinx.serialization.Serializable
enum class CardColor(val drawableResId: Int) {
    BLUE(R.drawable.ic_card_blue),
    YELLOW(R.drawable.ic_card_yellow),
    PURPLE(R.drawable.ic_card_purple)
}

enum class NumberLength {
    LONG,
    SHORT
}

@kotlinx.serialization.Serializable
data class CardModel(
    var id: String,
    var cardType: CardType,
    var productType: ProductType,
    var cardName: String,
    var cardHolderName: String,
    var cardAmount: Double,
    var cardNumber: String,
    var cardExpireDate: String,
    var cardCVV: String,
    var currency: Currency,
    var cardColor: CardColor
) {
    fun getHiddenCardNumber(length: NumberLength = NumberLength.SHORT): String =
        if (length == NumberLength.SHORT)
            "****${cardNumber.substring(cardNumber.lastIndex - 4, cardNumber.lastIndex)}"
        else "${
            cardNumber.substring(
                0,
                2
            )
        }** **** ****${cardNumber.substring(cardNumber.lastIndex - 4, cardNumber.lastIndex)}"

    fun getHiddenCvv()="***"

    fun getAmountWithCurrencyName(): String = "${cardAmount.format()} $currency"

    fun getAmountWithCurrencyChar(): String =
        "${cardAmount.format()} ${Helpers.returnCurrency(currency.toString())}"
}



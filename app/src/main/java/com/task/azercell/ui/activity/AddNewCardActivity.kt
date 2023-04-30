package com.task.azercell.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.task.azercell.databinding.ActivityAddNewCardBinding
import com.task.azercell.generateCard
import com.task.azercell.getRandomNumber
import com.task.azercell.model.*
import com.task.azercell.onTextChangeListener
import com.task.azercell.util.animateText
import com.task.azercell.util.animateView
import com.task.azercell.viewmodel.AddNewCardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddNewCardActivity : AppCompatActivity() {

    private val viewModel: AddNewCardViewModel by viewModels()

    private val binding by lazy {
        ActivityAddNewCardBinding.inflate(layoutInflater)
    }

    private var card: CardModel = generateCard()

    private var animationJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        setViews()
        animateCardItems()
        setClickEvents()
    }


    private fun setViews() {
        binding.apply {
            blue.isChecked = true
            azn.isChecked = true
            platinum.isChecked = true
            cardName.text = card.cardName


            colorRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    blue.id -> {
                        card.cardColor = CardColor.BLUE
                    }
                    purple.id -> {
                        card.cardColor = CardColor.PURPLE
                    }
                    yellow.id -> {
                        card.cardColor = CardColor.YELLOW
                    }
                }
                cardIcon.cardImage.animateView {
                    cardIcon.cardImage.setImageResource(card.cardColor.drawableResId)
                }
            }
            cardTypeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    visa.id -> {
                        card.cardType = CardType.VISA
                    }
                    mastercard.id -> {
                        card.cardType = CardType.MASTERCARD
                    }
                }
                cardIcon.cardImage.animateView {
                    cardIcon.cardType.setImageResource(card.cardType.drawableResId)
                }
            }
            currencyRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    azn.id -> {
                        card.currency = Currency.AZN
                    }
                    usd.id -> {
                        card.currency = Currency.USD
                    }
                    eur.id -> {
                        card.currency = Currency.EUR
                    }
                }
                cardIcon.amount.text = card.getAmountWithCurrencyName()
            }
            productTypeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    platinum.id -> {
                        card.productType = ProductType.PLATINUM
                    }
                    gold.id -> {
                        card.productType = ProductType.GOLD
                    }
                }
                 cardIcon.productType.animateText(card.productType.productName){}
            }
            cardNameField.onTextChangeListener {
                card.cardName = it
                cardName.text = it
            }

            cardNameField.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus && cardNameField.text.isEmpty()) {
                    cardNameField.setText(card.cardName)
                    cardNameField.setSelection(cardNameField.text.length)
                }
            }


            visa.isChecked = true
        }
    }

    private fun setClickEvents() {
        binding.apply {
            btnBack.setOnClickListener { finish() }
            btnAddCard.setOnClickListener {
                viewModel.addCard(card)
                startActivity(Intent(this@AddNewCardActivity, DashboardActivity::class.java))
            }
        }
    }

    private fun animateCardItems() {
        if (animationJob == null)
            animationJob = lifecycleScope.launch {
                binding.cardIcon.apply {
                    productType.animateText(
                        card.productType.productName,
                        60
                    ){}
                    cardNumber.animateText(
                        getRandomNumber(16, true),
                        40
                    ){}
                    amount.animateText(
                        card.getAmountWithCurrencyName(),
                        40
                    ){}
                    expireDate.animateText("10/30", 100){}
                    animationJob = null
                }
            }
    }


}
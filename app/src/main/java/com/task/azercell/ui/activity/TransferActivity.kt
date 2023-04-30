package com.task.azercell.ui.activity

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.task.azercell.CARD_ID
import com.task.azercell.HEADER
import com.task.azercell.R
import com.task.azercell.adapter.CustomListAdapter
import com.task.azercell.databinding.ActivityTransferBinding
import com.task.azercell.databinding.ItemCardSmallBinding
import com.task.azercell.databinding.ItemCardSmallWithHolderBinding
import com.task.azercell.databinding.ItemSingleTextBinding
import com.task.azercell.model.CardModel
import com.task.azercell.model.Currency
import com.task.azercell.ui.CardPurpose
import com.task.azercell.ui.bottomsheet.CustomSelectBottomSheetFragment
import com.task.azercell.util.*
import com.task.azercell.util.Helpers.format
import com.task.azercell.viewmodel.TransferViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TransferActivity : AppCompatActivity() {

    private val viewModel: TransferViewModel by viewModels()
    private lateinit var cardList: List<CardModel>
    private val currencyList: List<Currency> = Currency.values().toList()

    var currency: Currency? = null

    private var payFrom: CardModel? = null
    private var enrolTo: CardModel? = null

    private val binding by lazy {
        ActivityTransferBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        getData()
        setClickEvents()
        binding.transferAmount.inputType =
            InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
        binding.transferAmount.filters = arrayOf(DoubleInputFilter())
    }

    private fun getData() {

        val selectedCardId = intent.getStringExtra(CARD_ID)
        if (selectedCardId == null || selectedCardId.isEmpty()) return

        lifecycleScope.launch {
            viewModel.cardList.collect { cards ->
                if (cards != null)
                    cardList = cards
                payFrom = cards?.find { it.id == selectedCardId }
                binding.payFromCard.item = payFrom
            }
        }
    }


    private fun setClickEvents() {
        binding.apply {
            btnBack.setOnClickListener { finish() }
            payFromCard.root.setOnClickListener {
                binding.payFromCard.setCardBottomSheet(CardPurpose.PAY_FROM)
            }

            enrolToCard.root.setOnClickListener {
                binding.enrolToCard.setCardBottomSheet(CardPurpose.ENROLL_TO)
            }
            currencyCard.setOnClickListener {
                currencyText.setCurrencyBottomSheet()
            }


            swap.setOnClickListener {
                swapCards()
            }

            amount10.setOnClickListener {
                addAmount(10.00)
            }
            amount20.setOnClickListener {
                addAmount(20.00)
            }
            amount30.setOnClickListener {
                addAmount(30.00)
            }

            btnPayent.setOnClickListener { setPaymentProcess() }

        }
    }

    private fun setPaymentProcess() {
        binding.apply {
            if (payFrom == null || enrolTo == null) {
                Toast.makeText(this@TransferActivity, "Please set your cards !", Toast.LENGTH_SHORT)
                    .show()
                setCommissionVisibility(false)
                return
            }
            if (transferAmount.text.isEmpty() || transferAmount.text.toString()
                    .toDoubleOrNull() == null
            ) {
                Toast.makeText(this@TransferActivity, "Please set amount !", Toast.LENGTH_SHORT)
                    .show()
                setCommissionVisibility(false)
                return
            } else if (transferAmount.text.toString().toDouble() > payFrom!!.cardAmount) {
                Toast.makeText(this@TransferActivity, "Not enough amount !", Toast.LENGTH_SHORT)
                    .show()
                setCommissionVisibility(false)
                return
            }
            if (currency == null) {
                Toast.makeText(this@TransferActivity, "Please set currency !", Toast.LENGTH_SHORT)
                    .show()
                setCommissionVisibility(false)
                return
            }

            progressCircular.visibility = View.VISIBLE
            btnPayent.visibility = View.GONE
            lifecycleScope.launch {
                delay(1000)
                if (btnPayent.text == getString(R.string.continue_text)) {
                    setCommissionVisibility(true)
                } else {
                    viewModel.pay(
                        payFrom!!,
                        enrolTo!!,
                        transferAmount.text.toString().toDouble(),
                        currency!!
                    )
                    Toast.makeText(this@TransferActivity, "Amount successfully transferred", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
            }
        }
    }

    private fun setCommissionVisibility(isConfirmPayment: Boolean) {
        binding.apply {
            if (!isConfirmPayment) {
                progressCircular.visibility = View.GONE
                btnPayent.visibility = View.VISIBLE
                binding.commissionCard.visibility = View.GONE
                binding.btnPayent.text = getString(R.string.continue_text)
            } else {
                progressCircular.visibility = View.GONE
                btnPayent.visibility = View.VISIBLE
                binding.btnPayent.text = getString(R.string.confirm)
                binding.commissionCard.visibility = View.VISIBLE
            }
        }
    }

    private fun addAmount(value: Double) {
        binding.apply {
            val amountText = transferAmount.text.toString()
            if (amountText.isEmpty())
                transferAmount.animateText(value.format(), 20) {}
            else
                transferAmount.animateText((amountText.toDouble() + value).format()) {}
        }
    }

    private fun swapCards() {
        val payFromTemp = payFrom
        val enrolToTemp = enrolTo
        payFrom = enrolToTemp
        enrolTo = payFromTemp

        binding.apply {
            payFromCard.card.cardItem.animateView {
                payFromCard.item = enrolToTemp
            }

            enrolToCard.card.cardItem.animateView {
                this@TransferActivity.apply {
                    enrolToCard.item = payFromTemp
                }
            }
        }

        setCommissionVisibility(false)

    }

    private fun ItemCardSmallWithHolderBinding.setCardBottomSheet(cardPurpose: CardPurpose) {
        val bundle = Bundle()
        bundle.putString(HEADER, getString(R.string.cards))

        var cardBottomSheet: CustomSelectBottomSheetFragment<CardModel, ItemCardSmallBinding>? =
            null
        val customListAdapter = CustomListAdapter<CardModel, ItemCardSmallBinding>(
            CardDiffCallback(),
            R.layout.item_card_small
        )
        {

            if (cardPurpose == CardPurpose.PAY_FROM) {

                if (payFrom != it) setCommissionVisibility(false)
                payFrom = it
                if (payFrom?.id == enrolTo?.id) {
                    enrolTo = null
                    binding.enrolToCard.item = null
                }
            } else {
                if (enrolTo != it) setCommissionVisibility(false)
                setCommissionVisibility(false)
                enrolTo = it
                if (payFrom?.id == enrolTo?.id) {
                    payFrom = null
                    binding.payFromCard.item = null
                }
            }
            this.item = it


            cardBottomSheet?.dismissNow()
        }
        cardBottomSheet = CustomSelectBottomSheetFragment(customListAdapter)
        cardBottomSheet.arguments = bundle
        customListAdapter.submitList(cardList)
        cardBottomSheet.show(supportFragmentManager, "SelectTypeBottomSheet")
    }

    private fun TextView.setCurrencyBottomSheet() {
        val bundle = Bundle()
        bundle.putString(HEADER, getString(R.string.currencies))

        var currencyBottomSheet: CustomSelectBottomSheetFragment<Currency, ItemSingleTextBinding>? =
            null
        val customListAdapter = CustomListAdapter<Currency, ItemSingleTextBinding>(
            TextDiffCallback(),
            R.layout.item_single_text
        )
        {
            if (it != currency)
                setCommissionVisibility(false)

            this.text = it.toString()
            currency = it
            currencyBottomSheet?.dismissNow()
        }
        currencyBottomSheet = CustomSelectBottomSheetFragment(customListAdapter)
        currencyBottomSheet.arguments = bundle
        customListAdapter.submitList(currencyList)
        currencyBottomSheet.show(supportFragmentManager, "SelectTypeBottomSheet")
    }
}
package com.task.azercell.ui.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import az.expressbank.e24.dialogs.CustomDialog
import com.google.android.material.snackbar.Snackbar
import com.task.azercell.CARD_ID
import com.task.azercell.R
import com.task.azercell.databinding.FragmentCardDetailsBinding
import com.task.azercell.model.CardColor
import com.task.azercell.model.CardModel
import com.task.azercell.model.NumberLength
import com.task.azercell.onTextChangeListener
import com.task.azercell.util.Helpers.hideSoftKeyboard
import com.task.azercell.util.Helpers.showSoftKeyboard
import com.task.azercell.util.animateText
import com.task.azercell.util.animateView
import com.task.azercell.util.removeFragment
import com.task.azercell.viewmodel.CardDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CardDetailsFragment(private val listener:OnFragmentRemoved) : Fragment() {

    private val viewModel: CardDetailsViewModel by viewModels()

    interface OnFragmentRemoved{
       fun onFragmentRemoved()
    }

    private val binding by lazy {
        FragmentCardDetailsBinding.inflate(layoutInflater)
    }
    private var animationJob: Job? = null

    private lateinit var card: CardModel
    private lateinit var cardName: String
    private var cardColor: Int = 0

    private var isCardNumberShown = false
    private var isCvvShown = false
    private var isNameEditable = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        setClickEvents()
    }

    private fun setClickEvents() {
        binding.apply {

            btnBack.setOnClickListener { activity?.supportFragmentManager?.removeFragment(this@CardDetailsFragment) }

            btnDelete.setOnClickListener {
                showWarningDialog()
            }

            btnSave.setOnClickListener {
                saveCard()
            }

            btnCopyNumber.setOnClickListener {
                btnCopyNumber.animateView {}
                copyToClipboard(card.cardNumber)
                showSnackBar(
                    activity, "Card number copied to clipboard !", R.drawable.ic_copy
                )
            }
            btnShowNumber.setOnClickListener {
                if (!isCardNumberShown) {
                    btnShowNumber.animateView { btnShowNumber.setImageResource(R.drawable.ic_hide) }
                    cardNNumberField.text = card.cardNumber
                    isCardNumberShown = true
                } else {
                    btnShowNumber.animateView { btnShowNumber.setImageResource(R.drawable.ic_show) }
                    cardNNumberField.text = card.getHiddenCardNumber(NumberLength.LONG)
                    isCardNumberShown = false
                }
            }

            btnCopyCvv.setOnClickListener {
                btnCopyCvv.animateView {}
                copyToClipboard(card.cardCVV)
                showSnackBar(
                    activity, "Card CVV copied to clipboard !", R.drawable.ic_copy
                )
            }
            btnShowCvv.setOnClickListener {
                if (!isCvvShown) {
                    btnShowCvv.animateView { btnShowCvv.setImageResource(R.drawable.ic_hide) }
                    cardCvvField.text = card.cardCVV
                    isCvvShown = true
                } else {
                    btnShowCvv.animateView { btnShowCvv.setImageResource(R.drawable.ic_show) }
                    cardCvvField.text = card.getHiddenCvv()
                    isCvvShown = false
                }
            }

            btnEditName.setOnClickListener {
                if (!isNameEditable) {
                    btnEditName.animateView { btnEditName.setImageResource(R.drawable.ic_edit_off) }
                    cardNameField.isEnabled = true
                    cardNameField.requestFocusFromTouch()
                    cardNameField.showSoftKeyboard(activity)
                    cardNameField.setSelection(cardNameField.text.length)
                    isNameEditable = true
                } else {
                    btnEditName.animateView { btnEditName.setImageResource(R.drawable.ic_edit) }
                    cardNameField.isEnabled = false
                    cardNameField.clearFocus()
                    hideSoftKeyboard(activity)
                    isNameEditable = false
                }
            }
        }
    }

    private fun showWarningDialog() {
        Handler(Looper.getMainLooper()).post {
            val dialog = CustomDialog(requireContext(),
                "Card Deletion",
                "Are you sure to delete this card ?",
                { _, _ -> },
                null,
                null,
                null,
                {
                    deleteCard()
                },
                {},
                {})
            dialog.show()
        }
    }

    private fun saveCard() {
        viewModel.updateCard(card)
        activity?.supportFragmentManager?.removeFragment(this@CardDetailsFragment)
    }

    @SuppressLint("RestrictedApi")
    fun showSnackBar(activity: Activity?, message: String, iconResId: Int) {
        val contentView = activity?.findViewById<View>(android.R.id.content) as ViewGroup
        val snackBar = Snackbar.make(contentView, "", Snackbar.LENGTH_LONG)

        val customLayout = LinearLayout(activity)
        customLayout.orientation = LinearLayout.HORIZONTAL
        customLayout.setPadding(30, 30, 30, 30)
        customLayout.setBackgroundColor(
            ContextCompat.getColor(
                activity, R.color.color_secondary
            )
        )
        val icon = ImageView(activity)
        icon.setImageResource(iconResId)
        customLayout.addView(icon)

        val textView = TextView(activity)
        textView.text = message
        textView.gravity = Gravity.CENTER_VERTICAL
        textView.setTextColor(Color.WHITE)
        textView.setPadding(35, 12, 0, 0)
        customLayout.addView(textView)

        val snackbarLayout = snackBar.view as Snackbar.SnackbarLayout
        snackbarLayout.setPadding(0, 0, 0, 0)
        snackbarLayout.addView(customLayout, 0)

        val layoutParams = snackbarLayout.layoutParams as FrameLayout.LayoutParams
        layoutParams.gravity = Gravity.TOP
        snackbarLayout.layoutParams = layoutParams
        snackBar.show()
    }


    private fun copyToClipboard(textToCopy: String) {
        val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        val clip = ClipData.newPlainText("label", textToCopy)

        clipboard.setPrimaryClip(clip)

    }

    private fun getData() {
        val bundle = arguments ?: return
        val cardId = bundle.getString(CARD_ID)
        lifecycleScope.launch {
            viewModel.cardList.collect { cards ->
                if (cards == null) return@collect
                card = cards.find { it.id == cardId }!!
                cardName = card.cardName
                cardColor = card.cardColor.drawableResId
                animateItems()
                setViews()
            }
        }
    }

    private fun deleteCard() {
        if (viewModel.deleteCard(card.id)) {
            Toast.makeText(requireContext(), "Card Deleted Succesfully !", Toast.LENGTH_SHORT)
                .show()
            activity?.supportFragmentManager?.removeFragment(this@CardDetailsFragment)
        } else {
            Toast.makeText(
                requireContext(),
                "Card can't be deleted \n More than 2 cards required !",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun setViews() {
        binding.apply {
            cardNameField.inputType = InputType.TYPE_CLASS_TEXT
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
                setSaveBtnVisibility()
                cardIcon.cardImage.animateView {
                    cardIcon.cardImage.setImageResource(card.cardColor.drawableResId)
                }
            }

        }
    }

    private fun setSaveBtnVisibility() {
        binding.apply {
            if (card.cardColor.drawableResId != cardColor || card.cardName != cardName) btnSave.visibility =
                View.VISIBLE
            else btnSave.visibility = View.GONE
        }
    }

    private fun animateItems() {
        if (animationJob == null) animationJob = lifecycleScope.async {
            binding.apply {

                cardIcon.cardImage.setImageResource(card.cardColor.drawableResId)
                cardIcon.cardType.setImageResource(card.cardType.drawableResId)
                when (card.cardColor) {
                    CardColor.BLUE -> blue.isChecked = true
                    CardColor.PURPLE -> purple.isChecked = true
                    CardColor.YELLOW -> yellow.isChecked = true
                }

                cardNameField.animateText(card.cardName, 10) {
                    cardNameField.onTextChangeListener {
                        card.cardName = it
                        setSaveBtnVisibility()
                    }
                }
                cardNNumberField.animateText(card.getHiddenCardNumber(NumberLength.LONG), 10) {}
                cardCvvField.animateText(card.getHiddenCvv(), 10) {}
                cardExpireDateField.animateText(card.cardExpireDate, 10) {}
                cardHolderNameField.animateText(card.cardHolderName, 10) {}
                cardProductTypeField.animateText(card.productType.productName, 10) {}
                cardCardTypeField.animateText(card.cardType.name, 10) {}

                cardIcon.productType.animateText(
                    card.productType.productName, 60
                ) {}
                cardIcon.cardNumber.animateText(
                    card.getHiddenCardNumber(), 40
                ) {}
                cardIcon.amount.animateText(
                    card.getAmountWithCurrencyName(), 40
                ) {}
                cardIcon.expireDate.animateText(card.cardExpireDate, 100) {}
                animationJob = null
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        Log.i("aaa", "onDetach: ")
        listener.onFragmentRemoved()
    }


}

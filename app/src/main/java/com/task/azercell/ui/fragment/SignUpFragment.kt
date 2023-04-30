package com.task.azercell.ui.fragment

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.task.azercell.R
import com.task.azercell.birthPattern
import com.task.azercell.databinding.FragmentSignUpBinding
import com.task.azercell.onTextChangeListener
import com.task.azercell.phonePattern
import com.task.azercell.util.FingerprintUtil
import com.task.azercell.util.PatternInputFilter
import com.task.azercell.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private val binding by lazy {
        FragmentSignUpBinding.inflate(layoutInflater)
    }

    private val viewModel: SignUpViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setEditTexts()
        setClickEvents()
    }

    private fun setEditTexts() {
        binding.apply {
            nameField.filters = arrayOf(PatternInputFilter())
            surnameField.filters = arrayOf(PatternInputFilter())

            phoneNumberField.inputType = InputType.TYPE_CLASS_NUMBER
            birthDayField.inputType = InputType.TYPE_CLASS_NUMBER

            phoneNumberField.onTextChangeListener {
                if (it.trim().length == 19 && !phonePattern.toRegex()
                        .matches(it)
                ) phoneNumberError.visibility = View.VISIBLE
                else phoneNumberError.visibility = View.GONE
            }

            birthDayField.onTextChangeListener {
                if (it.trim().length == 10 && !birthPattern.toRegex()
                        .matches(it)
                ) birthDayError.visibility = View.VISIBLE
                else birthDayError.visibility = View.GONE
            }
        }
    }

    private fun setClickEvents() {
        binding.apply {
            btnCalendar.setOnClickListener {
                createSpinnerDatePickerDialogDate(birthDayField, birthDayField) { }
            }

            btnBack.setOnClickListener {
                activity?.supportFragmentManager?.beginTransaction()?.remove(this@SignUpFragment)
                    ?.commit()
            }

            btnSignIn.setOnClickListener {
                if (!validate()) return@setOnClickListener
                if (FingerprintUtil.canAuthenticate(requireContext())) startFingerprintAuthentication()
                else {
                    authenticateClient()
                    navigateToDashboard()
                }
            }
        }
    }


    private fun validate(): Boolean {
        binding.apply {
            if (!cbTermCondition.isChecked) return false
            if (nameField.text.isEmpty()) return false
            if (surnameField.text.isEmpty()) return false
            if (!phonePattern.toRegex().matches(phoneNumberField.text.toString())) return false
            if (!birthPattern.toRegex().matches(birthDayField.text.toString())) return false
            return true
        }
    }

    private fun startFingerprintAuthentication() {
        val authenticationCallback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                authenticateClient()
                navigateToDashboard()
            }
        }

        FingerprintUtil.authenticate(
            fragment = this,
            title = "Fingerprint Authentication",
            subtitle = "Please place your finger on the scanner",
            negativeButtonText = "Cancel",
            callback = authenticationCallback
        )
    }

    private fun createSpinnerDatePickerDialogDate(
        view: View, nonCustomerDateOfBirth: TextView, onDateSelected: (String) -> Unit
    ) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val contextThemeWrapper = ContextThemeWrapper(view.context, R.style.DatePickerDialogTheme)

        val datePickerDialog = DatePickerDialog(
            contextThemeWrapper,
            { _, year, month, dayOfMonth ->
                val monthStr = if (month + 1 < 10) "0${month + 1}" else (month + 1).toString()
                val dateStr = "$dayOfMonth.$monthStr.$year"

                nonCustomerDateOfBirth.text = dateStr
                val birthDate = "$year$monthStr$dayOfMonth"
                onDateSelected(birthDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.setCancelable(false)
        datePickerDialog.show()
    }

    private fun authenticateClient() {
        binding.apply {
            viewModel.authenticateClient(
                name = nameField.text.toString(),
                surname = surnameField.text.toString(),
                phoneNumber = phoneNumberField.text.toString(),
                birthday = birthDayField.text.toString(),
            )
        }
    }

    private fun navigateToDashboard() {
        val intent = Intent(activity, DashboardActivity::class.java)
        activity?.startActivity(intent)
    }


}
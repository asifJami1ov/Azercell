@file:Suppress("DEPRECATION")

package com.task.azercell.util

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.util.concurrent.Executor

object FingerprintUtil {
    fun canAuthenticate(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
    }

    fun authenticate(
        fragment: Fragment,
        title: String,
        subtitle: String,
        negativeButtonText: String,
        callback: BiometricPrompt.AuthenticationCallback
    ) {
        val executor: Executor = ContextCompat.getMainExecutor(fragment.requireContext())

        val biometricPrompt = BiometricPrompt(fragment, executor, callback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText(negativeButtonText)
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}

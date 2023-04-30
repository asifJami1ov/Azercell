package com.task.azercell.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.task.azercell.R
import com.task.azercell.data.PreferencesHelperFacade
import com.task.azercell.databinding.ActivityLoginBinding
import com.task.azercell.ui.fragment.SignUpFragment
import com.task.azercell.util.replaceFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    @Inject
    lateinit var shared: PreferencesHelperFacade

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private var doubleTapExitJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        checkClient()
        setContentView(binding.root)
        clickEvents()
    }

    override fun onResume() {
        super.onResume()
        doubleTapExitJob = null
        setBackPressedDispatcher()
    }

    private fun checkClient() {
        if (shared.getClient() != null) {
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
            finish()
        }
    }

    private fun clickEvents() {
        binding.apply {
            btnSignUp.setOnClickListener {
                replaceFragment(R.id.container, SignUpFragment())
            }

            btnSignIn.setOnClickListener {
                Toast.makeText(
                    this@LoginActivity, "Please Sign Up first !", Toast.LENGTH_SHORT
                ).show()
            }

        }
    }



    override fun onDestroy() {
        super.onDestroy()
        doubleTapExitJob?.cancel()
    }

    private fun setBackPressedDispatcher() {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (supportFragmentManager.fragments.size > 0) {
                        supportFragmentManager.beginTransaction()
                            .remove(supportFragmentManager.fragments[0])
                            .commit()
                        return
                    }

                    if (doubleTapExitJob == null) {
                        doubleTapExitJob = lifecycleScope.launch {
                            Toast.makeText(
                                this@LoginActivity,
                                "Press back again to exit",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            delay(2000) // wait for 2 seconds
                            doubleTapExitJob = null
                        }
                    } else {
                        doubleTapExitJob?.cancel()
                        this@LoginActivity.finish()
                    }
                }
            })
    }

}
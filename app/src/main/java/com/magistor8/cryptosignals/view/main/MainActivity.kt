package com.magistor8.cryptosignals.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.magistor8.cryptosignals.view.provider.ProviderFragment
import com.magistor8.cryptosignals.R
import com.magistor8.cryptosignals.view.signal.SignalFragment
import com.magistor8.cryptosignals.databinding.ActivityMainBinding
import com.magistor8.cryptosignals.utils.Navigation
import com.magistor8.cryptosignals.view.login.LoginFragment
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val navigation : Navigation by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(
            R.id.container,
            SignalFragment()
        ).commit()
        setBottomViewListener()
    }

    private fun setBottomViewListener() {
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.bottom_signal -> {
                    navigation.navigate(
                        SignalFragment::class.java,
                        Navigation.Action.REPLACE,
                        addToBS = true
                    )
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_provider -> {
                    navigation.navigate(
                        ProviderFragment::class.java,
                        Navigation.Action.REPLACE,
                        addToBS = true
                    )
                    return@setOnItemSelectedListener true
                }
                R.id.bottom_lk -> {
                    navigation.navigate(
                        LoginFragment::class.java,
                        Navigation.Action.REPLACE,
                        addToBS = true
                    )
                    return@setOnItemSelectedListener true
                }
                else -> {return@setOnItemSelectedListener true}
            }
        }
        bottomNavigationViewSetup()
    }

    private fun bottomNavigationViewSetup() {
        with(supportFragmentManager) {
            addOnBackStackChangedListener {
                when (findFragmentById(R.id.container)) {
                    is SignalFragment -> binding.bottomNavigationView.menu.findItem(R.id.bottom_signal).isChecked =
                        true
                    is ProviderFragment -> binding.bottomNavigationView.menu.findItem(R.id.bottom_provider).isChecked =
                        true
                    is LoginFragment -> binding.bottomNavigationView.menu.findItem(R.id.bottom_lk).isChecked =
                        true
                }
            }
        }
    }

}


package com.magistor8.cryptosignals.view.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.magistor8.cryptosignals.App
import com.magistor8.cryptosignals.view.provider.ProviderFragment
import com.magistor8.cryptosignals.R
import com.magistor8.cryptosignals.view.signal.SignalFragment
import com.magistor8.cryptosignals.databinding.ActivityMainBinding
import com.magistor8.cryptosignals.utils.Navigation
import com.magistor8.cryptosignals.view.login.LoginFragment
import com.magistor8.cryptosignals.view.user.UserFragment
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
        checkLogged()
    }

    private fun checkLogged() {
        val pref = getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
        val login = pref.getString(LOGIN, null)
        login?.let {
            App.instance.isLogged = true
        }
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
                    if (App.instance.isLogged) {
                        navigation.navigate(
                            UserFragment::class.java,
                            Navigation.Action.REPLACE,
                            addToBS = true
                        )
                    } else {
                        navigation.navigate(
                            LoginFragment::class.java,
                            Navigation.Action.REPLACE,
                            addToBS = true
                        )
                    }
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
                    is LoginFragment ->  {
                        binding.bottomNavigationView.menu.findItem(R.id.bottom_lk).isChecked = true
                        if (App.instance.isLogged) {
                            navigation.navigate(
                                UserFragment::class.java,
                                Navigation.Action.REPLACE,
                                addToBS = true
                            )
                        }
                    }
                    is UserFragment -> binding.bottomNavigationView.menu.findItem(R.id.bottom_lk).isChecked =
                        true
                }
            }
        }
    }

    companion object {
        const val SHARED_PREF = "SHARED_PREF"
        const val LOGIN = "LOGIN"
    }

}


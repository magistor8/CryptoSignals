package com.magistor8.cryptosignals.view.main

import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.magistor8.cryptosignals.App
import com.magistor8.cryptosignals.view.provider.ProviderFragment
import com.magistor8.cryptosignals.R
import com.magistor8.cryptosignals.view.signal.SignalFragment
import com.magistor8.cryptosignals.databinding.ActivityMainBinding
import com.magistor8.cryptosignals.domain.contracts.MainContract
import com.magistor8.cryptosignals.domain.repo.LoginRepo
import com.magistor8.cryptosignals.utils.Navigation
import com.magistor8.cryptosignals.view.login.LoginFragment
import com.magistor8.cryptosignals.view.user.UserFragment
import org.koin.android.ext.android.inject
import org.koin.android.scope.getOrCreateScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinScopeComponent
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope

class MainActivity : AppCompatActivity(), KoinScopeComponent {

    override val scope: Scope by getOrCreateScope()

    private lateinit var binding: ActivityMainBinding
    private val navigation : Navigation by inject { parametersOf(this) }
    private val viewModel : MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.viewState.observe(this) { state -> renderData(state) }

        setBottomViewListener()
        checkLogged()
        loadingLayout()


    }

    private fun renderData(state: MainContract.ViewState) {

        //Первая загрузка
        supportFragmentManager.beginTransaction().replace(
            R.id.container,
            SignalFragment()
        ).commit()

        when (state) {
            is MainContract.ViewState.Error -> stateError(state.throwable.message)
            is MainContract.ViewState.Success -> App.instance.isLogged = true
        }
    }

    private fun stateError(message: String?) {
        if (message == "logout") {
            App.instance.isLogged = false
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadingLayout() {
        val imageLoader = ImageLoader.Builder(this)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()

        val request = ImageRequest.Builder(this)
            .data(R.drawable.loading)
            .crossfade(true)
            .target(binding.loading)
            .build()

        imageLoader.enqueue(request)
    }

    private fun checkLogged() {
        val login = App.instance.getLogin()
        val pass = App.instance.getPassword()
        login?.let {
            viewModel.onEvent(MainContract.Events.CheckLogged(login, pass ?: ""))
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

    override fun onDestroy() {
        scope.close()
        super.onDestroy()
    }

    companion object {
        const val LOGIN = "LOGIN"
        const val PASS = "PASS"
    }

}


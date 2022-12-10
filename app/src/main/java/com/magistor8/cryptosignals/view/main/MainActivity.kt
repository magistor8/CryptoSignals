package com.magistor8.cryptosignals.view.main

import android.os.Build.VERSION.SDK_INT
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavGraph
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.get
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.magistor8.cryptosignals.App
import com.magistor8.cryptosignals.R
import com.magistor8.cryptosignals.databinding.ActivityMainBinding
import com.magistor8.cryptosignals.domain.contracts.MainContract
import org.koin.android.scope.getOrCreateScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinScopeComponent
import org.koin.core.scope.Scope

class MainActivity : AppCompatActivity(), KoinScopeComponent {


    override val scope: Scope by getOrCreateScope()
    private lateinit var binding: ActivityMainBinding
    private val viewModel : MainActivityViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.viewState.observe(this) { state -> renderData(state) }

        checkLogged()
        setBottomViewListener()
        loadingLayout()

    }

    private fun renderData(state: MainContract.ViewState) {
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
        val bottomNavigation = binding.bottomNavigationView
        //Находим NavController
        val navController = (supportFragmentManager.findFragmentById(R.id.container) as? NavHostFragment)?.navController
        //Установим в нижнюю навигацию
        navController?.let {
            bottomNavigation.setupWithNavController(it)
            if (App.instance.isLogged) {
                val node = it.graph.findNode(R.id.userNested)
                (node as NavGraph).setStartDestination(R.id.userFragment)
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


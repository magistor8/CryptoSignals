package com.magistor8.cryptosignals.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.magistor8.cryptosignals.domain.BaseViewModel
import com.magistor8.cryptosignals.domain.contracts.MainContract
import com.magistor8.cryptosignals.domain.repo.LoginRepo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainActivityViewModel : BaseViewModel(), MainContract.ViewModelInterface, KoinComponent {

    private val repository : LoginRepo by inject()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewState.mutable().postValue(MainContract.ViewState.Error(throwable))
    }

    override val viewState: LiveData<MainContract.ViewState> = MutableLiveData()

    override fun onEvent(event: MainContract.Events) {
        when (event) {
            is MainContract.Events.CheckLogged -> checkLogged(event.login, event.password)
        }
    }

    private fun checkLogged(login: String, password: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            var result = ""
            withContext(Dispatchers.IO) {
                result = repository.checkLogged(login, password)
            }
            when (result) {
                "logged" -> viewState.mutable().postValue(MainContract.ViewState.Success)
                "logout" -> viewState.mutable().postValue(MainContract.ViewState.Error(IllegalStateException("logout")))
            }
        }
    }


}
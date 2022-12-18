package com.magistor8.cryptosignals.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.magistor8.cryptosignals.domain.BaseViewModel
import com.magistor8.cryptosignals.domain.contracts.LoginContract
import com.magistor8.cryptosignals.domain.repo.LoginRepo
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class LoginFragmentViewModel : BaseViewModel(), LoginContract.ViewModelInterface, KoinComponent {

    private val repository : LoginRepo by inject()
    private var job: Job? = null

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewState.mutable().postValue(LoginContract.ViewState.Error(throwable))
    }

    override val viewState: LiveData<LoginContract.ViewState> = MutableLiveData()

    override fun onEvent(event: LoginContract.Events) {
        when(event) {
            is LoginContract.Events.Login -> login(event.login, event.password)
            is LoginContract.Events.Register -> register(event.login, event.email, event.password)
        }
    }

    private fun register(login: String, email: String, password: String) {
        job?.let {
            if (it.isActive) return
        }
        viewState.mutable().postValue(LoginContract.ViewState.Loading)
        job = viewModelScope.launch(coroutineExceptionHandler) {
            var result = ""
            withContext(Dispatchers.IO) {
                result = repository.register(login, password, email)
            }

            if (result == "success") loginCached(login, password)
            viewState.mutable().postValue(LoginContract.ViewState.Registered(result))
        }
    }

    private fun login(login: String, password: String) {
        job?.let {
            if (it.isActive) return
        }
        viewState.mutable().postValue(LoginContract.ViewState.Loading)
        job = viewModelScope.launch(coroutineExceptionHandler) {
            var result = ""
            withContext(Dispatchers.IO) {
                result = repository.login(login, password)
            }

            if (result == "success") loginCached(login, password)
            viewState.mutable().postValue(LoginContract.ViewState.Logged(result))
        }
    }

    private suspend fun loginCached(login: String, password: String) {
        repository.loginCached(login, password)
    }

    override fun onCleared() {
        job = null
        super.onCleared()
    }

}
package com.magistor8.cryptosignals.view.signal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.magistor8.cryptosignals.App
import com.magistor8.cryptosignals.domain.BaseViewModel
import com.magistor8.cryptosignals.domain.contracts.SignalsContract
import com.magistor8.cryptosignals.domain.entires.SignalType
import com.magistor8.cryptosignals.domain.repo.SignalRepo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SignalFragmentViewModel : BaseViewModel(), SignalsContract.ViewModelInterface, KoinComponent {

    private val repository : SignalRepo by inject()

    override val filterSettings = SignalsContract.FilterSettings(
        SignalType.All, 0, 0, 0
    )
    override val viewState: LiveData<SignalsContract.ViewState> = MutableLiveData()
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewState.mutable().postValue(SignalsContract.ViewState.Error(throwable))
    }

    init {
        getSignals()
    }

    override fun onEvent(event: SignalsContract.Events) = when(event) {
        is SignalsContract.Events.FilterType -> {
            filterSettings.type = event.type
            getSignals()
        }
        is SignalsContract.Events.Filter -> {
            filterSettings.earned = event.earned
            filterSettings.signals = event.signals
            filterSettings.registered = event.registered
            getSignals()
        }
    }

    private fun getSignals() {
        viewState.mutable().postValue(SignalsContract.ViewState.Loading)
        viewModelScope.launch(coroutineExceptionHandler) {
            withContext(Dispatchers.IO) {
                var data = repository.getSignals(filterSettings)
                val subsId = repository.getSubsId(App.instance.getLogin() ?: "")
                repository.setSignalAccess(data, subsId)
                viewState.mutable().postValue(SignalsContract.ViewState.AllSignalsLoaded(data))
            }
        }
    }

}
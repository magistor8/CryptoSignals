package com.magistor8.cryptosignals.view.signal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.magistor8.cryptosignals.domain.BaseViewModel
import com.magistor8.cryptosignals.domain.contracts.SignalsContract
import com.magistor8.cryptosignals.domain.repo.SignalRepo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SignalFragmentViewModel : BaseViewModel(), SignalsContract.ViewModelInterface, KoinComponent {

    private val repository : SignalRepo by inject()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewState.mutable().postValue(SignalsContract.ViewState.Error(throwable))
    }

    override val viewState: LiveData<SignalsContract.ViewState> = MutableLiveData()

    override fun onEvent(event: SignalsContract.Events) = when(event) {
        is SignalsContract.Events.GetSignals -> getSignals()
    }

    private fun getSignals() {
        viewState.mutable().postValue(SignalsContract.ViewState.Loading)
        viewModelScope.launch(coroutineExceptionHandler) {
            withContext(Dispatchers.IO) {
                val data = repository.getSignals()
                viewState.mutable().postValue(SignalsContract.ViewState.AllSignalsLoaded(data))
            }
        }
    }

}
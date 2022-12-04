package com.magistor8.cryptosignals.view.signal


import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.magistor8.cryptosignals.databinding.FragmentSignalsBinding
import com.magistor8.cryptosignals.domain.contracts.SignalsContract
import com.magistor8.cryptosignals.domain.entires.ProviderData
import com.magistor8.cryptosignals.domain.entires.SignalData
import com.magistor8.cryptosignals.domain.entires.SignalType
import com.magistor8.cryptosignals.domain.repo.SignalRepo
import com.magistor8.cryptosignals.view.BaseFragment
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.android.scope.getOrCreateScope
import org.koin.core.component.KoinScopeComponent
import org.koin.core.scope.Scope

const val SIGNAL_TYPE_DIALOG_FRAGMENT_TAG = "SIGNAL_TYPE_DIALOG_FRAGMENT_TAG"

class SignalFragment: BaseFragment(), KoinScopeComponent {

    private var _binding: FragmentSignalsBinding? = null
    private val binding get() = _binding!!

    private var data : MutableList<SignalData> = mutableListOf()

    private val filterSettings = SignalsContract.FilterSettings(
        SignalType.All, 0, 0, 0
    )

    private val adapter : SignalAdapter by inject()
    override val scope: Scope by getOrCreateScope()
    private val viewModel : SignalFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //recyclerview
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(context)

        //Загружаем данные сигналов
        getData()
        //Фильтр по типу сигнала
        signalTypeClick()
        //Фильтр по провайдеру
        filterClick()
        //Мои подписки
        mySubs()

        //ЛайвДата - подписка
        viewModel.viewState.observe(viewLifecycleOwner) { state -> renderData(state) }
    }

    //Чекбокс на мои подписки
    private fun mySubs() {
        binding.subscr.setOnCheckedChangeListener { _, p1 ->
            if (p1) {
                adapter.submitList(data.filter { it.access })
            } else {
                adapter.submitList(data)
            }
        }
    }

    private fun getData() {
        //Получаем тестовые данные
        viewModel.onEvent(SignalsContract.Events.GetSignals(filterSettings))
    }

    //Рендер лайвдаты
    private fun renderData(state: SignalsContract.ViewState) {
        when (state) {
            is SignalsContract.ViewState.Error -> Toast.makeText(context, state.throwable.message, Toast.LENGTH_SHORT).show()
            is SignalsContract.ViewState.ProviderDataLoaded -> Toast.makeText(context, "Данные загружены", Toast.LENGTH_SHORT).show()
            is SignalsContract.ViewState.AllSignalsLoaded -> {
                loadingScreen(GONE)
                data = state.data as MutableList

                if (binding.subscr.isChecked) {
                    adapter.submitList(data.filter { it.access })
                } else {
                    adapter.submitList(data)
                }
            }
            is SignalsContract.ViewState.Loading -> loadingScreen(VISIBLE)
        }
    }

    //Фильтр по провайдеру
    private fun filterClick() {
        binding.cheapFilter.setOnClickListener {
            val signalFilterDialogFragment = SignalFilterDialogFragment.newInstance()
            signalFilterDialogFragment.setOnFilterClickListener(object : SignalFilterDialogFragment.OnFilterClickListener {
                override fun onClick(earned: Int, signals: Int, registered: Int) {
                    filterSettings.earned = earned
                    filterSettings.signals = signals
                    filterSettings.registered = registered
                    viewModel.onEvent(SignalsContract.Events.GetSignals(filterSettings))
                }
            })
            activity?.let {
                signalFilterDialogFragment.show(
                    it.supportFragmentManager,
                    SIGNAL_TYPE_DIALOG_FRAGMENT_TAG
                )
            }
        }
    }

    //Фильтр по типу сигнала
    private fun signalTypeClick() {
        binding.cheapType.setOnClickListener {
            val signalTypeDialogFragment = SignalTypeDialogFragment.newInstance()
            signalTypeDialogFragment.setOnTypeClickListener(object : SignalTypeDialogFragment.OnTypeClickListener {
                override fun onClick(type: SignalType) {
                    filterSettings.type = type
                    viewModel.onEvent(SignalsContract.Events.GetSignals(filterSettings))
                }
            })
            activity?.let {
                signalTypeDialogFragment.show(
                    it.supportFragmentManager,
                    SIGNAL_TYPE_DIALOG_FRAGMENT_TAG
                )
            }
        }
    }

    override fun onDetach() {
        scope.close()
        super.onDetach()
    }

}
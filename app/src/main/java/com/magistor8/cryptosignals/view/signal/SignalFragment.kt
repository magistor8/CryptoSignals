package com.magistor8.cryptosignals.view.signal


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.magistor8.cryptosignals.databinding.FragmentSignalsBinding
import com.magistor8.cryptosignals.domain.contracts.SignalsContract
import com.magistor8.cryptosignals.domain.entires.SignalData
import com.magistor8.cryptosignals.domain.entires.SignalType
import com.magistor8.cryptosignals.view.BaseFragment
import org.koin.android.ext.android.inject
import org.koin.android.scope.getOrCreateScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinScopeComponent
import org.koin.core.scope.Scope

const val SIGNAL_TYPE_DIALOG_FRAGMENT_TAG = "SIGNAL_TYPE_DIALOG_FRAGMENT_TAG"

class SignalFragment: BaseFragment(), KoinScopeComponent {

    private var _binding: FragmentSignalsBinding? = null
    private val binding get() = _binding!!

    private var data : MutableList<SignalData> = mutableListOf()

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

    //Рендер лайвдаты
    private fun renderData(state: SignalsContract.ViewState) {
        when (state) {
            is SignalsContract.ViewState.Error -> Toast.makeText(context, state.throwable.message, Toast.LENGTH_SHORT).show()
            is SignalsContract.ViewState.ProviderDataLoaded -> Toast.makeText(context, "Данные загружены", Toast.LENGTH_SHORT).show()
            is SignalsContract.ViewState.AllSignalsLoaded -> allSignalsLoaded(state)
            is SignalsContract.ViewState.Loading -> loadingScreen(VISIBLE)
        }
    }

    private fun allSignalsLoaded(state: SignalsContract.ViewState.AllSignalsLoaded) {
        loadingScreen(GONE)
        data = state.data as MutableList

        if (binding.subscr.isChecked) {
            adapter.submitList(data.filter { it.access })
        } else {
            adapter.submitList(data)
        }
    }

    //Фильтр по провайдеру
    private fun filterClick() {
        binding.cheapFilter.setOnClickListener {
            val signalFilterDialogFragment = SignalFilterDialogFragment.newInstance()
            signalFilterDialogFragment.setOnFilterClickListener(object : SignalFilterDialogFragment.OnFilterClickListener {
                override fun onClick(earned: Int, signals: Int, registered: Int) {
                    viewModel.onEvent(SignalsContract.Events.Filter(earned, signals, registered))
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
                    viewModel.onEvent(SignalsContract.Events.FilterType(type))
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
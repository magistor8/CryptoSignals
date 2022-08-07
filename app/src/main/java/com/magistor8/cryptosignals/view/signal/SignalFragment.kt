package com.magistor8.cryptosignals.view.signal


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.magistor8.cryptosignals.databinding.FragmentSignalsBinding
import com.magistor8.cryptosignals.domain.contracts.SignalsContract
import com.magistor8.cryptosignals.domain.entires.ProviderData
import com.magistor8.cryptosignals.domain.entires.SignalData
import com.magistor8.cryptosignals.domain.entires.SignalType
import com.magistor8.cryptosignals.domain.repo.SignalRepo
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

const val SIGNAL_TYPE_DIALOG_FRAGMENT_TAG = "SIGNAL_TYPE_DIALOG_FRAGMENT_TAG"

class SignalFragment: Fragment() {

    private var _binding: FragmentSignalsBinding? = null
    private val binding get() = _binding!!

    private var data : MutableList<SignalData> = mutableListOf()
    private var filteredData : MutableList<SignalData> = mutableListOf()

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Toast.makeText(context, throwable.message, Toast.LENGTH_SHORT).show()
    }
    private val coroutineContext = Dispatchers.Main + SupervisorJob() + coroutineExceptionHandler
    private val scope = CoroutineScope(coroutineContext)
    private var job: Job? = null

    private val filterSettings = object : SignalsContract.FilterSettings {
        override var type: SignalType = SignalType.All
        override var earned: Int = 0
        override var signals: Int = 0
        override var registered: Int = 0
    }

    private val adapter : SignalAdapter by inject()
    private val repo : SignalRepo by inject()
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
        testData()
        //Фильтр по типу сигнала
        signalTypeClick()
        //Фильтр по провайдеру
        filterClick()

        //ЛайвДата - подписка
        viewModel.viewState.observe(viewLifecycleOwner) { state -> renderData(state) }
    }

    private fun testData() {
        //Получаем тестовые данные
        viewModel.onEvent(SignalsContract.Events.GetSignals)
    }

    //Рендер лайвдаты
    private fun renderData(state: SignalsContract.ViewState) {
        when (state) {
            is SignalsContract.ViewState.Error -> Toast.makeText(context, state.throwable.message, Toast.LENGTH_SHORT).show()
            is SignalsContract.ViewState.ProviderDataLoaded -> Toast.makeText(context, "Данные загружены", Toast.LENGTH_SHORT).show()
            is SignalsContract.ViewState.AllSignalsLoaded -> {
                data = state.data as MutableList
                updateList()
            }
            is SignalsContract.ViewState.Loading -> Toast.makeText(context, "Загрузка", Toast.LENGTH_SHORT).show()
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
                    updateList()
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
                    updateList()
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

    //Фильтруем данные и загружаем в адаптер
    private fun updateList() {
        filteredData = when (filterSettings.type) {
            is SignalType.FutureLong -> data.filter { it.type is SignalType.FutureLong } as MutableList<SignalData>
            is SignalType.FutureShort -> data.filter { it.type is SignalType.FutureShort } as MutableList<SignalData>
            is SignalType.Spot -> data.filter { it.type is SignalType.Spot } as MutableList<SignalData>
            else -> data
        }
        //Если фильтр по провайдеру не пустой
        if (filterSettings.earned != 0 ||
            filterSettings.signals != 0 ||
            filterSettings.registered != 0)
        {
            //Получаем айдишники
            filteredData = filteredData.filter { it.access } as MutableList<SignalData>
            val ids = filteredData.map {it.providerId.toString()}
            //Получаем данные провайдеров по айдишникам
            job = scope.launch(coroutineExceptionHandler) {
                var providerData = mutableListOf<ProviderData>()
                withContext(Dispatchers.IO) {
                    providerData = repo.getProviderDataFromIds(ids.distinct()) as MutableList<ProviderData>
                }
                //Убираем ненужное
                filterByProviderData(providerData)
                adapter.submitList(filteredData)
            }

        } else {
            adapter.submitList(filteredData)
        }

    }

    private fun filterByProviderData(providerData: List<ProviderData>) {
        val sd = filteredData.toMutableList()
        var offset = 0
        sd.forEachIndexed { index, signalData ->
            val pd = providerData.find { it.id == signalData.providerId }
            if (pd!!.earn30 < filterSettings.earned ||
                pd.signals30 < filterSettings.signals ||
                pd.registered < filterSettings.registered
            ) {
                filteredData.remove(filteredData[index - offset])
                offset++
            }
        }
    }

    override fun onDetach() {
        job = null
        super.onDetach()
    }

}
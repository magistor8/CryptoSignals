package com.magistor8.cryptosignals.view.provider


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.magistor8.cryptosignals.databinding.FragmentProvidersBinding
import com.magistor8.cryptosignals.domain.contracts.ProviderContract
import com.magistor8.cryptosignals.domain.entires.Sort
import com.magistor8.cryptosignals.view.BaseFragment
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.getOrCreateScope
import org.koin.core.scope.Scope

const val PROVIDER_TYPE_DIALOG_FRAGMENT_TAG = "PROVIDER_TYPE_DIALOG_FRAGMENT_TAG"
const val PROVIDER_SORT_DIALOG_FRAGMENT_TAG = "PROVIDER_SORT_DIALOG_FRAGMENT_TAG"

class ProviderFragment: BaseFragment(), KoinScopeComponent {

    private var _binding: FragmentProvidersBinding? = null
    private val binding get() = _binding!!

    private val adapter : ProviderAdapter by inject()
    override val scope: Scope by getOrCreateScope()
    private val viewModel : ProviderFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProvidersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //recyclerview
        binding.recyclerview.adapter = adapter
        binding.recyclerview.layoutManager = LinearLayoutManager(context)

        filterClick()
        sortClick()

        viewModel.viewState.observe(viewLifecycleOwner) { state -> renderData(state) }

    }

    private fun renderData(state: ProviderContract.ViewState) {
        when(state) {
            is ProviderContract.ViewState.Error -> Toast.makeText(context, state.throwable.message, Toast.LENGTH_SHORT).show()
            is ProviderContract.ViewState.Loading -> loadingScreen(View.VISIBLE)
            is ProviderContract.ViewState.ProviderDataSuccess -> {
                loadingScreen(View.GONE)
                adapter.submitList(state.data)
            }
        }
    }

    //Сортировка
    private fun sortClick() {
        binding.cheapSort.setOnClickListener {
            val providerSortDialogFragment = ProviderSortDialogFragment.newInstance()
            providerSortDialogFragment.setOnTypeClickListener(object : ProviderSortDialogFragment.OnSortClickListener {
                override fun onClick(sort: Sort) {
                    val data = adapter.getData()
                    when (sort) {
                        Sort.ByReg -> adapter.submitList(data.sortedByDescending { it.registered })
                        Sort.BySignals -> adapter.submitList(data.sortedByDescending { it.signalsAll })
                        Sort.ByEarn -> adapter.submitList(data.sortedByDescending { it.earnAll })
                        Sort.ByName -> adapter.submitList(data.sortedBy { it.name })
                    }
                }
            })
            activity?.let {
                providerSortDialogFragment.show(
                    it.supportFragmentManager,
                    PROVIDER_SORT_DIALOG_FRAGMENT_TAG
                )
            }
        }
    }

    //Фильтр по провайдеру
    private fun filterClick() {
        binding.cheapFilter.setOnClickListener {
            val providerFilterDialogFragment = ProviderFilterDialogFragment.newInstance()
            providerFilterDialogFragment.setOnFilterClickListener(object : ProviderFilterDialogFragment.OnFilterClickListener {
                override fun onClick(
                    earnPeriod: String,
                    earn: Int,
                    signalPeriod: String,
                    signal: Int,
                    name: String,
                    register: Int
                ) {
                    viewModel.onEvent(ProviderContract.Events.LoadData(earnPeriod, earn, signalPeriod, signal, name, register))
                }
            })
            activity?.let {
                providerFilterDialogFragment.show(
                    it.supportFragmentManager,
                    PROVIDER_TYPE_DIALOG_FRAGMENT_TAG
                )
            }
        }
    }

    override fun onDestroy() {
        scope.close()
        super.onDestroy()
    }

}
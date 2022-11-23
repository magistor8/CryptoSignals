package com.magistor8.cryptosignals.view.provider


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.magistor8.cryptosignals.databinding.FragmentProvidersBinding
import com.magistor8.cryptosignals.domain.contracts.ProviderContract
import com.magistor8.cryptosignals.domain.entires.ProviderData
import com.magistor8.cryptosignals.view.BaseFragment
import com.magistor8.cryptosignals.view.signal.SignalAdapter
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.getOrCreateScope
import org.koin.core.scope.Scope

class ProviderFragment: BaseFragment(), KoinScopeComponent {

    private var _binding: FragmentProvidersBinding? = null
    private val binding get() = _binding!!

    private val adapter : ProviderAdapter by inject()
    override val scope: Scope by getOrCreateScope()
    private val viewModel : ProviderFragmentViewModel by viewModel()

    private val filterSetting = ProviderContract.FilterSettings(
        "", 0, 0, 0, 0, 0
    )

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

        viewModel.viewState.observe(viewLifecycleOwner) { state -> renderData(state) }

        loadTestData()
        //loadData()
    }

    private fun loadTestData() {
        val pd = ProviderData(
            1, "Magistor8", "https://magistor8.tk/logos/admin.jpg", 155, 27, 146, 2415, 14, 155, false, 0
        )
        val pd2 = ProviderData(
            2, "Magistor99", "https://magistor8.tk/logos/provider_2.jpg", 26, 14, 29, 29, 11, 11, true, 26
        )
        adapter.submitList(listOf(pd, pd2))
    }

    private fun loadData() {
        viewModel.onEvent(ProviderContract.Events.LoadData(filterSetting))
    }

    private fun renderData(state: ProviderContract.ViewState) {

    }

    override fun onDestroy() {
        scope.close()
        super.onDestroy()
    }

}
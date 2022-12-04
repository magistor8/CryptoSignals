package com.magistor8.cryptosignals.view.provider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.magistor8.cryptosignals.databinding.FiltersProvidersDialogFragmentBinding

class ProviderFilterDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FiltersProvidersDialogFragmentBinding? = null
    private val binding get() = _binding!!
    private var onFilterClickListener: OnFilterClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FiltersProvidersDialogFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.save.setOnClickListener {
            onFilterClickListener?.onClick(
                binding.earnSpinner.selectedItem.toString(),
                if (binding.earnEdit.text.toString() == "") 0 else binding.earnEdit.text.toString().toInt(),
                binding.signalsSpinner.selectedItem.toString(),
                if (binding.signalsEdit.text.toString() == "") 0 else binding.signalsEdit.text.toString().toInt(),
                binding.nameEdit.text.toString(),
                if (binding.regEdit.text.toString() == "") 0 else binding.regEdit.text.toString().toInt(),
            )
            dismiss()
        }
    }

    fun setOnFilterClickListener(listener: OnFilterClickListener) {
        onFilterClickListener = listener
    }

    interface OnFilterClickListener {
        fun onClick(
            earnPeriod: String,
            earn: Int,
            signalPeriod: String,
            signal: Int,
            name: String,
            register: Int
        )
    }

    companion object {
        fun newInstance(): ProviderFilterDialogFragment {
            return ProviderFilterDialogFragment()
        }
    }

}
package com.magistor8.cryptosignals.view.provider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.magistor8.cryptosignals.databinding.ProviderSortDialogFragmentBinding
import com.magistor8.cryptosignals.domain.entires.Sort

class ProviderSortDialogFragment : BottomSheetDialogFragment() {

    private var _binding: ProviderSortDialogFragmentBinding? = null
    private val binding get() = _binding!!
    private var onSortClickListener: OnSortClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ProviderSortDialogFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.reg.setOnClickListener {
            onSortClickListener?.onClick(Sort.ByReg)
            dismiss()
        }
        binding.sign.setOnClickListener {
            onSortClickListener?.onClick(Sort.BySignals)
            dismiss()
        }
        binding.earn.setOnClickListener {
            onSortClickListener?.onClick(Sort.ByEarn)
            dismiss()
        }
        binding.name.setOnClickListener {
            onSortClickListener?.onClick(Sort.ByName)
            dismiss()
        }
    }

    fun setOnTypeClickListener(listener: OnSortClickListener) {
        onSortClickListener = listener
    }

    interface OnSortClickListener {
        fun onClick(sort: Sort)
    }

    companion object {
        fun newInstance(): ProviderSortDialogFragment {
            return ProviderSortDialogFragment()
        }
    }

}
package com.magistor8.cryptosignals.view.signal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.magistor8.cryptosignals.databinding.FiltersDialogFragmentBinding

class SignalFilterDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FiltersDialogFragmentBinding? = null
    private val binding get() = _binding!!
    private var onFilterClickListener: OnFilterClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FiltersDialogFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.save.setOnClickListener {
            onFilterClickListener?.onClick(
                if (binding.earnedEdit.text.toString() == "") 0 else binding.earnedEdit.text.toString().toInt(),
                if (binding.signalsEdit.text.toString() == "") 0 else binding.signalsEdit.text.toString().toInt(),
                if (binding.registerEdit.text.toString() == "") 0 else binding.registerEdit.text.toString().toInt()
            )
            dismiss()
        }
    }

    fun setOnFilterClickListener(listener: OnFilterClickListener) {
        onFilterClickListener = listener
    }

    interface OnFilterClickListener {
        fun onClick(earned: Int, signals: Int, registered: Int)
    }

    companion object {
        fun newInstance(): SignalFilterDialogFragment {
            return SignalFilterDialogFragment()
        }
    }

}
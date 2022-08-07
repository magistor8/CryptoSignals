package com.magistor8.cryptosignals.view.signal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.magistor8.cryptosignals.databinding.SignalTypeDialogFragmentBinding
import com.magistor8.cryptosignals.domain.entires.SignalType

class SignalTypeDialogFragment : BottomSheetDialogFragment() {

    private var _binding: SignalTypeDialogFragmentBinding? = null
    private val binding get() = _binding!!
    private var onTypeClickListener: OnTypeClickListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SignalTypeDialogFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.all.setOnClickListener {
            onTypeClickListener?.onClick(SignalType.All)
            dismiss()
        }
        binding.spot.setOnClickListener  {
            onTypeClickListener?.onClick(SignalType.Spot)
            dismiss()
        }
        binding.longx.setOnClickListener {
            onTypeClickListener?.onClick(SignalType.FutureLong())
            dismiss()
        }
        binding.shortx.setOnClickListener {
            onTypeClickListener?.onClick(SignalType.FutureShort())
            dismiss()
        }
    }

    fun setOnTypeClickListener(listener: OnTypeClickListener) {
        onTypeClickListener = listener
    }

    interface OnTypeClickListener {
        fun onClick(type: SignalType)
    }

    companion object {
        fun newInstance(): SignalTypeDialogFragment {
            return SignalTypeDialogFragment()
        }
    }

}
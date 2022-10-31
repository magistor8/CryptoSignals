package com.magistor8.cryptosignals.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.magistor8.cryptosignals.databinding.BalanceDialogFragmentBinding

class BalanceDialogFragment : DialogFragment() {

    private var _binding: BalanceDialogFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BalanceDialogFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        fun newInstance(): BalanceDialogFragment {
            return BalanceDialogFragment()
        }
    }

}
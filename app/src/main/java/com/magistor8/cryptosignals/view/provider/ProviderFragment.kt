package com.magistor8.cryptosignals.view.provider


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.magistor8.cryptosignals.databinding.FragmentProvidersBinding
import com.magistor8.cryptosignals.databinding.FragmentSignalsBinding

class ProviderFragment: Fragment() {

    private var _binding: FragmentProvidersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProvidersBinding.inflate(inflater, container, false)
        return binding.root
    }

}
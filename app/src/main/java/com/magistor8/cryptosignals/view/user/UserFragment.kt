package com.magistor8.cryptosignals.view.user


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.magistor8.cryptosignals.databinding.FragmentUserBinding
import com.magistor8.cryptosignals.utils.Navigation
import com.magistor8.cryptosignals.view.login.LoginFragment
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


class UserFragment: Fragment() {

    private val navigation : Navigation by inject { parametersOf(this) }

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}
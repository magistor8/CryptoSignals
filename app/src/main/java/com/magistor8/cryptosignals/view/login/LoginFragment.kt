package com.magistor8.cryptosignals.view.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.magistor8.cryptosignals.databinding.FragmentLoginBinding
import com.magistor8.cryptosignals.utils.Navigation
import com.magistor8.cryptosignals.view.user.UserFragment
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class LoginFragment: Fragment() {

    private var isRegisterField = false
    private val navigation : Navigation by inject { parametersOf(this) }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.login.setOnClickListener {
            if (isRegisterField) {
                isRegisterField = false
                binding.emailInputLayout.visibility = GONE
            } else {
                navigation.navigate(
                    UserFragment::class.java,
                    Navigation.Action.REPLACE,
                    addToBS = true
                )
            }
        }

        binding.register.setOnClickListener {
            if (!isRegisterField) {
                isRegisterField = true
                binding.emailInputLayout.visibility = VISIBLE
            }
        }
    }

}
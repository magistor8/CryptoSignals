package com.magistor8.cryptosignals.view.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import com.magistor8.cryptosignals.R
import com.magistor8.cryptosignals.databinding.FragmentLoginBinding
import com.magistor8.cryptosignals.domain.contracts.LoginContract
import com.magistor8.cryptosignals.view.BaseFragment
import org.koin.android.scope.getOrCreateScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinScopeComponent
import org.koin.core.scope.Scope

class LoginFragment: BaseFragment(), KoinScopeComponent {

    private var isRegisterField = false
    override val scope: Scope by getOrCreateScope()
    private val viewModel : LoginFragmentViewModel by viewModel()

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

        //ЛайвДата - подписка
        viewModel.viewState.observe(viewLifecycleOwner) { state -> renderData(state) }
        //Лисенеры
        setUp()
    }

    private fun setUp() {

        //Логин
        binding.login.setOnClickListener {
            binding.loginInputLayout.error = null
            binding.passwordInputLayout.error = null
            if (isRegisterField) {
                isRegisterField = false
                binding.emailInputLayout.visibility = GONE
            } else {
                val login = binding.loginEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                if (login == "") {
                    binding.loginInputLayout.error = getString(R.string.enterLogin)
                    return@setOnClickListener
                }
                if (password == "") {
                    binding.passwordInputLayout.error = getString(R.string.enterPassword)
                    return@setOnClickListener
                }
                viewModel.onEvent(LoginContract.Events.Login(login, password))
            }
        }

        //Регистрация
        binding.register.setOnClickListener {
            if (!isRegisterField) {
                isRegisterField = true
                binding.emailInputLayout.visibility = VISIBLE
            } else {
                val login = binding.loginEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                val email = binding.emailEditText.text.toString()
                if (login == "") {
                    binding.loginInputLayout.error = getString(R.string.enterLogin)
                    return@setOnClickListener
                }
                if (password == "") {
                    binding.passwordInputLayout.error = getString(R.string.enterPassword)
                    return@setOnClickListener
                }
                if (email == "") {
                    binding.emailInputLayout.error = getString(R.string.enterEmail)
                    return@setOnClickListener
                }
                viewModel.onEvent(LoginContract.Events.Register(login, email, password))
            }
        }
    }

    private fun renderData(state: LoginContract.ViewState) {
        when(state) {
            is LoginContract.ViewState.Error -> Toast.makeText(context, state.throwable.message, Toast.LENGTH_SHORT).show()
            is LoginContract.ViewState.Loading -> loadingScreen(VISIBLE)
            is LoginContract.ViewState.Logged -> loginResult(state.message)
            is LoginContract.ViewState.Registered -> registerResult(state.message)
        }
    }

    private fun loginResult(message: String) {
        loadingScreen(GONE)
        when(message) {
            getString(R.string.UNF) -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            getString(R.string.PI) -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            getString(R.string.S) -> loginSuccess()
        }
    }

    private fun registerResult(message: String) {
        loadingScreen(GONE)
        when(message) {
            getString(R.string.UAC) -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            getString(R.string.EAU) -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            getString(R.string.S) -> loginSuccess()
        }
    }

    private fun loginSuccess() {
        findNavController().also {
            val node = it.graph.findNode(R.id.userNested)
            (node as NavGraph).setStartDestination(R.id.userFragment)
        }
        findNavController().navigate(R.id.action_loginFragment_to_userFragment)
    }

    override fun onDestroy() {
        scope.close()
        super.onDestroy()
    }

}
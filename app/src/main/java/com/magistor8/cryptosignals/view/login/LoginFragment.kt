package com.magistor8.cryptosignals.view.login


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.magistor8.cryptosignals.databinding.FragmentLoginBinding
import com.magistor8.cryptosignals.domain.contracts.LoginContract
import com.magistor8.cryptosignals.utils.Navigation
import com.magistor8.cryptosignals.view.user.UserFragment
import org.koin.android.ext.android.inject
import org.koin.android.scope.getOrCreateScope
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinScopeComponent
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope

class LoginFragment: Fragment(), KoinScopeComponent {

    private var isRegisterField = false
    private val navigation : Navigation by inject { parametersOf(this) }
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
                    binding.loginInputLayout.error = "enter login"
                    return@setOnClickListener
                }
                if (password == "") {
                    binding.passwordInputLayout.error = "enter password"
                    return@setOnClickListener
                }
                viewModel.onEvent(LoginContract.Events.Login(login, password))
            }
        }

        binding.register.setOnClickListener {
            if (!isRegisterField) {
                isRegisterField = true
                binding.emailInputLayout.visibility = VISIBLE
            } else {
                //viewModel.onEvent(LoginContract.Events.Register())
            }
        }
    }

    private fun renderData(state: LoginContract.ViewState) {
        when(state) {
            is LoginContract.ViewState.Error -> Toast.makeText(context, state.throwable.message, Toast.LENGTH_SHORT).show()
            is LoginContract.ViewState.Loading -> Toast.makeText(context, "Загрузка", Toast.LENGTH_SHORT).show()
            is LoginContract.ViewState.Logged -> loginResult(state.message)
        }
    }

    private fun loginResult(message: String) {
        when(message) {
            "user not found" -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            "password incorrect" -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            "success" -> loginSuccess()
        }
    }

    private fun loginSuccess() {
        navigation.navigate(
            UserFragment::class.java,
            Navigation.Action.REPLACE,
            addToBS = true
        )
    }

    override fun onDestroy() {
        scope.close()
        super.onDestroy()
    }

}
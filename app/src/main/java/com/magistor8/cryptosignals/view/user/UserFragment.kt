package com.magistor8.cryptosignals.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.magistor8.cryptosignals.App
import com.magistor8.cryptosignals.databinding.FragmentUserBinding
import com.magistor8.cryptosignals.view.BaseFragment


class UserFragment: BaseFragment() {

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
        App.instance.getLogin()?.let {
            binding.username.text = it
        }

        balance()

    }

    private fun balance() {
        binding.balanceCard.setOnClickListener {
            val balanceDialogFragment = BalanceDialogFragment.newInstance()
            activity?.let {
                balanceDialogFragment.show(
                    it.supportFragmentManager,
                    BALANCE_DIALOG_FRAGMENT_TAG
                )
            }
        }
    }

    companion object {
        const val BALANCE_DIALOG_FRAGMENT_TAG = "BALANCE_DIALOG_FRAGMENT_TAG"
    }

}
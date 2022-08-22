package com.magistor8.cryptosignals.di

import com.magistor8.cryptosignals.data.LoginRepoImpl
import com.magistor8.cryptosignals.data.SignalRepoImpl
import com.magistor8.cryptosignals.data.retrofit.RemoteDataSource
import com.magistor8.cryptosignals.domain.repo.LoginRepo
import com.magistor8.cryptosignals.domain.repo.SignalRepo
import com.magistor8.cryptosignals.utils.Navigation
import com.magistor8.cryptosignals.view.login.LoginFragment
import com.magistor8.cryptosignals.view.login.LoginFragmentViewModel
import com.magistor8.cryptosignals.view.signal.SignalAdapter
import com.magistor8.cryptosignals.view.signal.SignalFragment
import com.magistor8.cryptosignals.view.signal.SignalFragmentViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val myModule = module {
    single { params -> Navigation(context = params.get()) }
    single { SignalAdapter() }
    single<SignalRepo> { SignalRepoImpl(RemoteDataSource()) }
    single<LoginRepo> { LoginRepoImpl(RemoteDataSource(), androidContext()) }

    scope<SignalFragment> {
        viewModel { SignalFragmentViewModel() }
    }

    scope<LoginFragment> {
        viewModel { LoginFragmentViewModel() }
    }
}
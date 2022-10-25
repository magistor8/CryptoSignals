package com.magistor8.cryptosignals.data

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.magistor8.cryptosignals.App
import com.magistor8.cryptosignals.data.retrofit.RemoteDataSource
import com.magistor8.cryptosignals.domain.repo.LoginRepo
import com.magistor8.cryptosignals.view.main.MainActivity.Companion.LOGIN
import com.magistor8.cryptosignals.view.main.MainActivity.Companion.PASS
import com.magistor8.cryptosignals.view.main.MainActivity.Companion.SHARED_PREF

class LoginRepoImpl(
    private val dataSource: RemoteDataSource,
    private val context: Context
) : LoginRepo {

    override suspend fun login(login: String, password: String): String {
        return dataSource.login(login, password)
    }

    override fun loginCached(login: String, password: String) {
        val pref = context.getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(LOGIN, login)
        editor.putString(PASS, password)
        editor.apply()
        App.instance.isLogged = true
    }

    override suspend fun checkLogged(login: String, password: String): String {
        return dataSource.checkLogged(login, password)
    }

}
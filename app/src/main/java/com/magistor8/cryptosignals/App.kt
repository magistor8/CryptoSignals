package com.magistor8.cryptosignals

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.magistor8.cryptosignals.di.myModule
import com.magistor8.cryptosignals.view.main.MainActivity
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    var isLogged: Boolean = false

    private val pref by lazy {
        getSharedPreferences(SHARED_PREF, MODE_PRIVATE)
    }

    companion object {
        const val SHARED_PREF = "SHARED_PREF"
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        startKoin{
            androidContext(this@App)
            modules(myModule)
        }
    }

    fun getLogin(): String? {
        return pref.getString(MainActivity.LOGIN, null)
    }

    fun getPassword(): String? {
        return pref.getString(MainActivity.PASS, null)
    }

}
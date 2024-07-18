package ru.o_mysin.kmpprojectlack

import android.app.Application
import initKoin
import org.koin.android.ext.koin.androidContext

class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MyApplication)
        }
    }
}
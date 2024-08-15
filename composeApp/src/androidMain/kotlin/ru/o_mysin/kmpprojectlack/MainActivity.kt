package ru.o_mysin.kmpprojectlack

import App
import BatteryManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import createDataStore
import io.ktor.client.engine.okhttp.OkHttp
import networking.InsultCensorClient
import networking.createHttpClient

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            // вечный сплеш
//            setKeepOnScreenCondition {
//                true
//            }
        }

        setContent {
            App(
                batteryManager =
                    remember {
                        BatteryManager(context = applicationContext)
                    },
                client =
                    remember {
                        InsultCensorClient(createHttpClient(OkHttp.create()))
                    },
                prefs = remember { createDataStore(applicationContext) },
            )
        }
    }
}

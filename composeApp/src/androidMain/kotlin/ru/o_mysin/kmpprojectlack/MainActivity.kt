package ru.o_mysin.kmpprojectlack

import App
import BatteryManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App(
                batteryManager = remember {
                    BatteryManager(context = applicationContext)
                }
            )
        }
    }
}

import androidx.compose.runtime.remember
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.initKoin
import io.ktor.client.engine.okhttp.OkHttp
import networking.InsultCensorClient
import networking.createHttpClient

fun main() {
    initKoin()
    val prefs =
        createDataStore {
            DATA_STORE_FILE_NAME
        }
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "KMPProjectLack",
        ) {
            App(
                batteryManager = remember { BatteryManager() },
                client =
                    remember {
                        InsultCensorClient(createHttpClient(OkHttp.create()))
                    },
                prefs =
                    remember {
                        prefs
                    },
            )
        }
    }
}

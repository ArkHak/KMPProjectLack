import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import di.initKoin
import io.ktor.client.engine.js.Js
import kotlinx.browser.document
import networking.InsultCensorClient
import networking.createHttpClient

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin()
    ComposeViewport(document.body!!) {
        App(
            batteryManager = BatteryManager(),
            client = InsultCensorClient(createHttpClient(Js.create())),
        )
    }
}

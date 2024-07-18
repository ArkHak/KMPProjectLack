import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kmpprojectlack.composeapp.generated.resources.Res
import kmpprojectlack.composeapp.generated.resources.hello_world
import kmpprojectlack.composeapp.generated.resources.pikachu
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App(
    batteryManager: BatteryManager,
) {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Image(
                    modifier = Modifier.size(240.dp),
                    painter = painterResource(Res.drawable.pikachu),
                    contentDescription = "pikachu"
                )
                Text(text = stringResource(Res.string.hello_world))
                Text(text = Greeting().greet())
                Text(text = "Уровень заряда батареи: ${batteryManager.getBatteryLevel()}")
            }
        }
    }
}
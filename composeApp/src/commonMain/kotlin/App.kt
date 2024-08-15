@file:OptIn(KoinExperimentalAPI::class)

import DateTimeScreen.DateTimeScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dependecies.MyViewModel
import dev.icerock.moko.permissions.PermissionState
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory
import kmpprojectlack.composeapp.generated.resources.Res
import kmpprojectlack.composeapp.generated.resources.hello_world
import kmpprojectlack.composeapp.generated.resources.pikachu
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import networking.InsultCensorClient
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import util.NetworkError
import util.onError
import util.onSuccess

@Composable
fun App(
    batteryManager: BatteryManager,
    client: InsultCensorClient,
    prefs: DataStore<Preferences>,
) {
    MaterialTheme {
        val navControllerSuper = rememberNavController()
        NavHost(
            navController = navControllerSuper,
            startDestination = ScreenA,
        ) {
            composable<ScreenA> {
                var censoredText by remember {
                    mutableStateOf<String?>(null)
                }

                var uncensoredText by remember {
                    mutableStateOf("")
                }

                var isLoading by remember {
                    mutableStateOf(false)
                }

                var errorMessage by remember {
                    mutableStateOf<NetworkError?>(null)
                }

                val factory = rememberPermissionsControllerFactory()
                val controller =
                    remember(factory) {
                        factory.createPermissionsController()
                    }

                BindEffect(controller)

                // отдельная vm !ТОЛЬКО! ради тестов
                val viewModelPermission =
                    viewModel {
                        PermissionsViewModel(controller)
                    }

                val counter by prefs
                    .data
                    .map {
                        val counterKey = intPreferencesKey("counter")
                        it[counterKey] ?: 0
                    }.collectAsState(0)

                val scope = rememberCoroutineScope()

                KoinContext {
                    NavHost(
                        navController = rememberNavController(),
                        startDestination = "home",
                    ) {
                        composable("home") {
                            val viewModel = koinViewModel<MyViewModel>()
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .background(color = Color.White),
                                contentAlignment = Alignment.Center,
                            ) {
                                Column(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    Button(onClick = {
                                        navControllerSuper.navigate(ScreenB)
                                    }) {
                                        Text("Navigate DateTime Screen")
                                    }

                                    Text(text = viewModel.getHelloWorldString())
                                    Image(
                                        modifier = Modifier.size(240.dp),
                                        painter = painterResource(Res.drawable.pikachu),
                                        contentDescription = "pikachu",
                                    )
                                    Text(text = stringResource(Res.string.hello_world))
                                    Text(text = Greeting().greet())
                                    Text(text = "Уровень заряда батареи: ${batteryManager.getBatteryLevel()}")

                                    TextField(
                                        value = uncensoredText,
                                        onValueChange = { uncensoredText = it },
                                        modifier =
                                            Modifier
                                                .padding(horizontal = 16.dp)
                                                .fillMaxWidth(),
                                        placeholder = {
                                            Text("Uncensored text")
                                        },
                                    )

                                    Button(onClick = {
                                        scope.launch {
                                            isLoading = true
                                            errorMessage = null

                                            client
                                                .censorWords(uncensoredText)
                                                .onSuccess {
                                                    censoredText = it
                                                }.onError {
                                                    errorMessage = it
                                                }

                                            isLoading = false
                                        }
                                    }) {
                                        if (isLoading) {
                                            CircularProgressIndicator(
                                                modifier =
                                                    Modifier
                                                        .size(15.dp),
                                                strokeWidth = 1.dp,
                                                color = Color.White,
                                            )
                                        } else {
                                            Text("Цензура!")
                                        }
                                    }

                                    censoredText?.let {
                                        Text(it)
                                    }

                                    errorMessage?.let {
                                        Text(
                                            text = it.name,
                                            color = Color.Red,
                                        )
                                    }

                                    Button(onClick = {
                                        scope.launch {
                                            prefs.edit { dataStore ->
                                                val counterKey = intPreferencesKey("counter")
                                                dataStore[counterKey] = counter + 1
                                            }
                                        }
                                    }) {
                                        Text("Increment: $counter")
                                    }

                                    when (viewModelPermission.state) {
                                        PermissionState.Granted -> {
                                            Text("Record audio permission granted!")
                                        }

                                        PermissionState.DeniedAlways -> {
                                            Text("Permission was permanently declined.")
                                            Button(onClick = {
                                                controller.openAppSettings()
                                            }) {
                                                Text("Open app settings")
                                            }
                                        }

                                        else -> {
                                            Button(onClick = {
                                                viewModelPermission.provideOrRequestRecordingAudioPermission()
                                            }) {
                                                Text("Request permission")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            composable<ScreenB> {
                Column {
                    DateTimeScreen()
                }
            }
        }
    }
}

@Serializable
object ScreenA

@Serializable
object ScreenB

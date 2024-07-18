package dependecies

import android.content.Context

actual class DbClient(
    private val context: Context,
) {
    actual fun getDataFromDb(): String = "dataAndroid"
}

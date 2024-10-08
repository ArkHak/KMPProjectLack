package dependecies

import androidx.lifecycle.ViewModel

class MyViewModel(
    private val repository: MyRepository,
) : ViewModel() {
    fun getHelloWorldString(): String = repository.helloWorld()
}

package reksoft.zadorozhnyi.stormy
import kotlinx.coroutines.*

internal actual fun dispatcher(): CoroutineDispatcher {
    return Dispatchers.Main
}

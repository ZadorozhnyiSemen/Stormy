package reksoft.zadorozhnyi.stormy

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class ForecastService : CoroutineScope {

    private val exceptionHandler = object : CoroutineExceptionHandler {
        override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler

        override fun handleException(context: CoroutineContext, exception: Throwable) {
            _errors.offer(exception)
        }
    }

    override val coroutineContext: CoroutineContext =
        dispatcher() + SupervisorJob() + exceptionHandler

    private val _forecast = ConflatedBroadcastChannel<Forecast>()
    private val _current = ConflatedBroadcastChannel<Currently?>()
    private val _daily = ConflatedBroadcastChannel<Daily?>()
    private val _hourly = ConflatedBroadcastChannel<Hourly?>()
    private val _errors = ConflatedBroadcastChannel<Throwable>()


    val forecast = _forecast.wrap()
    val current = _current.wrap()
    val daily = _daily.wrap()
    val hourly = _hourly.wrap()
    val errors = _errors.wrap()

    init {
        launch {
            while (true) {
                try {
                    val latitude = 60.000573
                    val longitude = 30.334711
                    loadForecast(latitude.toString(), longitude.toString())
                } catch (e: Throwable) {
                    _errors.offer(e)
                }
                delay(60 * 1000)
            }
        }
    }

    fun refresh() {
        launch {
            ClientApi.loadForecast().apply {
                _forecast.offer(this)
                _current.offer(this.currently)
                _daily.offer(this.daily)
                _hourly.offer(this.hourly)
            }
        }
    }

    private suspend fun loadForecast(lat: String, long: String) {
        ClientApi.loadForecast(lat, long).apply {
            _forecast.offer(this)
            _current.offer(this.currently)
            _daily.offer(this.daily)
            _hourly.offer(this.hourly)
        }
    }
}
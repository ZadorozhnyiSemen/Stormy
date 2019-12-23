package reksoft.zadorozhnyi.stormy

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import reksoft.zadorozhnyi.stormy.storage.ApplicationContext
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
class ForecastService(
    applicationContext: ApplicationContext
) : CoroutineScope {

    private val exceptionHandler = object : CoroutineExceptionHandler {
        override val key: CoroutineContext.Key<*> = CoroutineExceptionHandler

        override fun handleException(context: CoroutineContext, exception: Throwable) {
            _errors.offer(exception)
        }
    }

    private val locationService = LocationService(applicationContext)

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
                    locationService.getDeviceLocation().let {
                        loadForecast(it?.first.toString(), it?.second.toString())
                    }
                } catch (e: Throwable) {
                    _errors.offer(e)
                }
                delay(60 * 1000)
            }
        }
    }

    fun refreshDeviceLocation() {
        launch {
            locationService.getDeviceLocation().let {
                loadForecast(it?.first.toString(), it?.second.toString())
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

    fun changeLocation(lat: String, long: String) {
        launch {
            ClientApi.latitude = lat
            ClientApi.longitude = long
            refresh()
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
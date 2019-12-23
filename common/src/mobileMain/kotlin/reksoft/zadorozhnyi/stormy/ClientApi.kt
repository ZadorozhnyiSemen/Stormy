package reksoft.zadorozhnyi.stormy

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import reksoft.zadorozhnyi.stormy.exceptions.UnknownLatitudeException
import reksoft.zadorozhnyi.stormy.exceptions.UnknownLongitudeException
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object ClientApi {
    val backendUrl = "https://api.forecast.io/forecast"
    val apiKey = "4047a59817eabe74153da1798525bdd3"
    lateinit var latitude: String
    lateinit var longitude: String

    val endpoint
        get() = "$backendUrl/$apiKey/$latitude,$longitude?exclude=flags&units=auto"

    private val client = HttpClient {
        install(JsonFeature)
    }

    suspend fun loadForecast(lat: String? = latitude, long: String? = longitude): Forecast {
        latitude = lat ?: throw UnknownLatitudeException()
        longitude = long ?: throw UnknownLongitudeException()
        return client.get(endpoint)
    }
}
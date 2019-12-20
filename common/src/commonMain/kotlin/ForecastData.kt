import kotlinx.serialization.Serializable

@Serializable
class Forecast(
    val current: Current,
    val hourlyForecast: List<Hour>,
    val dailyForecast: List<Day>
)

@Serializable
class Current

@Serializable
class Hour

@Serializable
class Day
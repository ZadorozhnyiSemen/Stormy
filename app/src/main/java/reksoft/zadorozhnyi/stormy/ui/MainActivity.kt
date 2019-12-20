package reksoft.zadorozhnyi.stormy.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.activity_main.dailyButton
import kotlinx.android.synthetic.main.activity_main.daily_icon
import kotlinx.android.synthetic.main.activity_main.hourlyButton
import kotlinx.android.synthetic.main.activity_main.humidityValue
import kotlinx.android.synthetic.main.activity_main.precipValue
import kotlinx.android.synthetic.main.activity_main.progressBar
import kotlinx.android.synthetic.main.activity_main.refresh
import kotlinx.android.synthetic.main.activity_main.summaryLabel
import kotlinx.android.synthetic.main.activity_main.temperatureLabel
import kotlinx.android.synthetic.main.activity_main.timeLabel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import reksoft.zadorozhnyi.stormy.R
import reksoft.zadorozhnyi.stormy.weather.Current
import reksoft.zadorozhnyi.stormy.weather.Day
import reksoft.zadorozhnyi.stormy.weather.Forecast
import reksoft.zadorozhnyi.stormy.weather.Hour
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var mForecast: Forecast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)
        progressBar.visibility = View.INVISIBLE
        val latitude = 60.000573
        val longitude = 30.334711
        refresh.setOnClickListener { getForecast(latitude, longitude) }
        getForecast(latitude, longitude)

        dailyButton.setOnClickListener {
            val intent = Intent(this, DailyForecastActivity::class.java)
            intent.putExtra(DAILY_FORECAST, mForecast?.dailyForecast)
            startActivity(intent)
        }

        hourlyButton.setOnClickListener {
            val intent = Intent(this, HourlyForecastActivity::class.java)
            intent.putExtra(HOURLY_FORECAST, mForecast?.hourlyForecast)
            startActivity(intent)
        }
    }

    private fun getForecast(latitude: Double, longitude: Double) {
        val apiKey = "4047a59817eabe74153da1798525bdd3"
        val forecastUrl = ("https://api.forecast.io/forecast/" + apiKey + "/"
            + latitude + "," + longitude + "")
        if (isNetworkAvailable) {
            toggleRefresh()
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(forecastUrl)
                .build()
            val call = client.newCall(request)
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread { toggleRefresh() }
                    alertUserAboutError()
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread { toggleRefresh() }
                    try {
                        val jsonData = response.body()!!.string()
                        if (response.isSuccessful) {
                            mForecast = parseForecastDetails(jsonData)
                            runOnUiThread { updateDisplay() }
                        } else {
                            alertUserAboutError()
                        }
                    } catch (e: IOException) {
                        Log.e(TAG, "Exception caught: ", e)
                    } catch (e: JSONException) {
                        Log.e(TAG, "Unable to read JSON! Error:", e)
                    }
                }
            })
        } else {
            Toast.makeText(this, R.string.network_anavailable_message, Toast.LENGTH_LONG).show()
        }
    }

    private fun toggleRefresh() {
        if (progressBar.visibility == View.INVISIBLE) {
            progressBar.visibility = View.VISIBLE
            refresh.visibility = View.INVISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
            refresh.visibility = View.VISIBLE
        }
    }

    private fun updateDisplay() {
        val current = mForecast!!.current
        temperatureLabel.text = current.temperature.toString() + ""
        timeLabel.text = "At " + current.formattedTime + " it will be"
        humidityValue.text = current.humidity.toString() + ""
        precipValue.text = current.precipChance.toString() + "%"
        summaryLabel.text = current.summary
        val drawable = ResourcesCompat.getDrawable(resources, current.iconId, null)
        daily_icon.setImageDrawable(drawable)
    }

    @Throws(JSONException::class)
    private fun parseForecastDetails(jsonData: String): Forecast {
        val forecast = Forecast()
        forecast.current = getCurrentDetails(jsonData)
        forecast.hourlyForecast = getHourlyForecast(jsonData)
        forecast.dailyForecast = getDailyForecast(jsonData)
        return forecast
    }

    @Throws(JSONException::class)
    private fun getDailyForecast(jsonData: String): Array<Day?> {
        val forecast = JSONObject(jsonData)
        val timezone = forecast.getString("timezone")
        val daily = forecast.getJSONObject("daily")
        val data = daily.getJSONArray("data")
        val days = arrayOfNulls<Day>(data.length())
        for (i in 0 until data.length()) {
            val jsonDay = data.getJSONObject(i)
            val day = Day()
            day.summary = jsonDay.getString("summary")
            day.icon = jsonDay.getString("icon")
            day.setTemperatureMax(jsonDay.getDouble("temperatureMax"))
            day.time = jsonDay.getLong("time")
            day.timezone = timezone
            days[i] = day
        }
        return days
    }

    @Throws(JSONException::class)
    private fun getHourlyForecast(jsonData: String): Array<Hour?> {
        val forecast = JSONObject(jsonData)
        val timezone = forecast.getString("timezone")
        val hourly = forecast.getJSONObject("hourly")
        val data = hourly.getJSONArray("data")
        val hours = arrayOfNulls<Hour>(data.length())
        for (i in 0 until data.length()) {
            val jsonHour = data.getJSONObject(i)
            val hour = Hour()
            hour.summary = jsonHour.getString("summary")
            hour.setTemperature(jsonHour.getDouble("temperature"))
            hour.icon = jsonHour.getString("icon")
            hour.time = jsonHour.getLong("time")
            hour.timezone = timezone
            hours[i] = hour
        }
        return hours
    }

    @Throws(JSONException::class)
    private fun getCurrentDetails(jsonData: String): Current {
        val forecast = JSONObject(jsonData)
        val timezone = forecast.getString("timezone")
        Log.i(TAG, "From JSON: $timezone")
        val currently = forecast.getJSONObject("currently")
        val current = Current()
        current.humidity = currently.getDouble("humidity")
        current.time = currently.getLong("time")
        current.icon = currently.getString("icon")
        current.precipChance = currently.getDouble("precipProbability")
        current.summary = currently.getString("summary")
        current.setTemperature(currently.getDouble("temperature"))
        current.timeZone = timezone
        Log.d(TAG, current.formattedTime)
        return current
    }

    private val isNetworkAvailable: Boolean
        get() {
            val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = manager.activeNetworkInfo
            var isAvailable = false
            if (networkInfo != null && networkInfo.isConnected) {
                isAvailable = true
            }
            return isAvailable
        }

    private fun alertUserAboutError() {
        val dialogFragment = AlertDialogFragment()
        dialogFragment.show(supportFragmentManager, "error_dialog")
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        const val DAILY_FORECAST = "DAILY FORECAST"
        const val HOURLY_FORECAST = "HOURLY_FORECAST"
    }
}
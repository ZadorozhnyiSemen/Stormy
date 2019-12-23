package reksoft.zadorozhnyi.stormy.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import io.ktor.http.cio.websocket.Frame
import kotlinx.android.synthetic.main.activity_main.dailyButton
import kotlinx.android.synthetic.main.activity_main.hourlyButton
import kotlinx.android.synthetic.main.activity_main.humidityValue
import kotlinx.android.synthetic.main.activity_main.precipValue
import kotlinx.android.synthetic.main.activity_main.progressBar
import kotlinx.android.synthetic.main.activity_main.refresh
import kotlinx.android.synthetic.main.activity_main.summaryLabel
import kotlinx.android.synthetic.main.activity_main.temperatureLabel
import kotlinx.android.synthetic.main.activity_main.timeLabel
import reksoft.zadorozhnyi.stormy.Closeable
import reksoft.zadorozhnyi.stormy.ForecastService
import reksoft.zadorozhnyi.stormy.R
import reksoft.zadorozhnyi.stormy.StormyApplication
import reksoft.zadorozhnyi.stormy.storage.ApplicationContext
import reksoft.zadorozhnyi.stormy.utils.formatDate

class MainActivity : AppCompatActivity() {

    private val errorWatcher: Closeable? = null
    private val currentWather: Closeable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StormyApplication.service = ForecastService(ApplicationContext(this))
        progressBar.visibility = View.VISIBLE
        refresh.setOnClickListener {
            refresh.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            StormyApplication.service.refresh()
        }

        dailyButton.setOnClickListener {
            startActivity(Intent(this, DailyForecastActivity::class.java))
        }

        hourlyButton.setOnClickListener {
            startActivity(Intent(this, HourlyForecastActivity::class.java))
        }

        StormyApplication.service.current.watch {
            refresh.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            temperatureLabel.text = it?.temperature?.toInt().toString()
            timeLabel.text = it?.formatDate()
            humidityValue.text = it?.humidity?.toString() + "%"
            precipValue.text = it?.precipProbability?.toString() + "%"
            summaryLabel.text = it?.summary
        }
        StormyApplication.service.errors.watch {
            Log.e(TAG, it.message)
            Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        errorWatcher?.close()
        currentWather?.close()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 42 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            StormyApplication.service.refreshDeviceLocation()
        }
    }


    companion object {
        private val TAG = MainActivity::class.java.simpleName
        const val DAILY_FORECAST = "DAILY FORECAST"
        const val HOURLY_FORECAST = "HOURLY_FORECAST"
    }
}
package reksoft.zadorozhnyi.stormy.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.dailyButton
import kotlinx.android.synthetic.main.activity_main.hourlyButton
import kotlinx.android.synthetic.main.activity_main.humidityValue
import kotlinx.android.synthetic.main.activity_main.precipValue
import kotlinx.android.synthetic.main.activity_main.progressBar
import kotlinx.android.synthetic.main.activity_main.refresh
import kotlinx.android.synthetic.main.activity_main.summaryLabel
import kotlinx.android.synthetic.main.activity_main.temperatureLabel
import kotlinx.android.synthetic.main.activity_main.timeLabel
import reksoft.zadorozhnyi.stormy.ForecastService
import reksoft.zadorozhnyi.stormy.R
import reksoft.zadorozhnyi.stormy.StormyApplication
import reksoft.zadorozhnyi.stormy.utils.formatDate

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        StormyApplication.service = ForecastService()
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

        StormyApplication.service.forecast.watch {
            refresh.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
            temperatureLabel.text = it.currently?.temperature?.toInt().toString()
            timeLabel.text = it.currently?.formatDate(it.timezone)
            humidityValue.text = it.currently?.humidity?.toString() + "%"
            precipValue.text = it.currently?.precipProbability?.toString() + "%"
            summaryLabel.text = it.currently?.summary
        }
        StormyApplication.service.errors.watch {
            Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        const val DAILY_FORECAST = "DAILY FORECAST"
        const val HOURLY_FORECAST = "HOURLY_FORECAST"
    }
}
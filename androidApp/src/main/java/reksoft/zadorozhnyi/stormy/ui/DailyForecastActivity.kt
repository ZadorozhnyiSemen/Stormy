package reksoft.zadorozhnyi.stormy.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_daily_forecast.empty
import kotlinx.android.synthetic.main.activity_daily_forecast.list
import reksoft.zadorozhnyi.stormy.Closeable
import reksoft.zadorozhnyi.stormy.R
import reksoft.zadorozhnyi.stormy.StormyApplication
import reksoft.zadorozhnyi.stormy.adapters.DayAdapterKotlin
import reksoft.zadorozhnyi.stormy.utils.dayOfTheWeek

class DailyForecastActivity : Activity() {

    private val dailyAdapter: DayAdapterKotlin by lazy { DayAdapterKotlin() }

    private var dailyWather: Closeable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_forecast)

        list.adapter = dailyAdapter

        dailyWather = StormyApplication.service.daily.watch {
            it.let { dailyData ->
                if (dailyData?.data.isNullOrEmpty()) {
                    dailyAdapter.days = emptyList()
                    list.visibility = View.GONE
                    empty.visibility = View.VISIBLE
                } else {
                    dailyAdapter.days = dailyData?.data ?: emptyList()
                    list.visibility = View.VISIBLE
                    empty.visibility = View.GONE
                    dailyAdapter.listener = {
                        val dayOfTheWeek = it.dayOfTheWeek()
                        val conditions = it.summary
                        val highTemp = it.temperatureMax.toString() + ""
                        val message = String.format("On %s the high will be %s and it will be %s",
                            dayOfTheWeek, highTemp, conditions)
                        Toast.makeText(this@DailyForecastActivity, message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        dailyWather?.close()
    }
}
package reksoft.zadorozhnyi.stormy.ui

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_daily_forecast.empty
import kotlinx.android.synthetic.main.activity_daily_forecast.list
import reksoft.zadorozhnyi.stormy.R
import reksoft.zadorozhnyi.stormy.adapters.DayAdapterKotlin
import reksoft.zadorozhnyi.stormy.ui.MainActivity.Companion.DAILY_FORECAST
import reksoft.zadorozhnyi.stormy.weather.Day
import java.util.Arrays

class DailyForecastActivity : Activity() {
    private lateinit var mDays: List<Day>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_forecast)
        val intent = intent
        val parcelables = intent.getParcelableArrayExtra(DAILY_FORECAST)
        mDays = Arrays.copyOf(parcelables, parcelables.size, Array<Day>::class.java).toList()
        val adapter = DayAdapterKotlin {
            val dayOfTheWeek = it.dayOfTheWeek
            val conditions = it.summary
            val highTemp = it.temperatureMax.toString() + ""
            val message = String.format("On %s the high will be %s and it will be %s",
                dayOfTheWeek, highTemp, conditions)
            Toast.makeText(this@DailyForecastActivity, message, Toast.LENGTH_LONG).show()
        }
        list.adapter = adapter

        if (mDays.isEmpty()) {
            adapter.days = mDays
            list.visibility = View.GONE
            empty.visibility = View.VISIBLE
        } else {
            adapter.days = mDays
            list.visibility = View.VISIBLE
            empty.visibility = View.GONE
        }
    }
}
package reksoft.zadorozhnyi.stormy.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_hourly_forecast.recyclerView
import reksoft.zadorozhnyi.stormy.R
import reksoft.zadorozhnyi.stormy.StormyApplication
import reksoft.zadorozhnyi.stormy.adapters.HourAdapter

class HourlyForecastActivity : AppCompatActivity() {

    private val hourAdapter: HourAdapter by lazy { HourAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hourly_forecast)

        recyclerView.adapter = hourAdapter
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)

        StormyApplication.service.hourly.watch {
            it?.let { hourData ->
                hourAdapter.data = hourData.data
            }
        }
    }
}
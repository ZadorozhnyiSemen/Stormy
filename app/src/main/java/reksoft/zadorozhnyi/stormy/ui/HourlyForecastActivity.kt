package reksoft.zadorozhnyi.stormy.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import kotlinx.android.synthetic.main.activity_hourly_forecast.recyclerView
import reksoft.zadorozhnyi.stormy.R
import reksoft.zadorozhnyi.stormy.adapters.HourAdapter
import reksoft.zadorozhnyi.stormy.weather.Hour
import java.util.Arrays

class HourlyForecastActivity : AppCompatActivity() {
    private lateinit var mHours: Array<Hour>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hourly_forecast)
        ButterKnife.bind(this)
        val intent = intent
        val parcelables = intent.getParcelableArrayExtra(MainActivity.HOURLY_FORECAST)
        mHours = Arrays.copyOf(parcelables, parcelables.size, Array<Hour>::class.java)
        val adapter = HourAdapter(mHours)
        recyclerView.adapter = adapter
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
    }
}
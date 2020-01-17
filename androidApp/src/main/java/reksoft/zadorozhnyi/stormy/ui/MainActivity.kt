package reksoft.zadorozhnyi.stormy.ui

import android.content.pm.PackageManager
import android.os.Bundle
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.activity_main.add_city
import kotlinx.android.synthetic.main.activity_main.card_indicator
import kotlinx.android.synthetic.main.activity_main.city_date
import kotlinx.android.synthetic.main.activity_main.city_name
import kotlinx.android.synthetic.main.activity_main.details
import kotlinx.android.synthetic.main.activity_main.root
import kotlinx.android.synthetic.main.activity_main.weather_cards
import kotlinx.android.synthetic.main.activity_main.weather_daily
import kotlinx.android.synthetic.main.activity_main.weather_hourly
import kotlinx.android.synthetic.main.activity_main.weather_this_week
import kotlinx.android.synthetic.main.activity_main.weather_today

import reksoft.zadorozhnyi.stormy.Closeable
import reksoft.zadorozhnyi.stormy.Currently
import reksoft.zadorozhnyi.stormy.Daily
import reksoft.zadorozhnyi.stormy.Forecast
import reksoft.zadorozhnyi.stormy.ForecastService
import reksoft.zadorozhnyi.stormy.GeoData
import reksoft.zadorozhnyi.stormy.Hourly
import reksoft.zadorozhnyi.stormy.R
import reksoft.zadorozhnyi.stormy.StormyApplication
import reksoft.zadorozhnyi.stormy.ui.adapters.DetailsItemAdapter
import reksoft.zadorozhnyi.stormy.decorators.HorizontalMarginItemDecorator
import reksoft.zadorozhnyi.stormy.snap.StartSnapHelper
import reksoft.zadorozhnyi.stormy.storage.ApplicationContext
import reksoft.zadorozhnyi.stormy.ui.adapters.CurrentItemAdapter
import reksoft.zadorozhnyi.stormy.ui.adapters.DailyItemAdapter
import reksoft.zadorozhnyi.stormy.ui.adapters.HourlyItemAdapter
import reksoft.zadorozhnyi.stormy.utils.loverCasePMAM
import reksoft.zadorozhnyi.stormy.utils.today
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    private val cardAdapter: CurrentItemAdapter by lazy { CurrentItemAdapter() }
    private val hourAdapter: HourlyItemAdapter by lazy { HourlyItemAdapter() }
    private val dailyAdapter: DailyItemAdapter by lazy { DailyItemAdapter() }
    private val detailsAdapter: DetailsItemAdapter by lazy { DetailsItemAdapter() }

    private val errorWatcher: Closeable? = null
    private val currentWather: Closeable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weather_today.apply {
            isSelected = true
            setOnClickListener { toggleInfo(false) }
        }
        weather_this_week.apply {
            isSelected = false
            setOnClickListener { toggleInfo(true) }
        }

        weather_cards.apply {
            adapter = cardAdapter
            offscreenPageLimit = 1
            setPageTransformer { page, position ->
                val visibleArea = resources.getDimension(R.dimen.card_visible_offset)
                val margin = resources.getDimension(R.dimen.decor)
                val pageTranslation = visibleArea + margin
                page.translationX = -pageTranslation * position
                page.scaleY = 1 - (0.12f * abs(position))
            }
            addItemDecoration(HorizontalMarginItemDecorator(context))
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    val selectedForecast = cardAdapter.items[position].forecast
                    println(selectedForecast)
                    updateName(selectedForecast)
                    updateToday(selectedForecast?.hourly)
                    updateDaily(selectedForecast?.daily)
                    updateDetails(selectedForecast?.currently)
                }
            })
        }
        card_indicator.setViewPager2(weather_cards)


        weather_hourly.apply {
            val snap = LinearSnapHelper()
            snap.attachToRecyclerView(this)
            adapter = hourAdapter
        }

        weather_daily.apply {
            adapter = dailyAdapter
        }
        details.apply {
            val snap = StartSnapHelper()
            snap.attachToRecyclerView(this)
            adapter = detailsAdapter

        }

        add_city.setOnClickListener {
            StormyApplication.service.addLocation(
                GeoData("Kemerovo", 55.33333, 86.08333)
            )
        }

        StormyApplication.service = ForecastService(ApplicationContext(this))

        StormyApplication.service.forecast.watch {
            it.let {
                cardAdapter.addCurrentLocation(it)
                detailsAdapter.item = it.currently
                dailyAdapter.daily = it.daily
            }
        }

        StormyApplication.service.userForecast.watch {
            cardAdapter.addOtherLocations(it.values.toList())
        }

        StormyApplication.service.hourly.watch {
            it?.let {
                hourAdapter.items = it
            }
        }

        StormyApplication.service.errors.watch {
            Log.e(TAG, it.message, it)
            Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        errorWatcher?.close()
        currentWather?.close()
    }

    private fun updateName(forecast: Forecast?) {
        city_name.text = forecast?.cityName
        city_date.text = forecast?.currently?.today()?.loverCasePMAM()
    }

    private fun updateToday(hourly: Hourly?) {
        hourAdapter.items = hourly
    }

    private fun updateDaily(daily: Daily?) {
        dailyAdapter.daily = daily
    }

    private fun updateDetails(currently: Currently?) {
        detailsAdapter.item = currently
    }

    private fun toggleInfo(toWeekly: Boolean) {
        weather_daily.visibility = if (toWeekly) View.VISIBLE else View.GONE
        weather_hourly.visibility = if (!toWeekly) View.VISIBLE else View.GONE

        weather_today.isSelected = !toWeekly
        weather_today.elevation = resources.getDimension(if (!toWeekly) R.dimen.active_elevation else R.dimen.disabled_elevation)
        weather_today.setTextColor(ContextCompat.getColor(this, if (toWeekly) R.color.textPrimary else R.color.colorAccent))
        weather_this_week.isSelected = toWeekly
        weather_this_week.elevation = resources.getDimension(if (toWeekly) R.dimen.active_elevation else R.dimen.disabled_elevation)
        weather_this_week.setTextColor(ContextCompat.getColor(this, if (!toWeekly) R.color.textPrimary else R.color.colorAccent))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 42 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            StormyApplication.service.refreshDeviceLocation()
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }
}
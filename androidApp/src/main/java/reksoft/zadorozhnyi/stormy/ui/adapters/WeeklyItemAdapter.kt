package reksoft.zadorozhnyi.stormy.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_daliy.view.day
import kotlinx.android.synthetic.main.item_daliy.view.precip
import kotlinx.android.synthetic.main.item_daliy.view.temperature
import kotlinx.android.synthetic.main.item_daliy.view.weather
import reksoft.zadorozhnyi.stormy.Daily
import reksoft.zadorozhnyi.stormy.R
import reksoft.zadorozhnyi.stormy.utils.dayOfTheWeek
import java.util.Locale

class DailyItemAdapter : RecyclerView.Adapter<DailyItemAdapter.DailyItemViewHolder>() {

    var daily: Daily? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_daliy, parent, false)
        return DailyItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return daily?.data?.size ?: 0
    }

    @UseExperimental(ExperimentalStdlibApi::class)
    override fun onBindViewHolder(holder: DailyItemViewHolder, position: Int) {
        val item = daily?.data?.get(position)

        holder.apply {
            item?.let {
                if (position == 0) {
                    boldify(day, temperature, weather, precip)
                }
                day.text = when (position) {
                    0 -> "Today"
                    1 -> "Tomorrow"
                    else -> it.dayOfTheWeek()
                }
                temperature.text = it.temperatureMax.toInt().toString() + "°"
                weather.text = it.precipType?.capitalize(Locale.ENGLISH)
                precip.text = (it.precipProbability * 100).toInt().toString() + "%"
            }

        }

    }

    private fun boldify(vararg views: TextView) {
        views.forEach {
            it.typeface = ResourcesCompat.getFont(it.context, R.font.raleway_bold)
        }
    }

    inner class DailyItemViewHolder(
        view: View,
        val day: TextView = view.day,
        val temperature: TextView = view.temperature,
        val weather: TextView = view.weather,
        val precip: TextView = view.precip
    ) : RecyclerView.ViewHolder(view)
}
package reksoft.zadorozhnyi.stormy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_day.view.daily_icon
import kotlinx.android.synthetic.main.item_day.view.dayNameLabel
import kotlinx.android.synthetic.main.item_day.view.temperatureLabel
import reksoft.zadorozhnyi.stormy.R
import reksoft.zadorozhnyi.stormy.weather.Day

class DayAdapterKotlin(
    private inline val action: (Day) -> Unit
) : RecyclerView.Adapter<DayAdapterKotlin.DayItemHolder>() {

    var days: List<Day> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayItemHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day, parent, false)
        return DayItemHolder(view)
    }

    override fun getItemCount(): Int = days.size

    override fun onBindViewHolder(holder: DayItemHolder, position: Int) {
        val day = days[position]
        holder.apply {
            itemView.setOnClickListener { action.invoke(day) }
            dailyIcon.setImageResource(day.iconId)
            temperature.text = day.temperatureMax.toString()
            holder.dayLabel.text = if (position == 0) {
                "Today"
            } else {
                day.dayOfTheWeek
            }
        }
    }

    inner class DayItemHolder(
        itemView: View,
        val dailyIcon: ImageView = itemView.daily_icon,
        val dayLabel: TextView = itemView.dayNameLabel,
        val temperature: TextView = itemView.temperatureLabel
    ) : RecyclerView.ViewHolder(itemView)
}
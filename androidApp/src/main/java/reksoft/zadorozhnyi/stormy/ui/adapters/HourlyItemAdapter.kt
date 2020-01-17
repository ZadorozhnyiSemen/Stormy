package reksoft.zadorozhnyi.stormy.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_hourly.view.hour_value
import kotlinx.android.synthetic.main.item_hourly.view.icon
import kotlinx.android.synthetic.main.item_hourly.view.temperature_value
import reksoft.zadorozhnyi.stormy.Hourly
import reksoft.zadorozhnyi.stormy.R
import reksoft.zadorozhnyi.stormy.utils.hourAMPM
import reksoft.zadorozhnyi.stormy.utils.loverCasePMAM
import java.util.Calendar
import java.util.Date

class HourlyItemAdapter : RecyclerView.Adapter<HourlyItemAdapter.HourlyItemViewHolder>() {

    var items: Hourly? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hourly, parent, false)
        return HourlyItemViewHolder(view)
    }

    override fun getItemCount(): Int = items?.data?.take(24)?.size ?: 0

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HourlyItemViewHolder, position: Int) {
        val item = items?.data?.get(position)
        holder.apply {
            item?.let {

                temperature.text = it.apparentTemperature?.toInt().toString() + "°"
                icon.setImageResource(when (it.icon) {
                    "snowy" -> R.drawable.ic_cloudy
                    else -> R.drawable.ic_cloudy
                })
                if (position == 0) {
                    val wrapped = DrawableCompat.wrap(icon.drawable)
                    //DrawableCompat.setTint(wrapped, ContextCompat.getColor(itemView.context, R.color.colorAccent))
                }
                hour.text = if (position == 0) "Now" else it.hourAMPM().loverCasePMAM()


                if (position == 0) {
                    val drawable = DrawableCompat.wrap(ResourcesCompat.getDrawable(itemView.resources, R.drawable.ic_cloudy, null)!!)
                    drawable.mutate()
                    drawable?.let { dr ->
                        DrawableCompat.setTint(dr, ContextCompat.getColor(itemView.context, R.color.colorAccent))
                        icon.setImageDrawable(dr)
                    }
                }
            }
        }
    }

    inner class HourlyItemViewHolder(
        view: View,
        val hour: TextView = view.hour_value,
        val icon: ImageView = view.icon,
        val temperature: TextView = view.temperature_value
    ) : RecyclerView.ViewHolder(view)
}
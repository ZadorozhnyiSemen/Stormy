package reksoft.zadorozhnyi.stormy.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_details.view.detail_icon
import kotlinx.android.synthetic.main.item_details.view.detail_title
import kotlinx.android.synthetic.main.item_details.view.detail_value
import reksoft.zadorozhnyi.stormy.Currently
import reksoft.zadorozhnyi.stormy.R

class DetailsItemAdapter : RecyclerView.Adapter<DetailsItemAdapter.DetailsItemViewHolder>() {

    var item: Currently? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_details, parent, false)
        return DetailsItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: DetailsItemViewHolder, position: Int) {
        holder.apply {
            when (position) {
                0 -> {
                    item?.let {
                        icon.setImageResource(R.drawable.ic_feels_like)
                        title.text = "Feels like"
                        value.text = it.apparentTemperature?.toInt().toString() + "°"
                    }
                }
                1 -> {
                    item?.let {
                        icon.setImageResource(R.drawable.ic_humidity)
                        title.text = "Humidity"
                        value.text = ((it.humidity ?: 0.0) * 100).toString() + "%"
                    }
                }
                2 -> {
                    item?.let {
                        icon.setImageResource(R.drawable.ic_humidity)
                        title.text = "Wind speed"
                        value.text = it.windSpeed?.toInt().toString() + " kph"
                    }
                }
            }

        }
    }

    inner class DetailsItemViewHolder(
        view: View,
        val icon: ImageView = view.detail_icon,
        val title: TextView = view.detail_title,
        val value: TextView = view.detail_value
    ) : RecyclerView.ViewHolder(view)
}
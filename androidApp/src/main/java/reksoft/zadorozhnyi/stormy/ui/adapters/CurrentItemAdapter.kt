package reksoft.zadorozhnyi.stormy.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_current.view.weather_icon
import kotlinx.android.synthetic.main.item_current.view.weather_temperature
import reksoft.zadorozhnyi.stormy.Currently
import reksoft.zadorozhnyi.stormy.Forecast
import reksoft.zadorozhnyi.stormy.R

class CurrentItemAdapter : RecyclerView.Adapter<CurrentItemAdapter.CurrentItemViewHolder>() {

    var items = mutableListOf<CurrentAdapterData>()

    fun addCurrentLocation(data: Forecast) {
        if (items.isNotEmpty() && items[0].local) {
            items.removeAt(0)
            items.add(0, CurrentAdapterData.createLocal(data))
        } else {
            items.add(0, CurrentAdapterData.createLocal(data))
        }
        notifyDataSetChanged()
    }

    fun addOtherLocations(data: List<Forecast>) {
        items.addAll(data.map { CurrentAdapterData.createDefault(it) })
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_current, parent, false)
        return CurrentItemViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CurrentItemViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            itemView.background = ContextCompat.getDrawable(itemView.context, when (item.forecast?.currently?.icon) {
                "snowy" -> R.drawable.bg_weather_snowy
                else -> R.drawable.bg_weather_snowy
            })
            temperature.text = item.forecast?.currently?.apparentTemperature?.toInt().toString() + "°"
            icon.setImageResource(when (item.forecast?.currently?.icon) {
                "snowy" -> R.drawable.snowy
                else -> R.drawable.snowy
            })
        }
    }

    inner class CurrentItemViewHolder(
        view: View,
        val temperature: TextView = view.weather_temperature,
        val icon: ImageView = view.weather_icon
    ) : RecyclerView.ViewHolder(view)
}

class CurrentAdapterData(
    val local: Boolean,
    val forecast: Forecast?
) {
    companion object {
        fun createLocal(data: Forecast?) = CurrentAdapterData(true, data)
        fun createDefault(data: Forecast?) = CurrentAdapterData(false, data)
    }
}
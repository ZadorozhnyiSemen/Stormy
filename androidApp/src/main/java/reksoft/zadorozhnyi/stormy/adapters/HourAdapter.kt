package reksoft.zadorozhnyi.stormy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.hourly_list_item.view.daily_icon
import kotlinx.android.synthetic.main.hourly_list_item.view.summaryLabel
import kotlinx.android.synthetic.main.hourly_list_item.view.temperatureLabel
import kotlinx.android.synthetic.main.hourly_list_item.view.timeLabel
import reksoft.zadorozhnyi.stormy.Hour
import reksoft.zadorozhnyi.stormy.R
import reksoft.zadorozhnyi.stormy.adapters.HourAdapter.HourViewHolder

class HourAdapter : RecyclerView.Adapter<HourViewHolder>() {

    var data: List<Hour> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.hourly_list_item, parent, false)
        return HourViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {
        holder.bindHour(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class HourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var mTimeLabel: TextView = itemView.timeLabel as TextView
        private var mSummaryLabel: TextView = itemView.summaryLabel as TextView
        private var mTemperatureLabel: TextView = itemView.temperatureLabel as TextView
        private var mIconImageView: ImageView = itemView.daily_icon as ImageView
        fun bindHour(hour: Hour) {
            mTimeLabel.text = hour.time.toString()
            mSummaryLabel.text = hour.summary
            mTemperatureLabel.text = hour.temperature.toString() + ""
            //mIconImageView.setImageResource(hour.iconId)
        }

        override fun onClick(v: View) {
            val time = mTimeLabel.text.toString()
            val temperature = mTemperatureLabel.text.toString()
            val summary = mSummaryLabel.text.toString()
            val message = String.format("At %s it will ne %s and %s",
                time,
                temperature,
                summary)
            Toast.makeText(v.context, message, Toast.LENGTH_SHORT).show()
        }

        init {
            itemView.setOnClickListener(this)
        }
    }
}
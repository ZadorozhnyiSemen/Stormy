package reksoft.zadorozhnyi.stormy.utils

import reksoft.zadorozhnyi.stormy.Currently
import reksoft.zadorozhnyi.stormy.Daily
import reksoft.zadorozhnyi.stormy.Day
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

private val simpleDateFormat by lazy { SimpleDateFormat("h:mm a") }
private val weekFormat by lazy { SimpleDateFormat("EEEE") }

fun Currently.formatDate(): String {
    simpleDateFormat.timeZone = TimeZone.getTimeZone(timezone ?: TimeZone.getDefault().id)
    val date = Date(this.time * 1000)
    return "At ${simpleDateFormat.format(date)} it will be"
}

fun Day.dayOfTheWeek(): String {
    weekFormat.timeZone = TimeZone.getTimeZone(timezone ?: TimeZone.getDefault().id)
    return weekFormat.format(Date(this.time * 1000))
}
package reksoft.zadorozhnyi.stormy.utils

import reksoft.zadorozhnyi.stormy.Currently
import reksoft.zadorozhnyi.stormy.Day
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

private val simpleDateFormat by lazy { SimpleDateFormat("h:mm a") }
private val weekFormat by lazy { SimpleDateFormat("EEEE") }

fun Currently.formatDate(timezone: String): String {
    simpleDateFormat.timeZone = TimeZone.getTimeZone(timezone)
    val date = Date(this.time * 1000)
    return "At ${simpleDateFormat.format(date)} it will be"
}

fun Day.dayOfTheWeek(): String {
    return weekFormat.format(Date(this.time * 1000))
}
package reksoft.zadorozhnyi.stormy.utils

import reksoft.zadorozhnyi.stormy.Currently
import reksoft.zadorozhnyi.stormy.Day
import reksoft.zadorozhnyi.stormy.Hour
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

private val simpleDateFormat by lazy { SimpleDateFormat("h:mm a") }
private val weekFormat by lazy { SimpleDateFormat("EEEE") }
private val hourFormat by lazy { SimpleDateFormat("h a") }
private val currentFormat by lazy { SimpleDateFormat("EEEE, h a") }

fun Currently.formatDate(): String {
    simpleDateFormat.timeZone = TimeZone.getTimeZone(timezone ?: TimeZone.getDefault().id)
    val date = Date(this.time * 1000)
    return "At ${simpleDateFormat.format(date)} it will be"
}

fun Currently.today(): String {
    currentFormat.timeZone = TimeZone.getTimeZone(timezone)
    return currentFormat.format(Date(this.time * 1000))
}

fun Day.dayOfTheWeek(): String {
    weekFormat.timeZone = TimeZone.getTimeZone(timezone ?: TimeZone.getDefault().id)
    return weekFormat.format(Date(this.time * 1000))
}

fun Hour.hourAMPM(): String {
    hourFormat.timeZone = TimeZone.getTimeZone(timezone)
    return hourFormat.format(Date(this.time * 1000))
}

fun String.loverCasePMAM(): String {
    if (this.contains(" AM") || this.contains(" PM")) {
        val noAM = this.replace("AM", "am")
        return noAM.replace("PM", "pm")
    }
    return this
}

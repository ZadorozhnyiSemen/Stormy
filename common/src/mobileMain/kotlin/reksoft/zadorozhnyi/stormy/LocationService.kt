package reksoft.zadorozhnyi.stormy

import reksoft.zadorozhnyi.stormy.storage.ApplicationContext

expect class LocationService(context: ApplicationContext) {
    fun getDeviceLocation(): Pair<Double, Double>?
}
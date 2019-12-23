package reksoft.zadorozhnyi.stormy

import reksoft.zadorozhnyi.stormy.storage.ApplicationContext

actual class LocationService actual constructor(
    context: ApplicationContext
) {
    actual fun getDeviceLocation(): Pair<Double, Double>? {
        return 60.000573 to 30.334711
    }

}
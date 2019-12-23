package reksoft.zadorozhnyi.stormy.exceptions

open class StormyException(msg: String) : Throwable(msg)
class UnknownLatitudeException : StormyException("Unknown latitude")
class UnknownLongitudeException : StormyException("Unknown longitude")
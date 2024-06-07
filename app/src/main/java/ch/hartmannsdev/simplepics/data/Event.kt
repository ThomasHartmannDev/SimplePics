package ch.hartmannsdev.simplepics.data

open class Event<out T>(private val Content: T) {

    var hasBeenHandled = false
        private set
    fun getContentOrNull(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            Content
        }
    }
}
package ch.hartmannsdev.simplepics.data

/**
 * Wrapper class for handling events that should only be handled once.
 *
 * @param T The type of content.
 * @property Content The content of the event.
 */
open class Event<out T>(private val Content: T) {

    // Flag to track if the event has been handled
    var hasBeenHandled = false
        private set

    /**
     * Returns the content if it hasn't been handled yet, and marks it as handled.
     *
     * @return The content if not handled, or null if it has already been handled.
     */
    fun getContentOrNull(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            Content
        }
    }
}

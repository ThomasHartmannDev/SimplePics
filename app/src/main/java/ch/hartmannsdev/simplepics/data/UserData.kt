package ch.hartmannsdev.simplepics.data

/**
 * Data class representing user information.
 *
 * @property userId The unique identifier of the user.
 * @property name The name of the user.
 * @property username The username of the user.
 * @property imageUrl The URL of the user's profile image.
 * @property bio The bio of the user.
 * @property following The list of user IDs that the user is following.
 */
data class UserData(
    var userId: String? = null,
    var name: String? = null,
    var username: String? = null,
    var imageUrl: String? = null,
    var bio: String? = null,
    var following: List<String>? = null
) {
    /**
     * Converts the UserData object to a map.
     *
     * @return A map representing the UserData object.
     */
    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "username" to username,
        "imageUrl" to imageUrl,
        "bio" to bio,
        "following" to following
    )
}

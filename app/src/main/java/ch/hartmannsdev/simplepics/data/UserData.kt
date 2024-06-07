package ch.hartmannsdev.simplepics.data

data class UserData(
    var userId: String? = null,
    var name: String? = null,
    var username: String? = null,
    var imageUrl: String? = null,
    var email: String? = null,
    var bio: String? = null,
    var following: List<String>? = null,
){
    fun toMap() = mapOf(
        "userId" to userId,
        "name" to name,
        "username" to username,
        "imageUrl" to imageUrl,
        "email" to email,
        "bio" to bio,
    )
}

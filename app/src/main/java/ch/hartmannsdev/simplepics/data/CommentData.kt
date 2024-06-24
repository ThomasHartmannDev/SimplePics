package ch.hartmannsdev.simplepics.data

/**
 * Data class representing comment information.
 *
 * @property commentId The unique identifier of the comment.
 * @property postId The unique identifier of the post the comment is associated with.
 * @property username The username of the user who made the comment.
 * @property text The text content of the comment.
 * @property timestamp The timestamp of when the comment was made.
 */
data class CommentData(
    val commentId: String? = null,
    val postId: String? = null,
    val username: String? = null,
    val text: String? = null,
    val timestamp: Long? = null
)

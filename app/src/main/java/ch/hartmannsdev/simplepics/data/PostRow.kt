package ch.hartmannsdev.simplepics.data

/**
 * Data class representing a row of posts.
 *
 * @property post1 The first post in the row.
 * @property post2 The second post in the row.
 * @property post3 The third post in the row.
 */
data class PostRow(
    var post1: PostData? = null,
    var post2: PostData? = null,
    var post3: PostData? = null,
) {
    /**
     * Checks if the row is full.
     *
     * @return True if all three posts in the row are not null, false otherwise.
     */
    fun isFull() = post1 != null && post2 != null && post3 != null

    /**
     * Adds a post to the row.
     *
     * The post will be added to the first available slot (post1, post2, or post3).
     *
     * @param post The post to be added.
     */
    fun add(post: PostData) {
        if (post1 == null) {
            post1 = post
        } else if (post2 == null) {
            post2 = post
        } else if (post3 == null) {
            post3 = post
        }
    }
}

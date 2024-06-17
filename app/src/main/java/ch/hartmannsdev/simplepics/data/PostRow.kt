package ch.hartmannsdev.simplepics.data

data class PostRow(
    var post1: PostData? = null,
    var post2: PostData? = null,
    var post3: PostData? = null,
) {
    fun isFull() = post1 != null && post2 != null && post3 != null
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
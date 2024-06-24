package ch.hartmannsdev.simplepics.Router

import android.net.Uri
import ch.hartmannsdev.simplepics.data.PostData
import com.google.gson.Gson

/**
 * Sealed class representing different routes in the application.
 *
 * @param route The route string.
 */
sealed class Router(val route: String) {
    object Signup : Router("signUp")
    object Login : Router("login")
    object ForgotPassword : Router("forgotpassword")
    object Feed : Router("feed")
    object Search : Router("search")
    object MyPosts : Router("myposts")
    object Profile : Router("profile")

    /**
     * Route for creating a new post.
     *
     * @param imageUri The URI of the image to be posted.
     */
    object NewPost : Router("newpost/{imageUri}") {
        /**
         * Creates a route for a new post with the given image URI.
         *
         * @param uri The image URI.
         * @return The formatted route string.
         */
        fun createRoute(uri: String) = "newpost/$uri"
    }

    /**
     * Route for displaying a single post.
     *
     * @param postData The data of the post to be displayed.
     */
    object SinglePost : Router("singlepost/{postData}") {
        /**
         * Creates a route for a single post with the given post data.
         *
         * @param postData The post data.
         * @return The formatted route string.
         */
        fun createRoute(postData: PostData): String {
            return "singlepost/${Uri.encode(Gson().toJson(postData))}"
        }
    }

    /**
     * Route for displaying the comments of a post.
     *
     * @param postId The ID of the post.
     */
    object CommentsScreen : Router("commentscreen/{postId}") {
        /**
         * Creates a route for the comments screen with the given post ID.
         *
         * @param postId The post ID.
         * @return The formatted route string.
         */
        fun createRoute(postId: String): String {
            return "commentscreen/$postId}"
        }
    }
}

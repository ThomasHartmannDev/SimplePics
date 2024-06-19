package ch.hartmannsdev.simplepics.Router

import android.net.Uri
import ch.hartmannsdev.simplepics.data.PostData
import com.google.gson.Gson

sealed class Router(val route: String) {
    object Signup: Router("signUp")
    object Login: Router("login")
    object ForgotPassword: Router("forgotpassword")
    object Feed: Router("feed")
    object Search: Router("search")
    object MyPosts: Router("myposts")
    object Profile: Router("profile")
    object NewPost: Router("newpost/{imageUri}"){
        fun createRoute(uri: String) = "newpost/$uri"
    }
    object SinglePost : Router("singlepost/{postData}") {
        fun createRoute(postData: PostData): String {
            return "singlepost/${Uri.encode(Gson().toJson(postData))}"
        }
    }
    object CommentsScreen : Router("commentscreen/{postId}") {
        fun createRoute(postId: String): String {
            return "commentscreen/$postId}"
        }
    }
}
package ch.hartmannsdev.simplepics.Router

sealed class Router(val route: String) {
    object Signup: Router("signUp")
    object Login: Router("login")
    object ForgotPassword: Router("forgotpassword")
    object Feed: Router("feed")
    object Search: Router("search")
    object MyPosts: Router("myposts")
    object Profile: Router("profile")
}
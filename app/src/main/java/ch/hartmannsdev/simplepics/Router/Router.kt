package ch.hartmannsdev.simplepics.Router

sealed class Router(val route: String) {
    object Signup: Router("signUp")
    object Login: Router("login")
}
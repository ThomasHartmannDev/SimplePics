package ch.hartmannsdev.simplepics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ch.hartmannsdev.simplepics.Router.Router
import ch.hartmannsdev.simplepics.ui.screens.auth.LoginScreen
import ch.hartmannsdev.simplepics.ui.screens.auth.SignupScreen
import ch.hartmannsdev.simplepics.ui.screens.feed.FeedScreen
import ch.hartmannsdev.simplepics.ui.screens.feed.MyPostScreen
import ch.hartmannsdev.simplepics.ui.screens.feed.SearchScreen
import ch.hartmannsdev.simplepics.ui.theme.SimplePicsTheme
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel
import ch.hartmannsdev.simplepics.utils.NotificationMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimplePicsTheme {
                SimplePicsApp()
            }
        }
    }
}

@Composable
fun SimplePicsApp(modifier: Modifier = Modifier) {
    val vm = hiltViewModel<SimplePicsViewModel>()
    val navController = rememberNavController()
    NotificationMessage(vm = vm)
    NavHost(navController = navController, startDestination = Router.Login.route) {
        composable(Router.Signup.route){ SignupScreen(navController = navController, vm = vm) }
        composable(Router.Login.route){ LoginScreen(navController = navController, vm = vm) }
        composable(Router.Feed.route){ FeedScreen(navController = navController, vm = vm) }
        composable(Router.Search.route){ SearchScreen(navController = navController, vm = vm) }
        composable(Router.MyPosts.route){ MyPostScreen(navController = navController, vm = vm) }
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimplePicsTheme {
        SimplePicsApp()
    }
}

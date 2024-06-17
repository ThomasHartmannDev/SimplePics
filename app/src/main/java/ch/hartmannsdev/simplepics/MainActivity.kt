package ch.hartmannsdev.simplepics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ch.hartmannsdev.simplepics.Router.Router
import ch.hartmannsdev.simplepics.data.PostData
import ch.hartmannsdev.simplepics.ui.screens.auth.ForgotPasswordScreen
import ch.hartmannsdev.simplepics.ui.screens.auth.LoginScreen
import ch.hartmannsdev.simplepics.ui.screens.auth.SignupScreen
import ch.hartmannsdev.simplepics.ui.screens.feed.FeedScreen
import ch.hartmannsdev.simplepics.ui.screens.feed.SearchScreen
import ch.hartmannsdev.simplepics.ui.screens.feed.SinglePostScreen
import ch.hartmannsdev.simplepics.ui.screens.user.MyPostScreen
import ch.hartmannsdev.simplepics.ui.screens.user.NewPostScreen
import ch.hartmannsdev.simplepics.ui.screens.user.ProfileScreen
import ch.hartmannsdev.simplepics.ui.theme.SimplePicsTheme
import ch.hartmannsdev.simplepics.ui.viewmodels.SimplePicsViewModel
import ch.hartmannsdev.simplepics.utils.NotificationMessage
import com.google.gson.Gson
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

    // Here are created every single route we need to change screens on our app
    NavHost(navController = navController, startDestination = Router.Login.route) {
        composable(Router.Signup.route){ SignupScreen(navController = navController, vm = vm) }
        composable(Router.Login.route){ LoginScreen(navController = navController, vm = vm) }
        composable(Router.ForgotPassword.route){ ForgotPasswordScreen(navController = navController, vm = vm) }
        composable(Router.Feed.route){ FeedScreen(navController = navController, vm = vm) }
        composable(Router.Search.route){ SearchScreen(navController = navController, vm = vm) }
        composable(Router.MyPosts.route){ MyPostScreen(navController = navController, vm = vm) }
        composable(Router.Profile.route){ ProfileScreen(navController = navController, vm = vm) }
        composable(Router.NewPost.route){navBackStackEntry ->
            val imageUri = navBackStackEntry.arguments?.getString("imageUri")
            imageUri?.let {
               NewPostScreen(navController = navController, vm = vm,  encodedUri = it)
            }
        }
        composable(
            route = Router.SinglePost.route,
            arguments = listOf(navArgument("postData") { type = NavType.StringType })
        ) { navBackStackEntry ->
            val postDataJson = navBackStackEntry.arguments?.getString("postData")
            val postData = Gson().fromJson(postDataJson, PostData::class.java)

            postData?.let {
                SinglePostScreen(
                    vm = vm, navController = navController, post = it
                )
            }
        }

    }

}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimplePicsTheme {
        SimplePicsApp()
    }
}
